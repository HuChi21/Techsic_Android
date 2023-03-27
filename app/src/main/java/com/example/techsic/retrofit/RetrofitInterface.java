package com.example.techsic.retrofit;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.example.techsic.models.LoaiSPModel;
import com.example.techsic.models.SanPhamModel;
import com.example.techsic.models.TaiKhoan;
import com.example.techsic.models.TaiKhoanModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {
    //public static final String BASE_URL="http://192.168.83.189/techsic/" ;
    public static final String BASE_URL="http://192.168.22.109:8080/techsic/" ;

    @GET("getloaisp.php")
    Observable<LoaiSPModel> getLoaiSP();

    @GET("getspmoi.php")
    Observable<SanPhamModel> getSPMoi();

    @POST("getsp.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getDienThoai(
            @Field("page") int page,
            @Field("idloaisp") int idloaisp
    );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> setDangKy(
            @Field("email") String email,
            @Field("matkhau") String matkhau,
            @Field("hoten") String hoten,
            @Field("ngaysinh") String ngaysinh,
            @Field("sodt") String sodt,
            @Field("diachi") String diachi,
            @Field("isadmin") int isadmin
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> setDangNhap(
            @Field("email") String email,
            @Field("matkhau") String matkhau
    );

    @POST("resetmatkhau.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> setResetMK(
            @Field("email") String email
    );
}