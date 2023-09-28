package com.example.techsic.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.models.GioHang;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class MuaHangAdapter extends RecyclerView.Adapter<MuaHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> muaHangList;
    public MuaHangAdapter(Context context, List<GioHang> muaHangList) {
        this.context = context;
        this.muaHangList = muaHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_muahang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang muahang = muaHangList.get(position);

        Glide.with(context).load(muahang.getHinhanh()).into(holder.imgHinhanh);
        holder.txtTensp.setText(muahang.getTensp());
        holder.txtPhanloai.setText(muahang.getPhanloai());
        holder.txtThuonghieu.setText(muahang.getThuonghieu());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        holder.txtGia.setText(format.format(muahang.getGiasp()));
        BigDecimal giakm = muahang.getGiasp().add(new BigDecimal("500000")) ;
        holder.txtGiakm.setText(format.format(giakm));
        holder.txtGiakm.setPaintFlags(holder.txtGiakm.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtSoluong.setText("Số lượng: "+muahang.getSoluong()+"");
        BigDecimal tongtien = muahang.getGiasp().multiply(BigDecimal.valueOf(muahang.getSoluong()));
//        holder.txtTongtien.setText(format.format(tongtien));
    }

    @Override
    public int getItemCount() {
        return muaHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhanh;
        TextView txtTensp,txtPhanloai,txtGia, txtGiakm, txtSoluong,txtTongtien,txtThuonghieu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhanh = itemView.findViewById(R.id.itemgh_imgHinhanh);

            txtTensp = itemView.findViewById(R.id.itemgh_txtTensp);
            txtPhanloai = itemView.findViewById(R.id.itemgh_txtPhanloai);
            txtThuonghieu = itemView.findViewById(R.id.itemgh_txtThuongHieu);
            txtGia = itemView.findViewById(R.id.itemgh_txtGia);
            txtGiakm = itemView.findViewById(R.id.itemgh_txtGiaKM);
            txtSoluong = itemView.findViewById(R.id.itemgh_txtSoLuong);
            txtTongtien = itemView.findViewById(R.id.itemgh_txtTongtien);
        }
    }
}
