package com.example.techsicmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techsicmanager.R;
import com.example.techsicmanager.adapters.ChiTietDonHangAdapter;
import com.example.techsicmanager.firebase.NotificationSendData;
import com.example.techsicmanager.firebase.PushNotification;
import com.example.techsicmanager.firebase.RetrofitPushNotif;
import com.example.techsicmanager.models.DonHang;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonhangActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView donhangRecyclerview;
    private Toolbar toolbar;
    private TextView txtIddonhang,txtHoten,txtEmail,txtSodt,txtDiachi,txtGhichu,txtNgaydat,txtNgaynhan,txtTongthanhtoan,txtTinhtrang,txthinhthuc;
    private Button btnDoitinhtrang,btnHuydon;
    private ImageView btnChat;
    DonHang donHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    PushNotification apipushnotif;
    View overlay;
    ProgressBar progressBar;
    String ngaynhan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xemdonhang);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        apipushnotif = RetrofitPushNotif.getRetrofit().create(PushNotification.class);
        donHang = (DonHang) getIntent().getSerializableExtra("donhang");
        getWidget();
        getThongtindonhang();
        actionToolBar();
//        getDonHang();
    }

    private void getWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbarChitietdonhang);
        txtIddonhang = (TextView) findViewById(R.id.txtIdDonHang);
        txtHoten = (TextView) findViewById(R.id.txtTenNguoiDat);
        txtSodt = (TextView) findViewById(R.id.txtSoDTNguoiDat);
        txtEmail = (TextView) findViewById(R.id.txtEmailNguoiDat);
        txtDiachi = (TextView) findViewById(R.id.txtDiaChiDonHang);
        txtGhichu = (TextView) findViewById(R.id.txtGhiChuDonHang);
        txtNgaydat = (TextView) findViewById(R.id.txtNgayDatDonHang);
        txtNgaynhan = (TextView) findViewById(R.id.txtNgayNhanDonHang);
        txtTongthanhtoan = (TextView) findViewById(R.id.txtTongThanhToanDonHang);
        txtTinhtrang = (TextView) findViewById(R.id.txtTinhtrang);
        txthinhthuc = (TextView) findViewById(R.id.txthinhthuc);
        btnDoitinhtrang = (Button) findViewById(R.id.btnDoitinhtrang);
        btnHuydon = (Button) findViewById(R.id.btnHuydon);
        btnChat = (ImageView) findViewById(R.id.btnChat);
        donhangRecyclerview = (RecyclerView) findViewById(R.id.donHangRecyclerView);


        btnChat.setOnClickListener(this);
        progressBar = findViewById(R.id.progress_bar);
        overlay = findViewById(R.id.overlay);

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnDoitinhtrang:
                CapnhatDonhang();
                break;
            case R.id.btnHuydon:
                String huydon ="Đã huỷ";
                setDonhang(donHang.getId(),huydon);
                break;
            case R.id.btnChat:
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("id",2);
                startActivity(intent);
                break;
        }
    }

    private void getThongtindonhang() {
        txtIddonhang.setText(String.valueOf(donHang.getId()));
        txtHoten.setText(donHang.getHoten());
        txtSodt.setText(donHang.getSodt());
        txtEmail.setText(donHang.getEmail());
        txtDiachi.setText(donHang.getDiachi());
        txtGhichu.setText(donHang.getGhichu());
        txtNgaydat.setText(donHang.getNgaydat());
        txtNgaynhan.setText(donHang.getNgaynhan());
        txtTongthanhtoan.setText(donHang.getTongthanhtoan());
        txthinhthuc.setText(donHang.getHinhthuc());
        txtTinhtrang.setText(donHang.getKieutinhtrang());
        if(donHang.getKieutinhtrang().trim().equals("Đã hủy")){
            btnDoitinhtrang.setAlpha(.5f);
            btnDoitinhtrang.setEnabled(false);
            btnHuydon.setVisibility(View.GONE);
            txtTinhtrang.setTextColor(0xFFD0342C);
        }else if(donHang.getKieutinhtrang().trim().equals("Hoàn thành")){
            btnDoitinhtrang.setAlpha(.5f);
            btnDoitinhtrang.setEnabled(false);
            btnHuydon.setVisibility(View.GONE);
            txtTinhtrang.setTextColor(0xFF00FF00);
        }else{
            btnDoitinhtrang.setOnClickListener(this);
            btnHuydon.setOnClickListener(this);
            btnHuydon.setVisibility(View.VISIBLE);
            txtTinhtrang.setTextColor(0xFF121212);
        }
        compositeDisposable.add(apiconfig.getChitietdonhang(donHang.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        itemModel -> {
                            //chitietdonhang adapter
                            LinearLayoutManager layoutManager = new LinearLayoutManager(
                                    txtIddonhang.getContext(),
                                    LinearLayoutManager.VERTICAL,false
                            );
                            layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
                            ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(getApplicationContext(),itemModel.getResult());
                            donhangRecyclerview.setLayoutManager(layoutManager);
                            donhangRecyclerview.setAdapter(chiTietDonHangAdapter);
                        },
                        throwable -> {
                            Toast.makeText(this, "Xảy ra lỗi "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }
    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void CapnhatDonhang() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.item_chontinhtrang, null);
        bottomSheetDialog.setContentView(view);
        ListView listviewTinhtrang = view.findViewById(R.id.list_view);
        compositeDisposable.add(apiconfig.getTinhtrang().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                tinhtrangModel -> {
                    tinhtrangModel.getResult().remove(tinhtrangModel.getResult().size()-1);
                    List<String> tinhtrang = tinhtrangModel.getResult();
                    int indexToDelete = -1;
                    for (int i = 0; i < tinhtrang.size(); i++) {
                        if (tinhtrang.get(i).equals(txtTinhtrang.getText())) {
                            indexToDelete = i;
                            break;
                        }
                    }
                    if (indexToDelete != -1) {
                        tinhtrang.subList(0, indexToDelete+1).clear();
                    }
                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tinhtrang);
                    listviewTinhtrang.setAdapter(adapter);
                    listviewTinhtrang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = (String) parent.getItemAtPosition(position);
                            setDonhang(donHang.getId(),selectedItem);
                            bottomSheetDialog.dismiss();
                        }
                    });
                },throwable -> {
                    Log.d("error", throwable.getMessage());
                }
        ));
        bottomSheetDialog.show();
    }
    private void setDonhang(int id, String kieutinhtrang) {
        ngaynhan = donHang.getNgaynhan();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String homnay = df.format(calendar.getTime());
        if(kieutinhtrang.equals("Hoàn thành") || kieutinhtrang.equals("Đã huỷ")){
            try {
                calendar.setTime(df.parse(homnay));
                ngaynhan =homnay;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        compositeDisposable.add(apiconfig.setDonhang(id,kieutinhtrang,ngaynhan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            progressBar.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(XemDonhangActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    pushNotifToUser(donHang.getId(), kieutinhtrang);
                                    if(kieutinhtrang.equals("Hoàn thành")){
                                        txtTinhtrang.setText(kieutinhtrang);
                                        txtTinhtrang.setTextColor(0xFF00FF00);
                                        btnDoitinhtrang.setEnabled(false);
                                        btnDoitinhtrang.setText(kieutinhtrang);
                                        btnDoitinhtrang.setAlpha(.5f);
                                        btnHuydon.setVisibility(View.GONE);
                                        txtNgaynhan.setText(ngaynhan);

                                    }else if(kieutinhtrang.equals("Đã huỷ")){
                                        txtTinhtrang.setText(kieutinhtrang);
                                        txtTinhtrang.setTextColor(0xFFD0342C);
                                        btnDoitinhtrang.setEnabled(false);
                                        btnDoitinhtrang.setText(kieutinhtrang);
                                        btnDoitinhtrang.setAlpha(.5f);
                                        btnHuydon.setVisibility(View.GONE);
                                        txtNgaynhan.setText(ngaynhan);
                                    }else{
                                        txtTinhtrang.setText(kieutinhtrang);
                                        txtTinhtrang.setTextColor(0xFF121212);
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            },2500);
                        },throwable -> {
                            Log.d("error", throwable.getMessage());
                        }
                ));
    }

    private void pushNotifToUser(int iddonhang,String kieutrangthai) {
        compositeDisposable.add(apiconfig.getToken(donHang.getIdtaikhoan(),0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taiKhoanModel -> {
                            if(taiKhoanModel.isSuccess()){
                                for(int i=0;i<taiKhoanModel.getResult().size();i++){
                                    String token = taiKhoanModel.getResult().get(i).getToken();
                                    Map<String,String> data = new HashMap<>();
                                    data.put("title","Cập nhật đơn hàng");
                                    data.put("body","Đơn hàng: "+iddonhang+" "+kieutrangthai.toLowerCase());
                                    NotificationSendData notificationSendData = new NotificationSendData(token,data);
                                    compositeDisposable.add(apipushnotif.sendNotification(notificationSendData)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notificationResponse -> {
                                                    },
                                                    throwable -> {
                                                            Log.d("error", throwable.getMessage());
                                                    }
                                            ));
                                }
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Lỗi "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


}