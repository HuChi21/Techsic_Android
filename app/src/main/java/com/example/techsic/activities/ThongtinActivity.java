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
import android.view.MenuItem;
import android.view.View;

import com.example.techsic.R;
import com.google.android.material.navigation.NavigationView;

public class ThongtinActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin);

        getWidget();
        actionBar();

    }

    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolBarThongTin);
        navigationView = (NavigationView)  findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout)  findViewById(R.id.drawerLayout);
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
                        case R.id.menuHome:
                            Intent home = new Intent(getApplicationContext(),MainActivity.class);
                            home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(home);
                            finish();
                            break;
                        case R.id.menuLienhe:
                            Intent lienhe = new Intent(getApplicationContext(),LienheActivity.class);
                            lienhe.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(lienhe);
                            finish();
                            break;
                        case R.id.menuDangxuat:
                            AlertDialog.Builder builder = new AlertDialog.Builder(ThongtinActivity.this);
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
    }