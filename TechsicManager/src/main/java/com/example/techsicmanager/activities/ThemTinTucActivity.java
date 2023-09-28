package com.example.techsicmanager.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.techsicmanager.R;
import com.example.techsicmanager.models.MessageModel;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemTinTucActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText imgTintuc;
    private EditText tacgiaTintuc,thoigianTintuc,tieudeTintuc,noidungTintuc;
    private ImageView imgThemanh;
    private Button btnThem;
    private ProgressBar progressBar;
    private String mediaPath;
    RetrofitInterface apiconfig;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themtintuc);
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        getWidget();
        getDob();
        actionToolbar();
    }

    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolBarThemtintuc);
        progressBar = (ProgressBar) findViewById(R.id.progressBarThem);

        imgTintuc = findViewById(R.id.imgTintuc);
        tacgiaTintuc = findViewById(R.id.tacgiaTintuc);
        thoigianTintuc = findViewById(R.id.thoigianTintuc);
        tieudeTintuc = findViewById(R.id.tieudeTintuc);
        noidungTintuc = findViewById(R.id.noidungTintuc);

        btnThem = (Button) findViewById(R.id.btnThemTintuc);
        imgThemanh = (ImageView) findViewById(R.id.imgThemanh);
        btnThem.setOnClickListener(this);
        imgThemanh.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnThemTintuc:
                actionAdd();
                break;
            case R.id.imgThemanh:
                imagePicker(); // chọn hình ảnh
        }
    }

    private void getDob() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = now.format(formatter);
        thoigianTintuc.setText(formattedDateTime);
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
            Call<MessageModel> call = apiconfig.uploadAnhtintuc(fileUpload);
            call.enqueue(new Callback< MessageModel >() {
                @Override
                public void onResponse(Call < MessageModel > call, Response< MessageModel > response) {
                    MessageModel serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.isSuccess()) {
                            imgTintuc.setText(serverResponse.getName());
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

                }
            });
        }catch (Exception e){
            Log.d("uploadfile: ",e.getMessage());
        }

    }
    private void actionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void actionAdd() {
        String tieude = tieudeTintuc.getText().toString().trim();
        String hinhanh = imgTintuc.getText().toString().trim();
        String tacgia = tacgiaTintuc.getText().toString().trim();
        String thoigian = thoigianTintuc.getText().toString().trim();
        String noidung  = noidungTintuc.getText().toString().trim();

        if(tieude.isEmpty()) {
            tieudeTintuc.setError("Nhập tiêu đề!");
            tieudeTintuc.requestFocus();
            return;
        }
        if(hinhanh.isEmpty()) {
            imgTintuc.setError("Nhập link hình ảnh tin tức!");
            imgTintuc.requestFocus();
            return;
        }
        if(tacgia.isEmpty()) {
            tacgiaTintuc.setError("Nhập tác giả!");
            tacgiaTintuc.requestFocus();
            return;
        }
        if(thoigian.isEmpty()) {
            thoigianTintuc.setError("Nhập thời gian!");
            thoigianTintuc.requestFocus();
            return;
        }
        if(noidung.isEmpty()) {
            noidungTintuc.setError("Nhập mô tả sản phẩm!");
            noidungTintuc.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(apiconfig.themTintuc(tieude,hinhanh,tacgia,thoigian,noidung)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if(messageModel.isSuccess()){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(ThemTinTucActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), QuanlyTintucActivity.class);
                                        startActivity(intent);
                                        finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),QuanlyTintucActivity.class));
        finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}