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

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.SPMoiViewHolder> {
    List<SanPham> list;
    Context context;

    public SanPhamMoiAdapter(Context context, List<SanPham> list ) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SPMoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanphammoi,parent,false);
        return new SPMoiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SPMoiViewHolder holder, int position) {
        SanPham SPMoi = list.get(position);
        holder.txtTen.setText(SPMoi.getTensp());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        holder.txtGia.setText(format.format(SPMoi.getGiasp()));
        holder.txtThuonghieu.setText(SPMoi.getThuonghieu());
        holder.txtPhanloai.setText(SPMoi.getPhanloai());
        Glide.with(context).load(SPMoi.getHinhanh()).into(holder.imgHinhanh);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ChitietActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("chitiet",SPMoi);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SPMoiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgHinhanh;
        TextView txtTen,txtGia,txtThuonghieu,txtPhanloai;
        private ItemClickListener itemClickListener;
        public SPMoiViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = (TextView) itemView.findViewById(R.id.itemsp_tensp);
            txtGia = (TextView) itemView.findViewById(R.id.itemsp_giasp);
            txtThuonghieu = (TextView) itemView.findViewById(R.id.itemsp_thuonghieu);
            txtPhanloai = (TextView) itemView.findViewById(R.id.itemsp_phanloai);
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
