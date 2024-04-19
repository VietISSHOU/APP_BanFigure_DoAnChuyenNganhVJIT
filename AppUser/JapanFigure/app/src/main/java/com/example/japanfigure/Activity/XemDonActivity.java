package com.example.japanfigure.Activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.japanfigure.Adapter.DonHangAdapter;
import com.example.japanfigure.Model.DonHang;
import com.example.japanfigure.Model.NotiSendData;
import com.example.japanfigure.R;
import com.example.japanfigure.Retrofit.ApiBanHang;
import com.example.japanfigure.Retrofit.ApiPushNofication;
import com.example.japanfigure.Retrofit.RetrofitClient;
import com.example.japanfigure.Retrofit.RetrofitClientNoti;
import com.example.japanfigure.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.example.japanfigure.Model.EventBus.*;

import java.util.HashMap;
import java.util.Map;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView redonhang;
    Toolbar toolbar;
    DonHang donHang;
    int iddonhang ;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);
        initView();
        initToolbar();
        getOrder();
    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(), donHangModel.getResult());
                            redonhang.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        },
                        throwable -> {

                        }
                ));
    }
    @Subscribe(sticky = false,threadMode = ThreadMode.MAIN)
    public void evenDonHang(DonHangEvent event){
        if(event!= null){
            donHang = event.getDonHang();
            iddonhang = (int) donHang.getId();
            Log.d("log",iddonhang +"");
            showDeleteOrder();
        }
    }
    private void showDeleteOrder() {
        LayoutInflater inflater = getLayoutInflater();
        View view   = inflater.inflate(R.layout.chitietdonhang,null);
        TextView txtmahoadon,txtsdt,txtdiachi;
        AppCompatButton button = view.findViewById(R.id.dongy_dialog);
        txtmahoadon = view.findViewById(R.id.txtmahoadon);
        txtsdt = view.findViewById(R.id.txtsdt);
        txtdiachi = view.findViewById(R.id.txtdiachi);
        txtmahoadon.setText(String.valueOf(donHang.getId()));
        txtsdt.setText(donHang.getSodienthoai());
        txtdiachi.setText(donHang.getDiachi());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(donHang.getTrangthai() == 0){
                    deleteOrder(iddonhang);
                    pushNotiToUser();

                }else {
                    Toast.makeText(getApplicationContext(),"Đơn hàng không thể xóa!" ,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void deleteOrder(int idDonHang) {
        compositeDisposable.add(apiBanHang.xoadonhang(idDonHang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if(messageModel.isSuccess()){
                                getOrder();
                                dialog.dismiss();
                            }
                        },
                        throwable -> {

                        }
                ));
    }
    private void pushNotiToUser() {
        // get token admin
        compositeDisposable.add(apiBanHang.getToken(1)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                for(int i = 0 ; i< userModel.getResult().size(); i++ ){
                                    Map<String,String> data = new HashMap<>();
                                    data.put("title","Thông Báo");
                                    data.put("body",Utils.user_current.getUsername() + "  Đã hủy đơn hàng :" + iddonhang);
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