package com.example.techsic.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techsic.R;
import com.example.techsic.activities.NguoiDungActivity;
import com.example.techsic.activities.XemDonHangActivity;
import com.example.techsic.retrofit.RetrofitUtilities;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private ImageView imganh;
    private TextView txtHoten;
    private Button btnThongtinnguoidung,btnXemdonhang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        //getWidget();
        imganh = (ImageView) view.findViewById(R.id.imgAnh);
        txtHoten = (TextView) view.findViewById(R.id.txtHoTenNguoiDung);
        btnThongtinnguoidung = (Button) view.findViewById(R.id.btnThongTinNguoiDung);
        btnXemdonhang = (Button) view.findViewById(R.id.btnXemDonHang);

        btnThongtinnguoidung.setOnClickListener(this);
        btnXemdonhang.setOnClickListener(this);
        txtHoten.setText(RetrofitUtilities.taiKhoanGanDay.getHoten());
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnThongTinNguoiDung:
                Intent thongtin = new Intent(getContext(), NguoiDungActivity.class);
                startActivity(thongtin);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
            case R.id.btnXemDonHang:
                Intent xemdon = new Intent(getContext(), XemDonHangActivity.class);
                startActivity(xemdon);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
        }
    }
}