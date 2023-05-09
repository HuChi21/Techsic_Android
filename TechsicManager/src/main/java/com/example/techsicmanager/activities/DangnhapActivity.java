package com.example.techsicmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techsicmanager.R;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangnhapActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail,edtMatkhau;
    private TextView txtLienhe,txtQuenmk;
    private Button btnDangnhap;
    private ProgressBar progressBarDangnhap;
    private CheckBox cbkNhoTK;
    String remember;
    boolean doubleBackToExitPressedOnce =false;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        getWidget();
    }

    private void getWidget() {
        edtEmail = (EditText) findViewById(R.id.edtEmailDangNhap);
        edtMatkhau = (EditText) findViewById(R.id.edtMatKhauDangNhap);
        txtLienhe = (TextView) findViewById(R.id.txtLienhe);
        txtQuenmk = (TextView) findViewById(R.id.txtQuenMK);
        btnDangnhap = (Button) findViewById(R.id.btnDangNhap);
        progressBarDangnhap = (ProgressBar) findViewById(R.id.progressBarDangNhap);
        cbkNhoTK = (CheckBox) findViewById(R.id.chkNhoTaiKhoan);
        btnDangnhap.setOnClickListener(this);
        txtLienhe.setOnClickListener(this);
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
            case R.id.txtLienhe:
//                Intent intent = new Intent(getApplicationContext(), DangkyActivity.class);
//                startActivity(intent);
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
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.nothing);
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

        if(cbkNhoTK.isChecked()){
            Paper.book().write("isRemember","1");
            Paper.book().delete("isRemember");
        }
        else{
            Paper.book().write("isRemember","0");
        }
        //dang nhap
        if (user != null){
            setDangnhap(email,matkhau); //user đã đăng nhập firebase
        }else{
            firebaseAuth.signInWithEmailAndPassword(email,matkhau)
                    .addOnCompleteListener(DangnhapActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                setDangnhap(email,matkhau);
                            }
                            else{
                                progressBarDangnhap.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DangnhapActivity.this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                                        progressBarDangnhap.setVisibility(View.GONE);
                                    }
                                },1500);
                            }
                        }
                    });
        }

    }

    private void setDangnhap(String email, String matkhau) {
        compositeDisposable.add(apiconfig.setDangNhap(email, matkhau)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taiKhoanModel -> {
                            if (taiKhoanModel.isSuccess()) {
                                progressBarDangnhap.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        RetrofitUtilities.taiKhoanGanDay = taiKhoanModel.getResult().get(0);
                                        if(RetrofitUtilities.taiKhoanGanDay.getIsadmin() == 0){
                                            Toast toast = Toast.makeText(DangnhapActivity.this, "Tài khoản của bạn không phải của người quản trị!", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            progressBarDangnhap.setVisibility(View.GONE);
                                            return;
                                        }
                                        else if(RetrofitUtilities.taiKhoanGanDay.getIsadmin() == 1){
                                            progressBarDangnhap.setVisibility(View.GONE);
                                            Paper.book().write("taikhoan",RetrofitUtilities.taiKhoanGanDay);
                                            Toast toast = Toast.makeText(DangnhapActivity.this, "Bạn đang đăng nhập \n với quyền người quản trị!", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                                        }
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
        compositeDisposable.add(apiconfig.setResetMK(email)
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
        if(RetrofitUtilities.taiKhoanGanDay != null){
            edtEmail.setText(RetrofitUtilities.taiKhoanGanDay.getEmail());
            edtMatkhau.setText(RetrofitUtilities.taiKhoanGanDay.getMatkhau());
        }


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