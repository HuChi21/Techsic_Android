package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.techsic.R;
import com.example.techsic.adapters.DonHangAdapter;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonHangActivity extends AppCompatActivity {
    private RecyclerView donhangRecycler;
    private Toolbar toolbarDonhang;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apibanhang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xemdonhang);
        apibanhang = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWidget();
        actionToolBar();
        getDonHang();
    }

    private void getWidget() {
        donhangRecycler = (RecyclerView) findViewById(R.id.donHangRecyclerView);
        toolbarDonhang = (Toolbar) findViewById(R.id.toolBarDonHang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        donhangRecycler.setLayoutManager(layoutManager);
    }

    private void actionToolBar() {
        setSupportActionBar(toolbarDonhang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDonhang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void getDonHang() {
        compositeDisposable.add(apibanhang.getDonHang(RetrofitUtilities.taiKhoanGanDay.getIdtaikhoan())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            DonHangAdapter donHangAdapter = new DonHangAdapter(getApplicationContext(),donHangModel.getResult());
                            donhangRecycler.setAdapter(donHangAdapter);
                        },
                        throwable -> {
                            Toast.makeText(this, "Xảy ra lỗi "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}