package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.techsic.R;
import com.example.techsic.retrofit.RetrofitUtilities;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class TinhtienActivity extends AppCompatActivity {
    private EditText txtHoten,txtSodt,txtEmail,txtDiachi,txtTongtien1,txtGhichu,txtNgaydat,txtNgaynhan,txtHinhthuc;
    private EditText txtTongtien2,txtTienvanchuyen,txtVoucher,txtTongtt;
    private TextView txtTongthanhtoan;
    private ImageView imgDiachi,imgHinhthuc;
    private Button btnDathang;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NumberFormat format;
    private int random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinhtien);

        getWidget();
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

        format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));

    }

    private void actionTinhTien(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getThongTin();
        btnDathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                if(diachi.equals("Địa chỉ")){
                    txtDiachi.setError("Vui lòng chọn địa chỉ!");
                    txtDiachi.setEnabled(true);
                    return;
                }
                if(hinhthuc.equals("Hình thức thanh toán")){
                    txtHinhthuc.setError("Vui lòng chọn hình thức thanh toán!");
                    txtHinhthuc.setEnabled(true);
                    return;
                }
                Toast.makeText(TinhtienActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getThongTin(){
        txtHoten.setText(RetrofitUtilities.taiKhoanGanDay.getHoten());
        txtSodt.setText(RetrofitUtilities.taiKhoanGanDay.getSodt());
        txtEmail.setText(RetrofitUtilities.taiKhoanGanDay.getEmail());
        txtDiachi.setText(RetrofitUtilities.taiKhoanGanDay.getDiachi());

        random = new Random().nextInt(4) + 3;
        long tongtien = getIntent().getLongExtra("txtTongTien",0);
        BigDecimal tongtienhang = BigDecimal.valueOf(tongtien);
        txtTongtien1.setText(format.format(tongtienhang));
        txtTongtien2.setText(format.format(tongtienhang));


        long voucher = - tongtien * random / 100;
        BigDecimal tienVoucher = BigDecimal.valueOf(voucher);
        txtVoucher.setText(format.format(tienVoucher));


        txtGhichu.setText("Nevermind");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String ngaygui = df.format(calendar.getTime());
        try {
            calendar.setTime(df.parse(ngaygui));
            txtNgaydat.setText(ngaygui);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        
        calendar.add(Calendar.DATE, random);
        String ngaynhan = df.format(calendar.getTime());
        txtNgaynhan.setText(ngaynhan);

        long vanchuyenperday = 5000;
        txtTienvanchuyen.setText(format.format(vanchuyenperday*random));

        long tongthanhtoan = tongtien + vanchuyenperday * random + voucher;
        txtTongtt.setText(format.format(tongthanhtoan));
        txtTongthanhtoan.setText(format.format(tongthanhtoan));
    }
}