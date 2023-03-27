package com.example.techsic.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.techsic.R;
import com.example.techsic.adapters.SanphamAdapter;
import com.example.techsic.models.SanPham;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoaiFragment extends Fragment {
    private SanphamAdapter sanphamAdapter;
    private RecyclerView dienThoaiRecyclerView;
    //khai bao list
    private List<SanPham> sanPhamList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apibanhang;
    private GridLayoutManager layoutManager;
    private Handler handler = new Handler();
    private boolean isLoading = false;
    int page = 1;
    int idloaisp =1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        apibanhang = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        View view = inflater.inflate(R.layout.fragment_san_pham, container, false);
        //getWidget
        dienThoaiRecyclerView = (RecyclerView)  view.findViewById(R.id.dienthoaiRecyclerView);
        layoutManager = new GridLayoutManager(getContext(),2);
        dienThoaiRecyclerView.setLayoutManager(layoutManager);

        dienThoaiRecyclerView.setHasFixedSize(true);
        //khoi tao list
        sanPhamList = new ArrayList<>();

        getDienThoai(page);
        actionLoadMore();

        return view;
    }
    private void actionLoadMore() {
        dienThoaiRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(layoutManager.findLastCompletelyVisibleItemPosition()==sanPhamList.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //them phan tu null
                sanPhamList.add(null);
                sanphamAdapter.notifyItemInserted(sanPhamList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //xoa phan tu null
                sanPhamList.remove(sanPhamList.size()-1);
                sanphamAdapter.notifyItemRemoved(sanPhamList.size());
                page += 1;
                getDienThoai(page);
                sanphamAdapter.notifyDataSetChanged();
                isLoading=false;
            }
        },2000);
    }

    private void getDienThoai(int page) {
        compositeDisposable.add(apibanhang.getDienThoai(page,idloaisp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()){
                                if (sanphamAdapter ==null){
                                    sanPhamList = sanPhamModel.getResult();
                                    sanphamAdapter = new SanphamAdapter(getContext(),sanPhamList);
                                    dienThoaiRecyclerView.setAdapter(sanphamAdapter);
                                }
                                else{
                                    int position = sanPhamList.size()-1;
                                    int loadMoreNum = sanPhamModel.getResult().size();
                                    for(int i=0;i<loadMoreNum;i++){
                                        sanPhamList.add(sanPhamModel.getResult().get(i));
                                    }
                                    sanphamAdapter.notifyItemRangeChanged(position,loadMoreNum);
                                }
                            }
                            else{
                                Toast.makeText(getContext(), "Không còn dữ liệu!", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(), "Không kết nối được với server "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}