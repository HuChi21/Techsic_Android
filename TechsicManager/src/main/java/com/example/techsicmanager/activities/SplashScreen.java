package com.example.techsicmanager.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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

        // Kiểm tra phiên bản Android của thiết bị
        int currentSdkVersion = Build.VERSION.SDK_INT;

        // Nếu phiên bản Android của thiết bị dưới 5.0
        if (currentSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            // Hiển thị thông báo yêu cầu phiên bản Android tối thiểu
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Yêu cầu phiên bản Android")
                    .setMessage("Ứng dụng yêu cầu phiên bản Android từ 5.0 trở lên. Vui lòng nâng cấp phiên bản Android để sử dụng ứng dụng.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
        Paper.init(this);
        if(isConnected(this)){
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
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Yêu cầu kết nối mạng")
                    .setMessage("Ứng dụng yêu cầu sử dụng kết nối mạng ổn định (Wi-Fi hoặc 3G/4G).")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }

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
}
