package com.example.techsic.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.interfaces.ImageClickListener;
import com.example.techsic.models.GioHang;
import com.example.techsic.models.eventbus.TinhTongEvent;
import com.example.techsic.retrofit.RetrofitUtilities;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    static int REMOVE_ITEM = 1 ;
    static int ADD_ITEM = 2 ;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);

        Glide.with(context).load(gioHang.getHinhanh()).into(holder.imgHinhanh);
        holder.txtTensp.setText(gioHang.getTensp());
        holder.txtPhanloai.setText(gioHang.getPhanloai());
        holder.txtThuonghieu.setText(gioHang.getThuonghieu());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        holder.txtGia.setText(format.format(gioHang.getGiasp()));
        BigDecimal giakm = gioHang.getGiasp().add(new BigDecimal("500000")) ;
        holder.txtGiakm.setText(format.format(giakm));
        holder.txtGiakm.setPaintFlags(holder.txtGiakm.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtSoluong.setText(gioHang.getSoluong()+"");
        BigDecimal tongtien = gioHang.getGiasp().multiply(BigDecimal.valueOf(gioHang.getSoluong()));
        holder.txtTongtien.setText(format.format(tongtien));

        holder.cbkSanpham.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    RetrofitUtilities.muahangList.add(gioHang);
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }
                else{
                    for(int i=0;i<RetrofitUtilities.muahangList.size();i++){
                        if(RetrofitUtilities.muahangList.get(i).getIdsp() == gioHang.getIdsp()){
                            RetrofitUtilities.muahangList.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }
                }
            }
        });
        holder.setListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int value) {
                if(value == REMOVE_ITEM){
                    if(gioHangList.get(pos).getSoluong()>1){
                        //decrease item
                        int soLuongMoi = gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(soLuongMoi);
                        holder.txtSoluong.setText(gioHangList.get(pos).getSoluong()+"");
                        BigDecimal tongtien = gioHangList.get(pos).getGiasp().multiply(new BigDecimal(gioHangList.get(pos).getSoluong()));
                        holder.txtTongtien.setText(format.format(tongtien));

                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }else if(gioHangList.get(pos).getSoluong() ==1 ){
                        //remove item
                        AlertDialog.Builder alertRemoveItemConfirm = new AlertDialog.Builder(view.getRootView().getContext());
                        alertRemoveItemConfirm.setTitle("Thông báo");
                        alertRemoveItemConfirm.setMessage("Bạn chắc chắn muốn xoá sản phẩm ?");
                        alertRemoveItemConfirm.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RetrofitUtilities.giohanglist.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                                Toast.makeText(context, "Xoá sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Huỷ bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = alertRemoveItemConfirm.create();
                        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(0xFF121212);
                                alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(0xFFFF8800);
                            }
                        });
                        alertDialog.show();
                    }
                }else if(value == ADD_ITEM){
                    if(gioHangList.get(pos).getSoluong()<11){
                        int soLuongMoi = gioHangList.get(pos).getSoluong()+1;
                        gioHangList.get(pos).setSoluong(soLuongMoi);
                    }
                    holder.txtSoluong.setText(gioHangList.get(pos).getSoluong()+"");
                    BigDecimal tongtien = gioHangList.get(pos).getGiasp().multiply(new BigDecimal(gioHangList.get(pos).getSoluong()));
                    holder.txtTongtien.setText(format.format(tongtien));

                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgHinhanh,imgAdd,imgRemove;
        TextView txtTensp,txtPhanloai,txtGia, txtGiakm, txtSoluong,txtTongtien,txtThuonghieu;
        ImageClickListener listener;
        CheckBox cbkSanpham;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhanh = itemView.findViewById(R.id.itemgh_imgHinhanh);
            imgAdd = itemView.findViewById(R.id.imgAddSoLuong);
            imgRemove = itemView.findViewById(R.id.imgRemoveSoLuong);

            txtTensp = itemView.findViewById(R.id.itemgh_txtTensp);
            txtPhanloai = itemView.findViewById(R.id.itemgh_txtPhanloai);
            txtThuonghieu = itemView.findViewById(R.id.itemgh_txtThuongHieu);
            txtGia = itemView.findViewById(R.id.itemgh_txtGia);
            txtGiakm = itemView.findViewById(R.id.itemgh_txtGiaKM);
            txtSoluong = itemView.findViewById(R.id.itemgh_txtSoLuong);
            txtTongtien = itemView.findViewById(R.id.itemgh_txtTongtien);
            cbkSanpham = (CheckBox) itemView.findViewById(R.id.cbkSanPham);
            //action click add/remove
            imgAdd.setOnClickListener(this);
            imgRemove.setOnClickListener(this);
        }
        public void setListener(ImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if(view == imgRemove){
                listener.onImageClick(view,getAdapterPosition(),REMOVE_ITEM);
            } else if (view == imgAdd) {
                listener.onImageClick(view,getAdapterPosition(),ADD_ITEM);
            }else{

            }
        }
    }
}
