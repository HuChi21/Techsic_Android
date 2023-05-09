package com.example.techsic.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.activities.ChitietTintucActivity;
import com.example.techsic.adapters.PhanLoaiAdapter;
import com.example.techsic.adapters.SanphamAdapter;
import com.example.techsic.adapters.TintucAdapter;
import com.example.techsic.models.TinTuc;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewsTatcaFragment extends Fragment {
    private LinearLayout layouttintuc;
    private ImageView imgTintuc;
    private TextView tacgiaTintuc,thoigianTintuc,tieudeTintuc;
    private RecyclerView recyclerviewTintuc;
    private TintucAdapter tintucAdapter;
    private List<TinTuc> tinTucList;
    private TinTuc tinTucMoi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tintuc, container, false);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        getWidget(view);
        String moinhat = "order by idtintuc desc";
        getTintuc(moinhat);
        gettintucmoi(moinhat);
        return view;
    }

    private void getWidget(View view) {
        layouttintuc = view.findViewById(R.id.layouttintuc);
        imgTintuc = view.findViewById(R.id.imgTintuc);
        tacgiaTintuc = view.findViewById(R.id.tacgiaTintuc);
        thoigianTintuc = view.findViewById(R.id.thoigianTintuc);
        tieudeTintuc = view.findViewById(R.id.tieudeTintuc);

        recyclerviewTintuc = view.findViewById(R.id.recyclerviewTintuc);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerviewTintuc.setLayoutManager(layoutManager);
        recyclerviewTintuc.setHasFixedSize(true);
    }
    private void gettintucmoi(String moinhat) {
        compositeDisposable.add(apiconfig.getTintuc(moinhat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tinTucModel -> {
                            if(tinTucModel.isSuccess()){
                                tinTucMoi = tinTucModel.getResult().get(0);
                                if(tinTucMoi.getHinhanh().contains("https")){
                                    Glide.with(getActivity()).load(tinTucMoi.getHinhanh()).into(imgTintuc);
                                }
                                else{
                                    String hinhanh = RetrofitInterface.BASE_URL+"images/" + tinTucMoi.getHinhanh();
                                    Glide.with(getActivity()).load(hinhanh).into(imgTintuc);
                                }
                                tacgiaTintuc.setText(tinTucMoi.getTacgia());
                                thoigianTintuc.setText(tinTucMoi.getThoigian());
                                tieudeTintuc.setText(tinTucMoi.getTieude());
                                layouttintuc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), ChitietTintucActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("tintuc",tinTucMoi);
                                        startActivity(intent);
                                    }
                                });
                            }
                        },
                        throwable -> {
                            Log.d("Loi tin tuc", "" + throwable.getMessage());
                        }
                ));
    }

    private void getTintuc(String moinhat) {
        compositeDisposable.add(apiconfig.getTintuc(moinhat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tinTucModel -> {
                            if(tinTucModel.isSuccess()){
                                tinTucModel.getResult().remove(0);
                                tinTucList = tinTucModel.getResult();
                                tintucAdapter = new TintucAdapter(getActivity(), tinTucList);
                                recyclerviewTintuc.setAdapter(tintucAdapter);
                            }
                        },
                        throwable -> {
                            Log.d("Loi tin tuc", "" + throwable.getMessage());
                        }
                ));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
