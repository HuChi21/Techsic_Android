package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techsic.R;
import com.example.techsic.adapters.ChiTietDonHangAdapter;
import com.example.techsic.firebase.NotificationSendData;
import com.example.techsic.firebase.PushNotification;
import com.example.techsic.firebase.RetrofitPushNotif;
import com.example.techsic.models.DonHang;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChitietdonhangActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView donhangRecyclerview;
    private Toolbar toolbar;
    private TextView txtIddonhang, txtHoten, txtEmail, txtSodt, txtDiachi, txtGhichu, txtNgaydat, txtNgaynhan, txtTongthanhtoan, txtTinhtrang,txthinhthuc;
    private Button btnThanhtoan, btnHuydon;
    private ImageView btnChat;
    DonHang donHang;
    String kieutinhtrang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    PushNotification apipushnotif;
    View overlay;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietdonhang);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        apipushnotif = RetrofitPushNotif.getRetrofit().create(PushNotification.class);
        donHang = (DonHang) getIntent().getSerializableExtra("donhang");
        kieutinhtrang = getIntent().getStringExtra("kieutinhtrang");
        getWidget();
        getThongtindonhang();
        actionToolBar();
    }

    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbarChitietdonhang);
        txtIddonhang = (TextView) findViewById(R.id.txtIdDonHang);
        txtHoten = (TextView) findViewById(R.id.txtTenNguoiDat);
        txtSodt = (TextView) findViewById(R.id.txtSoDTNguoiDat);
        txtEmail = (TextView) findViewById(R.id.txtEmailNguoiDat);
        txtDiachi = (TextView) findViewById(R.id.txtDiaChiDonHang);
        txtGhichu = (TextView) findViewById(R.id.txtGhiChuDonHang);
        txtNgaydat = (TextView) findViewById(R.id.txtNgayDatDonHang);
        txtNgaynhan = (TextView) findViewById(R.id.txtNgayNhanDonHang);
        txtTongthanhtoan = (TextView) findViewById(R.id.txtTongThanhToanDonHang);
        txtTinhtrang = (TextView) findViewById(R.id.txtTinhtrang);
        txthinhthuc = (TextView) findViewById(R.id.txtHinhthuc);
        btnThanhtoan = (Button) findViewById(R.id.btnThanhtoan);
        btnHuydon = (Button) findViewById(R.id.btnHuydon);
        btnChat = (ImageView) findViewById(R.id.btnChat);
        donhangRecyclerview = (RecyclerView) findViewById(R.id.donHangRecyclerView);

        btnChat.setOnClickListener(this);
        if (donHang.getIdtinhtrang()== 0) {  //tinh trang = chua thanh toan
            btnThanhtoan.setVisibility(View.VISIBLE);
            btnThanhtoan.setOnClickListener(this);
            txtTinhtrang.setTextColor(0xFFD0342C);
        } else if (donHang.getIdtinhtrang() == 5) { //tinh trang = hoan thanh
            btnHuydon.setAlpha(.5f);
            btnHuydon.setClickable(false);
            txtTinhtrang.setTextColor(0xFF00FF00);
        }else if (donHang.getIdtinhtrang() == 6) { //tinh trang = huy don
            btnHuydon.setAlpha(.5f);
            btnHuydon.setClickable(false);
            txtTinhtrang.setTextColor(0xFFD0342C);
            btnThanhtoan.setVisibility(View.GONE);
        } else {
            txtTinhtrang.setTextColor(0xFF121212);
        }
        btnHuydon.setOnClickListener(this);
        progressBar = findViewById(R.id.progress_bar);
        overlay = findViewById(R.id.overlay);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnThanhtoan:
                thanhtoanDonhang();
                break;
            case R.id.btnHuydon:
                kieutinhtrang = "Huỷ đơn";
                huyDonhang(kieutinhtrang);
                break;
            case R.id.btnChat:
                Toast.makeText(this, "Chat thoi nèo", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getThongtindonhang() {
        txtIddonhang.setText(String.valueOf(donHang.getId()));
        txtHoten.setText(donHang.getHoten());
        txtSodt.setText(donHang.getSodt());
        txtEmail.setText(donHang.getEmail());
        txtDiachi.setText(donHang.getDiachi());
        txtGhichu.setText(donHang.getGhichu());
        txtNgaydat.setText(donHang.getNgaydat());
        txtNgaynhan.setText(donHang.getNgaynhan());
        txtTongthanhtoan.setText(donHang.getTongthanhtoan());
        txthinhthuc.setText(donHang.getHinhthuc());
        txtTinhtrang.setText(kieutinhtrang);

        compositeDisposable.add(apiconfig.getChitietdonhang(donHang.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        itemModel -> {
                            //chitietdonhang adapter
                            LinearLayoutManager layoutManager = new LinearLayoutManager(
                                    txtIddonhang.getContext(),
                                    LinearLayoutManager.VERTICAL, false
                            );
                            layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
                            ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(getApplicationContext(), itemModel.getResult());
                            donhangRecyclerview.setLayoutManager(layoutManager);
                            donhangRecyclerview.setAdapter(chiTietDonHangAdapter);
                        },
                        throwable -> {
                            Toast.makeText(this, "Xảy ra lỗi " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void thanhtoanDonhang() {
        Intent intent = new Intent(getApplicationContext(),ThanhtoanActivity.class);
        intent.putExtra("donHang",donHang);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_in);
    }

    private void huyDonhang(String kieutinhtrang) {
        compositeDisposable.add(apiconfig.setTinhtrang(donHang.getId(),kieutinhtrang )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            progressBar.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    pushNotifToAdmin(donHang.getId(),kieutinhtrang);
                                    btnHuydon.setAlpha(.5f);
                                    btnHuydon.setClickable(false);
                                    txtTinhtrang.setTextColor(0xFFD0342C);
                                    txtTinhtrang.setText(kieutinhtrang);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 2500);
                        }, throwable -> {
                            Log.d("error", throwable.getMessage());
                        }
                ));
    }

    private void pushNotifToAdmin(int iddonhang, String kieutrangthai) {
        compositeDisposable.add(apiconfig.getToken(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taiKhoanModel -> {
                            if (taiKhoanModel.isSuccess()) {
                                for (int i = 0; i < taiKhoanModel.getResult().size(); i++) {
                                    String token = taiKhoanModel.getResult().get(i).getToken();
                                    Map<String, String> data = new HashMap<>();
                                    data.put("title", "Cập nhật đơn hàng");
                                    data.put("body", "Đơn hàng có mã " + iddonhang + " : " + kieutrangthai.toLowerCase());
                                    NotificationSendData notificationSendData = new NotificationSendData(token, data);
                                    compositeDisposable.add(apipushnotif.sendNotification(notificationSendData)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notificationResponse -> {
                                                    },
                                                    throwable -> {
                                                        Log.d("error", throwable.getMessage());
                                                    }
                                            ));
                                }
                            }
                        },
                        throwable -> {
                            Log.d("error", throwable.getMessage());
                        }
                ));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}