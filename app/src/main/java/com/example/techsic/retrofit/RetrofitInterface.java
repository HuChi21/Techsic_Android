package com.example.techsic.retrofit;

import com.example.techsic.models.DonHangModel;
import com.example.techsic.models.SanPhamModel;
import com.example.techsic.models.TaiKhoanModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {
//    public static final String BASE_URL="http://192.168.83.187/techsic/" ;
//   public static final String BASE_URL="http://192.168.22.109:8080/techsic/" ;
   public static final String BASE_URL="http://192.168.1.37:8080/techsic/" ;

    @POST("getsp.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getSP(
            @Field("search") String search
    );

    @GET("getspmoi.php")
    Observable<SanPhamModel> getSPMoi();

    @POST("getsptheoloai.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getSPtheoloai(
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

    @POST("donhang.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> setDonHang(
            @Field("idtaikhoan") int idtaikhoan,
            @Field("hoten") String hoten,
            @Field("sodt") String sodt,
            @Field("email") String email,
            @Field("diachi") String diachi,
            @Field("ghichu") String ghichu,
            @Field("ngaydat") String ngaydat,
            @Field("ngaynhan") String ngaynhan,
            @Field("soluong") int soluong,
            @Field("tongthanhtoan") String tongthanhtoan,
            @Field("item") String item
    );

    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> getDonHang(
            @Field("idtaikhoan") int idtaikhoan
    );
}