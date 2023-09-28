package com.example.techsicmanager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techsicmanager.R;
import com.example.techsicmanager.adapters.SanphamAdapter;
import com.example.techsicmanager.models.SanPham;
import com.example.techsicmanager.models.eventbus.SuaXoaspEvent;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanlySanphamActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imgThem;
    private SanphamAdapter sanphamAdapter;
    private RecyclerView sanphamRecyclerview;
    //khai bao list
    private List<SanPham> sanPhamList;
    private SanPham sanPhamSuaXoa;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    private boolean isLoading = false;
    int idloaisp;
    String loaisp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanlysanpham);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        getWidget();
        actionToolBar();
        idloaisp = getIntent().getIntExtra("idloaisp",0);
        loaisp = getIntent().getStringExtra("loaisp");
        getSanPham(idloaisp);
    }

    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbarQuanlySanPham);
        sanphamRecyclerview = (RecyclerView) findViewById(R.id.recyclerSanPham);
        imgThem = (ImageView) findViewById(R.id.imgThemsp);

        sanphamRecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        sanphamRecyclerview.setLayoutManager(layoutManager);

        imgThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sanpham = new Intent(getApplicationContext(), ThemSanPhamActivity.class);
                sanpham.putExtra("loaisp",loaisp);
                sanpham.putExtra("idloaisp",idloaisp);
                startActivity(sanpham);
                finish();
                overridePendingTransition(R.anim.slide_in,R.anim.nothing);
            }
        });
    }
    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Quản lý "+ getIntent().getStringExtra("loaisp"));
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getSanPham(int idloaisp) {
        compositeDisposable.add(apiconfig.getSPtheoloai(idloaisp,"idsp desc")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()) {
                                    sanPhamList = sanPhamModel.getResult();
                                    sanphamAdapter = new SanphamAdapter(getApplicationContext(), sanPhamList);
                                    sanphamRecyclerview.setAdapter(sanphamAdapter);
                            }},
                            throwable -> {
                                Log.d("Error", "Không kết nối được với server "+throwable.getMessage());
                        }
                ));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == 0){
            suaSanPham();
        } else if (item.getItemId() == 1) {
            xoaSanPham();
        }
        return super.onContextItemSelected(item);
    }

    private void suaSanPham() {
        Intent intent = new Intent(getApplicationContext(), SuaSanPhamActivity.class);
        intent.putExtra("sanPhamSuaXoa",sanPhamSuaXoa);
        intent.putExtra("loaisp",loaisp);
        startActivity(intent);
    }
    private void xoaSanPham() {
        compositeDisposable.add(apiconfig.xoaSP(sanPhamSuaXoa.getIdsp())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel ->{
                            if(messageModel.isSuccess()){
                                getSanPham(idloaisp);
                                Toast.makeText(this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventSuaXoasp(SuaXoaspEvent event){
        if(event != null){
            sanPhamSuaXoa = event.getSanPham();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}