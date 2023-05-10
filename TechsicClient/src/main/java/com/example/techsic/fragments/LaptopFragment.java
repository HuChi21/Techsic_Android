package com.example.techsic.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techsic.R;
import com.example.techsic.adapters.PhanLoaiAdapter;
import com.example.techsic.adapters.SanphamAdapter;
import com.example.techsic.adapters.ThuongHieuAdapter;
import com.example.techsic.interfaces.OnItemClickListener;
import com.example.techsic.interfaces.OnItemClickListener2;
import com.example.techsic.models.PhanLoai;
import com.example.techsic.models.SanPham;
import com.example.techsic.retrofit.RetrofitInterface;
import com.example.techsic.retrofit.RetrofitUtilities;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LaptopFragment extends Fragment implements OnItemClickListener, OnItemClickListener2 {
    private CardView layoutLoc,a;
    private Button btnLoc,btnClear;
    private ImageView imgClose;
    private RecyclerView dienThoaiRecyclerView,thuongHieuRecyclerView,phanLoaiRecyclerView;
    private SanphamAdapter sanphamAdapter;
    //khai bao list
    private List<SanPham> sanPhamList,thuonghieuList;
    private List<PhanLoai> phanLoaiList;
    private ThuongHieuAdapter thuongHieuAdapter;
    private PhanLoaiAdapter phanLoaiAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetrofitInterface apiconfig;
    private GridLayoutManager layoutManager;
    private Handler handler = new Handler();
    private boolean isLoading = false;
    int page = 1;
    int idloaisp = 2;
    String sapxep = "RAND()",thuonghieu,phanloai;
    private int count = 0;
    private TextView txtMoinhat, txtBanchay, txtGia, txtLoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        apiconfig = RetrofitUtilities.getRetrofit(RetrofitInterface.BASE_URL).create(RetrofitInterface.class);

        View view = inflater.inflate(R.layout.fragment_sanpham, container, false);
        //getWidget
        txtMoinhat = (TextView) view.findViewById(R.id.txt_moinhat);
        txtBanchay = (TextView) view.findViewById(R.id.txt_banchay);
        txtGia = (TextView) view.findViewById(R.id.txt_gia);
        txtLoc = (TextView) view.findViewById(R.id.txt_loc);
        layoutLoc = view.findViewById(R.id.layoutLoc);
        a = view.findViewById(R.id.a);
        btnLoc = view.findViewById(R.id.btnLoc);
        btnClear = view.findViewById(R.id.btnClear);
        imgClose = view.findViewById(R.id.imgClose);

        thuongHieuRecyclerView = view.findViewById(R.id.recyclerviewThuonghieu);
        phanLoaiRecyclerView = view.findViewById(R.id.recyclerviewPhanLoai);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        thuongHieuRecyclerView.setLayoutManager(layoutManager1);
        thuongHieuRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        phanLoaiRecyclerView.setLayoutManager(layoutManager2);
        phanLoaiRecyclerView.setHasFixedSize(true);

        dienThoaiRecyclerView = (RecyclerView) view.findViewById(R.id.dienthoaiRecyclerView);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        dienThoaiRecyclerView.setLayoutManager(layoutManager);
        dienThoaiRecyclerView.setHasFixedSize(true);

        //khoi tao list
        sanPhamList = new ArrayList<>();

        getLaptop(page, sapxep);
        setSapxep();
        actionLoadMore();
        return view;
    }
    private void getLaptop(int page, String sapxep) {
        compositeDisposable.add(apiconfig.getSPtheoloai(page, idloaisp, sapxep)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.isSuccess()) {
                                if (sanphamAdapter == null) {
                                    sanPhamList = sanPhamModel.getResult();
                                    sanphamAdapter = new SanphamAdapter(getActivity(), sanPhamList);
                                    dienThoaiRecyclerView.setAdapter(sanphamAdapter);
                                } else {
                                    int position = sanPhamList.size() - 1;
                                    int loadMoreNum = sanPhamModel.getResult().size();
                                    for (int i = 0; i < loadMoreNum; i++) {
                                        sanPhamList.add(sanPhamModel.getResult().get(i));
                                    }
                                    sanphamAdapter.notifyItemRangeChanged(position, loadMoreNum);
                                }
                            }
                        },
                        throwable -> {
                            Toast.makeText(getActivity(), "Không kết nối được với server " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
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
                if (isLoading == false) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == sanPhamList.size() - 1) {
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
                sanphamAdapter.notifyItemInserted(sanPhamList.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //xoa phan tu null
                try{
                    if(sanPhamList.size()==0){

                    }else{
                        sanPhamList.remove(sanPhamList.size() - 1);
                        sanphamAdapter.notifyItemRemoved(sanPhamList.size());
                        page += 1;
                        getLaptop(page, sapxep);
                        sanphamAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                }
                catch (NullPointerException e) {
                    Log.d("Sanphamlist.size-1",e.getMessage());
                }
            }}, 2000);
    }
    private void setSapxep() {
        txtMoinhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMoinhat.setTextColor(0xFFFF8800);
                txtBanchay.setTextColor(0xFFD3D3D3);
                txtGia.setTextColor(0xFFD3D3D3);
                txtGia.getCompoundDrawables()[2].setTint(0xFFD3D3D3);
                txtLoc.setTextColor(0xFFD3D3D3);
                txtLoc.getCompoundDrawables()[2].setTint(0xFFD3D3D3);
                sapxep = "idsp desc";
                getDienthoaiSapxep("",sapxep);
            }
        });
        txtBanchay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMoinhat.setTextColor(0xFFD3D3D3);
                txtBanchay.setTextColor(0xFFFF8800);
                txtGia.setTextColor(0xFFD3D3D3);
                txtGia.getCompoundDrawables()[2].setTint(0xFFD3D3D3);
                txtLoc.setTextColor(0xFFD3D3D3);
                txtLoc.getCompoundDrawables()[2].setTint(0xFFD3D3D3);
                sapxep = "SANPHAM_SOLUONG.sldaban DESC";
                getDienthoaiSapxep("",sapxep);
            }
        });
        txtGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                txtMoinhat.setTextColor(0xFFD3D3D3);
                txtBanchay.setTextColor(0xFFD3D3D3);
                txtLoc.setTextColor(0xFFD3D3D3);
                txtLoc.getCompoundDrawables()[2].setTint(0xFFD3D3D3);
                if (count % 2 == 0) {
                    // Số lần bấm là số chẵn
                    Drawable drawable = getResources().getDrawable(R.drawable.baseline_keyboard_arrow_up_24);
                    txtGia.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
                    txtGia.setTextColor(0xFFFF8800);
                    txtGia.getCompoundDrawables()[2].setTint(0xFFFF8800);
                    sapxep = "giasp asc";
                    getDienthoaiSapxep("",sapxep);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.baseline_keyboard_arrow_down_24);
                    txtGia.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
                    txtGia.setTextColor(0xFFFF8800);
                    txtGia.getCompoundDrawables()[2].setTint(0xFFFF8800);
                    sapxep = "giasp desc";
                    getDienthoaiSapxep("",sapxep);
                }
            }
        });
        txtLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLoc.setVisibility(View.VISIBLE);
                a.setVisibility(View.VISIBLE);
                txtMoinhat.setTextColor(0xFFD3D3D3);
                txtBanchay.setTextColor(0xFFD3D3D3);
                txtGia.setTextColor(0xFFD3D3D3);
                txtGia.getCompoundDrawables()[2].setTint(0xFFD3D3D3);
                txtLoc.setTextColor(0xFFFF8800);
                txtLoc.getCompoundDrawables()[2].setTint(0xFFFF8800);
                sapxep = "RAND()";
                locSapxep();
            }
        });

    }
    private void locSapxep() {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutLoc.getVisibility()==View.GONE) {
                    layoutLoc.setVisibility(View.VISIBLE);
                    a.setVisibility(View.VISIBLE);
                }
                else {
                    layoutLoc.setVisibility(View.GONE);
                    a.setVisibility(View.GONE);
                }
            }
        });
        compositeDisposable.add(apiconfig.getThuonghieu(idloaisp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()){
                                thuonghieuList = sanPhamModel.getResult();
                                thuongHieuAdapter = new ThuongHieuAdapter(getActivity(),thuonghieuList);
                                thuongHieuAdapter.setOnItemClickListener(this);
                                thuongHieuAdapter.getDefaultSelectedItem(this);
                                thuongHieuRecyclerView.setAdapter(thuongHieuAdapter);
                            }
                        },
                        throwable -> {
                            Log.e("apiconfig", "Không kết nối được với server", throwable);
                        }
                ));
        compositeDisposable.add(apiconfig.getPhanloai(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        phanLoaiModel -> {
                            phanLoaiList = phanLoaiModel.getResult();
                            phanLoaiAdapter = new PhanLoaiAdapter(getActivity(),phanLoaiList);
                            phanLoaiAdapter.setOnItemClickListener(this);
                            phanLoaiAdapter.getDefaultSelectedItem(this);
                            phanLoaiRecyclerView.setAdapter(phanLoaiAdapter);
                        },
                        throwable -> {
                            Log.e("apiconfig", "Không kết nối được với server", throwable);
                        }
                ));
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locSapxep();
            }
        });
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hehe = "and sanpham_phanloai.phanloai = \""+phanloai+"\""+ "and sanpham.thuonghieu = \""+thuonghieu+"\"";
                getDienthoaiSapxep(hehe,"idsp desc");
            }
        });
    }
    private void getDienthoaiSapxep(String loc,String sapxep) {
        compositeDisposable.add(apiconfig.locSP( idloaisp,loc,sapxep)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.isSuccess()) {
                                sanPhamList = sanPhamModel.getResult();
                                sanPhamList.add(null);
                                sanphamAdapter = new SanphamAdapter(getActivity(), sanPhamList);
                                dienThoaiRecyclerView.setAdapter(sanphamAdapter);
                            } else {
                                sanPhamList.clear();
                                sanphamAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            sanPhamList.clear();
                            sanphamAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Không kết nối được với server " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("loi", "Không kết nối được với server ");
                        }
                ));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


    @Override
    public void onItemClick(String value, int position) {
        phanloai = value;
    }

    @Override
    public void onItemClick2(String value, int position) {
        thuonghieu = value;
    }
}