package com.example.techsic.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.models.MessageModel;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NguoiDungActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbarNguoidung;
    private TextView txtEmail,txtHoten,txtNgaySinh,txtDiachi,txtSodt,txtEmail_bottom1,txtEmail_bottom2,txtTieude;
    private EditText edtHoten,edtHinhAnh,edtNgaysinh,edtDiachi,edtSodt, edtMatkhau,edtConMatkhau;
    private ImageView imgAnh,btnClose,imgThemanh;
    private Button btnDoithongtin,btnDoiMK,btnXacnhanDoimk,btnXacnhanDoitt;
    private ProgressBar progessBar;
    private LinearLayout layout, layoutDoitt, layoutDoimk;
    private BottomSheetBehavior bottomSheetBehavior;
    int age;
    private String mediaPath;
    RetrofitInterface apiconfig;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoidung);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

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
        imgAnh = (ImageView) findViewById(R.id.imgAnh);
        btnDoithongtin = (Button) findViewById(R.id.btnDoithongtin);
        btnDoiMK = (Button) findViewById(R.id.btnDoimk);
        progessBar = (ProgressBar) findViewById(R.id.progressBarNguoiDung);

        txtEmail_bottom1 =(TextView) findViewById(R.id.txtEmail_bottom);
        txtEmail_bottom2 =(TextView) findViewById(R.id.txtEmail_bottom2);
        txtTieude = (TextView) findViewById(R.id.txtTieude);
        edtHoten =(EditText) findViewById(R.id.edtHoTen_bottom);
        edtHinhAnh =(EditText) findViewById(R.id.edtHinhanh_bottom);
        edtNgaysinh =(EditText) findViewById(R.id.edtNgaySinh_bottom);
        edtNgaysinh.setInputType(InputType.TYPE_NULL);
        edtNgaysinh.setTextIsSelectable(false);
        edtNgaysinh.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return true;  // Blocks input from hardware keyboards.
            }
        });
        edtDiachi =(EditText) findViewById(R.id.edtDiaChi_bottom);
        edtSodt =(EditText) findViewById(R.id.edtSoDT_bottom);
        edtMatkhau =(EditText) findViewById(R.id.edtMatkhau_bottom);
        edtConMatkhau =(EditText) findViewById(R.id.edtConMatkhau_bottom);
        imgThemanh = (ImageView) findViewById(R.id.imgThemanh);

        layout = (LinearLayout)findViewById(R.id.suathongtin_bottom);
        bottomSheetBehavior = BottomSheetBehavior.from(layout);
        layoutDoitt =(LinearLayout) findViewById(R.id.layout_suatt);
        layoutDoimk =(LinearLayout) findViewById(R.id.layout_doimk);
        btnClose = (ImageView) findViewById(R.id.btnClose);
        btnXacnhanDoimk = (Button) findViewById(R.id.btnXacNhanDoiMK);
        btnXacnhanDoitt = (Button) findViewById(R.id.btnXacNhanDoiTT);

        btnDoithongtin.setOnClickListener(this);
        btnDoiMK.setOnClickListener(this);
        txtEmail_bottom1.setOnClickListener(this);
        txtEmail_bottom2.setOnClickListener(this);
        btnXacnhanDoimk.setOnClickListener(this);
        btnXacnhanDoitt.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        imgThemanh.setOnClickListener(this);

        getDoB();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDoithongtin:
                layoutDoitt.setVisibility(View.VISIBLE);
                layoutDoimk.setVisibility(View.GONE);
                txtTieude.setText(btnDoithongtin.getText());
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.btnDoimk:
                layoutDoitt.setVisibility(View.GONE);
                layoutDoimk.setVisibility(View.VISIBLE);
                txtTieude.setText(btnDoiMK.getText());
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.txtEmail_bottom:
                Toast.makeText(this, "Không được thay đổi thông tin email!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.txtEmail_bottom2:
                Toast.makeText(this, "Không được thay đổi thông tin email!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnXacNhanDoiTT:
                doiThongTin();
                break;
            case R.id.btnXacNhanDoiMK:
                doiMatKhau();
                break;
            case R.id.btnClose:
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.imgThemanh:
                imagePicker();
                break;
        }
    }
    private void actionToolBar() {
        setSupportActionBar(toolbarNguoidung);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarNguoidung.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbarNguoidung.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void getThongtin() {
        txtEmail.setText(RetrofitUtilities.taiKhoanGanDay.getEmail().trim());
        txtEmail_bottom1.setText(RetrofitUtilities.taiKhoanGanDay.getEmail().trim());
        txtEmail_bottom2.setText(RetrofitUtilities.taiKhoanGanDay.getEmail().trim());
        Log.d("hinhanh",RetrofitUtilities.taiKhoanGanDay.getHinhanh().toString());
        if(RetrofitUtilities.taiKhoanGanDay.getHinhanh().contains("http")){
            Glide.with(getApplicationContext()).load(RetrofitUtilities.taiKhoanGanDay.getHinhanh()).into(imgAnh);
        }
        else{
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + RetrofitUtilities.taiKhoanGanDay.getHinhanh();
            Glide.with(getApplicationContext()).load(hinhanh).into(imgAnh);
        }
        txtHoten.setText(RetrofitUtilities.taiKhoanGanDay.getHoten().trim());
        txtNgaySinh.setText(RetrofitUtilities.taiKhoanGanDay.getNgaysinh().trim());
        txtDiachi.setText(RetrofitUtilities.taiKhoanGanDay.getDiachi().trim());
        txtSodt.setText(RetrofitUtilities.taiKhoanGanDay.getSodt().trim());
    }
    private void getDoB() {
        Calendar calendar = Calendar.getInstance();
        final int year = 2005;
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        edtNgaysinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        NguoiDungActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        edtNgaysinh.setText(date);
                        int currentYear = calendar.get(Calendar.YEAR);
                        age = currentYear - year;
                    }
                }, year, month, day);
                datePickerDialog.show();
                edtNgaysinh.setTextColor(Color.parseColor("#FF646464"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
    }

    private void imagePicker() {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }
    private String getPath(Uri uri){
        String result;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        if(cursor == null){
            result = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    private void uploadMultipleFiles() {
        try{
            Uri uri = Uri.parse(mediaPath);
            File file = new File(getPath(uri));
            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            Call<MessageModel> call = apiconfig.uploadAnh(fileUpload,RetrofitUtilities.taiKhoanGanDay.getIdtaikhoan());
            call.enqueue(new Callback< MessageModel >() {
                @Override
                public void onResponse(Call < MessageModel > call, Response< MessageModel > response) {
                    MessageModel serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.isSuccess()) {
                            edtHinhAnh.setText(serverResponse.getName());
                        } else {
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.v("Response", serverResponse.toString());
                    }
                }
                @Override
                public void onFailure(Call < MessageModel > call, Throwable t) {
                    Log.d("Call file upload:",t.getMessage());
                    Toast.makeText(NguoiDungActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception e){
            Log.d("uploadfile: ",e.getMessage());
        }

    }

    private void doiThongTin() {
        String email = txtEmail_bottom1.getText().toString().trim();
        String hoten = edtHoten.getText().toString().trim();
        String hinhanh = edtHinhAnh.getText().toString().trim();
        String ngaysinh = edtNgaysinh.getText().toString().trim();
        String diachi = edtDiachi.getText().toString().trim();
        String sodt = edtSodt.getText().toString().trim();
        getDoB();
        if (hoten.isEmpty()) {
            edtHoten.setError("Nhập họ tên!");
            edtHoten.requestFocus();
            return;
        }
        if (age < 18 || age > 150) {
            edtNgaysinh.setError("Tuổi phải lớn hơn 18!");
            return;
        } else if (ngaysinh.isEmpty()) {
            edtNgaysinh.setError(null);
            return;
        }

        if (diachi.isEmpty()) {
            edtDiachi.setError("Nhập địa chỉ!");
            edtDiachi.requestFocus();
            return;
        }
        if (sodt.isEmpty()) {
            edtSodt.setError("Nhập số điện thoại!");
            edtSodt.requestFocus();
            return;
        } else if (!Patterns.PHONE.matcher(sodt).matches()) {
            edtSodt.setError("Nhập số điện thoại hợp lệ!");
            edtSodt.requestFocus();
            return;
        }
        changeInfo(RetrofitUtilities.taiKhoanGanDay.getEmail(),RetrofitUtilities.taiKhoanGanDay.getMatkhau(),hoten,hinhanh,ngaysinh,sodt,diachi);
    }

    private void doiMatKhau() {
        String matkhau = edtMatkhau.getText().toString().trim();
        String conmatkhau = edtConMatkhau.getText().toString().trim();

        if(matkhau.isEmpty()){
            edtMatkhau.setError("Nhập mật khẩu mới");
            edtMatkhau.requestFocus();
            return;
        }
        if(!conmatkhau.equals(matkhau)){
            edtConMatkhau.setError("Mật khẩu không trùng nhau!");
            edtConMatkhau.requestFocus();
            return;
        }
        changeInfo(RetrofitUtilities.taiKhoanGanDay.getEmail(),matkhau,RetrofitUtilities.taiKhoanGanDay.getHoten(),RetrofitUtilities.taiKhoanGanDay.getHinhanh(),
                RetrofitUtilities.taiKhoanGanDay.getNgaysinh(),RetrofitUtilities.taiKhoanGanDay.getSodt(),RetrofitUtilities.taiKhoanGanDay.getDiachi());
    }

    public void changeInfo(String email,String matkhau,String hoten,String hinhanh,String ngaysinh,String sodt,String diachi){
        compositeDisposable.add(apiconfig.suaThongTin(email,matkhau,hoten,hinhanh,ngaysinh,sodt,diachi)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taiKhoanModel -> {
                            if(taiKhoanModel.isSuccess()){
                                progessBar.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        RetrofitUtilities.taiKhoanGanDay.setHoten(hoten);
                                        RetrofitUtilities.taiKhoanGanDay.setNgaysinh(ngaysinh);
                                        RetrofitUtilities.taiKhoanGanDay.setDiachi(diachi);
                                        RetrofitUtilities.taiKhoanGanDay.setSodt(sodt);
                                        RetrofitUtilities.taiKhoanGanDay.setHinhanh(hinhanh);
                                        Toast.makeText(getApplicationContext(), taiKhoanModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        progessBar.setVisibility(View.GONE);
                                        layoutDoitt.setVisibility(View.GONE);
                                        layoutDoimk.setVisibility(View.GONE);
                                        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                                        Toast.makeText(NguoiDungActivity.this, "Vui lòng đăng nhập lại để cập nhật thông tin!", Toast.LENGTH_SHORT).show();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                    }
                                },1500);

                            }else{
                                progessBar.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), taiKhoanModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        progessBar.setVisibility(View.GONE);
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
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }


}