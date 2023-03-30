package com.example.techsic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techsic.R;
import com.example.techsic.models.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> donHangList;

    public DonHangAdapter(Context context, List<DonHang> donHangList) {
        this.context = context;
        this.donHangList = donHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        holder.txtIddonhang.setText(String.valueOf(donHang.getId()));
        holder.txtTen.setText(donHang.getHoten());
        holder.txtSodt.setText(donHang.getSodt());
        holder.txtEmail.setText(donHang.getEmail());
        holder.txtDiachi.setText(donHang.getDiachi());
        holder.txtGhichu.setText(donHang.getGhichu());
        holder.txtNgaydat.setText(donHang.getNgaydat());
        holder.txtNgaynhan.setText(donHang.getNgaynhan());
        holder.txtTongthanhtoan.setText(donHang.getTongthanhtoan());

        LinearLayoutManager layoutManager = new LinearLayoutManager(
          holder.txtIddonhang.getContext(),
                LinearLayoutManager.VERTICAL,false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        //chitietdonhang adapter
        ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(context,donHang.getItem());
        holder.donhangRecyclerView.setLayoutManager(layoutManager);
        holder.donhangRecyclerView.setAdapter(chiTietDonHangAdapter);
        holder.donhangRecyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtIddonhang,txtTen,txtSodt,txtEmail,txtDiachi,txtGhichu,txtNgaydat,txtNgaynhan,txtTongthanhtoan;
        RecyclerView donhangRecyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIddonhang = (TextView) itemView.findViewById(R.id.txtIdDonHang);
            txtTen = (TextView) itemView.findViewById(R.id.txtTenNguoiDat);
            txtSodt = (TextView) itemView.findViewById(R.id.txtSoDTNguoiDat);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmailNguoiDat);
            txtDiachi = (TextView) itemView.findViewById(R.id.txtDiaChiDonHang);
            txtGhichu = (TextView) itemView.findViewById(R.id.txtGhiChuDonHang);
            txtNgaydat = (TextView) itemView.findViewById(R.id.txtNgayDatDonHang);
            txtNgaynhan= (TextView) itemView.findViewById(R.id.txtNgayNhanDonHang);
            txtTongthanhtoan = (TextView) itemView.findViewById(R.id.txtTongThanhToanDonHang);

            donhangRecyclerView = (RecyclerView) itemView.findViewById(R.id.donHangRecyclerView);
        }
    }
}
