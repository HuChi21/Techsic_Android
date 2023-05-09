package com.example.techsic.retrofit;

import com.example.techsic.models.DonHangModel;
import com.example.techsic.models.ItemModel;
import com.example.techsic.models.LoaiSP;
import com.example.techsic.models.LoaiSPModel;
import com.example.techsic.models.MessageModel;
import com.example.techsic.models.PhanLoai;
import com.example.techsic.models.PhanLoaiModel;
import com.example.techsic.models.SanPhamModel;
import com.example.techsic.models.TaiKhoanModel;
import com.example.techsic.models.TinTucModel;
import com.example.techsic.models.eventbus.NhanXetModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitInterface {
//   public static final String BASE_URL="http://192.168.83.198/techsic/" ;
//   public static final String BASE_URL="http://192.168.22.106:8080/techsic/" ;
     public static final String BASE_URL="http://204.0.136.75:8080/techsic/" ;
//   public static final String BASE_URL="https://techsic.000webhostapp.com/techsic/" ;
//   public static final String BASE_URL="http://192.168.22.105:8080/techsic/" ;
//   public static final String BASE_URL="http://192.168.1.37:8080/techsic/" ;

    @GET("getloaiSP.php")
    Observable<LoaiSPModel> getloaiSP();
    @POST("getTintuc.php")
    @FormUrlEncoded
    Observable<TinTucModel> getTintuc(
            @Field("moinhat") String moinhat
    );
    @POST("getSP.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getSP(
            @Field("search") String search
    );

    @GET("getSPmoi.php")
    Observable<SanPhamModel> getSPMoi();

    @POST("locSP.php")
    @FormUrlEncoded
    Observable<SanPhamModel> locSP(
            @Field("idloaisp") int idloaisp,
            @Field("sapxep") String sapxep
    );
    @POST("getSPtheoloai.php")
       @FormUrlEncoded
       Observable<SanPhamModel> getSPtheoloai(
               @Field("page") int page,
               @Field("idloaisp") int idloaisp,
               @Field("sapxep") String sapxep
       );
    @POST("getPhanloai.php")
    @FormUrlEncoded
    Observable<PhanLoaiModel> getPhanloai(
            @Field("idsp") int idsp
    );
    @POST("getNhanxet.php")
    @FormUrlEncoded
    Observable<NhanXetModel> getNhanxet(
            @Field("idsp") int idsp
    );
    @POST("getSPgoiy.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getSPgoiy(
            @Field("idloaisp") int idloaisp
    );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> setDangKy(
            @Field("email") String email,
            @Field("matkhau") String matkhau,
            @Field("hoten") String hoten,
            @Field("hinhanh") String hinhanh,
            @Field("ngaysinh") String ngaysinh,
            @Field("sodt") String sodt,
            @Field("diachi") String diachi,
            @Field("trangthai") int trangthai,
            @Field("isadmin") int isadmin,
            @Field("uid") String uid
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> setDangNhap(
            @Field("email") String email,
            @Field("matkhau") String matkhau
    );
    @POST("updateToken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
         @Field("idtaikhoan") int idtaikhoan,
         @Field("token") String token
    );
    @POST("updateThanhtoan.php")
    @FormUrlEncoded
    Observable<MessageModel> updateThanhtoan(
         @Field("id") int id,
         @Field("idtinhtrang") String idtinhtrang,
         @Field("idthanhtoan") String idthanhtoan
    );
    @POST("getToken.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> getToken(
         @Field("isadmin") int isadmin);

    @POST("getToken.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> getTokenUser(
            @Field("idtaikhoan") int idtaikhoan,
            @Field("isadmin") int isadmin
    );
    @POST("resetmatkhau.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> setResetMK(
            @Field("email") String email
    );

    @POST("donhang.php")
    @FormUrlEncoded
    Observable<MessageModel> setDonHang(
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
            @Field("idtinhtrang") int idtinhtrang,
            @Field("hinhthuc") String hinhthuc,
            @Field("idthanhtoan") String idthanhtoan,
            @Field("item") String item
    );

    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> getDonHang(
            @Field("idtaikhoan") int idtaikhoan
    );
    @POST("admin_getChitietdonhang.php")
    @FormUrlEncoded
    Observable<ItemModel> getChitietdonhang(
            @Field("iddonhang") int iddonhang
    );
    @POST("admin_setTinhtrang.php")
    @FormUrlEncoded
    Observable<MessageModel> setTinhtrang(
            @Field("id") int id,
            @Field("kieutinhtrang") String kieutinhtrang
    );
    @POST("xemdonhangtheoid.php")
    @FormUrlEncoded
    Observable<DonHangModel> getDonhangtheoid(
            @Field("id") int id
    );

    @POST("suathongtin.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> suaThongTin(
            @Field("email") String email,
            @Field("matkhau") String matkhau,
            @Field("hoten") String hoten,
            @Field("hinhanh") String hinhanh,
            @Field("ngaysinh") String ngaysinh,
            @Field("sodt") String sodt,
            @Field("diachi") String diachi
    );
    @Multipart
    @POST("uploadanhkhachhang.php")
    Call<MessageModel> uploadAnh(@Part MultipartBody.Part file,
                                 @Part("idtaikhoan") int idtaikhoan);

    @Multipart
    @POST("xoaanh.php")
    Call<MessageModel> xoaAnh(@Part MultipartBody.Part file,
                              @Part("idloaisp") int idloaisp);

}