package com.example.techsicmanager.activities;

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
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techsicmanager.R;
import com.example.techsicmanager.adapters.ThongKeMainAdapter;
import com.example.techsicmanager.models.DonHang;
import com.example.techsicmanager.models.TaiKhoan;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //khai bao layout
    private Toolbar toolbar;
    private Button btnSanpham,btnTintuc,btnNguoidung,btnDonhang,btnDienthoai,btnLaptop,btnTablet,btnPhuKien;
    private ImageView btnClosebottom,imgChat;
    private TextView btnThongke;
    private DrawerLayout drawerLayout;
    private NavigationView navigationViewAdmin;
    private LinearLayout layout;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //anhxa
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        getWidget();
        getTokenFirebase();
        getThongtin();
        Paper.init(this);
        if(Paper.book().read("taikhoan")!=null){
            TaiKhoan taiKhoan = Paper.book().read("taikhoan");
            RetrofitUtilities.taiKhoanGanDay = taiKhoan;
        }
        actionToolBar();
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
                                                Log.d("Id va token", RetrofitUtilities.taiKhoanGanDay.getIdtaikhoan()+"  và  "+ s);
                                            },
                                            throwable -> {
                                                Log.d("Error", "Không kết nối được với server "+throwable.getMessage());
                                            }
                                    ));
                        }
                    }
                });
    }
    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolBarTrangChu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        viewpager = (ViewPager) findViewById(R.id.viewPager);
        navigationViewAdmin = (NavigationView)  findViewById(R.id.navigationViewAdmin);
        drawerLayout = (DrawerLayout)  findViewById(R.id.drawerLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewThongKemain);
        layout = (LinearLayout)findViewById(R.id.layoutBottomSanPham);
        bottomSheetBehavior = BottomSheetBehavior.from(layout);

        btnSanpham = (Button) findViewById(R.id.btnQuanLySanPham);
        btnTintuc = (Button) findViewById(R.id.btnQuanLyTinTuc);
        btnNguoidung = (Button) findViewById(R.id.btnQuanLyNguoiDung);
        btnDonhang = (Button) findViewById(R.id.btnQuanLyDonHang);
        btnDienthoai = (Button) findViewById(R.id.btnDienThoai);
        btnLaptop = (Button) findViewById(R.id.btnLapTop);
        btnTablet = (Button) findViewById(R.id.btnTablet);
        btnPhuKien = (Button) findViewById(R.id.btnPhuKien);
        btnThongke = (TextView) findViewById(R.id.btnThongke);
        btnClosebottom = (ImageView) findViewById(R.id.btnClosebottom);
        imgChat = (ImageView) findViewById(R.id.imgChat);

        btnSanpham.setOnClickListener(this);
        btnTintuc.setOnClickListener(this);
        btnNguoidung.setOnClickListener(this);
        btnDonhang.setOnClickListener(this);
        btnThongke.setOnClickListener(this);
        imgChat.setOnClickListener(this);

        btnClosebottom.setOnClickListener(this);
        btnDienthoai.setOnClickListener(this);
        btnLaptop.setOnClickListener(this);
        btnTablet.setOnClickListener(this);
        btnPhuKien.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgChat:
                startActivity(new Intent(getApplicationContext(), QuanlyChatActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
            case R.id.btnQuanLySanPham:
                if(bottomSheetBehavior.getState() != bottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);
                }else {
                    bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.btnQuanLyTinTuc:
                startActivity(new Intent(getApplicationContext(), QuanlyTintucActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
            case R.id.btnQuanLyNguoiDung:
                startActivity(new Intent(getApplicationContext(), QuanlyNguoidungActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
            case R.id.btnQuanLyDonHang:
                startActivity(new Intent(getApplicationContext(),QuanlyDonhangActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
            case R.id.btnThongke:
                startActivity(new Intent(getApplicationContext(),ThongkeActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
            case R.id.btnClosebottom:
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btnDienThoai:
                Intent dienthoai = new Intent(getApplicationContext(), QuanlySanphamActivity.class);
                dienthoai.putExtra("loaisp","Điện thoại");
                dienthoai.putExtra("idloaisp",1);
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                startActivity(dienthoai);
                overridePendingTransition(R.anim.slide_in,R.anim.nothing);
                break;
            case R.id.btnLapTop:
                Intent laptop = new Intent(getApplicationContext(), QuanlySanphamActivity.class);
                laptop.putExtra("loaisp","Laptop");
                laptop.putExtra("idloaisp",2);
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                startActivity(laptop);
                overridePendingTransition(R.anim.slide_in,R.anim.nothing);
                break;
            case R.id.btnTablet:
                Intent tablet = new Intent(getApplicationContext(), QuanlySanphamActivity.class);
                tablet.putExtra("loaisp","Tablet");
                tablet.putExtra("idloaisp",3);
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                startActivity(tablet);
                overridePendingTransition(R.anim.slide_in,R.anim.nothing);
                break;
            case R.id.btnPhuKien:
                Intent phukien = new Intent(getApplicationContext(), QuanlySanphamActivity.class);
                phukien.putExtra("loaisp","Phụ kiện");
                phukien.putExtra("idloaisp",4);
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                startActivity(phukien);
                overridePendingTransition(R.anim.slide_in,R.anim.nothing);
                break;
        }
    }
    private void getThongtin() {
        compositeDisposable.add(apiconfig.getDonhang("WHERE donhang_tinhtrang.idtinhtrang IN (5,7) " +
                        "ORDER BY STR_TO_DATE(donhang.ngaynhan, '%d/%m/%Y')  DESC LIMIT 10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            if(donHangModel.isSuccess()){
                                List<DonHang> donHangList = donHangModel.getResult();
                                ThongKeMainAdapter thongKeAdapter = new ThongKeMainAdapter(getApplicationContext(), donHangList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(thongKeAdapter);
                            }
                        },
                        throwable -> {
                            Log.d("Error", "Không kết nối được với server "+throwable.getMessage());
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
            }
        });

        if (RetrofitUtilities.taiKhoanGanDay.getIsadmin()==1) {
            navigationViewAdmin.setVisibility(View.VISIBLE);
        }
        navigationViewAdmin.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuSanpham:
                        startActivity(new Intent(getApplicationContext(), QuanlySanphamActivity.class));
                        finish();
                        overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                        break;
                    case R.id.menuTinTuc:
                        Toast.makeText(getApplicationContext(), "TinTuc", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuNguoidung:
                        startActivity(new Intent(MainActivity.this, QuanlyNguoidungActivity.class));
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
                                        FirebaseAuth.getInstance().signOut(); // đăng xuất firebase
                                        Intent dangxuat = new Intent(getApplicationContext(), DangnhapActivity.class);
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
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xFF141414);
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