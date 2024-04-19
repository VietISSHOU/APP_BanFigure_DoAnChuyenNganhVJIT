package com.appquanly.japanfigure.Retrofit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import com.appquanly.japanfigure.Model.DonHangModel;
import com.appquanly.japanfigure.Model.LoaiSpModel;
import com.appquanly.japanfigure.Model.SanPhamMoiModel;
import  com.appquanly.japanfigure.Model.*;

public interface ApiBanHang {
    //get data
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();

    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();
    @GET("thongkesp.php")
    Observable<ThongKeModel> getThongKe();
    @GET("thongkethang.php")
    Observable<ThongKeModel> getThongKeThang();
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
    Observable<UserModel> createOder(
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
    @POST("xoasp.php")
    @FormUrlEncoded
    Observable<MessageModel> xoasp(
            @Field("id") int id
    );
    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> insertSp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("loai") int loai,
            @Field("mota") String mota,
            @Field("slsp") int sl

    );
    @POST("updatesp.php")
    @FormUrlEncoded
    Observable<MessageModel> updateSp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("loai") int loai,
            @Field("mota") String mota,
            @Field("id") int id,
            @Field("slsp") int slsp
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
            @Field("status") int status,
            @Field("iduser") int iduser
    );

    @POST("updatedonhang.php")
    @FormUrlEncoded
    Observable<MessageModel> updatedonhang(
            @Field("id") int id,
            @Field("trangthai") int trangthai

    );
    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);
    // sau xóa hoặc updat
    @POST("sanphamreload.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPhamReload(
            @Field("loai") int loai
    );

    @POST("getiduser.php")
    @FormUrlEncoded
    Observable<UserModel> getIdUser(
            @Field("status") int status

    );
    @POST("updateuser.php")
    @FormUrlEncoded
    Observable<MessageModel> updateUser(
            @Field("id") int id,
            @Field("live") int live

    );

}
