package com.example.techsic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.models.Item;
import com.example.techsic.models.SanPham;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.MyViewHolder> {
    List<Item> itemList;
    List<SanPham> sanPhamList;
    Context context;
    public ChiTietDonHangAdapter(Context context,List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitietdonhang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtTen.setText(item.getTensp()+"");
        holder.txtPhanloai.setText(item.getPhanloai());
        holder.txtThuongHieu.setText(item.getThuonghieu());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        holder.txtGia.setText(format.format(item.getGiasp()));
        holder.txtSoLuong.setText(item.getSoluong());
        Glide.with(context).load(item.getHinhanh()).into(holder.imgAnh);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnh;
        TextView txtTen,txtPhanloai,txtThuongHieu,txtGia,txtSoLuong;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnh = (ImageView) itemView.findViewById(R.id.itemtt_imgAnh);
            txtTen = (TextView) itemView.findViewById(R.id.itemtt_txtTen);
            txtPhanloai = (TextView) itemView.findViewById(R.id.itemtt_txtPhanLoai);
            txtThuongHieu = (TextView) itemView.findViewById(R.id.itemtt_txtThuongHieu);
            txtGia = (TextView) itemView.findViewById(R.id.itemtt_txtGia);
            txtSoLuong = (TextView) itemView.findViewById(R.id.itemtt_txtSoLuong);
        }
    }

}
