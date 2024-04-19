package com.appquanly.japanfigure.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.appquanly.japanfigure.Adapter.DonHangAdapter;
import com.appquanly.japanfigure.Model.DonHang;
import com.appquanly.japanfigure.Model.EventBus.DonHangEvent;
import com.appquanly.japanfigure.Model.NotiSendData;
import com.appquanly.japanfigure.Retrofit.ApiBanHang;
import com.appquanly.japanfigure.Retrofit.ApiPushNofication;
import com.appquanly.japanfigure.Retrofit.RetrofitClient;
import com.appquanly.japanfigure.Retrofit.RetrofitClientNoti;
import com.appquanly.japanfigure.utils.Utils;
import com.appquanly.japanfigure.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView redonhang;
    Toolbar toolbar;
    DonHang donHang;
    int trangthai;
    AlertDialog dialog;
    int iduser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);
        initView();
        initToolbar();
        getOrder();
    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(), donHangModel.getResult());
                            redonhang.setAdapter(adapter);
                        },
                        throwable -> {

                        }
                ));
    }
    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        redonhang = findViewById(R.id.recyclerview_donhang);
        toolbar = findViewById(R.id.toolbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        redonhang.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Subscribe(sticky = false,threadMode = ThreadMode.MAIN)
    public void evenDonHang(DonHangEvent event){
        if(event!= null){
            donHang = event.getDonHang();
            iduser = (int) donHang.getIduser();
            Log.d("log",iduser +"");
            showChiTietDonHang();
        }
    }

    private void showChiTietDonHang() {
        LayoutInflater inflater = getLayoutInflater();
        View view   = inflater.inflate(R.layout.chitietdonhang,null);
        TextView txtmahoadon,txttenuser,txtsdt,txtdiachi;
        Spinner spinner = view.findViewById(R.id.spinner_dialog);
        AppCompatButton button = view.findViewById(R.id.dongy_dialog);
         txtmahoadon = view.findViewById(R.id.txtmahoadon);
         txttenuser = view.findViewById(R.id.txttenuser);
         txtsdt = view.findViewById(R.id.txtsdt);
         txtdiachi = view.findViewById(R.id.txtdiachi);
         txtmahoadon.setText(String.valueOf(donHang.getId()));
         txttenuser.setText(donHang.getUsername());
         txtsdt.setText(donHang.getSodienthoai());
         txtdiachi.setText(donHang.getDiachi());
        List<String> list = new ArrayList<>();
        list.add("ĐƠN HÀNG ĐANG ĐƯỢC XỬ LÝ");
        list.add("ĐƠN HÀNG ĐÃ XỬ LÝ THÀNH CÔNG");
        list.add("ĐƠN HÀNG ĐÃ ĐƯỢC GIAO ĐẾN ĐƠN VỊ VẬN CHUYỂN");
        list.add("THÀNH CÔNG");
        list.add("ĐƠN HÀNG ĐÃ HỦY");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,list);
        spinner.setAdapter(adapter);
        spinner.setSelection(donHang.getTrangthai());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trangthai = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDonHang();
            }
        });
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void updateDonHang() {
        compositeDisposable.add(apiBanHang.updatedonhang(donHang.getId(),trangthai)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                                getOrder();
                                dialog.dismiss();
                                pushNotiToUser();
                        },
                        throwable -> {
                            Log.d("log",throwable.getMessage());

                        }
                ));
    }
    private void pushNotiToUser() {
        compositeDisposable.add(apiBanHang.getToken(0,iduser)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                for(int i = 0 ; i< userModel.getResult().size(); i++ ){
                                    Map<String,String> data = new HashMap<>();
                                    data.put("title","Thông báo");
                                    data.put("body","Mã đơn:" + donHang.getId() + " "+trangThaiDonHang(trangthai));
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
    private String trangThaiDonHang(int status){
        String result = "";
        switch (status){
            case 0 :
                result = "ĐƠN HÀNG ĐANG ĐƯỢC XỬ LÝ";
                break;
            case 1 :
                result = "ĐƠN HÀNG ĐÃ XỬ LÝ THÀNH CÔNG";
                break;
            case 2 :
                result = "ĐƠN HÀNG ĐÃ ĐƯỢC GIAO ĐẾN ĐƠN VỊ VẬN CHUYỂN";
                break;
            case 3 :
                result = "THÀNH CÔNG";
                break;
            case 4:
                result = "ĐƠN HÀNG ĐÃ HỦY";
                break;
        }
        return result;
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }
}