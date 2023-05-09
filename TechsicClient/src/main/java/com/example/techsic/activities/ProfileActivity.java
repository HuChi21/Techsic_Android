package com.example.techsic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.adapters.ViewPagerAdapter;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;

import io.paperdb.Paper;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView imgTimkiem;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private NotificationBadge notifsoluong;
    private FrameLayout framecart;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int count = 0;
    private LinearLayout linearLayout;
    private ImageView imganh;
    private TextView txtHoten;
    private Button btnThongtinnguoidung,btnXemdonhang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWidget();
        actionToolBar();
        actionBottomNav();
        if(isConnected(this)){

        }else{
            Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
        }
    }
    private void getWidget() {
        imganh = (ImageView) findViewById(R.id.imgAnh);
        if(RetrofitUtilities.taiKhoanGanDay.getHinhanh().contains("https")){
            Glide.with(getApplicationContext()).load(RetrofitUtilities.taiKhoanGanDay.getHinhanh()).into(imganh);
        }
        else{
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + RetrofitUtilities.taiKhoanGanDay.getHinhanh();
            Glide.with(getApplicationContext()).load(hinhanh).into(imganh);
        }
        txtHoten = (TextView) findViewById(R.id.txtHoTenNguoiDung);
        txtHoten.setText(RetrofitUtilities.taiKhoanGanDay.getHoten());
        btnThongtinnguoidung = (Button) findViewById(R.id.btnThongTinNguoiDung);
        btnXemdonhang = (Button) findViewById(R.id.btnXemDonHang);

        btnThongtinnguoidung.setOnClickListener(this);
        btnXemdonhang.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolBarTrangChu);
        linearLayout = (LinearLayout) findViewById(R.id.layoutSapxep);
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
        notifsoluong = (NotificationBadge) findViewById(R.id.notifSoLuong);
        framecart = (FrameLayout) findViewById(R.id.frameCart);
        navigationView = (NavigationView)  findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout)  findViewById(R.id.drawerLayout);

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
        bottomNavigationView.setSelectedItemId(R.id.menuProfile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(R.anim.nothing,R.anim.nothing);
                        break;
                    case R.id.menuSanpham:
                        startActivity(new Intent(getApplicationContext(),SanphamActivity.class));
                        overridePendingTransition(R.anim.nothing,R.anim.nothing);
                        break;
                    case R.id.menuProfile:
                        Toast.makeText(getApplicationContext(), "nothing", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnThongTinNguoiDung:
                Intent thongtin = new Intent(getApplicationContext(), NguoiDungActivity.class);
                startActivity(thongtin);
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
            case R.id.btnXemDonHang:
                Intent xemdon = new Intent(getApplicationContext(), XemDonHangActivity.class);
                startActivity(xemdon);
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
        }
    }
}