package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techsic.Cache;
import com.example.techsic.R;
import com.example.techsic.adapters.NhanXetAdapter;
import com.example.techsic.adapters.PhanLoaiAdapter;
import com.example.techsic.adapters.SanphamGoiyAdapter;
import com.example.techsic.interfaces.OnItemClickListener;
import com.example.techsic.models.GioHang;
import com.example.techsic.models.NhanXet;
import com.example.techsic.models.PhanLoai;
import com.example.techsic.models.SanPham;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.nex3z.notificationbadge.NotificationBadge;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChitietActivity extends AppCompatActivity implements OnItemClickListener, View.OnClickListener {
    private LinearLayout layoutDanhgia;
    private ImageView imgHinhanh,imgBack, imgHinhanhChonSL,imgthumbnail;
    private TextView txtTensp,txtGia,txtGiaKM,txtDaban,txtThuonghieu,txtThongso,txtMota, txtGiaChonSL,txtGiaKMChonSL,txtNhanXetTrong,txtRateScore;
    private RatingBar ratingBar;
    private ImageButton showMore;
    private Button btnChatnow,btnAddCard,btnBuynow, btnBuyChonSL;
    private Spinner spinner;
    private SanPham sanPham;
    private List<SanPham> sanPhamList = new ArrayList<>();
    private List<PhanLoai> phanLoaiList = new ArrayList<>();
    private List<NhanXet> nhanXetList = new ArrayList<>();
    private NotificationBadge notifSoLuong;
    private FrameLayout frameCart;
    private Dialog dialog;
    private NumberFormat format;
    private RecyclerView recyclerViewNhanxet, recyclerviewGoiy,recyclerviewPhanloai, recyclerviewDialogPhanloai;
    private SanphamGoiyAdapter sanPhamGoiYAdapter;
    private PhanLoaiAdapter phanLoaiAdapter;
    private NhanXetAdapter nhanXetAdapter;
    private long tongtt;
    private int soluong;
    private String phanloai;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        getWidget();
        getThongTin();
        getNhanxet();
    }
    @Override
    public void onItemClick(String value,int position) {
        phanloai = value;
    }
    private void getWidget() {
        imgthumbnail = (ImageView) findViewById(R.id.imgthumbnail);
        imgHinhanh = (ImageView) findViewById(R.id.imgHinhanh);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtTensp = (TextView) findViewById(R.id.txtTensp);
        txtGia = (TextView) findViewById(R.id.txtGia);
        txtGiaKM = (TextView) findViewById(R.id.txtGiaKM);
        txtDaban = (TextView) findViewById(R.id.txtDaban);
        txtThuonghieu = (TextView) findViewById(R.id.txtThuonghieu);
        txtThongso = (TextView) findViewById(R.id.txtThongso);
        txtMota = (TextView) findViewById(R.id.txtMota);
        layoutDanhgia = findViewById(R.id.txtDanhgiasanpham);
        txtNhanXetTrong = findViewById(R.id.txtNhanXetTrong);
        txtRateScore = findViewById(R.id.txtRateScore);
        ratingBar = findViewById(R.id.rating_bar);

        notifSoLuong = (NotificationBadge) findViewById(R.id.notifSoLuong);
        frameCart = (FrameLayout) findViewById(R.id.frameCart);
        format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));

        recyclerviewGoiy = (RecyclerView) findViewById(R.id.goiyRecyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerviewGoiy.setLayoutManager(layoutManager);

        recyclerviewPhanloai = (RecyclerView) findViewById(R.id.phanloaiRecyclerView);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerviewPhanloai.setLayoutManager(layoutManager2);

        recyclerViewNhanxet = (RecyclerView) findViewById(R.id.nhanxetRecyclerview);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewNhanxet.setLayoutManager(layoutManager3);


        frameCart.setOnClickListener(this);
        //
        btnChatnow = (Button) findViewById(R.id.btnChatnow);
        btnAddCard = (Button) findViewById(R.id.btnAddcart);
        btnBuynow = (Button) findViewById(R.id.btnBuynow);

        imgBack.setOnClickListener(this);
        btnChatnow.setOnClickListener(this);
        btnAddCard.setOnClickListener(this);
        btnBuynow.setOnClickListener(this);
        if(RetrofitUtilities.giohanglist != null){
            int tongItem = 0;
            for(int i = 0; i<RetrofitUtilities.giohanglist.size(); i++){
                tongItem += RetrofitUtilities.giohanglist.get(i).getSoluong();
            }
            notifSoLuong.setText(String.valueOf(tongItem));
        }
    }
    private void getThongTin() {
        sanPham = (SanPham) getIntent().getSerializableExtra("chitiet");
        if(sanPham.getHinhanh().contains("https")){
            Glide.with(getApplicationContext()).load(sanPham.getHinhanh()).into(imgHinhanh);
        }
        else{
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + sanPham.getHinhanh();
            Glide.with(getApplicationContext()).load(hinhanh).into(imgHinhanh);
        }

        txtTensp.setText(sanPham.getTensp());
        txtGia.setText(format.format(sanPham.getGiasp()));
        BigDecimal giakm = sanPham.getGiasp().add(new BigDecimal("500000")) ;
        txtGiaKM.setText(format.format(giakm));
        txtGiaKM.setPaintFlags(txtGiaKM.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtDaban.setText("Đã bán "+sanPham.getSldaban());
        txtThuonghieu.setText(sanPham.getThuonghieu());
        txtThongso.setText(sanPham.getThongso());
        txtMota.setText(sanPham.getMota());
        txtThuonghieu.setText(sanPham.getThuonghieu());
        //kiểm tra sản phẩm mới nhất
        int spmoi =Cache.getInstance().getData() - 10;
        if(spmoi <= sanPham.getIdsp()){
            imgthumbnail.setVisibility(View.VISIBLE);
        }
        txtMota = (TextView) findViewById(R.id.txtMota);
        showMore = (ImageButton) findViewById(R.id.showMore);
        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtMota.getMaxLines() == 5) {
                    txtMota.setMaxLines(Integer.MAX_VALUE);
                    showMore.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                }
                else{
                    txtMota.setMaxLines(5);
                    showMore.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                }
            }
        });

        compositeDisposable.add(apiconfig.getPhanloai(sanPham.getIdsp())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        phanLoaiModel -> {
                            if(phanLoaiModel.isSuccess()){
                                phanLoaiList = phanLoaiModel.getResult();
                                phanLoaiAdapter = new PhanLoaiAdapter(getApplicationContext(), phanLoaiList);
                                phanLoaiAdapter.setOnItemClickListener(this);
                                phanLoaiAdapter.getDefaultSelectedItem(this);
                                recyclerviewPhanloai.setAdapter(phanLoaiAdapter);
                            }
                        },
                        throwable -> {
                            Log.d("apiconfig", "Không kết nối được với server "+throwable.getMessage());
                        }
                ));
        compositeDisposable.add(apiconfig.getSPgoiy(sanPham.getIdloaisp())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()){
                                sanPhamList = sanPhamModel.getResult();
                                sanPhamGoiYAdapter = new SanphamGoiyAdapter(getApplicationContext(), sanPhamList);
                                recyclerviewGoiy.setAdapter(sanPhamGoiYAdapter);
                            }
                        },
                        throwable -> {
                            Log.d("apiconfig", "Không kết nối được với server "+throwable.getMessage());
                        }
                ));
    }
    private void getNhanxet() {
        layoutDanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerViewNhanxet.getVisibility()==View.GONE){
                    recyclerViewNhanxet.setVisibility(View.VISIBLE);
                }else{
                    recyclerViewNhanxet.setVisibility(View.GONE);
                }
            }
        });
        compositeDisposable.add(apiconfig.getNhanxet(sanPham.getIdsp())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        nhanXetModel -> {
                            if(nhanXetModel.isSuccess()){
                                if(nhanXetModel.getResult().size()==0){
                                    txtNhanXetTrong.setVisibility(View.VISIBLE);
                                    layoutDanhgia.setVisibility(View.GONE);
                                }
                                nhanXetList = nhanXetModel.getResult();
                                nhanXetAdapter = new NhanXetAdapter(getApplicationContext(),nhanXetList);
                                recyclerViewNhanxet.setAdapter(nhanXetAdapter);
                                int luotdanhgia = 0;
                                float danhgiaavg = 0;
                                for(int i=0;i<nhanXetModel.getResult().size();i++){
                                    luotdanhgia = nhanXetModel.getResult().size();
                                    danhgiaavg += nhanXetModel.getResult().get(i).getDanhgia();
                                }
                                ratingBar.setRating(danhgiaavg/luotdanhgia);
                                txtRateScore.setText(danhgiaavg/luotdanhgia+"/5"+"\t("+luotdanhgia+" đánh giá)");
                            }
                        },
                        throwable -> {
                            Log.d("apiconfig", "Không kết nối được với server "+throwable.getMessage());
                        }
                ));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frameCart:
                Intent intent = new Intent(getApplicationContext(), GiohangActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btnChatnow:
                startActivity(new Intent(getApplicationContext(),ChatActivity.class));
                overridePendingTransition(R.anim.nothing,R.anim.slide_out);
                break;
            case R.id.btnAddcart:
                actionThemVaoGio();
                btnBuyChonSL.setText("Thêm vào giỏ");
                break;
            case R.id.btnBuynow:
                actionMuaNgay();
                break;
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
        btnBuyChonSL = (Button) dialog.findViewById(R.id.itemchonsl_btnBuy);
        txtGiaChonSL =(TextView)dialog.findViewById(R.id.itemchonsl_txtGia);
        txtGiaKMChonSL =(TextView)dialog.findViewById(R.id.itemchonsl_txtGiaKM);

        recyclerviewDialogPhanloai = (RecyclerView) dialog.findViewById(R.id.phanloaiRecyclerViewDialog);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerviewDialogPhanloai.setLayoutManager(layoutManager3);
        compositeDisposable.add(apiconfig.getPhanloai(sanPham.getIdsp())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        phanLoaiModel -> {
                            if(phanLoaiModel.isSuccess()){
                                phanLoaiList = phanLoaiModel.getResult();
                                phanLoaiAdapter = new PhanLoaiAdapter(getApplicationContext(), phanLoaiList);
                                phanLoaiAdapter.setOnItemClickListener(this);
                                recyclerviewDialogPhanloai.setAdapter(phanLoaiAdapter);
                            }
                        },
                        throwable -> {
                            Log.d("apiconfig", "Không kết nối được với server "+throwable.getMessage());
                        }
                ));

        spinner = (Spinner) dialog.findViewById(R.id.spinner);
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            numbers.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(sanPham.getHinhanh().contains("https")){
            Glide.with(getApplicationContext()).load(sanPham.getHinhanh()).into(imgHinhanhChonSL);
        }
        else{
            String hinhanh = RetrofitInterface.BASE_URL+"images/" + sanPham.getHinhanh();
            Glide.with(getApplicationContext()).load(hinhanh).into(imgHinhanhChonSL);
        }
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
                if(spinner.getSelectedItem() == null){
                    dialog.show();
                }
                else{
                    dialog.dismiss();
                    btnAddCard.setText("Xem trong giỏ");
                    Toast.makeText(ChitietActivity.this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
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
            }
        });
    }
    private void actionMuaNgay()  {
        getDialogSoLuong(Gravity.BOTTOM);
        dialog.show();
        btnBuyChonSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinner.getSelectedItem() == null ){
                    Toast.makeText(ChitietActivity.this, "Chọn số lượng!", Toast.LENGTH_SHORT).show();
                }
                else{
                    soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                    long tongtt = soluong * sanPham.getGiasp().longValue();
                    RetrofitUtilities.muahangList.clear();
//                RetrofitUtilities.muahangList.add(sanPham);
                    GioHang muahang = new GioHang();
                    muahang.setIdsp(sanPham.getIdsp());
                    muahang.setTensp(sanPham.getTensp());
                    muahang.setHinhanh(sanPham.getHinhanh());
                    muahang.setGiasp(sanPham.getGiasp());
                    muahang.setSoluong(soluong);
                    muahang.setPhanloai(phanloai);
                    muahang.setThuonghieu(sanPham.getThuonghieu());
                    RetrofitUtilities.muahangList.add(muahang);
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),TinhtienActivity.class);
                    intent.putExtra("txtTongTien", tongtt);
                    intent.putExtra("txtSoLuong", soluong);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in, R.anim.nothing);
                }
            }
        });
    }
    private void actionThemSoLuong() {
        if(RetrofitUtilities.giohanglist.size() > 0){
            boolean flag = false;
            if(spinner.getSelectedItem() == null){
                Toast.makeText(this, "Chọn số lượng", Toast.LENGTH_SHORT).show();
            }
            else {
                soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            }
                for (int i = 0; i < RetrofitUtilities.giohanglist.size(); i++) {
                    if (RetrofitUtilities.giohanglist.get(i).getIdsp() == sanPham.getIdsp()) {
                        RetrofitUtilities.giohanglist.get(i).setSoluong(soluong + RetrofitUtilities.giohanglist.get(i).getSoluong());
                        flag = true;
                    }
                }
            if (flag == false) {
                BigDecimal gia = sanPham.getGiasp().multiply(new BigDecimal(soluong));
                GioHang gioHang = new GioHang();
                gioHang.setIdsp(sanPham.getIdsp());
                gioHang.setTensp(sanPham.getTensp());
                gioHang.setHinhanh(sanPham.getHinhanh());
                gioHang.setGiasp(sanPham.getGiasp());
                gioHang.setSoluong(soluong);
                gioHang.setPhanloai(phanloai);
                gioHang.setThuonghieu(sanPham.getThuonghieu());
                RetrofitUtilities.giohanglist.add(gioHang);
            }
        }else{
            if(spinner.getSelectedItem() == null){
                Toast.makeText(this, "Chọn số lượng", Toast.LENGTH_SHORT).show();
            }else{
                soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            }
            BigDecimal gia = sanPham.getGiasp().multiply(new BigDecimal(soluong));
            GioHang gioHang = new GioHang();
            gioHang.setIdsp(sanPham.getIdsp());
            gioHang.setTensp(sanPham.getTensp());
            gioHang.setHinhanh(sanPham.getHinhanh());
            gioHang.setGiasp(sanPham.getGiasp());
            gioHang.setSoluong(soluong);
            gioHang.setPhanloai(phanloai);
            gioHang.setThuonghieu(sanPham.getThuonghieu());
            RetrofitUtilities.giohanglist.add(gioHang);
        }
        int tongItem = 0;
        for(int i = 0; i<RetrofitUtilities.giohanglist.size(); i++){
            tongItem += RetrofitUtilities.giohanglist.get(i).getSoluong();
        }
        notifSoLuong.setText(String.valueOf(tongItem));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


}