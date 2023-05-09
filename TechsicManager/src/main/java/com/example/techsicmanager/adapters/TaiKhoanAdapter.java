package com.example.techsicmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techsicmanager.R;
import com.example.techsicmanager.activities.ChatActivity;
import com.example.techsicmanager.activities.XemNguoidungActivity;
import com.example.techsicmanager.interfaces.ItemClickListener;
import com.example.techsicmanager.models.TaiKhoan;
import com.example.techsicmanager.retrofit.RetrofitInterface;

import java.util.List;

public class TaiKhoanAdapter extends RecyclerView.Adapter<TaiKhoanAdapter.MyViewHolder> {
   Context context;
   List<TaiKhoan> list;

    public TaiKhoanAdapter(Context context, List<TaiKhoan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TaiKhoanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nguoidung,parent,false);
        return new TaiKhoanAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaiKhoanAdapter.MyViewHolder holder, int position) {
        TaiKhoan taiKhoan = list.get(position);
        if(taiKhoan.getTrangthai() == 0){
            holder.txtTrangthai.setText("Bị chặn");
            holder.txtTrangthai.setTextColor(0xFFD0342C);
        }
        else{
            holder.txtTrangthai.setText("Đang hoạt động");
            holder.txtTrangthai.setTextColor(0xFF00FF00);
        }
        holder.txtEmail.setText(taiKhoan.getEmail());
        holder.txtHoten.setText(taiKhoan.getHoten());
        holder.txtSoDt.setText(taiKhoan.getSodt());
        if(taiKhoan.getHinhanh().contains("http")){
            Glide.with(context).load(taiKhoan.getHinhanh()).into(holder.imgHinhanh);
        } else {
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + taiKhoan.getHinhanh();
            Glide.with(context).load(hinhanh).into(holder.imgHinhanh);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick) {
                    Intent intent = new Intent(context, XemNguoidungActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("taikhoan",taiKhoan);
                    context.startActivity(intent);
                }
            }
        });
        holder.btnNhantin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",taiKhoan.getIdtaikhoan());
                context.startActivity(intent);
            }
        });
        holder.btnChan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taiKhoan.setTrangthai(0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgHinhanh;
        TextView txtEmail,txtHoten,txtSoDt,txtTrangthai;
        Button btnNhantin, btnChan;
        private  ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTrangthai = (TextView) itemView.findViewById(R.id.itemtk_trangthai);
            txtEmail = (TextView) itemView.findViewById(R.id.itemtk_email);
            txtHoten = (TextView) itemView.findViewById(R.id.itemtk_hoten);
            txtSoDt = (TextView) itemView.findViewById(R.id.itemtk_sodt);
            imgHinhanh = (ImageView) itemView.findViewById(R.id.itemtk_hinhanh);
            btnNhantin = (Button) itemView.findViewById(R.id.btnChat);
            btnChan = (Button) itemView.findViewById(R.id.btnChan);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getPosition(),false);
        }
    }
}
