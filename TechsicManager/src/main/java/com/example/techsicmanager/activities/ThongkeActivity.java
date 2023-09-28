package com.example.techsicmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techsicmanager.R;
import com.example.techsicmanager.adapters.ThongKeMainAdapter;
import com.example.techsicmanager.models.DonHang;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThongkeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private PieChart pieChart;
    private TextView txtTuNgay,txtDenNgay,btnGui;
    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    String tungay = "01/01/2000",denngay="31/12/2024";
    List<DonHang> donHangList;
    List<PieEntry> pieEntries = new ArrayList<>(); ;
    private String startDateString,endDateString;
    private PieData data;
    private PieDataSet pieDataSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        getWidget();
        actionToolbar();
        getThongTin(tungay,denngay);
        getTruyvan();
    }


    private void getWidget() {
        toolbar = findViewById(R.id.toolbarThongke);
        pieChart = findViewById(R.id.piechartThongke);
        txtTuNgay= findViewById(R.id.txtTuNgay);
        txtDenNgay= findViewById(R.id.txtDenNgay);
        btnGui= findViewById(R.id.btnGui);
        recyclerView = findViewById(R.id.recyclerviewThongKe);

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

    private void getTruyvan() {
        // Khởi tạo ngày bắt đầu và ngày kết thúc mặc định
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        // Tạo DatePickerDialog cho ngày bắt đầu
        DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDate.set(Calendar.YEAR, year);
                startDate.set(Calendar.MONTH, month);
                startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDateString = new SimpleDateFormat("dd/MM/yyyy").format(startDate.getTime());
                // Lấy giá trị của startday dưới dạng chuỗi
                Calendar today = Calendar.getInstance();
                if (endDate.before(today) && startDate.before(endDate)) {
                    // Tính toán date range
                    long startTimeInMillis = startDate.getTimeInMillis();
                    long endTimeInMillis = endDate.getTimeInMillis();
                    tungay =startDateString;
                    txtTuNgay.setText(startDateString);
                    // Chuyển đổi date range từ milliseconds sang days hoặc giờ hoặc phút hoặc giây tùy theo nhu cầu của bạn
                } else {
                    Toast.makeText(ThongkeActivity.this, "Vui lòng chọn ngày bắt đầu trước ngày kết thúc, ngày kết thúc trước ngày hiện tại.", Toast.LENGTH_SHORT).show();
                }
            }
        };
        DatePickerDialog startDatePickerDialog = new DatePickerDialog(ThongkeActivity.this, startDateListener, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));

        // Tạo DatePickerDialog cho ngày kết thúc
        DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDate.set(Calendar.YEAR, year);
                endDate.set(Calendar.MONTH, month);
                endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDateString = new SimpleDateFormat("dd/MM/yyyy").format(endDate.getTime());
                // Lấy giá trị của endday dưới dạng chuỗi
                // Kiểm tra điều kiện startday < endday và endday phải <= hôm nay
                Calendar today = Calendar.getInstance();
                if (endDate.before(today) && startDate.before(endDate)) {
                    // Tính toán date range
                    long startTimeInMillis = startDate.getTimeInMillis();
                    long endTimeInMillis = endDate.getTimeInMillis();
                    denngay = endDateString;
                    txtDenNgay.setText(endDateString);
                    // Chuyển đổi date range từ milliseconds sang days hoặc giờ hoặc phút hoặc giây tùy theo nhu cầu của bạn
                } else {
                    Toast.makeText(ThongkeActivity.this, "Vui lòng chọn ngày bắt đầu trước ngày kết thúc, ngày kết thúc trước ngày hiện tại.", Toast.LENGTH_SHORT).show();
                }
            }
        };
        DatePickerDialog endDatePickerDialog = new DatePickerDialog(ThongkeActivity.this, endDateListener, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));

        // Hiển thị DatePickerDialog cho ngày bắt đầu và ngày kết thúc khi người dùng nhấn vào một nút hoặc một vùng nhập liệu tương tự
        txtTuNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePickerDialog.show();
            }
        });
        txtDenNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePickerDialog.show();
            }
        });
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                getThongTin(tungay,denngay);
            }
        });
    }
    private void clearData() {
        // Xóa dữ liệu của PieChart
        pieEntries.clear();
        pieChart.clear();

        // Xóa dữ liệu của RecyclerView
        donHangList.clear();
        recyclerView.setAdapter(null);
    }
    private void getThongTin(String tungay, String denngay) {
        // getDataChart
        compositeDisposable.add(apiconfig.getThongke(tungay,denngay)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        thongKeModel -> {
                            if(thongKeModel.isSuccess()){
                                for(int i=0;i<thongKeModel.getResult().size();i++){
                                    String tenloaisp = thongKeModel.getResult().get(i).getTenloaisp();
                                    int tongsp = thongKeModel.getResult().get(i).getTongsp();
                                    pieEntries.add(new PieEntry(tongsp,tenloaisp));
                                }
                                pieDataSet = new PieDataSet(pieEntries,"");
                                data = new PieData();
                                data.setDataSet(pieDataSet);
                                data.setValueTextSize(15f);
                                data.setValueFormatter(new PercentFormatter(pieChart));
                                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                pieChart.setData(data);
                                pieChart.animateXY(2000,2000);
                                pieChart.setUsePercentValues(true);
                                pieChart.getDescription().setEnabled(false);
                                pieChart.invalidate();
                            }
                        }
                ));
        //xong phần thống kê, còn phần lọc bên frontend
        //getRecyclerView
        compositeDisposable.add(apiconfig.getDonhang("WHERE donhang_tinhtrang.idtinhtrang IN  (5,7) AND STR_TO_DATE(donhang.ngaynhan, '%d/%m/%Y') >= STR_TO_DATE('"+tungay+"', '%d/%m/%Y')\n" +
                        " AND STR_TO_DATE(donhang.ngaynhan, '%d/%m/%Y') <= STR_TO_DATE('"+denngay+"', '%d/%m/%Y') ORDER BY STR_TO_DATE(donhang.ngaynhan, '%d/%m/%Y')  DESC LIMIT 30")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            if(donHangModel.isSuccess()){
                                donHangList = donHangModel.getResult();
                                ThongKeMainAdapter thongKeAdapter = new ThongKeMainAdapter(getApplicationContext(), donHangList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(thongKeAdapter);
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
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}