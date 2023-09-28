package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techsic.R;
import com.example.techsic.adapters.MuaHangAdapter;
import com.example.techsic.firebase.NotificationSendData;
import com.example.techsic.firebase.PushNotification;
import com.example.techsic.models.DonHang;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.firebase.RetrofitPushNotif;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TinhtienActivity extends AppCompatActivity {
    private EditText txtHoten,txtSodt,txtEmail,txtDiachi,txtTongtien1,txtGhichu,txtNgaydat,txtNgaynhan;
    private EditText txtTongtien2,txtTienvanchuyen,txtVoucher,txtTongtt;
    private TextView txtTongthanhtoan, txtHinhthucthanhtoan,dieukhoan;
    private ImageView imgDiachi,imgClose;
    private Button btnDathang,btnmomo,btnzalo;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout layout;
    private ConstraintLayout txtHinhthuc;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ProgressBar progressbarTinhtien;
    private NumberFormat format;
    private int random,tongItem,soluong;
    private long tongtien, tongthanhtoan;
    DonHang donHang;
    int iddonhang;
    RetrofitInterface apiconfig;
    PushNotification apipushnotif;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView sanphamRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinhtien);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        apipushnotif = RetrofitPushNotif.getRetrofit().create(PushNotification.class);
        getWidget();
        countItem();
        getGioHang();
        actionTinhTien();
    }
    private void getWidget() {
        txtHoten = (EditText) findViewById(R.id.txtHoTen);
        txtSodt = (EditText) findViewById(R.id.txtSoDT);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtDiachi = (EditText) findViewById(R.id.txtDiaChi);
        txtTongtien1 = (EditText) findViewById(R.id.txtTongTien1);
        txtGhichu = (EditText) findViewById(R.id.txtGhiChu);
        txtNgaydat = (EditText) findViewById(R.id.txtNgayDat);
        txtNgaynhan = (EditText) findViewById(R.id.txtNgayNhan);
        txtHinhthucthanhtoan = (TextView) findViewById(R.id.txtHinhThucThanhToan);
        txtHinhthuc = (ConstraintLayout) findViewById(R.id.hinhthuc);
        txtTongtien2 = (EditText) findViewById(R.id.txtTongTien2);
        txtTienvanchuyen = (EditText) findViewById(R.id.txtTienVanChuyen);
        txtVoucher = (EditText) findViewById(R.id.txtVoucher);
        txtTongtt = (EditText) findViewById(R.id.txtTongTT);
        txtTongthanhtoan = (TextView) findViewById(R.id.txtTongThanhToan);
        dieukhoan = (TextView) findViewById(R.id.dieukhoan);
        imgDiachi = (ImageView) findViewById(R.id.imgChonDiaChi);
        btnDathang = (Button) findViewById(R.id.btnDatHang);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbarTinhtien);
        progressbarTinhtien = (ProgressBar) findViewById(R.id.progressBarTinhTien);
        sanphamRecyclerview = (RecyclerView) findViewById(R.id.sanphamRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        sanphamRecyclerview.setLayoutManager(layoutManager);

        format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));

        layout = (LinearLayout)findViewById(R.id.hinhthuc_bottom);
        bottomSheetBehavior = BottomSheetBehavior.from(layout);

        // Thiết lập layout cho BottomSheetDialog

        imgClose = findViewById(R.id.imgClose);
        btnmomo = findViewById(R.id.button_momo);
        btnzalo = findViewById(R.id.button_zalopay);
        dieukhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DieuKhoanActivity.class));
                overridePendingTransition(R.anim.nothing,R.anim.slide_in);
            }
        });
    }
    private void countItem() {
        soluong = getIntent().getIntExtra("txtSoLuong",0);
        tongItem = soluong;
        if(tongItem == 0){
            for (int i = 0; i < RetrofitUtilities.muahangList.size(); i++) {
                tongItem += RetrofitUtilities.muahangList.get(i).getSoluong();
            }
        }
    }
    private void getThongTin(){
        txtHoten.setText(RetrofitUtilities.taiKhoanGanDay.getHoten());
        txtSodt.setText(RetrofitUtilities.taiKhoanGanDay.getSodt());
        txtEmail.setText(RetrofitUtilities.taiKhoanGanDay.getEmail());
        txtDiachi.setText(RetrofitUtilities.taiKhoanGanDay.getDiachi());

        random = new Random().nextInt(4) + 3;
        tongtien = getIntent().getLongExtra("txtTongTien",0);
        BigDecimal tongtienhang = BigDecimal.valueOf(tongtien);
        txtTongtien1.setText(format.format(tongtienhang));
        txtTongtien2.setText(format.format(tongtienhang));

        long voucher = - tongtien * random / 100;
        BigDecimal tienVoucher = BigDecimal.valueOf(voucher);
        txtVoucher.setText(format.format(tienVoucher));

        txtGhichu.setText("Nevermind");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String ngaydat = df.format(calendar.getTime());
        try {
            calendar.setTime(df.parse(ngaydat));
            txtNgaydat.setText(ngaydat);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        calendar.add(Calendar.DATE, random);
        String ngaynhan = df.format(calendar.getTime());
        txtNgaynhan.setText(ngaynhan);

        long vanchuyenperday = 5000;
        txtTienvanchuyen.setText(format.format(vanchuyenperday*random));

        tongthanhtoan = tongtien + vanchuyenperday * random + voucher;
        txtTongtt.setText(format.format(tongthanhtoan));
        txtTongthanhtoan.setText(format.format(tongthanhtoan));
    }

    private void actionTinhTien(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getThongTin();

        imgDiachi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TinhtienActivity.this, "Sẽ tích hợp map sau. Vui lòng đợi!", Toast.LENGTH_SHORT).show();
            }
        });
        txtHinhthuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);
                btnmomo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtHinhthucthanhtoan.setText("Momo");
                        txtHinhthucthanhtoan.setTextColor(0xFFb0006d);
                        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
                btnzalo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtHinhthucthanhtoan.setText("Zalopay");
                        txtHinhthucthanhtoan.setTextColor(0xFF0068ff);
                        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
            }
        });
        btnDathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idtaikhoan = RetrofitUtilities.taiKhoanGanDay.getIdtaikhoan();
                String hoten = txtHoten.getText().toString().trim();
                String sodt = txtSodt.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String diachi = txtDiachi.getText().toString().trim();
                String tongtien1 = txtTongtien1.getText().toString().trim();
                String ghichu = txtGhichu.getText().toString().trim();
                String ngaydat = txtNgaydat.getText().toString().trim();
                String ngaynhan = txtNgaynhan.getText().toString().trim();
                String hinhthuc = txtHinhthucthanhtoan.getText().toString().trim();
                String tongtien2 = txtTongtien1.getText().toString().trim();
                String tienvanchuyen = txtTienvanchuyen.getText().toString().trim();
                String voucher = txtVoucher.getText().toString().trim();
                String tongtt = txtTongtt.getText().toString().trim();
                String tongthanhtoan = txtTongthanhtoan.getText().toString().trim();

                if (diachi.equals("Địa chỉ")) {
                    txtDiachi.setError("Vui lòng chọn địa chỉ!");
                    txtDiachi.setEnabled(true);
                    return;
                }
                if (hinhthuc.equals("Hình thức thanh toán")) {
                    Toast.makeText(getApplicationContext(),"Vui lòng chọn hình thức thanh toán!",Toast.LENGTH_SHORT).show();
                    txtHinhthucthanhtoan.setEnabled(true);
                    return;
                }
                compositeDisposable.add(apiconfig.setDonHang(idtaikhoan, hoten, sodt,email, diachi, ghichu, ngaydat, ngaynhan, tongItem, tongthanhtoan,0 ,hinhthuc,"",new Gson().toJson(RetrofitUtilities.muahangList))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                messageModel -> {
                                    progressbarTinhtien.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressbarTinhtien.setVisibility(View.GONE);
                                            for(int i=0;i<RetrofitUtilities.muahangList.size();i++){
                                                RetrofitUtilities.giohanglist.remove(RetrofitUtilities.muahangList.get(i));
                                            }
                                            iddonhang = messageModel.getIddonhang();
                                            pushNotifToUser(iddonhang);
                                            Log.e("iddonhang", String.valueOf(iddonhang));
                                            compositeDisposable.add(apiconfig.getDonhangtheoid(iddonhang)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(
                                                            donHangModel -> {
                                                                if(donHangModel.isSuccess()){
                                                                    donHang = donHangModel.getResult().get(0);
                                                                    Intent intent = new Intent(getApplicationContext(),ThanhtoanActivity.class);
                                                                    intent.putExtra("donHang",donHang);
                                                                    startActivity(intent);
                                                                    onBackPressed();
                                                                    Log.d("donhang", String.valueOf(donHang.getId()));
                                                                }
                                                            },
                                                            throwable -> {
                                                                Log.d("Error", "Không kết nối được với server "+throwable.getMessage());
                                                            }
                                                    ));

                                        }
                                    },1500);

                                },
                                throwable -> {
                                    Log.d("Error", "Không kết nối được với server "+throwable.getMessage());
                                }
                        ));
            }
        });
    }

    private void pushNotifToUser(int iddonhang) {
        compositeDisposable.add(apiconfig.getToken(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taiKhoanModel -> {
                            if(taiKhoanModel.isSuccess()){
                                for(int i=0;i<taiKhoanModel.getResult().size();i++){
                                    String token = taiKhoanModel.getResult().get(i).getToken();
                                    Map<String,String> data = new HashMap<>();
                                    data.put("title","Thông báo");
                                    data.put("body","Bạn có đơn hàng mới :"+iddonhang);
                                    NotificationSendData notificationSendData = new NotificationSendData(token,data);

                                    compositeDisposable.add(apipushnotif.sendNotification(notificationSendData)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notificationResponse -> {
                                                    },
                                                    throwable -> {
                                                        Log.d("Error", "Không kết nối được với server "+throwable.getMessage());
                                                    }
                                            ));
                                }
                            }
                        },
                        throwable -> {
                            Log.d("Error", "Không kết nối được với server "+throwable.getMessage());
                        }
                ));
    }
    private void getGioHang() {
        sanphamRecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        sanphamRecyclerview.setLayoutManager(layoutManager);
        MuaHangAdapter muaHangAdapter = new MuaHangAdapter(getApplicationContext(),RetrofitUtilities.muahangList);
        sanphamRecyclerview.setAdapter(muaHangAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RetrofitUtilities.muahangList.clear();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}