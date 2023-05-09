package com.example.techsicmanager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.techsicmanager.R;
import com.example.techsicmanager.adapters.TaiKhoanAdapter;
import com.example.techsicmanager.adapters.TaiKhoanChatAdapter;
import com.example.techsicmanager.models.TaiKhoan;
import com.example.techsicmanager.retrofit.RetrofitInterface;
import com.example.techsicmanager.retrofit.RetrofitUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanlyChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TaiKhoanChatAdapter taiKhoanChatAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanlychat);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        getWidget();
        actionToolbar();
        getTaikhoanchatFromFirebase();
    }

    private void getWidget() {
        toolbar = findViewById(R.id.toolbarTaikhoanChat);
        recyclerView = findViewById(R.id.recyclerViewTaiKhoanChat);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
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
    private void getTaikhoanchatFromFirebase() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("taikhoans")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<TaiKhoan> taiKhoanList = new ArrayList<>();
                            for(QueryDocumentSnapshot documentSnapshot :task.getResult()){
                                TaiKhoan taiKhoan = new TaiKhoan();
                                taiKhoan.setIdtaikhoan(documentSnapshot.getLong("id").intValue());
                                taiKhoan.setEmail(documentSnapshot.getString("email"));
                                taiKhoan.setHoten(documentSnapshot.getString("hoten"));
                                taiKhoan.setHinhanh(documentSnapshot.getString("hinhanh"));
                                taiKhoanList.add(taiKhoan);
                            }
                            if(taiKhoanList.size()>0){
                                taiKhoanChatAdapter = new TaiKhoanChatAdapter(getApplicationContext(),taiKhoanList);
                                recyclerView.setAdapter(taiKhoanChatAdapter);
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}