package com.example.techsicmanager.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.techsicmanager.R;
import com.example.techsicmanager.models.TaiKhoan;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemNguoidungActivity extends AppCompatActivity implements View.OnClickListener {
    private androidx.appcompat.widget.Toolbar toolbarNguoidung;
    private TextView txtEmail,txtHoten,txtNgaySinh,txtDiachi,txtSodt,txtStatus;
    private ImageView imgChat,imgAnh;
    private Button btnXemDonhang,btnChan;
    private ProgressBar progressBar;
    private TaiKhoan taiKhoan;
    RetrofitInterface apiconfig;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xemnguoidung);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        taiKhoan = (TaiKhoan) getIntent().getSerializableExtra("taikhoan");
        getWidget();
        actionToolBar();
        getThongtin();
    }

    private void getWidget() {
        toolbarNguoidung = (Toolbar) findViewById(R.id.toolbarNguoidung);
        txtEmail =(TextView) findViewById(R.id.txtEmail);
        txtHoten =(TextView) findViewById(R.id.txtHoTen);
        txtNgaySinh =(TextView) findViewById(R.id.txtNgaySinh);
        txtDiachi =(TextView) findViewById(R.id.txtDiaChi);
        txtSodt =(TextView) findViewById(R.id.txtSoDT);
        txtStatus =(TextView) findViewById(R.id.txtStatus);
        imgChat = (ImageView) findViewById(R.id.imgChat);
        imgAnh = (ImageView) findViewById(R.id.imgAnh);
        btnXemDonhang = (Button) findViewById(R.id.btnXemdonhang);
        btnChan = (Button) findViewById(R.id.btnChan);
        progressBar = (ProgressBar) findViewById(R.id.progressBarNguoiDung);

        imgChat.setOnClickListener(this);
        btnXemDonhang.setOnClickListener(this);
        btnChan.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgChat:
                Intent chat = new Intent(getApplicationContext(),LienheActivity.class);
                startActivity(chat);
                overridePendingTransition(R.anim.slide_in,R.anim.nothing);
                break;
            case R.id.btnXemdonhang:
                xemDonhang();
                break;
            case R.id.btnChan:
                chanNguoidung();
                break;
        }
    }
    private void actionToolBar() {
        setSupportActionBar(toolbarNguoidung);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarNguoidung.setTitle("Người dùng "+taiKhoan.getIdtaikhoan());
        toolbarNguoidung.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbarNguoidung.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getThongtin() {
        txtEmail.setText(taiKhoan.getEmail().trim());
        txtHoten.setText(taiKhoan.getHoten().trim());
        txtNgaySinh.setText(taiKhoan.getNgaysinh().trim());
        txtDiachi.setText(taiKhoan.getDiachi().trim());
        txtSodt.setText(taiKhoan.getSodt().trim());
        if(taiKhoan.getHinhanh().contains("http")){
            Glide.with(getApplicationContext()).load(taiKhoan.getHinhanh()).into(imgAnh);
        } else {
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + taiKhoan.getHinhanh();
            Glide.with(getApplicationContext()).load(hinhanh).into(imgAnh);
        }
        switch (taiKhoan.getTrangthai()){
            case 0:
                btnChan.setText("Bỏ chặn");
                txtStatus.setText("Bị chặn");
                txtStatus.setTextColor(0xFFD0342C);
                break;
            case 1:
                btnChan.setText("Chặn");
                txtStatus.setText("Đang hoạt động");
                txtStatus.setTextColor(0xFF00FF00);
                break;
        }
    }

    private void xemDonhang() {
        Intent intent = new Intent(getApplicationContext(),QuanlyDonhangActivity.class);
        intent.putExtra("taikhoan",taiKhoan);
        startActivity(intent);
        overridePendingTransition(R.anim.nothing,R.anim.slide_in);
    }
    private void chanNguoidung() {
        AlertDialog.Builder builder = new AlertDialog.Builder(XemNguoidungActivity.this);
        String positiveButtonText = null;
        switch (taiKhoan.getTrangthai()){
            case 0:
                taiKhoan.setTrangthai(1);
                positiveButtonText = "Có,bỏ chặn người dùng";
                builder.setTitle("Bỏ chặn người dùng");
                builder.setMessage("Bạn chắc chắn muốn bỏ chặn người dùng?");
                break;
            case 1:
                taiKhoan.setTrangthai(0);
                positiveButtonText = "Có,chặn người dùng";
                builder.setTitle("Chặn người dùng");
                builder.setMessage("Bạn chắc chắn muốn chặn người dùng?");
                break;
        }

        builder.setCancelable(false).setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        compositeDisposable.add(apiconfig.chanNguoidung(taiKhoan.getIdtaikhoan(),  taiKhoan.getTrangthai())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        messageModel -> {
                                            if(messageModel.isSuccess()){
                                                progressBar.setVisibility(View.VISIBLE);
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(XemNguoidungActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        switch (taiKhoan.getTrangthai()){
                                                            case 0:
                                                                btnChan.setText("Bỏ chặn");
                                                                txtStatus.setText("Bị chặn");
                                                                txtStatus.setTextColor(0xFFD0342C);
                                                                break;
                                                            case 1:
                                                                btnChan.setText("Chặn");
                                                                txtStatus.setText("Đang hoạt động");
                                                                txtStatus.setTextColor(0xFF00FF00);
                                                                break;}
                                                        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
                                                    }
                                                },2000);
                                            }
                                        },
                                        throwable -> {
                                            Log.d("Error", "Không kết nối được với server "+throwable.getMessage());
                                        }
                                ));

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),QuanlyNguoidungActivity.class));
        finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}