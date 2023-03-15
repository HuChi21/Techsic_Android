package com.example.techsic.retrofit;

import com.example.techsic.models.LoaiSPModel;
import com.example.techsic.models.SanPhamModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface RetrofitInterface {
    public static final String BASE_URL="http://192.168.22.117:8080/techsic/";
    @GET("getloaisp.php")
    Observable<LoaiSPModel> getLoaiSP();

    @GET("getspmoi.php")
    Observable<SanPhamModel> getSPMoi();
}