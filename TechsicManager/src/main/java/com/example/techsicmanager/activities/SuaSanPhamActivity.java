package com.example.techsicmanager.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.techsicmanager.R;
import com.example.techsicmanager.models.MessageModel;
import com.example.techsicmanager.models.PhanLoai;
import com.example.techsicmanager.models.SanPham;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuaSanPhamActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtTensp,edtThuonghieu,edtGia,edtHinhanh,edtPhanloai,edtThongso,edtMota,edtSlTonkho;
    private TextView txtLoaisp;
    private ImageView imgSuaanh,btnXoasp;
    private Button btnSuasp;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private int idloaisp;
    private String mediaPath,loaisp;
    private SanPham sanPhamSua;
    RetrofitInterface apiconfig;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suasanpham);
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        sanPhamSua = (SanPham) getIntent().getSerializableExtra("sanPhamSuaXoa");
        idloaisp = sanPhamSua.getIdloaisp();
        loaisp = getIntent().getStringExtra("loaisp");
        getWidget();
        actionToolbar();
        getThongtin();
    }

    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbarSuaSP);
        progressBar = (ProgressBar) findViewById(R.id.progressbarSuasp);

        edtTensp = (EditText) findViewById(R.id.edtTensp);
        edtThuonghieu = (EditText) findViewById(R.id.edtThuonghieu);
        edtGia = (EditText) findViewById(R.id.edtGia);
        edtHinhanh = (EditText) findViewById(R.id.edtHinhanh);
        edtPhanloai = (EditText) findViewById(R.id.edtPhanloai);
        edtThongso = (EditText) findViewById(R.id.edtThongso);
        edtMota = (EditText) findViewById(R.id.edtMota);
        edtSlTonkho = (EditText) findViewById(R.id.edtSoluongtonkho);
        idloaisp = sanPhamSua.getIdloaisp();
        txtLoaisp = (TextView) findViewById(R.id.txtLoaisp);

        btnSuasp = (Button) findViewById(R.id.btnSuasp);
        btnXoasp = (ImageView) findViewById(R.id.btnXoasp);
        imgSuaanh = (ImageView) findViewById(R.id.imgSuaanh);
        btnSuasp.setOnClickListener(this);
        btnXoasp.setOnClickListener(this);
        imgSuaanh.setOnClickListener(this);
        edtThongso.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edtThongso.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        edtMota.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edtMota.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSuasp:
                actionSuasp();
                break;
            case R.id.imgSuaanh:
                imagePicker(); // chọn hình ảnh
                break;
            case R.id.btnXoasp:
                actionXoaSP();
        }
    }
    private void getThongtin() {
        edtTensp.setText(sanPhamSua.getTensp());
        edtThuonghieu.setText(sanPhamSua.getThuonghieu());
        edtGia.setText(sanPhamSua.getGiasp().toString());
        edtHinhanh.setText(sanPhamSua.getHinhanh());
        //getphanloai
        compositeDisposable.add(apiconfig.getPhanloai(sanPhamSua.getIdsp())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        phanLoaiModel -> {
                            if(phanLoaiModel.isSuccess()){
                                String phanloai = "";
                                for(int i=0;i<phanLoaiModel.getResult().size();i++){
                                    phanloai  += phanLoaiModel.getResult().get(i).getPhanloai() +"; ";
                                }
                                if (!phanloai.isEmpty()) {
                                    phanloai = phanloai.substring(0, phanloai.length() - 1);
                                }
                                edtPhanloai.setText(phanloai);
                                //Coi như xong phần số lượng và phân loại. còn báo cáo thống kê và tin tức
                            }
                        },
                        throwable -> {
                            Log.d("apiconfig", "Không kết nối được với server "+throwable.getMessage());
                        }
                ));
        edtThongso.setText(sanPhamSua.getThongso());
        edtMota.setText(sanPhamSua.getMota());
        edtSlTonkho.setText(String.valueOf(sanPhamSua.getSltonkho()));
        txtLoaisp.setText(loaisp);
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
            Call<MessageModel> call = apiconfig.uploadAnh(fileUpload,idloaisp);
            call.enqueue(new Callback< MessageModel >() {
                @Override
                public void onResponse(Call < MessageModel > call, Response< MessageModel > response) {
                    MessageModel serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.isSuccess()) {
                            edtHinhanh.setText(serverResponse.getName());
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
        toolbar.setTitle("Sửa sản phẩm "+ getIntent().getStringExtra("loaisp"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void actionSuasp() {
        String tensp = edtTensp.getText().toString().trim();
        String thuonghieu = edtThuonghieu.getText().toString().trim();
        String gia = edtGia.getText().toString().trim();
        String hinhanh = edtHinhanh.getText().toString().trim();
        String phanloai = edtPhanloai.getText().toString().trim();
        String thongso = edtThongso.getText().toString().trim();
        String mota = edtMota.getText().toString().trim();
        String sltonkho = edtSlTonkho.getText().toString().trim();

        if(tensp.isEmpty()) {
            edtTensp.setError("Nhập tên sản phẩm!");
            edtTensp.requestFocus();
            return;
        }
        if(thuonghieu.isEmpty()) {
            edtThuonghieu.setError("Nhập tên thương hiệu!");
            edtThuonghieu.requestFocus();
            return;
        }
        if(gia.isEmpty()) {
            edtGia.setError("Nhập giá sản phẩm!");
            edtGia.requestFocus();
            return;
        }
        if(hinhanh.isEmpty()) {
            edtHinhanh.setError("Nhập link hình ảnh sản phẩm!");
            edtHinhanh.requestFocus();
            return;
        }
        if(phanloai.isEmpty()) {
            edtPhanloai.setError("Nhập phân loại!");
            edtPhanloai.requestFocus();
            return;
        }
        if(thongso.isEmpty()) {
            edtThongso.setError("Nhập thông số sản phẩm!");
            edtThongso.requestFocus();
            return;
        }
        if(mota.isEmpty()) {
            edtMota.setError("Nhập mô tả sản phẩm!");
            edtMota.requestFocus();
            return;
        }
        String[] phanloaisub = phanloai.split(";");
        List<PhanLoai> phanLoaiList = new ArrayList<>();
        for (int i = 0; i < phanloaisub.length; i++) {
            PhanLoai phanLoaiModel = new PhanLoai();
            phanLoaiModel.setIdsp(1);
            phanLoaiModel.setIdphanloai(i+1);
            phanLoaiModel.setPhanloai(phanloaisub[i].trim());
            phanLoaiList.add(phanLoaiModel);
        }
        Gson gson = new GsonBuilder().setLenient().create();
        String phanloaiJson = gson.toJson(phanLoaiList);

        try {
            compositeDisposable.add(apiconfig.suaSanpham(sanPhamSua.getIdsp(), tensp,thuonghieu,gia,hinhanh,thongso,mota,sltonkho,phanloaiJson)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if(messageModel.isSuccess()){
                                    progressBar.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SuaSanPhamActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            Intent intent = new Intent(getApplicationContext(), QuanlySanphamActivity.class);
                                            intent.putExtra("idloaisp",idloaisp);
                                            intent.putExtra("loaisp",loaisp);
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

    private void deleteFile() {
        try{
            Uri uri = Uri.parse(mediaPath);
            File file = new File(getPath(uri));
            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            Call<MessageModel> call = apiconfig.xoaAnh(fileUpload,idloaisp);
            call.enqueue(new Callback< MessageModel >() {
                @Override
                public void onResponse(Call < MessageModel > call, Response< MessageModel > response) {
                    MessageModel serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.isSuccess()) {
                            edtHinhanh.setText(serverResponse.getName());
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

    private void actionXoaSP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SuaSanPhamActivity.this);
        builder.setTitle("Xoá sản phẩm");
        builder.setMessage("Bạn chắc chắn muốn xoá sản phẩm?").setCancelable(false)
                .setPositiveButton("Có, xoá sản phẩm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        compositeDisposable.add(apiconfig.xoaSP(sanPhamSua.getIdsp())
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
                                                        Toast.makeText(SuaSanPhamActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        Intent intent = new Intent(getApplicationContext(), QuanlySanphamActivity.class);
                                                        intent.putExtra("idloaisp",idloaisp);
                                                        intent.putExtra("loaisp",loaisp);
                                                        startActivity(intent);
                                                        finish();
                                                        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
                                                    }
                                                },2000);
                                            }
                                            else{
                                                Toast.makeText(SuaSanPhamActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getApplicationContext(), QuanlySanphamActivity.class);
        intent.putExtra("idloaisp",idloaisp);
        intent.putExtra("loaisp",loaisp);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}