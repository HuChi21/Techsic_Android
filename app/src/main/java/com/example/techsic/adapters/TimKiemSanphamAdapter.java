package com.example.techsic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.activities.ChitietActivity;
import com.example.techsic.interfaces.ItemClickListener;
import com.example.techsic.models.SanPham;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class TimKiemSanphamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPham> list;


    public TimKiemSanphamAdapter(Context context, List<SanPham> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timkiemsanpham,parent,false);
            return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            SanPham sanPham = list.get(position);
            myViewHolder.txtTen.setText(sanPham.getTensp());
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(0);
            format.setCurrency(Currency.getInstance("VND"));
            myViewHolder.txtGia.setText(format.format(sanPham.getGiasp()));
            myViewHolder.txtThuonghieu.setText(sanPham.getThuonghieu());
            myViewHolder.txtPhanLoai.setText(sanPham.getPhanloai());
            Glide.with(context).load(sanPham.getHinhanh()).into(myViewHolder.imgHinhanh);

            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        Intent intent = new Intent(context, ChitietActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("chitiet",sanPham);
                        context.startActivity(intent);
                    }
                }
            });

        }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgHinhanh;
        private TextView txtTen,txtGia,txtThuonghieu, txtPhanLoai ;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = (TextView) itemView.findViewById(R.id.itemtk_txtTensp);
            txtGia = (TextView) itemView.findViewById(R.id.itemtk_txtGia);
            txtPhanLoai = (TextView) itemView.findViewById(R.id.itemtk_txtPhanloai);
            txtThuonghieu = (TextView) itemView.findViewById(R.id.itemtk_txtThuongHieu);
            imgHinhanh = (ImageView) itemView.findViewById(R.id.itemtk_imgHinhanh);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }



}
