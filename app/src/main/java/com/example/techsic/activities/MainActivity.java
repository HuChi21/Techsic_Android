package com.example.techsic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.techsic.R;
import com.example.techsic.adapters.LoaiSanPhamAdapter;
import com.example.techsic.adapters.ViewPagerAdapter;
import com.example.techsic.models.LoaiSP;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    //khai bao layout
    private Toolbar toolbar;
    private ListView listViewTrangChu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewpager;
    private NotificationBadge notifsoluong;
    private FrameLayout framecart;
    //khai bao adapter
    private ViewPagerAdapter viewPagerAdapter;
    private LoaiSanPhamAdapter loaiSanPhamAdapter;
    //khai bao list
    private List<LoaiSP> loaiSPList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apibanhang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        apibanhang = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        //anhxa
        getWidget();
        actionBottomNav();
        actionBar();
        if(isConnected(this)){
//            getLoaiSP();
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

    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolBarTrangChu);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        viewpager = (ViewPager) findViewById(R.id.viewPager);
        notifsoluong = (NotificationBadge) findViewById(R.id.notifSoLuong);
        framecart = (FrameLayout) findViewById(R.id.frameCart);
        navigationView = (NavigationView)  findViewById(R.id.navigationView);
//        listViewTrangChu = (ListView)  findViewById(R.id.listViewTrangChu);
        drawerLayout = (DrawerLayout)  findViewById(R.id.drawerLayout);
        //khoi tao list
//        loaiSPList = new ArrayList<>();
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
            }
        });
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
    private void actionBottomNav() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.menuDienthoai:
                        viewpager.setCurrentItem(1);
                        break;
                    case R.id.menuLaptop:
                        viewpager.setCurrentItem(2);
                        break;
                    case R.id.menuTablet:
                        viewpager.setCurrentItem(3);
                        break;
                    case R.id.menuProfile:
                        viewpager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.menuHome:
//                        break;
//                    case R.id.menuDienthoai:
//                        break;
//                    case R.id.menuLaptop:
//                        break;
//                    case R.id.menuTablet:
//                        break;
//                    case R.id.menuProfile:
//                        break;
//                }
//                return true;
//            }
//        });
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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuThongtin:
                        Intent thongtin = new Intent(getApplicationContext(),ThongtinActivity.class);
                        thongtin.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(thongtin);
                        finish();
                        break;
                    case R.id.menuLienhe:
                        Intent lienhe = new Intent(getApplicationContext(),LienheActivity.class);
                        lienhe.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(lienhe);
                        finish();
                        break;
                    case R.id.menuDangxuat:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Đăng xuất");
                        builder.setMessage("Bạn có muốn đăng xuất?").setCancelable(false)
                                .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent dangxuat = new Intent(getApplicationContext(), DangnhapActivity.class);
                                        dangxuat.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(dangxuat);
                                        finish();
                                    }
                                }).setNegativeButton("Huỷ bỏ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.corner_button);
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xFFFFFFFF);
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xFFFF8800);
                            }
                        });
                        alertDialog.show();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    private void actionViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menuHome).setChecked(true);
                        toolbar.setTitle("Home");
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menuDienthoai).setChecked(true);
                        toolbar.setTitle("Điên thoại");
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menuLaptop).setChecked(true);
                        toolbar.setTitle("Laptop");
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menuTablet).setChecked(true);
                        toolbar.setTitle("Tablet");
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.menuProfile).setChecked(true);
                        toolbar.setTitle("Profile");
                        break;

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
//    private void getLoaiSP() {
//        compositeDisposable.add(apibanhang.getLoaiSP()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        loaiSPModel -> {
//                            if (loaiSPModel.isSuccess()){
//                                loaiSPList =loaiSPModel.getResult();
//                                loaiSPAdapter = new LoaiSPAdapter(getApplicationContext(), loaiSPList);
//                                listViewTrangChu.setAdapter(loaiSPAdapter);
//                            }
//                        },
//                        throwable -> {
//                            Toast.makeText(getApplicationContext(), "Không kết nối được với server "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                ));
//    }

}