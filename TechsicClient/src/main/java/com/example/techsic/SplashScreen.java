package com.example.techsic;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techsic.activities.DangnhapActivity;
import com.example.techsic.activities.MainActivity;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Kiểm tra phiên bản Android của thiết bị
        int currentSdkVersion = Build.VERSION.SDK_INT;

        // Nếu phiên bản Android của thiết bị dưới 5.0
        if (currentSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            // Hiển thị thông báo yêu cầu phiên bản Android tối thiểu
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Yêu cầu phiên bản Android")
                    .setMessage("Ứng dụng yêu cầu phiên bản Android từ 5.0 trở lên. Vui lòng nâng cấp phiên bản Android để sử dụng ứng dụng.")
                    .setPositiveButton("OK", null)
                    .show();
        }

        Paper.init(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Paper.book().read("taikhoan")==null){
                    startActivity(new Intent(getApplicationContext(), DangnhapActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    finish();
                }
                else{
                    startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    finish();
                }

            }
        },3000);
    }
}
