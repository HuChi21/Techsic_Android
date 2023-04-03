package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techsic.R;
import com.example.techsic.adapters.GioHangAdapter;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TinhtienActivity extends AppCompatActivity {
    private EditText txtHoten,txtSodt,txtEmail,txtDiachi,txtTongtien1,txtGhichu,txtNgaydat,txtNgaynhan,txtHinhthuc;
    private EditText txtTongtien2,txtTienvanchuyen,txtVoucher,txtTongtt;
    private TextView txtTongthanhtoan;
    private ImageView imgDiachi,imgHinhthuc;
    private Button btnDathang;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ProgressBar progressbarTinhtien;
    private NumberFormat format;
    private int random,tongItem,soluong;
    private long tongtien, tongthanhtoan;
    RetrofitInterface apibanhang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView sanphamRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinhtien);
        apibanhang = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWidget();
        countItem();
        getGioHang();
        actionTinhTien();
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

    private void getWidget() {
        txtHoten = (EditText) findViewById(R.id.txtHoTen);
        txtSodt = (EditText) findViewById(R.id.txtSoDT);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtDiachi = (EditText) findViewById(R.id.txtDiaChi);
        txtTongtien1 = (EditText) findViewById(R.id.txtTongTien1);
        txtGhichu = (EditText) findViewById(R.id.txtGhiChu);
        txtNgaydat = (EditText) findViewById(R.id.txtNgayDat);
        txtNgaynhan = (EditText) findViewById(R.id.txtNgayNhan);
        txtHinhthuc = (EditText) findViewById(R.id.txtHinhThucThanhToan);
        txtTongtien2 = (EditText) findViewById(R.id.txtTongTien2);
        txtTienvanchuyen = (EditText) findViewById(R.id.txtTienVanChuyen);
        txtVoucher = (EditText) findViewById(R.id.txtVoucher);
        txtTongtt = (EditText) findViewById(R.id.txtTongTT);
        txtTongthanhtoan = (TextView) findViewById(R.id.txtTongThanhToan);
        imgDiachi = (ImageView) findViewById(R.id.imgChonDiaChi);
        imgHinhthuc = (ImageView) findViewById(R.id.imgChonHinhThucTT);
        btnDathang = (Button) findViewById(R.id.btnDatHang);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbarThanhtoan);
        progressbarTinhtien = (ProgressBar) findViewById(R.id.progressBarTinhTien);
        sanphamRecyclerview = (RecyclerView) findViewById(R.id.sanphamRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        sanphamRecyclerview.setLayoutManager(layoutManager);

        format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitUtilities.muahangList.clear();
                Intent intent = new Intent(getApplicationContext(),GiohangActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out);
            }
        });
        getThongTin();

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
                String hinhthuc = txtHinhthuc.getText().toString().trim();
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
                if (hinhthuc.equals("Hình thức thanh ")) {
                    txtHinhthuc.setError("Vui lòng chọn hình thức thanh toán!");
                    txtHinhthuc.setEnabled(true);
                    return;
                }
                Log.d("Tinhtien", new Gson().toJson(RetrofitUtilities.muahangList));
                compositeDisposable.add(apibanhang.setDonHang(idtaikhoan, hoten, sodt,email, diachi, ghichu, ngaydat, ngaynhan, tongItem, tongthanhtoan, new Gson().toJson(RetrofitUtilities.muahangList))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                taiKhoanModel -> {
                                    progressbarTinhtien.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressbarTinhtien.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                                            for(int i=0;i<RetrofitUtilities.muahangList.size();i++){
                                                RetrofitUtilities.giohanglist.remove(RetrofitUtilities.muahangList.get(i));
                                            }
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.nothing, R.anim.slide_out);
                                            finish();
                                        }
                                    },1500);

                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), "Không kết nối được với server "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));

            }
        });
    }

    private void getGioHang() {
        sanphamRecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        sanphamRecyclerview.setLayoutManager(layoutManager);
        GioHangAdapter gioHangAdapter = new GioHangAdapter(getApplicationContext(),RetrofitUtilities.muahangList);
        sanphamRecyclerview.setAdapter(gioHangAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RetrofitUtilities.muahangList.clear();
        Intent intent = new Intent(getApplicationContext(),GiohangActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}