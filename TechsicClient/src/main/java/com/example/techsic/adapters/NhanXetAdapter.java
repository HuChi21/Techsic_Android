package com.example.techsic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.activities.ChitietTintucActivity;
import com.example.techsic.interfaces.ItemClickListener;
import com.example.techsic.models.NhanXet;
import com.example.techsic.models.TinTuc;
import com.example.techsic.retrofit.RetrofitInterface;

import java.util.List;

public class NhanXetAdapter extends RecyclerView.Adapter<NhanXetAdapter.MyViewHolder> {
    Context context;
    List<NhanXet> list;

    public NhanXetAdapter(Context context, List<NhanXet> list ) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nhanxet,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NhanXet nhanXet = list.get(position);
        holder.txtHoten.setText(nhanXet.getHoten());
        holder.txtNhanxet.setText(nhanXet.getNhanxet());
        holder.ratingBarDanhgia.setRating(nhanXet.getDanhgia()/ 1.0f);
        if(nhanXet.getHinhanh().contains("https")){
            Glide.with(context).load(nhanXet.getHinhanh()).into(holder.imgAnh);
        }
        else{
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + nhanXet.getHinhanh();
            Glide.with(context).load(hinhanh).into(holder.imgAnh);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAnh;
        TextView txtHoten,txtNhanxet;
        RatingBar ratingBarDanhgia;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnh = (ImageView) itemView.findViewById(R.id.imgAnh);
            txtHoten = (TextView) itemView.findViewById(R.id.txtHoten);
            txtNhanxet = (TextView) itemView.findViewById(R.id.txtNhanxet);
            ratingBarDanhgia = (RatingBar) itemView.findViewById(R.id.ratingDanhgia);
        }
    }
}
