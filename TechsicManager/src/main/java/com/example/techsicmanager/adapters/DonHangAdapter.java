package com.example.techsicmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techsicmanager.R;
import com.example.techsicmanager.activities.XemDonhangActivity;
import com.example.techsicmanager.interfaces.ItemClickListener;
import com.example.techsicmanager.models.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    Context context;
    List<DonHang> donHangList;

    public DonHangAdapter(Context context, List<DonHang> donHangList) {
        this.context = context;
        this.donHangList = donHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        holder.txtIddonhang.setText(String.valueOf(donHang.getId()));
        holder.txtTen.setText(donHang.getHoten());
        holder.txtSodt.setText(donHang.getSodt());
        holder.txtEmail.setText(donHang.getEmail());
        holder.txtTongthanhtoan.setText(donHang.getTongthanhtoan());
        holder.txtTinhtrang.setText(String.valueOf(donHang.getKieutinhtrang()));
        if (donHang.getKieutinhtrang().equals("Hoàn thành")) {
            holder.txtTinhtrang.setTextColor(0xFF00FF00);
        } else if (donHang.getKieutinhtrang().equals("Hủy đơn hàng")) {
            holder.txtTinhtrang.setTextColor(0xFFD0342C);
        }
        else{
            holder.txtTinhtrang.setTextColor(0xFF121212);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(context, XemDonhangActivity.class);
                    intent.putExtra("donhang", donHang);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtIddonhang,txtTen,txtSodt,txtEmail,txtTongthanhtoan,txtTinhtrang;
        ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIddonhang = (TextView) itemView.findViewById(R.id.txtIdDonHang);
            txtTen = (TextView) itemView.findViewById(R.id.txtHoten);
            txtSodt = (TextView) itemView.findViewById(R.id.txtSoDt);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            txtTongthanhtoan = (TextView) itemView.findViewById(R.id.txtTongThanhToanDonHang);
            txtTinhtrang = (TextView) itemView.findViewById(R.id.txtTinhtrang);

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
