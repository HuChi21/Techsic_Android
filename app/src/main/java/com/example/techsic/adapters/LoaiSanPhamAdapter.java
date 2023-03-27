package com.example.techsic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.models.LoaiSP;

import java.util.List;

public class LoaiSanPhamAdapter extends BaseAdapter {

    List<LoaiSP> array;
    Context context;

    public LoaiSanPhamAdapter(Context context, List<LoaiSP> array) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        TextView textLoaiSP;
        ImageView imgHinhAnh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder viewHolder = null;
         if(convertView == null){
             viewHolder = new ViewHolder();
             LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             convertView = layoutInflater.inflate(R.layout.item_loaisanpham,null);
             viewHolder.textLoaiSP = convertView.findViewById(R.id.itemloaisp_tenloaisp);
             viewHolder.imgHinhAnh = convertView.findViewById(R.id.itemloaisp_imageloaisp);
             convertView.setTag(viewHolder);
         }else{
             viewHolder =(ViewHolder) convertView.getTag();

         }
        viewHolder.textLoaiSP.setText(array.get(position).getTenloaisp());
        Glide.with(context).load(array.get(position).getHinhanh()).into(viewHolder.imgHinhAnh);
         return convertView;
    }
}
