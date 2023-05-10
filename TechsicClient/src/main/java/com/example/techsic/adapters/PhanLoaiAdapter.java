package com.example.techsic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techsic.R;
import com.example.techsic.interfaces.ItemClickListener;
import com.example.techsic.interfaces.OnItemClickListener;
import com.example.techsic.models.PhanLoai;

import java.util.List;

public class PhanLoaiAdapter extends RecyclerView.Adapter<PhanLoaiAdapter.MyViewHolder> {
    List<PhanLoai> phanLoaiList;
    Context context;
    int newPosition;
    boolean selected = false;
    public PhanLoaiAdapter(Context context, List<PhanLoai> phanLoaiList) {
        this.context = context;
        this.phanLoaiList = phanLoaiList;
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phanloai,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PhanLoai phanLoai = phanLoaiList.get(position);
        holder.txtPhanloai.setText(phanLoai.getPhanloai());
        holder.txtPhanloai.setTextColor(0xFF121212);
        holder.txtPhanloai.setBackgroundColor(0xFFf5f5f5);
        holder.itemView.setBackgroundResource(R.color.selected_item_color);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = true;
                if (listener != null) {
                    // Sử dụng holder.getAdapterPosition() để lấy giá trị position mới
                    newPosition = holder.getAdapterPosition();
                    holder.txtPhanloai.setBackgroundColor(0xFFf5f5f5);
                    // Sử dụng newPosition thay vì position
                    listener.onItemClick(phanLoai.getPhanloai(),newPosition);
                    notifyDataSetChanged();
                }

            }
        });
        if(newPosition == position){
            holder.txtPhanloai.setTextColor(0xFFFF8800);
            holder.txtPhanloai.setBackgroundResource(R.drawable.border_cardview4);
        }
    }
    public void getDefaultSelectedItem(OnItemClickListener listener) {
        if (!selected && phanLoaiList.size() > 0) { // nếu chưa chọn giá trị và danh sách không rỗng
            PhanLoai phanLoai = phanLoaiList.get(0);
            listener.onItemClick(phanLoai.getPhanloai(), 0);
        }
    }
    @Override
    public int getItemCount() {
        return phanLoaiList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtPhanloai;
        ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPhanloai = (TextView) itemView.findViewById(R.id.txtPhanloai);
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
