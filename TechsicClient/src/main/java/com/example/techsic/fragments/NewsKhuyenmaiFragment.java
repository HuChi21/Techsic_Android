package com.example.techsic.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techsic.R;
import com.example.techsic.adapters.TintucAdapter;
import com.example.techsic.adapters.TintucTheoloaiAdapter;
import com.example.techsic.models.TinTuc;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewsKhuyenmaiFragment extends Fragment {
    private RecyclerView recyclerviewTintuc;
    private TintucTheoloaiAdapter tintucAdapter;
    private List<TinTuc> tinTucList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tintuctheoloai, container, false);

        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);
        getWidget(view);
        String moinhat = "where kieutintuc = \"khuyến mại\" order by idtintuc desc";
        getTintuc(moinhat);
        return view;
    }

    private void getWidget(View view) {

        recyclerviewTintuc = view.findViewById(R.id.recyclerviewTintuc);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerviewTintuc.setLayoutManager(layoutManager);
        recyclerviewTintuc.setHasFixedSize(true);
    }

    private void getTintuc(String moinhat) {
        compositeDisposable.add(apiconfig.getTintuc(moinhat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tinTucModel -> {
                            if(tinTucModel.isSuccess()){
                                tinTucList = tinTucModel.getResult();
                                tintucAdapter = new TintucTheoloaiAdapter(getActivity(), tinTucList);
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
