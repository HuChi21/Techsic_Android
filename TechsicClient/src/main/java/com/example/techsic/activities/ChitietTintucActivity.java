package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.adapters.TintucAdapter;
import com.example.techsic.adapters.TintucTheoloaiAdapter;
import com.example.techsic.models.TinTuc;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChitietTintucActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imgTintuc;
    private TextView tacgiaTintuc,thoigianTintuc,tieudeTintuc,noidungTintuc;
    private RecyclerView recyclerviewTintuc;
    private TintucAdapter tintucAdapter;
    private List<TinTuc> tinTucList;
    private TinTuc tinTuc;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiettintuc);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        tinTuc = (TinTuc) getIntent().getSerializableExtra("tintuc");

        getWidget();
        String moinhat = "where kieutintuc = \"" + tinTuc.getKieutintuc() + "\" order by rand()";
        actionToolbar();
        getThongtin();
        getTintucgoiy(moinhat);
    }


    private void getWidget() {
        toolbar = findViewById(R.id.toolBarChitietTintuc);
        imgTintuc = findViewById(R.id.imgTintuc);
        tacgiaTintuc = findViewById(R.id.tacgiaTintuc);
        thoigianTintuc = findViewById(R.id.thoigianTintuc);
        tieudeTintuc = findViewById(R.id.tieudeTintuc);
        noidungTintuc = findViewById(R.id.noidungTintuc);

        recyclerviewTintuc = findViewById(R.id.recyclerviewTintuc);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerviewTintuc.setLayoutManager(layoutManager);
        recyclerviewTintuc.setHasFixedSize(true);
    }
    private void actionToolbar() {
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

    private void getThongtin() {
        if(tinTuc.getHinhanh().contains("https")){
            Glide.with(getApplicationContext()).load(tinTuc.getHinhanh()).into(imgTintuc);
        }
        else{
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + tinTuc.getHinhanh();
            Glide.with(getApplicationContext()).load(hinhanh).into(imgTintuc);
        }
        tacgiaTintuc.setText(tinTuc.getTacgia());
        thoigianTintuc.setText(tinTuc.getThoigian());
        tieudeTintuc.setText(tinTuc.getTieude());
        noidungTintuc.setText(tinTuc.getNoidung());
    }

    private void getTintucgoiy(String moinhat) {
        compositeDisposable.add(apiconfig.getTintuc(moinhat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tinTucModel -> {
                            if(tinTucModel.isSuccess()){
                                tinTucList = tinTucModel.getResult();
                                tintucAdapter = new TintucAdapter(getApplicationContext(), tinTucList);
                                recyclerviewTintuc.setAdapter(tintucAdapter);
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
        startActivity(new Intent(getApplicationContext(),TintucActivity.class));
        finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
    }
}