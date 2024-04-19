package com.appquanly.japanfigure.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appquanly.japanfigure.Adapter.DonHangAdapter;
import com.appquanly.japanfigure.Model.NotiSendData;
import com.appquanly.japanfigure.R;
import com.appquanly.japanfigure.Retrofit.ApiBanHang;
import com.appquanly.japanfigure.Retrofit.ApiPushNofication;
import com.appquanly.japanfigure.Retrofit.RetrofitClient;
import com.appquanly.japanfigure.Retrofit.RetrofitClientNoti;
import com.appquanly.japanfigure.utils.Utils;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LiveActivity extends AppCompatActivity {
    String userID, name, liveID;
    boolean isHost;
    TextView liveIDText;
    ImageView shareBtn,shop_btn;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    int iduser = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        liveIDText = findViewById(R.id.live_id_textview);
        shareBtn = findViewById(R.id.share_btn);

        userID = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");
        liveID = getIntent().getStringExtra("live_id");
        isHost = getIntent().getBooleanExtra("host", false);

        liveIDText.setText(liveID);

        addFragment();

        shareBtn.setOnClickListener((v)->{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Join my live in FigureLive app\n Live ID: "+liveID);
            startActivity(Intent.createChooser(intent, "Share via"));
        });

        updateUser(1);
        getIdUser();
    }
    void addFragment(){
        ZegoUIKitPrebuiltLiveStreamingConfig config;
        config = ZegoUIKitPrebuiltLiveStreamingConfig.host();
        ZegoUIKitPrebuiltLiveStreamingFragment fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
                AppConstants.appId, AppConstants.appSign, userID, name, liveID, config);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frament_container, fragment)
                .commitNow();
    }
    private void updateUser(int i){
        compositeDisposable.add(apiBanHang.updateUser(Utils.user_current.getId(),i)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {

                        },
                        throwable -> {
                            Log.d("log",throwable.getMessage());
                        }
                ));
    }
    private void getIdUser(){
        compositeDisposable.add(apiBanHang.getIdUser(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                for(int i =0 ; i< userModel.getResult().size();i++){
                                    iduser = (int) userModel.getResult().get(i).getId();
                                    pushNotiToUser();
                                }
                            }
                        },
                        throwable -> {

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
                                    data.put("body","AppFigureLiveStream");
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        updateUser(0);
    }
}