package com.example.techsicmanager.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.techsicmanager.R;
import com.example.techsicmanager.models.MessageModel;
import com.example.techsicmanager.models.TinTuc;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuaTinTucActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText imgTintuc;
    private ImageView imgThemanh, btnXoatintuc;
    private Button btnSuaTintuc;
    private ProgressBar progressBar;
    private EditText tacgiaTintuc, thoigianTintuc, tieudeTintuc, noidungTintuc;
    private TinTuc tinTuc;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    private String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suatintuc);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        tinTuc = (TinTuc) getIntent().getSerializableExtra("tintuc");

        getWidget();
        actionToolbar();
        getThongtin();
        getDob();

    }

    private void getWidget() {
        toolbar = findViewById(R.id.toolBarChitietTintuc);
        progressBar = (ProgressBar) findViewById(R.id.progressBarThem);

        imgTintuc = findViewById(R.id.imgTintuc);
        imgThemanh = findViewById(R.id.imgThemanh);
        btnSuaTintuc = findViewById(R.id.btnSuaTintuc);
        btnXoatintuc = findViewById(R.id.btnXoatintuc);
        tacgiaTintuc = findViewById(R.id.tacgiaTintuc);
        thoigianTintuc = findViewById(R.id.thoigianTintuc);
        tieudeTintuc = findViewById(R.id.tieudeTintuc);
        noidungTintuc = findViewById(R.id.noidungTintuc);

        btnSuaTintuc.setOnClickListener(this);
        btnXoatintuc.setOnClickListener(this);
        imgThemanh.setOnClickListener(this);
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

    private void getThongtin() {
        imgTintuc.setText(tinTuc.getHinhanh());
        tacgiaTintuc.setText(tinTuc.getTacgia());
        thoigianTintuc.setText(tinTuc.getThoigian());
        tieudeTintuc.setText(tinTuc.getTieude());
        noidungTintuc.setText(tinTuc.getNoidung());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnThemsp:
                capnhatTinTuc();
                break;
            case R.id.imgThemanh:
                imagePicker(); // chọn hình ảnh
            case R.id.btnXoatintuc:
                xoaTintuc(); // chọn hình ảnh
        }
    }
    private void getDob() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = now.format(formatter);
        thoigianTintuc.setText(formattedDateTime);
    }
    private void capnhatTinTuc() {
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
        try {
            compositeDisposable.add(apiconfig.suaTintuc(tinTuc.getIdsp(), tieude,hinhanh,tacgia,thoigian,noidung)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if(messageModel.isSuccess()){
                                    progressBar.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(SuaTinTucActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
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
        catch (Exception e){
            Log.d("Error", "Không kết nối được với server "+e.getMessage());
        }


    }

    private void xoaTintuc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SuaTinTucActivity.this);
        builder.setTitle("Xoá tin tức");
        builder.setMessage("Bạn chắc chắn muốn xoá tin tức?").setCancelable(false)
                .setPositiveButton("Có, xoá tin tức", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        compositeDisposable.add(apiconfig.xoaTintuc(tinTuc.getIdtintuc())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        messageModel ->{
                                            if(messageModel.isSuccess()){
                                                deleteFile();
                                                progressBar.setVisibility(View.VISIBLE);
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(SuaTinTucActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        Intent intent = new Intent(getApplicationContext(), QuanlyTintucActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
                                                    }
                                                },2000);
                                            }
                                            else{
                                                Toast.makeText(SuaTinTucActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void deleteFile() {
        try{
            Uri uri = Uri.parse(mediaPath);
            File file = new File(getPath(uri));
            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            Call<MessageModel> call = apiconfig.xoaAnhtintuc(fileUpload,tinTuc.getIdtintuc());
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
                    Log.d("Error", "Không kết nối được với server "+t.getMessage());

                }
            });
        }catch (Exception e){
            Log.d("uploadfile: ",e.getMessage());
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
    }

    private void imagePicker() {
        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    private String getPath(Uri uri) {
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    private void uploadMultipleFiles() {
        try {
            Uri uri = Uri.parse(mediaPath);
            File file = new File(getPath(uri));
            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            Call<MessageModel> call = apiconfig.uploadAnhtintuc(fileUpload);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
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
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    Log.d("Call file upload:", t.getMessage());

                }
            });
        } catch (Exception e) {
            Log.d("uploadfile: ", e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), QuanlyTintucActivity.class));
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();

    }
}

