package com.example.techsic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.techsic.retrofit.RetrofitInterface;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class SanphamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPham> list;
    List<SanPham> listSearch;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public SanphamAdapter(Context context, List<SanPham> list) {
        this.context = context;
        this.list = list;
        this.listSearch = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham,parent,false);
            return new MyViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
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
            myViewHolder.txtDaban.setText("Đã bán "+sanPham.getSldaban());
            myViewHolder.txtThuonghieu.setText(sanPham.getThuonghieu());
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
            if(sanPham.getHinhanh().contains("https")){
                Glide.with(context).load(sanPham.getHinhanh()).into(myViewHolder.imgHinhanh);
            }
            else{
                String hinhanh = RetrofitInterface.BASE_URL+"images/" + sanPham.getHinhanh();
                Glide.with(context).load(hinhanh).into(myViewHolder.imgHinhanh);
            }

        }
        else{
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);

        }

    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarLoadMore);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgHinhanh;
        private TextView txtTen,txtGia,txtThuonghieu ,txtDaban;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = (TextView) itemView.findViewById(R.id.itemsp_tensp);
            txtGia = (TextView) itemView.findViewById(R.id.itemsp_giasp);
            txtDaban = (TextView) itemView.findViewById(R.id.itemsp_sldaban);
            txtThuonghieu = (TextView) itemView.findViewById(R.id.itemsp_thuonghieu);
            imgHinhanh = (ImageView) itemView.findViewById(R.id.itemsp_hinhanh);

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
