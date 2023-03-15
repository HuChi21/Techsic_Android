package com.example.techsic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.adapters.LoaiSPAdapter;
import com.example.techsic.adapters.SPMoiAdapter;
import com.example.techsic.models.LoaiSP;
import com.example.techsic.models.SanPham;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    //khai bao
    private Toolbar toolbar;
    private ViewFlipper viewFlipper;
    private RecyclerView recyclerViewTrangChu;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private ListView listViewTrangChu;
    private DrawerLayout drawerLayout;
    private LoaiSPAdapter loaiSPAdapter;
    private SPMoiAdapter spMoiAdapter;
    //khai bao list
    private List<LoaiSP> loaiSPList;
    private List<SanPham> SanPhamList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apibanhang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apibanhang = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);


        //anhxa
        getWidget();
        actionBar();
        actionBottomNav();
        if(isConnected(this)){

            actionViewFlipper();
            getLoaiSP();
            getSPMoi();
        }else{
            Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
        }

    }

    private void actionBottomNav() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.itemHome:
                        break;
                    case R.id.itemDienthoai:
                        break;
                    case R.id.itemLaptop:
                        break;
                    case R.id.itemTablet:
                        break;
                    case R.id.itemProfile:
                        break;
                }
                return true;
            }
        });
    }

    private void getSPMoi() {
        compositeDisposable.add(apibanhang.getSPMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()){
                                SanPhamList = sanPhamModel.getResult();
                                spMoiAdapter = new SPMoiAdapter(getApplicationContext(),SanPhamList);
                                recyclerViewTrangChu.setAdapter(spMoiAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getLoaiSP() {
        compositeDisposable.add(apibanhang.getLoaiSP()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSPModel -> {
                            if (loaiSPModel.isSuccess()){
                            loaiSPList =loaiSPModel.getResult();
                            loaiSPAdapter = new LoaiSPAdapter(getApplicationContext(), loaiSPList);
                            listViewTrangChu.setAdapter(loaiSPAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    private void actionViewFlipper() {
        List<String> ArrayQC = new ArrayList<>();
        //Add model tintuc
        ArrayQC.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/720x220-720x220-25.png");
        ArrayQC.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/realme720-220-720x220-3.png");
        ArrayQC.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/s23-720-220-720x220-7.png");
        ArrayQC.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/02/banner/reno8t-720-220-720x220-8.png");
        for(int i=0; i<ArrayQC.size();i++){
            ImageView imageView = new ImageView((getApplicationContext()));
            Glide.with(getApplicationContext()).load(ArrayQC.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_right_in);
        Animation slide_out  = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_right_out);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolBarTrangChu);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        recyclerViewTrangChu = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewTrangChu.setLayoutManager(layoutManager);

        recyclerViewTrangChu.setHasFixedSize(true);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        listViewTrangChu = (ListView) findViewById(R.id.listViewTrangChu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        //khoi tao list
        loaiSPList = new ArrayList<>();
        SanPhamList = new ArrayList<>();
        //khoi tao adapter

    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo cellular = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected())||(cellular != null && cellular.isConnected())){
            return true;
        }
        else return false;

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}