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
import com.example.techsic.activities.ChitietTintucActivity;
import com.example.techsic.interfaces.ItemClickListener;
import com.example.techsic.models.SanPham;
import com.example.techsic.models.TinTuc;
import com.example.techsic.retrofit.RetrofitInterface;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class TintucAdapter extends RecyclerView.Adapter<TintucAdapter.MyViewHolder> {
    Context context;
    List<TinTuc> list;

    public TintucAdapter(Context context, List<TinTuc> list ) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tintuc,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TinTuc tinTuc = list.get(position);
        holder.tacgia.setText(tinTuc.getTacgia());
        holder.tieude.setText(tinTuc.getTieude());
        holder.thoigian.setText(tinTuc.getThoigian());
        if(tinTuc.getHinhanh().contains("https")){
            Glide.with(context).load(tinTuc.getHinhanh()).into(holder.imgTintuc);
        }
        else{
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + tinTuc.getHinhanh();
            Glide.with(context).load(hinhanh).into(holder.imgTintuc);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ChitietTintucActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tintuc",tinTuc);
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
        ImageView imgTintuc;
        TextView tacgia,thoigian,tieude;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTintuc = (ImageView) itemView.findViewById(R.id.item_hinhanhtintuc);
            tacgia = (TextView) itemView.findViewById(R.id.item_tacgiaTintuc);
            thoigian = (TextView) itemView.findViewById(R.id.item_thoigianTintuc);
            tieude = (TextView) itemView.findViewById(R.id.item_tieudeTintuc);
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
