package com.example.techsic.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.techsic.R;
import com.example.techsic.adapters.SanPhamMoiAdapter;
import com.example.techsic.models.SanPham;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    private ViewFlipper viewFlipper;
    private RecyclerView recyclerViewTrangChu;
    private SanPhamMoiAdapter sanPhamMoiAdapter;
    //khai bao list
    private List<SanPham> sanPhamList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apibanhang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        apibanhang = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //getWidget
        viewFlipper = (ViewFlipper)view.findViewById(R.id.viewFlipper);
        recyclerViewTrangChu = (RecyclerView)  view.findViewById(R.id.homeRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerViewTrangChu.setLayoutManager(layoutManager);
        recyclerViewTrangChu.setHasFixedSize(true);
        //khoi tao list
        sanPhamList = new ArrayList<>();

        getSPMoi();
        actionViewFlipper();

        return view;
    }

    private void getSPMoi() {
        compositeDisposable.add(apibanhang.getSPMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()){
                                sanPhamList = sanPhamModel.getResult();
                                sanPhamMoiAdapter = new SanPhamMoiAdapter(getContext(), sanPhamList);
                                recyclerViewTrangChu.setAdapter(sanPhamMoiAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(), "Không kết nối được với server "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    private void actionViewFlipper() {
        List<String> ArrayQC = new ArrayList<>();
        //Add model tintuc
        ArrayQC.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/720x220-720x220-25.png");
        ArrayQC.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/realme720-220-720x220-3.png");
        ArrayQC.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/s23-720-220-720x220-7.png");
        ArrayQC.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/02/banner/reno8t-720-220-720x220-8.png");
        for(int i=0; i<ArrayQC.size();i++){
            ImageView imageView = new ImageView((getContext()));
            Glide.with(getContext()).load(ArrayQC.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getContext(),R.anim.slide_right_in);
        Animation slide_out  = AnimationUtils.loadAnimation(getContext(),R.anim.slide_right_out);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }
    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}