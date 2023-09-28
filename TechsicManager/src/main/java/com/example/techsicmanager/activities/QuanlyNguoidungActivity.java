package com.example.techsicmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techsicmanager.R;
import com.example.techsicmanager.adapters.TaiKhoanAdapter;
import com.example.techsicmanager.models.TaiKhoan;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanlyNguoidungActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TaiKhoanAdapter taiKhoanAdapter;
    private RecyclerView nguoidungRecyclerview;
    //khai bao list
    private List<TaiKhoan> taiKhoanList;
    private TaiKhoan taiKhoan;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    private boolean isLoading = false;
    int idloaisp;
    String loaisp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanlynguoidung);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        getWidget();
        actionToolBar();
        getNguoidung();
    }

    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbarQuanlyNguoidung);
        nguoidungRecyclerview = (RecyclerView) findViewById(R.id.recyclerNguoiDung);

        nguoidungRecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        nguoidungRecyclerview.setLayoutManager(layoutManager);
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

    private void getNguoidung() {
        compositeDisposable.add(apiconfig.getNguoidung("ORDER BY idtaikhoan desc")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taiKhoanModel -> {
                            if(taiKhoanModel.isSuccess()){
                                taiKhoanList = taiKhoanModel.getResult();
                                taiKhoanAdapter = new TaiKhoanAdapter(getApplicationContext(),taiKhoanList);
                                nguoidungRecyclerview.setAdapter(taiKhoanAdapter);
                            }
                        },
                        throwable -> {
                            Log.d("Error", "Không kết nối được với server "+throwable.getMessage());
                        }
                ));
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}