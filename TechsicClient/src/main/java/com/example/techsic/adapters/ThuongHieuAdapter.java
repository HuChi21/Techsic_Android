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
import com.example.techsic.interfaces.OnItemClickListener;
import com.example.techsic.interfaces.OnItemClickListener2;
import com.example.techsic.models.PhanLoai;
import com.example.techsic.models.SanPham;
import com.example.techsic.retrofit.RetrofitInterface;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class ThuongHieuAdapter extends RecyclerView.Adapter<ThuongHieuAdapter.MyViewHolder> {
    List<SanPham> list;
    Context context;
    int newPosition;
    boolean selected = false;
    public ThuongHieuAdapter(Context context, List<SanPham> list ) {
        this.list = list;
        this.context = context;
    }
    private OnItemClickListener2 listener;
    public void setOnItemClickListener(OnItemClickListener2 listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_locthuonghieu,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPham sanpham = list.get(position);
        holder.txtThuonghieu.setText(sanpham.getThuonghieu());
        holder.txtThuonghieu.setTextColor(0xFF121212);
        holder.txtThuonghieu.setBackgroundColor(0xFFf5f5f5);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = true;
                if (listener != null) {
                    // Sử dụng holder.getAdapterPosition() để lấy giá trị position mới
                    newPosition = holder.getAdapterPosition();
                    holder.txtThuonghieu.setBackgroundColor(0xFFf5f5f5);
                    // Sử dụng newPosition thay vì position
                    listener.onItemClick2(sanpham.getThuonghieu(),newPosition);
                    notifyDataSetChanged();
                }

            }
        });
        if(newPosition == position){
            holder.txtThuonghieu.setTextColor(0xFFFF8800);
            holder.txtThuonghieu.setBackgroundResource(R.drawable.border_cardview4);
        }
    }
    public void getDefaultSelectedItem(OnItemClickListener2 listener) {
        if (!selected && list.size() > 0) { // nếu chưa chọn giá trị và danh sách không rỗng
            SanPham sanPham = list.get(0);
            listener.onItemClick2(sanPham.getThuonghieu(), 0);
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtThuonghieu;
        ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtThuonghieu = (TextView) itemView.findViewById(R.id.txtLoc);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);

        }
    }
}
