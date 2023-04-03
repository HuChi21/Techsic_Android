package com.example.techsic.retrofit;

import com.example.techsic.models.GioHang;
import com.example.techsic.models.TaiKhoan;

import java.util.ArrayList;
import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtilities {
    private static Retrofit retrofit;
    public static Retrofit getRetrofit(String baseUrl){
        if(retrofit ==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static List<GioHang> giohanglist;
    public static List<GioHang> muahangList = new ArrayList<>();
    public static TaiKhoan taiKhoanGanDay = new TaiKhoan();


}
