package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techsic.R;
import com.example.techsic.adapters.TimKiemSanphamAdapter;
import com.example.techsic.models.SanPham;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private String searchTensp ;
    private ImageView imgClear;
    private Toolbar toolbarTimkiem;
    private RecyclerView recyclerView;
    private List<SanPham> sanPhamList;
    private TimKiemSanphamAdapter timkiemAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RetrofitInterface apibanhang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        apibanhang = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        getWidget();
        actionToolbar();
    }

    private void getWidget() {
        toolbarTimkiem = findViewById(R.id.toolbarTimkiem);
        recyclerView = findViewById(R.id.searchRecycler);
        edtSearch = findViewById(R.id.edtTimKiem);
        imgClear = findViewById(R.id.imgClear);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtSearch.setText("");
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    sanPhamList.clear();
                    timkiemAdapter = new TimKiemSanphamAdapter(getApplicationContext(),sanPhamList);
                    recyclerView.setAdapter(timkiemAdapter);
                }else{
                    searchTensp = charSequence.toString().trim();
                    timkiemSP(searchTensp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void actionToolbar() {
        setSupportActionBar(toolbarTimkiem);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTimkiem.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbarTimkiem.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void timkiemSP(String string ) {
        compositeDisposable.add(apibanhang.getSP(string)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()){
                                sanPhamList = sanPhamModel.getResult();
                                timkiemAdapter = new TimKiemSanphamAdapter(getApplicationContext(),sanPhamList);
                                recyclerView.setAdapter(timkiemAdapter);
                            }else{
                                sanPhamList.clear();
                                timkiemAdapter.notifyDataSetChanged();
                                Toast.makeText(this, "Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
                            }
                        },throwable -> {
                            sanPhamList.clear();
                            timkiemAdapter.notifyDataSetChanged();
                            Toast.makeText(this, "Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
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