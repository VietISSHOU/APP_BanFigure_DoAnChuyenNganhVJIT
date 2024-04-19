package com.example.japanfigure.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.japanfigure.Adapter.DonHangAdapter;
import com.example.japanfigure.R;
import com.example.japanfigure.Retrofit.ApiBanHang;
import com.example.japanfigure.Retrofit.RetrofitClient;
import com.example.japanfigure.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LiveStreamActivity extends AppCompatActivity {

    Button goLiveBtn;

    String liveID = "", name, userID;
    SharedPreferences sharedPreferences;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        sharedPreferences = getSharedPreferences("name_pref", MODE_PRIVATE);

        goLiveBtn = findViewById(R.id.go_live_btn);


        goLiveBtn.setOnClickListener((v)->{
            getLive();
            if(liveID != ""){
                startMeeting();
            }



        });
    }
    private void getLive() {
        compositeDisposable.add(apiBanHang.getLive(1,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                for(int i = 0 ; i< userModel.getResult().size() ; i++){
                                    liveID = String.valueOf(userModel.getResult().get(i).getId());
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "Hiện tại chưa có live.", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {

                        }
                ));
    }
    private void startMeeting() {
        //sharedPreferences.edit().putString("name", name).apply();
        Log.i("LOG", "Start meeting");
        //boolean isHost = true;
        //if(liveID.length()==5) isHost = false;
        //else liveID = generateLiveID();
        //userID = UUID.randomUUID().toString();
        userID = String.valueOf(Utils.user_current.getId());
        name = Utils.user_current.getUsername();
        Intent intent = new Intent(LiveStreamActivity.this, LiveActivity.class);
        intent.putExtra("user_id", userID);
        intent.putExtra("name", name);
        intent.putExtra("live_id", liveID);
        //intent.putExtra("host", isHost);
        startActivity(intent);
    }
    String generateLiveID(){
        StringBuilder id = new StringBuilder();
        while (id.length()!=5){
            int random = new Random().nextInt(10);
            id.append(random);
        }
        return id.toString();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}