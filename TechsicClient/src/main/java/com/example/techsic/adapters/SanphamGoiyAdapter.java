package com.example.techsic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.activities.ChitietActivity;
import com.example.techsic.interfaces.ItemClickListener;
import com.example.techsic.models.SanPham;
import com.example.techsic.retrofit.RetrofitInterface;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class SanphamGoiyAdapter extends RecyclerView.Adapter<SanphamGoiyAdapter.MyViewHolder> {
    List<SanPham> list;
    Context context;

    public SanphamGoiyAdapter(Context context, List<SanPham> list ) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanphamgoiy,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPham sanPhamGoiy = list.get(position);
        holder.txtTen.setText(sanPhamGoiy.getTensp());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        holder.txtGia.setText(format.format(sanPhamGoiy.getGiasp()));
        holder.txtThuonghieu.setText(sanPhamGoiy.getThuonghieu());
        holder.txtDaban.setText("Đã bán "+sanPhamGoiy.getSldaban());
        if(sanPhamGoiy.getHinhanh().contains("https")){
            Glide.with(context).load(sanPhamGoiy.getHinhanh()).into(holder.imgHinhanh);
        }
        else{
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + sanPhamGoiy.getHinhanh();
            Glide.with(context).load(hinhanh).into(holder.imgHinhanh);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ChitietActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("chitiet",sanPhamGoiy);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgHinhanh;
        TextView txtTen,txtGia,txtThuonghieu, txtDaban;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = (TextView) itemView.findViewById(R.id.itemsp_tensp);
            txtGia = (TextView) itemView.findViewById(R.id.itemsp_giasp);
            txtThuonghieu = (TextView) itemView.findViewById(R.id.itemsp_thuonghieu);
            txtDaban = (TextView) itemView.findViewById(R.id.itemsp_sldaban);
            imgHinhanh = (ImageView) itemView.findViewById(R.id.itemsp_hinhanh);
            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getPosition(),false);
        }
    }
}
