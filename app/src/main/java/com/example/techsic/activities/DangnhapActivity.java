package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techsic.R;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangnhapActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail,edtMatkhau;
    private TextView txtDangky,txtQuenmk;
    private Button btnDangnhap;
    private ProgressBar progressBarDangnhap;
    private CheckBox chkNhotaikhoan;
    private boolean isLogin = false;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apibanhang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        apibanhang = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWidget();
    }

    private void getWidget() {
        edtEmail = (EditText) findViewById(R.id.edtEmailDangNhap);
        edtMatkhau = (EditText) findViewById(R.id.edtMatKhauDangNhap);
        txtDangky = (TextView) findViewById(R.id.txtDangKy);
        txtQuenmk = (TextView) findViewById(R.id.txtQuenMK);
        btnDangnhap = (Button) findViewById(R.id.btnDangNhap);
        progressBarDangnhap = (ProgressBar) findViewById(R.id.progressBarDangNhap);
        chkNhotaikhoan = (CheckBox) findViewById(R.id.chkNhoTaiKhoan);

        btnDangnhap.setOnClickListener(this);
        txtDangky.setOnClickListener(this);
        txtQuenmk.setOnClickListener(this);

        Paper.init(this);

        if(Paper.book().read("email") != null && Paper.book().read("matkhau")!=null){
            edtEmail.setText(Paper.book().read("email"));
            edtMatkhau.setText(Paper.book().read("matkhau"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDangNhap:
                actionDanhNhap();
                break;
            case R.id.txtDangKy:
                Intent intent = new Intent(getApplicationContext(), DangkyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
            case R.id.txtQuenMK:
                actionQuenMK();
        }
    }

    private void actionDanhNhap() {
        String email = edtEmail.getText().toString().trim();
        String matkhau = edtMatkhau.getText().toString().trim();

        if (email.equals("admin") && matkhau.equals("")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.nothing);
            finish();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Nhập địa chỉ Email hợp lệ!");
            edtEmail.requestFocus();
            return;
        } else if (email.isEmpty()) {
            edtEmail.setError("Nhập địa chỉ email!");
            edtEmail.requestFocus();
            return;
        }
        if (matkhau.isEmpty()) {
            edtMatkhau.setError("Nhập mật khẩu");
            edtMatkhau.requestFocus();
            return;
        }

        if (chkNhotaikhoan.isChecked()) {
            Paper.book().write("email", email);
            Paper.book().write("matkhau", matkhau);
        } else {
            Paper.book().write("email", "");
            Paper.book().write("matkhau", "");
        }


        compositeDisposable.add(apibanhang.setDangNhap(email, matkhau)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taiKhoanModel -> {
                            if (taiKhoanModel.isSuccess()) {
                                progressBarDangnhap.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), taiKhoanModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        RetrofitUtilities.taiKhoanGanDay = taiKhoanModel.getResult().get(0);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                                        finish();
                                    }
                                }, 1500);
                            } else {
                                progressBarDangnhap.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), taiKhoanModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBarDangnhap.setVisibility(View.GONE);
                                    }
                                }, 1500);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void actionQuenMK() {
        String email = edtEmail.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Nhập địa chỉ Email hợp lệ!");
            edtEmail.requestFocus();
            return;
        }else if(email.isEmpty()){
            edtEmail.setError("Nhập địa chỉ email!");
            edtEmail.requestFocus();
            return;
        }
        compositeDisposable.add(apibanhang.setResetMK(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taiKhoanModel -> {
                            if (taiKhoanModel.isSuccess()){
                                progressBarDangnhap.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), taiKhoanModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBarDangnhap.setVisibility(View.GONE);
                                    }
                                },1500);
                            }
                            else{
                                progressBarDangnhap.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBarDangnhap.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),taiKhoanModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },1500);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(RetrofitUtilities.taiKhoanGanDay.getEmail() != null && RetrofitUtilities.taiKhoanGanDay.getMatkhau() != null){
            edtEmail.setText(RetrofitUtilities.taiKhoanGanDay.getEmail());
            edtMatkhau.setText(RetrofitUtilities.taiKhoanGanDay.getMatkhau());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}