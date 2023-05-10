package com.example.techsic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techsic.R;
import com.example.techsic.adapters.ViewPagerAdapter;
import com.example.techsic.models.TaiKhoan;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SanphamActivity extends AppCompatActivity  {
    //khai bao layout
    private Toolbar toolbar;
    private ImageView imgTimkiem;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView,sanphamNavigationView;
    private ViewPager viewpager;
    private NotificationBadge notifsoluong;
    private FrameLayout framecart;
    //khai bao adapter
    private ViewPagerAdapter viewPagerAdapter;
    //khai bao list
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanpham);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        //anhxa
        getWidget();

        actionBottomNav();
        actionToolBar();

        if(isConnected(this)){
            actionViewPager();
        }else{
            Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int tongItem = 0;
        for(int i = 0; i<RetrofitUtilities.giohanglist.size(); i++){
            tongItem += RetrofitUtilities.giohanglist.get(i).getSoluong();
        }
        notifsoluong.setText(String.valueOf(tongItem));
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
    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolBarTrangChu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        imgTimkiem = (ImageView)findViewById(R.id.imgTimKiem);
        imgTimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
            }
        });
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        sanphamNavigationView = (BottomNavigationView) findViewById(R.id.sanpham_nav);
        viewpager = (ViewPager) findViewById(R.id.viewPager);
        notifsoluong = (NotificationBadge) findViewById(R.id.notifSoLuong);
        framecart = (FrameLayout) findViewById(R.id.frameCart);
        navigationView = (NavigationView)  findViewById(R.id.navigationView);


        //khoi tao list
        if(RetrofitUtilities.giohanglist == null){
            RetrofitUtilities.giohanglist = new ArrayList<>();
        }else {
            int tongItem = 0;
            for (int i = 0; i < RetrofitUtilities.giohanglist.size(); i++) {
                tongItem += RetrofitUtilities.giohanglist.get(i).getSoluong();
            }
            notifsoluong.setText(String.valueOf(tongItem));
        }
        framecart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),GiohangActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
            }
        });
    }
    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_home_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
                overridePendingTransition(R.anim.nothing,R.anim.slide_out);
            }
        });
    }
    private void actionViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        sanphamNavigationView.getMenu().findItem(R.id.menuDienthoai).setChecked(true);
                        toolbar.setTitle("Điện thoại");
                        break;
                    case 1:
                        sanphamNavigationView.getMenu().findItem(R.id.menuLaptop).setChecked(true);
                        toolbar.setTitle("Laptop");
                        break;
                    case 2:
                        sanphamNavigationView.getMenu().findItem(R.id.menuTablet).setChecked(true);
                        toolbar.setTitle("Tablet");
                        break;
                    case 3:
                        sanphamNavigationView.getMenu().findItem(R.id.menuPhukien).setChecked(true);
                        toolbar.setTitle("Phụ kiện");
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int itemIndex = getIntent().getIntExtra("item_index", 0);
        viewpager.setCurrentItem(itemIndex); // thiết lập item được chọn cho ViewPager
        sanphamNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuDienthoai:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.menuLaptop:
                        viewpager.setCurrentItem(1);
                        break;
                    case R.id.menuTablet:
                        viewpager.setCurrentItem(2);
                        break;
                    case R.id.menuPhukien:
                        viewpager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }

    private void actionBottomNav() {
        bottomNavigationView.setSelectedItemId(R.id.menuSanpham);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(R.anim.nothing,R.anim.nothing);
                        break;
                    case R.id.menuProfile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(R.anim.nothing,R.anim.nothing);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Chạm lần nữa để thoát !", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}