package com.example.techsic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techsic.R;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangkyActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail,edtHoten,edtDiachi,edtSodt,edtMatKhau,edtConmatkhau,edtNgaysinh;
    private TextView txtDangNhap;
    private Button btnDangKy;
    private ProgressBar progressBarDangky;
    int age;
    RetrofitInterface apiconfig;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String hinhanh = "https://cdn-icons-png.flaticon.com/512/1077/1077114.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWidget();

    }

    private void getWidget() {
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtHoten = (EditText) findViewById(R.id.edtHoTen);
        edtNgaysinh = (EditText) findViewById(R.id.edtNgaySinh);
        edtNgaysinh.setInputType(InputType.TYPE_NULL);
        edtNgaysinh.setTextIsSelectable(false);
        edtNgaysinh.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return true;  // Blocks input from hardware keyboards.
            }
        });
        edtDiachi = (EditText) findViewById(R.id.edtDiaChi);
        edtSodt = (EditText) findViewById(R.id.edtSoDT);
        edtMatKhau = (EditText) findViewById(R.id.edtMatKhau);
        edtConmatkhau = (EditText) findViewById(R.id.edtConMatKhau);

        txtDangNhap = (TextView) findViewById(R.id.txtDangNhap);
        btnDangKy =(Button) findViewById(R.id.btnDangKy);
        progressBarDangky = (ProgressBar) findViewById(R.id.progressBarDangKy);

        txtDangNhap.setOnClickListener(this::onClick);
        btnDangKy.setOnClickListener(this::onClick);

        getDoB();

    }

    private void getDoB(){
        Calendar calendar = Calendar.getInstance();
        final int year = 2005;
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        edtNgaysinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DangkyActivity.this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        String date = dayOfMonth +"/"+ month +"/"+ year;
                        edtNgaysinh.setText(date);
                        int currentYear = calendar.get(Calendar.YEAR);
                        age = currentYear - year;
                    }
                },year,month,day);
                datePickerDialog.show();
                edtNgaysinh.setTextColor(Color.parseColor("#FF646464"));
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtDangNhap:
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out);

                break;
            case R.id.btnDangKy:
                actionDangKy();
                break;
        }
    }

    private void actionDangKy() {
        String email = edtEmail.getText().toString().trim();
        String hoten = edtHoten.getText().toString().trim();
        String ngaysinh = edtNgaysinh.getText().toString().trim();
        String diachi = edtDiachi.getText().toString().trim();
        String sodt = edtSodt.getText().toString().trim();
        String matkhau = edtMatKhau.getText().toString().trim();
        String conmatkhau = edtConmatkhau.getText().toString().trim();
        getDoB();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Nhập địa chỉ Email hợp lệ!");
            edtEmail.requestFocus();
            return;
        }else if(email.isEmpty()){
            edtEmail.setError("Nhập địa chỉ email!");
            edtEmail.requestFocus();
            return;
        }
        if(hoten.isEmpty()) {
            edtHoten.setError("Nhập họ tên!");
            edtHoten.requestFocus();
            return;
        }
        if (age < 18 || age > 150){
            edtNgaysinh.setError("Tuổi phải lớn hơn 18!");
            return;
        }else if(ngaysinh.isEmpty()){
            edtNgaysinh.setError(null);
            return;
        }

        if(diachi.isEmpty()){
            edtDiachi.setError("Nhập địa chỉ!");
            edtDiachi.requestFocus();
            return;
        }
        if(sodt.isEmpty()) {
            edtSodt.setError("Nhập số điện thoại!");
            edtSodt.requestFocus();
            return;
        }else if(!Patterns.PHONE.matcher(sodt).matches()) {
            edtSodt.setError("Nhập số điện thoại hợp lệ!");
            edtSodt.requestFocus();
            return;
        }
        if(matkhau.isEmpty()){
            edtMatKhau.setError("Nhập mật khẩu");
            edtMatKhau.requestFocus();
            return;
        }
        if(matkhau.length()<6){
            edtMatKhau.setError("Nhập mật khẩu tổi thiểu 6 chữ số");
            edtMatKhau.requestFocus();
            return;
        }
        if(!conmatkhau.equals(matkhau)){
            edtConmatkhau.setError("Mật khẩu không trùng nhau!");
            edtConmatkhau.requestFocus();
            return;
        }
        //dang ky
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,matkhau)
                .addOnCompleteListener(DangkyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(user != null){
                                setDangky(email,matkhau, hoten,hinhanh, ngaysinh,sodt,diachi,user.getUid());
                            }
                        }
                        else{
                            Toast.makeText(DangkyActivity.this, "Email da ton tai", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setDangky(String email,String matkhau, String hoten,String hinhanh, String ngaysinh,String sodt,String diachi,String uid) {
        compositeDisposable.add(apiconfig.setDangKy(email,matkhau,hoten,hinhanh,ngaysinh,sodt,diachi,1,0,uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taiKhoanModel -> {
                            if(taiKhoanModel.isSuccess()){
                                Toast.makeText(getApplicationContext(), taiKhoanModel.getMessage(), Toast.LENGTH_SHORT).show();
                                if(taiKhoanModel.getMessage().contains("Email đã tồn tại")){
                                    edtEmail.setError("Email đã tồn tại");
                                    edtEmail.requestFocus();
                                    return;
                                }else{
                                    Handler handler = new Handler();
                                    progressBarDangky.setVisibility(View.VISIBLE);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            RetrofitUtilities.taiKhoanGanDay.setEmail(email);
                                            RetrofitUtilities.taiKhoanGanDay.setMatkhau(matkhau);
                                            progressBarDangky.setVisibility(View.GONE);
                                            Intent intent = new Intent(getApplicationContext(), DangnhapActivity.class);
                                            startActivity(intent);
                                            finish();
                                            overridePendingTransition(R.anim.nothing, R.anim.slide_out);
                                        }
                                    },1500);
                                }
                            }else{
                                progressBarDangky.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), taiKhoanModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBarDangky.setVisibility(View.GONE);
                                    }
                                },1500);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}

