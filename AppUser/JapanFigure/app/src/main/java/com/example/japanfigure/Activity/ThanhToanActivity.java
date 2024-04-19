package com.example.japanfigure.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.japanfigure.R;
import com.example.japanfigure.Retrofit.ApiBanHang;
import com.example.japanfigure.Retrofit.RetrofitClient;
import com.example.japanfigure.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

import com.example.japanfigure.Model.*;
import  com.example.japanfigure.Retrofit.*;

import org.json.JSONObject;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsodt, txtemail;
    EditText edtdiachi;
    AppCompatButton btndathang, btnzalo;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;
    int iddonhang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        //zalo
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
        totalItem = 0 ;
        for(int i = 0 ; i < Utils.mangmuahang.size(); i++){
            totalItem = totalItem+ Utils.mangmuahang.get(i).getSoluong();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien",0);
        txttongtien.setText(decimalFormat.format(tongtien));
        txtemail.setText(Utils.user_current.getEmail());
        txtsodt.setText(Utils.user_current.getMobile());

        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }else {
                    //post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.mangmuahang)); // kiểm tra
                    compositeDisposable.add(apiBanHang.createOder(str_email, str_sdt, String.valueOf(tongtien), id, str_diachi, totalItem, new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        pushNotiToUser();
                                        Toast.makeText(getApplicationContext(), "thanh cong", Toast.LENGTH_SHORT).show();
                                        for(int i = 0 ; i< Utils.mangmuahang.size();i++ ){
                                            GioHang  gioHang = Utils.mangmuahang.get(i);
                                            if(Utils.manggiohang.contains(gioHang)){
                                                Utils.manggiohang.remove(gioHang);
                                            }
                                        }
                                        Utils.mangmuahang.clear();
                                        Paper.book().write("giohang", Utils.manggiohang); // ghi lai hang con trong giỏ khi thoat ap
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
        btnzalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }else {
                    //post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.mangmuahang)); // kiểm tra
                    compositeDisposable.add(apiBanHang.createOder(str_email, str_sdt, String.valueOf(tongtien), id, str_diachi, totalItem, new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        pushNotiToUser();
                                        Toast.makeText(getApplicationContext(), "thanh cong", Toast.LENGTH_SHORT).show();
                                        iddonhang = Integer.parseInt(messageModel.getIddonhang());
                                        requestZalo();
                                        for(int i = 0 ; i< Utils.mangmuahang.size();i++ ){
                                            GioHang  gioHang = Utils.mangmuahang.get(i);
                                            if(Utils.manggiohang.contains(gioHang)){
                                                Utils.manggiohang.remove(gioHang);
                                            }
                                        }
                                        Utils.mangmuahang.clear();
                                        Paper.book().write("giohang", Utils.manggiohang);
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void requestZalo() {
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder(String.valueOf(tongtien));
            String code = data.getString("return_code");
            Log.d("test",code);
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                Log.d("test",code);

                ZaloPaySDK.getInstance().payOrder(ThanhToanActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        compositeDisposable.add(apiBanHang.updateZalopay(iddonhang, token)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        messageModel -> {
                                            if(messageModel.isSuccess()) {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        },
                                        throwable -> {
                                            Log.d("error", throwable.getMessage());
                                        }
                                ));
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {

                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
// gửi thông báo cho user
    private void pushNotiToUser() {
        // get token admin
        compositeDisposable.add(apiBanHang.getToken(1)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                for(int i = 0 ; i< userModel.getResult().size(); i++ ){
                                    Map<String,String> data = new HashMap<>();
                                    data.put("title","Thong bao");
                                    data.put("body",Utils.user_current.getUsername() + "Đã mua hang");
                                    NotiSendData notiSendData = new NotiSendData(userModel.getResult().get(i).getToken(),data);
                                    ApiPushNofication apiPushNofication = RetrofitClientNoti.getInstance().create(ApiPushNofication.class);
                                    compositeDisposable.add(apiPushNofication.sendNofication(notiSendData)
                                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                                    notiResponse -> {
                                                    },
                                                    throwable -> {
                                                        Log.d("log",throwable.getMessage());
                                                    }
                                            ));
                                }
                            }
                        },
                        throwable -> {
                            Log.d("log",throwable.getMessage());
                        }
                ));

    }


    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbar);
        txttongtien = findViewById(R.id.txttongtien);
        txtsodt = findViewById(R.id.txtsodienthoai);
        txtemail = findViewById(R.id.txtemail);
        edtdiachi = findViewById(R.id.edtdiachi);
        btndathang = findViewById(R.id.btndathang);
        btnzalo = findViewById(R.id.btnzalopay);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}