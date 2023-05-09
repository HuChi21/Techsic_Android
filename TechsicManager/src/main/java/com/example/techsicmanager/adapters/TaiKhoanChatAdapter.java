package com.example.techsicmanager.adapters;

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
import com.example.techsicmanager.R;
import com.example.techsicmanager.activities.ChatActivity;
import com.example.techsicmanager.interfaces.ItemClickListener;
import com.example.techsicmanager.models.TaiKhoan;
import com.example.techsicmanager.retrofit.RetrofitInterface;

import java.util.List;

public class TaiKhoanChatAdapter extends RecyclerView.Adapter<TaiKhoanChatAdapter.MyViewHolder> {
    Context context;
    List<TaiKhoan> list;

    public TaiKhoanChatAdapter(Context context, List<TaiKhoan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TaiKhoanChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_taikhoanchat,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaiKhoanChatAdapter.MyViewHolder holder, int position) {
        TaiKhoan taiKhoan = list.get(position);
        holder.txtEmail.setText(taiKhoan.getEmail());
        holder.txtHoten.setText(taiKhoan.getHoten());
        if(taiKhoan.getHinhanh().contains("https")){
            Glide.with(context).load(taiKhoan.getHinhanh()).into(holder.imgAnh);
        } else {
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + taiKhoan.getHinhanh();
            Glide.with(context).load(hinhanh).into(holder.imgAnh);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("id",taiKhoan.getIdtaikhoan());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        private TextView txtEmail,txtHoten;
        private ImageView imgAnh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEmail = itemView.findViewById(R.id.itemtk_email);
            txtHoten = itemView.findViewById(R.id.itemtk_hoten);
            imgAnh = itemView.findViewById(R.id.itemtk_hinhanh);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }
}
