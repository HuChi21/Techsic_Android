package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.models.GioHang;
import com.example.techsic.models.SanPham;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.nex3z.notificationbadge.NotificationBadge;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

public class ChitietActivity extends AppCompatActivity {
    private ImageView imgHinhanh,imgBack, imgHinhanhChonSL;
    private TextView txtTensp,txtGia,txtGiaKM,txtThuonghieu,txtPhanloai,txtThongso,txtMota, txtGiaChonSL,txtGiaKMChonSL,txtPhanloaiChonSL;
    private ImageButton show, hide;
    private Button btnChatnow,btnAddCard,btnBuynow, btnBuyChonSL;
    private Spinner spinner;
    private SanPham sanPham;
    private NotificationBadge notifSoLuong;
    private FrameLayout frameCart;
    private Toolbar toolbarChitiet;
    private Dialog dialog;
    private NumberFormat format;
    private long tongtt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWidget();
        getThongTin();
    }

    private void actionShowMore() {
        txtMota = (TextView) findViewById(R.id.txtMota);
        show = (ImageButton) findViewById(R.id.showMore);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Show button");
                show.setVisibility(View.INVISIBLE);
                hide.setVisibility(View.VISIBLE);
                txtMota.setMaxLines(Integer.MAX_VALUE);
            }
        });
        hide = (ImageButton) findViewById(R.id.hideMore);
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Hide button");
                hide.setVisibility(View.INVISIBLE);
                show.setVisibility(View.VISIBLE);
                txtMota.setMaxLines(5);
            }
        });
    }

    private void getWidget() {
        imgHinhanh = (ImageView) findViewById(R.id.imgHinhanh);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtTensp = (TextView) findViewById(R.id.txtTensp);
        txtGia = (TextView) findViewById(R.id.txtGia);
        txtGiaKM = (TextView) findViewById(R.id.txtGiaKM);
        txtThuonghieu = (TextView) findViewById(R.id.txtThuonghieu);
        txtPhanloai = (TextView) findViewById(R.id.txtPhanloai);
        txtThongso = (TextView) findViewById(R.id.txtThongso);
        txtMota = (TextView) findViewById(R.id.txtMota);
        notifSoLuong = (NotificationBadge) findViewById(R.id.notifSoLuong);
        frameCart = (FrameLayout) findViewById(R.id.frameCart);
        format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));

        frameCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),GiohangActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
            }
        });
        //
        btnChatnow = (Button) findViewById(R.id.btnChatnow);
        btnAddCard = (Button) findViewById(R.id.btnAddcart);
        btnBuynow = (Button) findViewById(R.id.btnBuynow);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out);
            }
        });


        btnChatnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionThemVaoGio();
                btnBuyChonSL.setText("Thêm vào giỏ");
            }
        });
        btnBuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMuaNgay();
            }
        });
        if(RetrofitUtilities.giohanglist != null){
            int tongItem = 0;
            for(int i = 0; i<RetrofitUtilities.giohanglist.size(); i++){
                tongItem += RetrofitUtilities.giohanglist.get(i).getSoluong();
            }
            notifSoLuong.setText(String.valueOf(tongItem));
        }
    }

    private void getDialogSoLuong(int gravity)  {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_chonsoluong);

        Window window = dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);


        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = gravity;
        window.setAttributes(windowAttribute);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }
        //anhxa
        imgHinhanhChonSL = (ImageView)dialog.findViewById(R.id.itemchonsl_imgHinhanh) ;
        txtPhanloaiChonSL = (TextView) dialog.findViewById(R.id.itemchonsl_txtPhanloai);
        btnBuyChonSL = (Button) dialog.findViewById(R.id.itemchonsl_btnBuy);
        txtGiaChonSL =(TextView)dialog.findViewById(R.id.itemchonsl_txtGia);
        txtGiaKMChonSL =(TextView)dialog.findViewById(R.id.itemchonsl_txtGiaKM);

        spinner = (Spinner) dialog.findViewById(R.id.spinner);
        Integer[] No = new Integer[]{1,2,3,4,5,6,7,8,9,10,11};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,No);
        spinner.setAdapter(arrayAdapter);
        Glide.with(getApplicationContext()).load(sanPham.getHinhanh()).into(imgHinhanhChonSL);
        txtPhanloaiChonSL.setText(sanPham.getPhanloai());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        txtGiaChonSL.setText(format.format(sanPham.getGiasp()));
        BigDecimal giakm = sanPham.getGiasp().add(new BigDecimal("500000")) ;
        txtGiaKMChonSL.setText(format.format(giakm));
        txtGiaKMChonSL.setPaintFlags(txtGiaKMChonSL.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void actionThemVaoGio(){
        getDialogSoLuong(Gravity.BOTTOM);
        dialog.show();
        btnBuyChonSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionThemSoLuong();
                Toast.makeText(ChitietActivity.this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                btnAddCard.setText("Xem trong giỏ");
                btnAddCard.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.baseline_shopping_cart_ff8800_24,0,0);
                btnAddCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),GiohangActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                    }
                });
            }
        });
    }
    private void actionMuaNgay()  {
        getDialogSoLuong(Gravity.BOTTOM);
        dialog.show();
        btnBuyChonSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                long tongtt = soluong * sanPham.getGiasp().longValue();
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),TinhtienActivity.class);
                intent.putExtra("txtTongTien", tongtt);
                intent.putExtra("txtSoLuong", soluong);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
            }
        });
    }

    private void actionThemSoLuong() {
        if(RetrofitUtilities.giohanglist.size() > 0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i = 0; i<RetrofitUtilities.giohanglist.size(); i++){
                if(RetrofitUtilities.giohanglist.get(i).getIdsp()==sanPham.getIdsp()){
                    RetrofitUtilities.giohanglist.get(i).setSoluong(soluong + RetrofitUtilities.giohanglist.get(i).getSoluong());
                    flag = true;
                }

            }

            if(flag == false){
                BigDecimal gia = sanPham.getGiasp().multiply(new BigDecimal(soluong));
                GioHang gioHang = new GioHang();
                gioHang.setIdsp(sanPham.getIdsp());
                gioHang.setTensp(sanPham.getTensp());
                gioHang.setHinhanh(sanPham.getHinhanh());
                gioHang.setGiasp(sanPham.getGiasp());
                gioHang.setSoluong(soluong);
                gioHang.setPhanloai(sanPham.getPhanloai());
                gioHang.setThuonghieu(sanPham.getThuonghieu());
                RetrofitUtilities.giohanglist.add(gioHang);
            }
        }else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            BigDecimal gia = sanPham.getGiasp().multiply(new BigDecimal(soluong));
            GioHang gioHang = new GioHang();
            gioHang.setIdsp(sanPham.getIdsp());
            gioHang.setTensp(sanPham.getTensp());
            gioHang.setHinhanh(sanPham.getHinhanh());
            gioHang.setGiasp(sanPham.getGiasp());
            gioHang.setSoluong(soluong);
            gioHang.setPhanloai(sanPham.getPhanloai());
            gioHang.setThuonghieu(sanPham.getThuonghieu());
            RetrofitUtilities.giohanglist.add(gioHang);
        }
        int tongItem = 0;
        for(int i = 0; i<RetrofitUtilities.giohanglist.size(); i++){
            tongItem += RetrofitUtilities.giohanglist.get(i).getSoluong();
        }
        notifSoLuong.setText(String.valueOf(tongItem));
    }

    private void getThongTin() {
        sanPham = (SanPham) getIntent().getSerializableExtra("chitiet");
        Glide.with(getApplicationContext()).load(sanPham.getHinhanh()).into(imgHinhanh);
        txtTensp.setText(sanPham.getTensp());
        txtGia.setText(format.format(sanPham.getGiasp()));
        BigDecimal giakm = sanPham.getGiasp().add(new BigDecimal("500000")) ;
        txtGiaKM.setText(format.format(giakm));
        txtGiaKM.setPaintFlags(txtGiaKM.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtThuonghieu.setText(sanPham.getThuonghieu());
        txtPhanloai.setText(sanPham.getPhanloai());
        txtThongso.setText(sanPham.getThongso());
        txtMota.setText(sanPham.getMota());
        txtThuonghieu.setText(sanPham.getThuonghieu());

        actionShowMore();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }
}