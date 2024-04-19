package com.appquanly.japanfigure.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.appquanly.japanfigure.Adapter.LoaiSpAdapter;
import com.appquanly.japanfigure.Adapter.SanPhamMoiAdapter;
import com.appquanly.japanfigure.Model.Loai;
import com.appquanly.japanfigure.Model.SanPhamMoi;
import com.appquanly.japanfigure.Model.User;
import com.appquanly.japanfigure.Retrofit.ApiBanHang;
import com.appquanly.japanfigure.Retrofit.RetrofitClient;
import com.appquanly.japanfigure.utils.Utils;
import com.appquanly.japanfigure.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
    ImageView imgsearch;
    ///adim
    CardView them,fig,pop,nen,ctdonhang,chatmessage,thongkesp,livestream;
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
            getLoaiSanPham();
            getEventClick();
            initControl();

        }else {
            Toast.makeText(getApplicationContext(),"no", Toast.LENGTH_LONG).show();
        }
    }

    //firebase token
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
                    compositeDisposable.add(apiBanHang.updateUser(Utils.user_current.getId(),0)
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
            }
        });
    }
    private void getEventClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent thongtin = new Intent(getApplicationContext(),ThongTinActivity.class);
                        startActivity(thongtin);
                        break;
                    case 1:
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



    private void getLoaiSanPham() {

        listLoaiSp.add(new Loai("Thông tin", ""));
        listLoaiSp.add(new Loai("Đăng xuất", ""));
        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),listLoaiSp);
        listView.setAdapter(loaiSpAdapter);
    }

    // bar
    private  void AnhXa(){

        navigationView = findViewById(R.id.navigationview);
        listView = findViewById(R.id.listview);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerlayout);

        //khoi tao listloai
        listLoaiSp = new ArrayList<>();

        them = findViewById(R.id.them_sanpham);
        nen = findViewById(R.id.nen);
        fig = findViewById(R.id.fig);
        pop = findViewById(R.id.pop);
        ctdonhang = findViewById(R.id.chitietdonhang);
        chatmessage = findViewById(R.id.chatmessage);
        thongkesp = findViewById(R.id.thongkesp);
        livestream = findViewById(R.id.livestream);

    }
    private void initControl(){
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent them = new Intent(getApplicationContext(), ThemSPActivity.class);
                them.putExtra("ten","Thêm Sản Phẩm");
                startActivity(them);
            }
        });

        nen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Nendoroid = new Intent(getApplicationContext(),NendoroidActivity.class);
                Nendoroid.putExtra("loai",1);
                Nendoroid.putExtra("ten", "Nendoroid");
                startActivity(Nendoroid);
            }
        });

        fig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent Figma = new Intent(getApplicationContext(),NendoroidActivity.class);
            Figma.putExtra("loai",3);
            Figma.putExtra("ten", "Figma");
            startActivity(Figma);
            }
        });

        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PopUpParade = new Intent(getApplicationContext(),NendoroidActivity.class);
                PopUpParade.putExtra("loai",2);
                PopUpParade.putExtra("ten", "PopUpParade");
                startActivity(PopUpParade);
            }
        });
        ctdonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), XemDonActivity.class);
                startActivity(intent);
            }
        });
        chatmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent  = new Intent(getApplicationContext(),UserChatActivity.class);
                startActivity(intent);
            }
        });
        thongkesp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent  = new Intent(getApplicationContext(),ThongKeActivity.class);
                startActivity(intent);
            }
        });
        livestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent  = new Intent(getApplicationContext(), LiveStreamActivity.class);
                startActivity(intent);
            }
        });



    }
    @Override
    protected void onResume() {
        compositeDisposable.clear();
        super.onResume();

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




}