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
import com.example.techsic.activities.SanphamActivity;
import com.example.techsic.interfaces.ItemClickListener;
import com.example.techsic.models.LoaiSP;
import com.example.techsic.models.SanPham;
import com.example.techsic.retrofit.RetrofitInterface;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class LoaiSPAdapter extends RecyclerView.Adapter<LoaiSPAdapter.MyViewHolder> {
    List<LoaiSP> list;
    Context context;

    public LoaiSPAdapter(Context context, List<LoaiSP> list ) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loaisp,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LoaiSP loaiSP = list.get(position);
        holder.txtTen.setText(loaiSP.getTenloaisp());
        if(loaiSP.getHinhanh().contains("https")){
            Glide.with(context).load(loaiSP.getHinhanh()).into(holder.imgHinhanh);
        }
        else{
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + loaiSP.getHinhanh();
            Glide.with(context).load(hinhanh).into(holder.imgHinhanh);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, SanphamActivity.class);
                    intent.putExtra("item_index", loaiSP.getIdloaisp()-1); // truyền dữ liệu của item cần mở
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
        ImageView imgHinhanh;
        TextView txtTen,txtGia,txtThuonghieu,txtPhanloai;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = (TextView) itemView.findViewById(R.id.itemloaisp_ten);
            imgHinhanh = (ImageView) itemView.findViewById(R.id.itemloaisp_anh);
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
