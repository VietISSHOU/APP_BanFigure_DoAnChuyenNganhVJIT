package com.example.japanfigure.Retrofit;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import  com.example.japanfigure.Model.*;

public interface ApiBanHang {
    //get data
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();

    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();
    //post data
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );
    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangKy(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("uid") String uid
    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhap(
            @Field("email") String email,
            @Field("pass") String pass
    );
    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel> resetPass(
            @Field("email") String email
    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<MessageModel> createOder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );
    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemDonHang(
            @Field("iduser") int id
    );
    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> search(
            @Field("search") String search
    );
    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("id") int id,
            @Field("token") String token

    );

    @POST("gettoken.php")
    @FormUrlEncoded
    Observable<UserModel> getToken(
            @Field("status") int status
    );

    @POST("updatezalopay.php")
    @FormUrlEncoded
    Observable<MessageModel> updateZalopay(
            @Field("id") int id,
            @Field("token") String token
    );

    @POST("getlive.php")
    @FormUrlEncoded
    Observable<UserModel> getLive(
            @Field("status") int status,
            @Field("live") int live
    );
    @POST("xoadonhang.php")
    @FormUrlEncoded
    Observable<MessageModel> xoadonhang(
            @Field("iddonhang") int id
    );

    @GET("getsplive.php")
    Observable<SanPhamMoiModel> getSpLive();
}
