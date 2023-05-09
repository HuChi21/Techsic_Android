package com.example.techsicmanager.adapters;

import android.content.Context;
import android.content.Intent;
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

public class ThongKeMainAdapter extends RecyclerView.Adapter<ThongKeMainAdapter.MyViewHolder> {
    Context context;
    List<DonHang> donHangList;

    public ThongKeMainAdapter(Context context, List<DonHang> donHangList) {
        this.context = context;
        this.donHangList = donHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thongkemain,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        holder.txtIddonhang.setText(String.valueOf(donHang.getId()));
        holder.txtTongthanhtoan.setText(donHang.getTongthanhtoan());
        holder.txtThoigian.setText("Thời gian " + donHang.getNgaynhan());
        if (donHang.getKieutinhtrang().equals("Hoàn thành")) {
            holder.txtTongthanhtoan.setTextColor(0xFF00FF00);
            holder.txtTongthanhtoan.setText("+ "+ donHang.getTongthanhtoan());
        } else if (donHang.getKieutinhtrang().equals("Đã trả hàng")) {
            holder.txtTongthanhtoan.setTextColor(0xFFD0342C);
            holder.txtTongthanhtoan.setText("- " + donHang.getTongthanhtoan());
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
        TextView txtIddonhang,txtTongthanhtoan,txtThoigian;
        ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIddonhang = (TextView) itemView.findViewById(R.id.txtIdDonHang);
            txtTongthanhtoan = (TextView) itemView.findViewById(R.id.txtTongThanhToanDonHang);
            txtThoigian = (TextView) itemView.findViewById(R.id.txtThoigian);

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
