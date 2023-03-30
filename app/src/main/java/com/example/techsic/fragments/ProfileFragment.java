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
import com.example.techsic.activities.InforActivity;
import com.example.techsic.activities.XemDonHangActivity;

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
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnThongTinNguoiDung:
                Intent thongtin = new Intent(getContext(), InforActivity.class);
                thongtin.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(thongtin);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
            case R.id.btnXemDonHang:
                Intent xemdon = new Intent(getContext(), XemDonHangActivity.class);
                xemdon.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(xemdon);
               getActivity().overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
        }
    }
}