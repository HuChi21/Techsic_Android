package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.techsic.R;
import com.example.techsic.adapters.GioHangAdapter;
import com.example.techsic.models.GioHang;
import com.example.techsic.models.eventbus.TinhTongEvent;
import com.example.techsic.retrofit.RetrofitUtilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class GiohangActivity extends AppCompatActivity {
    //private ImageView imgHinhanh; //imgAdd,imgRemove
    private TextView txtTongthanhtoan; //txtTensp,txtPhanloai,txtGia,txtGiakm,txtSoLuong,
    private RecyclerView giohangRecyclerview;
    private ConstraintLayout layoutGiohangtrong;
    private Button btnBuy;
    private Toolbar toolbarGioHang;

    private List<GioHang> gioHangList;
    private GioHangAdapter gioHangAdapter;
    private long tongtt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWidget();
        actionGioHang();
        actionTongThanhToan();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event){
        if(event!= null){
            actionTongThanhToan();
        }
    }

    private void actionTongThanhToan() {
        tongtt = 0;
        for(int i = 0; i<RetrofitUtilities.giohanglist.size(); i++){

            tongtt = tongtt + RetrofitUtilities.giohanglist.get(i).getGiasp().longValue() * RetrofitUtilities.giohanglist.get(i).getSoluong();
        }
        BigDecimal tongthanhtoan = BigDecimal.valueOf(tongtt);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        txtTongthanhtoan.setText(format.format(tongthanhtoan));
        btnBuy.setText("Mua ngay("+RetrofitUtilities.giohanglist.size()+")");
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TinhtienActivity.class);
                intent.putExtra("txtTongTien", tongtt);
                startActivity(intent);
            }
        });
    }

    private void actionGioHang() {
        setSupportActionBar(toolbarGioHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGioHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        giohangRecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        giohangRecyclerview.setLayoutManager(layoutManager);
        if(RetrofitUtilities.giohanglist.size() == 0){
            layoutGiohangtrong.setVisibility(View.VISIBLE);
            btnBuy.setAlpha(.5f);
            btnBuy.setEnabled(false);
//            btnBuy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in, R.anim.nothing);
//                    finish();
//                }
//            });
        }else{
            gioHangAdapter = new GioHangAdapter(getApplicationContext(),RetrofitUtilities.giohanglist);
            giohangRecyclerview.setAdapter(gioHangAdapter);
        }
    }

    private void getWidget() {
        layoutGiohangtrong = (ConstraintLayout) findViewById(R.id.layoutGioHangTrong);
        txtTongthanhtoan = (TextView) findViewById(R.id.txtTongThanhToan);
        giohangRecyclerview = (RecyclerView) findViewById(R.id.gioHangRecyclerView);
        btnBuy = (Button) findViewById(R.id.btnBuy);
        toolbarGioHang = (Toolbar) findViewById(R.id.toolBarGioHang);

    }
}