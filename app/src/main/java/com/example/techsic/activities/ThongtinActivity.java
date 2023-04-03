package com.example.techsic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.techsic.R;
import com.google.android.material.navigation.NavigationView;

public class ThongtinActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean doubleBackToExitPressedOnce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWidget();
        actionToolBar();

    }

    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolBarThongTin);
        navigationView = (NavigationView)  findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout)  findViewById(R.id.drawerLayout);
    }

        private void actionToolBar() {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.baseline_info_24);
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
                        case R.id.menuHome:
                            Intent home = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(home);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                            break;
                        case R.id.menuLienhe:
                            Intent lienhe = new Intent(getApplicationContext(),LienheActivity.class);
                            startActivity(lienhe);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                            break;
                        case R.id.menuDangxuat:
                            AlertDialog.Builder builder = new AlertDialog.Builder(ThongtinActivity.this);
                            builder.setTitle("Đăng xuất");
                            builder.setMessage("Bạn có muốn đăng xuất?").setCancelable(false)
                                    .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent dangxuat = new Intent(getApplicationContext(), DangnhapActivity.class);
                                            startActivity(dangxuat);
                                            finish();
                                            overridePendingTransition(R.anim.slide_in, R.anim.nothing);
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
    }