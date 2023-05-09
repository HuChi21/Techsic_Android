package com.example.techsicmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techsicmanager.R;
import com.example.techsicmanager.adapters.DonHangAdapter;
import com.example.techsicmanager.models.DonHang;
import com.example.techsicmanager.models.TaiKhoan;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanlyDonhangActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DonHangAdapter donHangAdapter;
    private RecyclerView donhangRecyclerview;
    //khai bao list
    private List<DonHang> donHangList;
    private DonHang donHang;
    private TaiKhoan taiKhoan;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanlydonhang);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        donHang =(DonHang) getIntent().getSerializableExtra("donhang");
        taiKhoan = (TaiKhoan) getIntent().getSerializableExtra("taikhoan");
        getWidget();
        actionToolBar();
        if(taiKhoan == null){
            getDonhang();
        }
        else{
            getDonhangtheoNguoidung(taiKhoan.getIdtaikhoan(),taiKhoan.getHoten());
        }

    }

    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbarQuanlyDonhang);
        donhangRecyclerview = (RecyclerView) findViewById(R.id.recyclerDonhang);

        donhangRecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        donhangRecyclerview.setLayoutManager(layoutManager);
    }


    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getDonhang() {
        compositeDisposable.add(apiconfig.getDonhang("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            if(donHangModel.isSuccess()){
                                donHangList = donHangModel.getResult();
                                donHangAdapter = new DonHangAdapter(getApplicationContext(), donHangList);
                                donhangRecyclerview.setAdapter(donHangAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }
    private void getDonhangtheoNguoidung(int idtaikhoan,String hoten) {
        toolbar.setTitle("Đơn hàng của: "+hoten);
        compositeDisposable.add(apiconfig.getDonhangtheoNguoidung(idtaikhoan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            if(donHangModel.isSuccess()){
                                donHangList = donHangModel.getResult();
                                donHangAdapter = new DonHangAdapter(getApplicationContext(), donHangList);
                                donhangRecyclerview.setAdapter(donHangAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }



    @Override
    public void onBackPressed() {
        if(taiKhoan == null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        else{
            finish();
        }
        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}