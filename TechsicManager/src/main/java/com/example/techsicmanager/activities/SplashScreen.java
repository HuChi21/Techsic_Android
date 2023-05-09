package com.example.techsicmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techsicmanager.R;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Paper.init(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Paper.book().read("taikhoan")==null){
                    startActivity(new Intent(getApplicationContext(), DangnhapActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    finish();
                }
                else{
                    Toast.makeText(SplashScreen.this, "Bạn đang đăng nhập với quyền người quản trị!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    finish();
                }

            }
        },3000);
    }
}
