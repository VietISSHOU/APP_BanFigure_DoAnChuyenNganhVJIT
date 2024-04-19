package com.example.japanfigure.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.bumptech.glide.Glide;
import com.example.japanfigure.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.example.japanfigure.Adapter.*;
import com.example.japanfigure.Model.*;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import  com.example.japanfigure.Retrofit.*;
import  com.example.japanfigure.utils.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

public class MainActivity extends AppCompatActivity {
    // slide
    private ViewFlipper viewFlipper;
    private Timer mTimer;
    // Home
    Toolbar toolbar;
    RecyclerView recyclerViewHome;
    NavigationView navigationView;
    ListView listView ;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<Loai> listLoaiSp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi>  sanPhamMoiList ;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch,imagemess, livestream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if(Paper.book().read("user")!=null){
            User user = Paper.book().read("user");
            Utils.user_current=user;
        }
        //firebase
        getToken();
        AnhXa();
        ActionBar();
        if(isConnected(this)){
            ActionViewFlipper();
            getLoaiSanPham();
            getSanPhamMoi();
            getEventClick();

        }else {
            Toast.makeText(getApplicationContext(),"no", Toast.LENGTH_LONG).show();
        }
    }
    private void   getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if(!TextUtils.isEmpty(s)){
                    compositeDisposable.add(apiBanHang.updateToken(Utils.user_current.getId(),s)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {

                                    },
                                    throwable -> {
                                        Log.d("log",throwable.getMessage());

                                    }
                            ));
                }
            }
        });
        compositeDisposable.add(apiBanHang.getToken(1)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                Utils.ID_RECEIVED = String.valueOf(userModel.getResult().get(0).getId());
                            }
                        },
                        throwable -> {
                            Log.d("log",throwable.getMessage());

                        }
                ));

    }
    private void getEventClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent Nendoroid = new Intent(getApplicationContext(),NendoroidActivity.class);
                        Nendoroid.putExtra("loai",1);
                        Nendoroid.putExtra("ten", "Nendoroid");
                        startActivity(Nendoroid);
                        break;
                    case 2:
                        Intent PopUpParade = new Intent(getApplicationContext(),NendoroidActivity.class);
                        PopUpParade.putExtra("loai",2);
                        PopUpParade.putExtra("ten", "PopUpParade");
                        startActivity(PopUpParade);
                        break;
                    case 3:
                        Intent Figma = new Intent(getApplicationContext(),NendoroidActivity.class);
                        Figma.putExtra("loai",3);
                        Figma.putExtra("ten", "Figma");
                        startActivity(Figma);
                        break;
                    case 4:
                        Intent lienhe = new Intent(getApplicationContext(),LienHeActivity.class);
                        startActivity(lienhe);
                        break;
                    case 5:
                        Intent thongtin = new Intent(getApplicationContext(),ThongTinActivity.class);
                        startActivity(thongtin);
                        break;
                    case 6:
                        Intent donhang = new Intent(getApplicationContext(),XemDonActivity.class);
                        startActivity(donhang);
                        break;
                    case 7:
                        //xoa key user
                        Paper.book().delete("user");
                        FirebaseAuth.getInstance().signOut();
                        Intent dangnhap = new Intent(getApplicationContext(),DangNhapActivity.class);
                        startActivity(dangnhap);
                        finish();
                        break;

                }
            }
        });
    }

    private void getSanPhamMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                sanPhamMoiList = sanPhamMoiModel.getResult();
                                sanPhamMoiAdapter = new SanPhamMoiAdapter (getApplicationContext(), sanPhamMoiList);
                                recyclerViewHome.setAdapter(sanPhamMoiAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với sever" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()){
                                listLoaiSp = loaiSpModel.getResult();
                                listLoaiSp.add(new Loai("Đăng xuất", ""));
                                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),listLoaiSp);
                                listView.setAdapter(loaiSpAdapter);
                            }
                        }
                ));
    }

    // bar
    private  void AnhXa(){
        imgsearch = findViewById(R.id.imgsearch);
        viewFlipper = findViewById(R.id.view_Flipper);
        recyclerViewHome = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setHasFixedSize(true);
        navigationView = findViewById(R.id.navigationview);
        listView = findViewById(R.id.listview);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerlayout);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        imagemess = findViewById(R.id.image_message);
        livestream = findViewById(R.id.livestream);
        //khoi tao listloai
        listLoaiSp = new ArrayList<>();
        sanPhamMoiList = new ArrayList<>();
        if(Paper.book().read("giohang") != null){
            Utils.manggiohang = Paper.book().read("giohang");
        }
        if(Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();

        }else {
            if(Utils.manggiohang != null){
                int totalItem = 0 ;
                for(int i = 0 ; i < Utils.manggiohang.size(); i++){
                    totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
                }
                badge.setText(String.valueOf(totalItem));
            }
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        imagemess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                startActivity(intent);
            }
        });
        livestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  live  = new Intent(getApplicationContext(), LiveStreamActivity.class);
                startActivity(live);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.manggiohang != null){
            int totalItem = 0 ;
            for(int i = 0 ; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }

    // internet
    private boolean isConnected (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo (ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected()) ) {
            return true;
        }else {
            return false;
        }
    }
    //Bar
    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
    // Slide
    private void ActionViewFlipper(){
        List<String> mListPhoto = new ArrayList<>();
        mListPhoto.add("https://theme.hstatic.net/1000160337/1000885200/14/slide_2_img.jpg?v=311");
        mListPhoto.add("https://theme.hstatic.net/1000160337/1000885200/14/slide_3_mb.jpg?v=311");
        mListPhoto.add("https://theme.hstatic.net/1000160337/1000885200/14/slide_4_mb.jpg?v=311");
        for(int i = 0 ;i< mListPhoto.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mListPhoto.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out);
        viewFlipper.setInAnimation(slide);
        viewFlipper.setOutAnimation(slide_out);
    }


}