package com.example.techsic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.techsic.Cache;
import com.example.techsic.R;
import com.example.techsic.adapters.LoaiSPAdapter;
import com.example.techsic.adapters.SanPhamMoiAdapter;
import com.example.techsic.models.LoaiSP;
import com.example.techsic.models.SanPham;
import com.example.techsic.models.TaiKhoan;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity  {
    //khai bao layout
    private Toolbar toolbar;
    private ImageView imgTimkiem,imgChat;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private NotificationBadge notifsoluong;
    private FrameLayout framecart;
    private FloatingActionButton fab_phone,fab_gmail,fab_messager;
    //khai bao list
    private ViewFlipper viewFlipper;
    private RecyclerView recyclerViewTrangChu,recyclerViewLoaiSP;
    private SanPhamMoiAdapter sanPhamMoiAdapter;
    //khai bao list
    private List<SanPham> sanPhamList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    boolean doubleBackToExitPressedOnce = false;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        //anhxa
        getWidget();

        actionBottomNav();
        actionToolBar();
        supportOnClick();
        Paper.init(this);
        if(Paper.book().read("taikhoan")!=null){
            TaiKhoan taiKhoan = Paper.book().read("taikhoan");
            RetrofitUtilities.taiKhoanGanDay = taiKhoan;
        }
        getTokenFirebase();
        if(isConnected(this)){
            getHome();
            actionViewFlipper();
        }else{
            Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getHome() {
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        //getWidget
        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        //khoi tao list
        sanPhamList = new ArrayList<>();
        compositeDisposable.add(apiconfig.getSPMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()){
                                sanPhamList = sanPhamModel.getResult();
                                for (int i=0;i<sanPhamList.size();i++){
                                    int sanphammoi = sanPhamList.get(0).getIdsp();
                                    Cache.getInstance().setData(sanphammoi);
                                }
                                sanPhamMoiAdapter = new SanPhamMoiAdapter(getApplicationContext(), sanPhamList);
                                recyclerViewTrangChu.setAdapter(sanPhamMoiAdapter);
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
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in);
        Animation slide_out  = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TintucActivity.class));
                overridePendingTransition(R.anim.slide_in,R.anim.nothing);
            }
        });
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
    private void getTokenFirebase(){
        FirebaseMessaging.getInstance().getToken()
            .addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if(!TextUtils.isEmpty(s)){
                        compositeDisposable.add(apiconfig.updateToken(RetrofitUtilities.taiKhoanGanDay.getIdtaikhoan(), s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        messageModel -> {
                                        },
                                        throwable -> {
                                            Toast.makeText(getApplicationContext(), "Lỗi " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                ));
                    }
                }
        });
    }
    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolBarTrangChu);
        imgTimkiem = (ImageView)findViewById(R.id.imgTimKiem);
        imgTimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
            }
        });
        imgChat = (ImageView)findViewById(R.id.imgChat);
        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
            }
        });
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        notifsoluong = (NotificationBadge) findViewById(R.id.notifSoLuong);
        framecart = (FrameLayout) findViewById(R.id.frameCart);
        navigationView = (NavigationView)  findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout)  findViewById(R.id.drawerLayout);

        fab_phone = findViewById(R.id.fab_phone);
        fab_gmail = findViewById(R.id.fab_gmail);
        fab_messager = findViewById(R.id.fab_messager);

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
        recyclerViewTrangChu = (RecyclerView)  findViewById(R.id.homeRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerViewTrangChu.setLayoutManager(layoutManager);
        recyclerViewTrangChu.setHasFixedSize(true);

        recyclerViewLoaiSP = (RecyclerView)  findViewById(R.id.loaispRecyclerview);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewLoaiSP.setLayoutManager(layoutManager1);
        recyclerViewLoaiSP.setHasFixedSize(true);

        compositeDisposable.add(apiconfig.getloaiSP()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    loaiSPModel -> {
                        if(loaiSPModel.isSuccess()){
                            List<LoaiSP> loaiSPList = loaiSPModel.getResult();
                            LoaiSPAdapter loaiSPAdapter = new LoaiSPAdapter(getApplicationContext(), loaiSPList);
                            recyclerViewLoaiSP.setAdapter(loaiSPAdapter);
                        }
                    },throwable -> {
                        Log.d("loaisp",throwable.getMessage());
                        }
                ));
    }
    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_home_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
                View headerView = navigationView.getHeaderView(0);
                TextView hoten = headerView.findViewById(R.id.header_txtHoten);
                ImageView img = headerView.findViewById(R.id.header_img);
                hoten.setText(RetrofitUtilities.taiKhoanGanDay.getHoten());
                hoten.setTextColor(0xFF121212);
                if(RetrofitUtilities.taiKhoanGanDay.getHinhanh().contains("https")){
                    Glide.with(getApplicationContext()).load(RetrofitUtilities.taiKhoanGanDay.getHinhanh()).into(img);
                }
                else{
                    String hinhanh = RetrofitInterface.BASE_URL+"images/" + RetrofitUtilities.taiKhoanGanDay.getHinhanh();
                    Glide.with(getApplicationContext()).load(hinhanh).into(img);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuDieukhoan:
                        Intent dieukhoan = new Intent(getApplicationContext(), DieuKhoanActivity.class);
                        startActivity(dieukhoan);
                        overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                        break;
                    case R.id.menuThongtinLienhe:
                        Intent lienhe = new Intent(getApplicationContext(), ThongtinLienheActivity.class);
                        startActivity(lienhe);
                        finish();
                        overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                        break;
                    case R.id.menuDangxuat:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Đăng xuất");
                        builder.setMessage("Bạn có muốn đăng xuất?").setCancelable(false)
                                .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent dangxuat = new Intent(getApplicationContext(), DangnhapActivity.class);
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(dangxuat);
                                        if(Paper.book().read("isRemember")==null){
                                            //dont delete taiKhoanGanDay
                                        }
                                        else if(Paper.book().read("isRemember")!=null){
                                            Paper.book().delete("taikhoan");
                                        }
                                        finish();
                                        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
                                    }
                                }).setNegativeButton("Huỷ bỏ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xFF121212);
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xFFFF8800);
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

    private void actionBottomNav() {
        bottomNavigationView.setSelectedItemId(R.id.menuHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
                        break;
                    case R.id.menuSanpham:
                        startActivity(new Intent(getApplicationContext(),SanphamActivity.class));
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
    public void supportOnClick(){
        fab_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel="0981575389";
                Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tel,null));
                startActivity(intent1);
            }
        });

        fab_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail="hotro.techsic@gmail.com";
                String password="asdasd@12";
                Intent intent2 = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto",mail,null));
                startActivity(intent2);
            }
        });
        fab_messager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FbUserID="zlbkz";
                Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("fb-messenger://user/" + FbUserID));
                startActivity(intent3);
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