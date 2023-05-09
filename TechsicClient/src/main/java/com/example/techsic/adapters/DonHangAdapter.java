package com.example.techsic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techsic.R;
import com.example.techsic.activities.ChitietdonhangActivity;
import com.example.techsic.interfaces.ItemClickListener;
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
        holder.txtTongthanhtoan.setText(donHang.getTongthanhtoan());
        switch (donHang.getIdtinhtrang()) {
            case 0:
                holder.txtTinhtrang.setText("Chưa thanh toán");
                break;
            case 1:
                holder.txtTinhtrang.setText("Đã thanh toán");
                break;
            case 2:
                holder.txtTinhtrang.setText("Đã nhận đơn hàng");
                break;
            case 3:
                holder.txtTinhtrang.setText("Đang xử lý");
                break;
            case 4:
                holder.txtTinhtrang.setText("Đang vận chuyển");
                break;
            case 5:
                holder.txtTinhtrang.setText("Hoàn thành");
                holder.txtTinhtrang.setTextColor(0xFF00FF00);
                break;
            case 6:
                holder.txtTinhtrang.setText("Đã hủy");
                holder.txtTinhtrang.setTextColor(0xFFD0342C);
                break;
            case 7:
                holder.txtTinhtrang.setText("Đã trả hàng");
                holder.txtTinhtrang.setTextColor(0xFFD0342C);
                break;
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(context, ChitietdonhangActivity.class);
                    intent.putExtra("donhang", donHang);
                    intent.putExtra("kieutinhtrang", holder.txtTinhtrang.getText());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtIddonhang,txtTen,txtSodt,txtEmail,txtTongthanhtoan,txtTinhtrang;
        ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIddonhang = (TextView) itemView.findViewById(R.id.txtIdDonHang);
            txtTen = (TextView) itemView.findViewById(R.id.txtHoten);
            txtSodt = (TextView) itemView.findViewById(R.id.txtSoDt);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            txtTongthanhtoan = (TextView) itemView.findViewById(R.id.txtTongThanhToanDonHang);
            txtTinhtrang = (TextView) itemView.findViewById(R.id.txtTinhtrang);

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
