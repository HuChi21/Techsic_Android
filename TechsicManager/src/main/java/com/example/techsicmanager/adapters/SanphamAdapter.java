package com.example.techsicmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techsicmanager.R;
import com.example.techsicmanager.activities.SuaSanPhamActivity;
import com.example.techsicmanager.interfaces.ItemClickListener;
import com.example.techsicmanager.models.SanPham;
import com.example.techsicmanager.models.eventbus.SuaXoaspEvent;
import com.example.techsicmanager.retrofit.RetrofitInterface;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class SanphamAdapter extends  RecyclerView.Adapter<SanphamAdapter.MyViewHolder>{
    Context context;
    List<SanPham> list;

    public SanphamAdapter(Context context, List<SanPham> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SanphamAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham,parent,false);
        return new SanphamAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanphamAdapter.MyViewHolder holder, int position) {
        SanPham sanPham = list.get(position);
        holder.txtTen.setText(sanPham.getTensp());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        holder.txtGia.setText(format.format(sanPham.getGiasp()));
        holder.txtThuonghieu.setText(sanPham.getThuonghieu());
        holder.txtSoluong.setText(String.valueOf("Số lượng tồn kho: "+sanPham.getSltonkho()));
        if(sanPham.getHinhanh().contains("https")){
            Glide.with(context).load(sanPham.getHinhanh()).into(holder.imgHinhanh);
        }
        else{
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + sanPham.getHinhanh();
            Glide.with(context).load(hinhanh).into(holder.imgHinhanh);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, SuaSanPhamActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("sanPhamSuaXoa",sanPham);
                    String loaisp = null;
                    switch (sanPham.getIdloaisp()){
                        case 1:
                            loaisp = "Điện thoại";
                            break;
                        case 2:
                            loaisp = "Laptop";
                            break;
                        case 3:
                            loaisp = "tablet";
                            break;
                    }
                    intent.putExtra("loaisp",loaisp);
                    context.startActivity(intent);
                }else{
                    EventBus.getDefault().postSticky(new SuaXoaspEvent(sanPham));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        ImageView imgHinhanh;
        TextView txtTen,txtGia,txtThuonghieu, txtSoluong;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = (TextView) itemView.findViewById(R.id.itemsp_tensp);
            txtGia = (TextView) itemView.findViewById(R.id.itemsp_giasp);
            txtThuonghieu = (TextView) itemView.findViewById(R.id.itemsp_thuonghieu);
            txtSoluong = (TextView) itemView.findViewById(R.id.itemsp_Soluong);
            imgHinhanh = (ImageView) itemView.findViewById(R.id.itemsp_hinhanh);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,0,getAdapterPosition(),"Sửa");
            menu.add(0,1,getAdapterPosition(),"Xoá");
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getPosition(),false);

        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getPosition(),true);
            return false;
        }
    }
}
