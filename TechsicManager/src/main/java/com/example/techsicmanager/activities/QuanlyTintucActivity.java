package com.example.techsicmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.techsicmanager.R;
import com.example.techsicmanager.adapters.TintucAdapter;
import com.example.techsicmanager.models.TinTuc;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanlyTintucActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TintucAdapter tintucAdapter;
    private ImageView imgThem;
    private RecyclerView tintucRecyclerview;
    //khai bao list
    private List<TinTuc> tinTucList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanlytintuc);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        getWidget();
        actionToolBar();
        String moinhat = "order by idtintuc desc";
        getTintuc(moinhat);
    }
    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbarTinTuc);
        imgThem = (ImageView) findViewById(R.id.imgThemtintuc);
        tintucRecyclerview = (RecyclerView) findViewById(R.id.recyclerTintuc);

        tintucRecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tintucRecyclerview.setLayoutManager(layoutManager);
        imgThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tintuc = new Intent(getApplicationContext(), ThemTinTucActivity.class);
                startActivity(tintuc);
                finish();
                overridePendingTransition(R.anim.slide_in,R.anim.nothing);
            }
        });
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
    private void getTintuc(String moinhat) {
        compositeDisposable.add(apiconfig.getTintuc(moinhat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tinTucModel -> {
                            if(tinTucModel.isSuccess()){
                                tinTucModel.getResult().remove(0);
                                tinTucList = tinTucModel.getResult();
                                tintucAdapter = new TintucAdapter(getApplicationContext(), tinTucList);
                                tintucRecyclerview.setAdapter(tintucAdapter);
                            }
                        },
                        throwable -> {
                            Log.d("Loi tin tuc", "" + throwable.getMessage());
                        }
                ));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
    }
}