package com.example.techsicmanager.retrofit;
import com.example.techsicmanager.models.DonHangModel;
import com.example.techsicmanager.models.ItemModel;
import com.example.techsicmanager.models.MessageModel;
import com.example.techsicmanager.models.PhanLoaiModel;
import com.example.techsicmanager.models.SanPhamModel;
import com.example.techsicmanager.models.TaiKhoanModel;
import com.example.techsicmanager.models.ThongKeModel;
import com.example.techsicmanager.models.TinTucModel;
import com.example.techsicmanager.models.TinhtrangModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitInterface {
   public static final String BASE_URL="https://techsic.000webhostapp.com/techsic/" ;

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
      @POST("resetmatkhau.php")
      @FormUrlEncoded
      Observable<TaiKhoanModel> setResetMK(
              @Field("email") String email
      );
    @POST("getPhanloai.php")
    @FormUrlEncoded
    Observable<PhanLoaiModel> getPhanloai(
            @Field("idsp") int idsp
    );
    @POST("admin_getSPtheoloai.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getSPtheoloai(
            @Field("idloaisp") int idloaisp,
            @Field("orderby") String orderby
    );

    @POST("admin_xoaSP.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaSP(
            @Field("idsp") int idsp);

    @GET("getsSPmoi.php")
    Observable<SanPhamModel> getSPMoi();

//    @POST("donhang.php")
//    @FormUrlEncoded
//    Observable<TaiKhoanModel> setDonHang(
//            @Field("idtaikhoan") int idtaikhoan,
//            @Field("hoten") String hoten,
//            @Field("sodt") String sodt,
//            @Field("email") String email,
//            @Field("diachi") String diachi,
//            @Field("ghichu") String ghichu,
//            @Field("ngaydat") String ngaydat,
//            @Field("ngaynhan") String ngaynhan,
//            @Field("soluong") int soluong,
//            @Field("tongthanhtoan") String tongthanhtoan,
//            @Field("item") String item
//    );

    @POST("admin_themSP.php")
    @FormUrlEncoded
    Observable<MessageModel> themSanpham(
            @Field("tensp") String tensp,
            @Field("thuonghieu") String thuonghieu,
            @Field("giasp") String giasp,
            @Field("hinhanh") String hinhanh,
            @Field("thongso") String thongso,
            @Field("mota") String mota,
            @Field("idloaisp") int idloaisp,
            @Field("soluongco") String soluongco,
            @Field("phanloai") String phanloai
    );
    @POST("admin_suaSP.php")
    @FormUrlEncoded
    Observable<MessageModel> suaSanpham(
            @Field("idsp") int idsp,
            @Field("tensp") String tensp,
            @Field("thuonghieu") String thuonghieu,
            @Field("giasp") String giasp,
            @Field("hinhanh") String hinhanh,
            @Field("thongso") String thongso,
            @Field("mota") String mota,
            @Field("sltonkho") String sltonkho,
            @Field("phanloai") String phanloai
            );
    @Multipart
    @POST("uploadanh.php")
    Call<MessageModel> uploadAnh(@Part MultipartBody.Part file,
                                 @Part("idloaisp") int idloaisp);

    @Multipart
    @POST("xoaanh.php")
    Call<MessageModel> xoaAnh(@Part MultipartBody.Part file,
                                 @Part("idloaisp") int idloaisp);
    @POST("admin_themtintuc.php")
    @FormUrlEncoded
    Observable<MessageModel> themTintuc(
            @Field("tieude") String tieude,
            @Field("hinhanh") String hinhanh,
            @Field("tacgia") String tacgia,
            @Field("thoigian") String thoigian,
            @Field("noidung") String noidung
    );
   @POST("admin_suatintuc.php")
   @FormUrlEncoded
   Observable<MessageModel> suaTintuc(
           @Field("idtintuc") int idtintuc,
           @Field("tieude") String tieude,
           @Field("hinhanh") String hinhanh,
           @Field("tacgia") String tacgia,
           @Field("thoigian") String thoigian,
           @Field("noidung") String noidung
   );
    @POST("admin_xoaTintuc.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaTintuc(
            @Field("idtintuc") int idtintuc);
    @Multipart
    @POST("uploadanhtintuc.php")
    Call<MessageModel> uploadAnhtintuc(@Part MultipartBody.Part file);

    @Multipart
    @POST("xoaanhtintuc.php")
    Call<MessageModel> xoaAnhtintuc(@Part MultipartBody.Part file,
                                 @Part("idtintuc") int idtintuc);

    @POST("getToken.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> getToken(
            @Field("idtaikhoan") int idtaikhoan,
            @Field("isadmin") int isadmin
    );
    @POST("getTintuc.php")
    @FormUrlEncoded
    Observable<TinTucModel> getTintuc(
            @Field("moinhat") String moinhat
    );
    @POST("admin_getNguoidung.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> getNguoidung(
            @Field("sapxep") String sapxep
    );

    @POST("admin_chanNguoidung.php")
    @FormUrlEncoded
    Observable<MessageModel> chanNguoidung(
            @Field("idtaikhoan") int idtaikhoan,
            @Field("trangthai") int trangthai
    );
    @GET("tinhtrang.php")
    Observable<TinhtrangModel> getTinhtrang();
    @POST("admin_getDonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> getDonhang(
            @Field("gioihan") String gioihan
    );
    @POST("admin_getDonhangTheoNguoidung.php")
    @FormUrlEncoded
    Observable<DonHangModel> getDonhangtheoNguoidung(
            @Field("idtaikhoan") int idtaikhoan
    );
    @POST("admin_getChitietdonhang.php")
    @FormUrlEncoded
    Observable<ItemModel> getChitietdonhang(
            @Field("iddonhang") int iddonhang
    );
    @POST("admin_setDonhang.php")
    @FormUrlEncoded
    Observable<MessageModel> setDonhang(
            @Field("id") int id,
            @Field("kieutinhtrang") String kieutinhtrang,
            @Field("ngaynhan") String ngaynhan
    );

    @POST("getThongke.php")
    @FormUrlEncoded
    Observable<ThongKeModel> getThongke(
            @Field("tungay") String tungay,
            @Field("denngay") String denngay
    );



}