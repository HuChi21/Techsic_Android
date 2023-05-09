package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.techsic.R;
import com.example.techsic.firebase.NotificationSendData;
import com.example.techsic.firebase.PushNotification;
import com.example.techsic.firebase.RetrofitPushNotif;
import com.example.techsic.models.CreateOrder;
import com.example.techsic.models.DonHang;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import vn.momo.momo_partner.AppMoMoLib;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ThanhtoanActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    private TextView txthinhthuc,txtiddonhang,txtsotien,txtGhichu;
    private Button btnThanhtoan,btnXacnhan;
    //momo
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "Techsic Shop";
    private String merchantCode = "MOMOC2IC20220510";
    private String merchantNameLabel = "Techsic Shop";
    private String description = "Thanh toán mua hàng";
    DonHang donHang;
    int iddonhang;
    private LinearLayout layoutThanhtoan,layoutThanhcong;
    private ProgressBar progressBar;
    RetrofitInterface apiconfig;
    PushNotification apipushnotif;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhtoan);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        apipushnotif = RetrofitPushNotif.getRetrofit().create(PushNotification.class);
        donHang = (DonHang) getIntent().getSerializableExtra("donHang");

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
        //AppZalopay
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        getWidget();
        actionToolbar();
        getThongtin();


    }

    private void actionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getWidget() {
        toolbar = findViewById(R.id.toolbarThanhtoan);
        txthinhthuc = findViewById(R.id.txthinhthuc);
        txtiddonhang = findViewById(R.id.txtIdDonHang);
        txtsotien = findViewById(R.id.txtSotien);
        txtGhichu = findViewById(R.id.txtGhichu);
        btnThanhtoan = findViewById(R.id.btnThanhtoan);

        layoutThanhtoan = findViewById(R.id.layoutThanhtoan);
        layoutThanhcong = findViewById(R.id.layoutThanhcong);
        progressBar = findViewById(R.id.progressBarTinhTien);
    }
    private void getThongtin() {
        txthinhthuc.setText(donHang.getHinhthuc());
        txtsotien.setText(donHang.getTongthanhtoan());
        String str = donHang.getTongthanhtoan();
        str = str.replaceAll("[^\\d]", ""); // loại bỏ tất cả các ký tự không phải số trong chuỗi
        int number = Integer.parseInt(str); // chuyển chuỗi thành một số nguyên
        txtiddonhang.setText(String.valueOf(donHang.getId()));
        txtGhichu.setText(donHang.getGhichu());
        if(donHang.getHinhthuc().trim().equals("Momo")){
            btnThanhtoan.setText("Thanh toán qua momo");
            btnThanhtoan.setTextColor(0xFFb0006d);
            txthinhthuc.setTextColor(0xFFb0006d);
            Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.momologoapp);
            btnThanhtoan.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            btnThanhtoan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPaymentMomo(number,donHang.getId());

                }
            });
        }
        else if(donHang.getHinhthuc().trim().equals("Zalopay")){
            btnThanhtoan.setText("Thanh toán qua Zalopay");
            btnThanhtoan.setTextColor(0xFF0068ff);
            txthinhthuc.setTextColor(0xFF0068ff);
            Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.zalopaylogoapp);
            btnThanhtoan.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            btnThanhtoan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Zalo thì chờ tý", Toast.LENGTH_SHORT).show();
                    requestPaymentZalopay(number,donHang.getId());
                }
            });
        }
    }
    //    Get token through MoMo app
    private void requestPaymentMomo(int total_amount, int iddonhang) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
//        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() != 0)
//            amount = edAmount.getText().toString().trim();

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", total_amount/1000); //Kiểu integer
        eventValue.put("orderId", iddonhang); //uniqueue id cho BillId, giá trị duy nhất cho mỗi BILL
        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Đơn hàng");//gán nhãn
        eventValue.put("fee", fee); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+iddonhang+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }
    //Get token callback from MoMo app an submit to server side
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    Log.d("Thanh cong:",data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    donHang.setIdtinhtrang(1);
                    compositeDisposable.add(apiconfig.updateThanhtoan(donHang.getId(), String.valueOf(donHang.getIdtinhtrang()),token)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        if(messageModel.isSuccess()){
                                            progressBar.setVisibility(View.VISIBLE);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    layoutThanhtoan.setVisibility(View.GONE);
                                                    layoutThanhcong.setVisibility(View.VISIBLE);
                                                    pushNotifToAdmin(donHang.getId());
                                                    pushNotifToUser(donHang.getId());
                                                }
                                            },4000);
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), "Lỗi "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                        Log.d("Thanh cong:","không thành công");

                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    Log.d("Thanh cong:","không thành công"+message);
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.d("Thanh cong:","không thành công");
                } else {
                    //TOKEN FAIL
                    Log.d("Thanh cong:","không thành công");
                }
            } else {
                Log.d("Thanh cong:","không thành công");
            }
        } else {
            Log.d("Thanh cong:","không thành công");
        }
    }
    private void requestPaymentZalopay(int total_amount, int iddonhang) {
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder(String.valueOf(total_amount/1000));
            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");

                ZaloPaySDK.getInstance().payOrder(ThanhtoanActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        compositeDisposable.add(apiconfig.updateThanhtoan(donHang.getId(),"1",token)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        messageModel -> {
                                            if(messageModel.isSuccess()){
                                                progressBar.setVisibility(View.VISIBLE);
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        layoutThanhtoan.setVisibility(View.GONE);
                                                        layoutThanhcong.setVisibility(View.VISIBLE);
                                                        pushNotifToAdmin(donHang.getId());
                                                        pushNotifToUser(donHang.getId());
                                                    }
                                                },2000);
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        finish();
                                                    }
                                                },5000);

                                            }
                                        },
                                        throwable -> {
                                            Toast.makeText(getApplicationContext(), "Lỗi "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                ));
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void pushNotifToAdmin(int iddonhang) {
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
                                    data.put("body","Đơn hàng: "+iddonhang+" đã thanh toán!");
                                    NotificationSendData notificationSendData = new NotificationSendData(token,data);

                                    compositeDisposable.add(apipushnotif.sendNotification(notificationSendData)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notificationResponse -> {
                                                    },
                                                    throwable -> {
                                                        Toast.makeText(getApplicationContext(), "Lỗi "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void pushNotifToUser(int iddonhang) {
        compositeDisposable.add(apiconfig.getTokenUser(donHang.getIdtaikhoan(),0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taiKhoanModel -> {
                            if(taiKhoanModel.isSuccess()){
                                for(int i=0;i<taiKhoanModel.getResult().size();i++){
                                    String token = taiKhoanModel.getResult().get(i).getToken();

                                    Map<String,String> data = new HashMap<>();
                                    data.put("title","Thông báo");
                                    data.put("body","Đã thanh toán thành công đơn hàng: "+iddonhang+" !");
                                    NotificationSendData notificationSendData = new NotificationSendData(token,data);

                                    compositeDisposable.add(apipushnotif.sendNotification(notificationSendData)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notificationResponse -> {
                                                        Intent intent = new Intent(getApplicationContext(),ChitietdonhangActivity.class);
                                                        intent.putExtra("donhang",donHang);
                                                        intent.putExtra("kieutinhtrang","Đã thanh toán");
                                                        startActivity(intent);
                                                        onBackPressed();
                                                    },
                                                    throwable -> {
                                                        Toast.makeText(getApplicationContext(), "Lỗi "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
        overridePendingTransition(R.anim.nothing,R.anim.slide_in);
    }
}