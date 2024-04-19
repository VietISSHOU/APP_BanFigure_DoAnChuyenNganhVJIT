package com.appquanly.japanfigure.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.appquanly.japanfigure.Adapter.NendoroidAdapter;
import com.appquanly.japanfigure.Model.EventBus.SuaXoaEvent;
import com.appquanly.japanfigure.Model.SanPhamMoi;
import com.appquanly.japanfigure.Retrofit.ApiBanHang;
import com.appquanly.japanfigure.Retrofit.RetrofitClient;
import com.appquanly.japanfigure.utils.Utils;
import com.appquanly.japanfigure.R;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.appquanly.japanfigure.Adapter.*;
import com.appquanly.japanfigure.Model.*;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class NendoroidActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    int page = 1 ;
    int loai;
    String ten ;
    NendoroidAdapter nendoroidAdapter;
    List<SanPhamMoi> sanPhamMoiList ;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    SanPhamMoi sanPhamSuaXoa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nendoroid);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai",1);
        ten = getIntent().getStringExtra("ten");
        AnhXa();
        ActionBar();
        getSanPhamMoi(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMoiList.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.add(null);
                nendoroidAdapter.notifyItemInserted(sanPhamMoiList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.remove(sanPhamMoiList.size()-1);
                nendoroidAdapter.notifyItemRemoved(sanPhamMoiList.size());
                page = page+1;
                getSanPhamMoi(page);
                nendoroidAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }

    private void getSanPhamMoi(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page,loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                if(nendoroidAdapter == null){
                                    sanPhamMoiList = sanPhamMoiModel.getResult();
                                    nendoroidAdapter = new NendoroidAdapter(getApplication(),sanPhamMoiList);
                                    recyclerView.setAdapter(nendoroidAdapter);
                                }else {
                                    int vitri = sanPhamMoiList.size()-1;
                                    int soluongadd = sanPhamMoiModel.getResult().size();
                                    for(int i=0; i<soluongadd; i++){
                                        sanPhamMoiList.add(sanPhamMoiModel.getResult().get(i));
                                    }
                                    nendoroidAdapter.notifyItemRangeInserted(vitri, soluongadd);
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "Không còn sản phẩm", Toast.LENGTH_LONG).show();
                                isLoading = true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với sever" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ten);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview);
        linearLayoutManager = new LinearLayoutManager(this, linearLayoutManager.VERTICAL, false);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>( );
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

// sửa xóa sản phẩm
    private void getSanPhamReload(){
        compositeDisposable.add(apiBanHang.getSanPhamReload(loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                    sanPhamMoiList = sanPhamMoiModel.getResult();
                                    nendoroidAdapter = new NendoroidAdapter(getApplication(),sanPhamMoiList);
                                    recyclerView.setAdapter(nendoroidAdapter);
                            }else {
                                Toast.makeText(getApplicationContext(), "Không còn sản phẩm", Toast.LENGTH_LONG).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với sever" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Sửa")){
            suaSanPham();
        }else if(item.getTitle().equals("Xóa")) {
            xoaSanPham();
        }
        return super.onContextItemSelected(item);
    }

    private void xoaSanPham() {
        compositeDisposable.add(apiBanHang.xoasp(sanPhamSuaXoa.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if(messageModel.isSuccess()){
                                Toast.makeText(getApplicationContext(),messageModel.getMessage() ,Toast.LENGTH_SHORT).show();
                                getSanPhamReload();
                            }else {
                                Toast.makeText(getApplicationContext(),messageModel.getMessage() ,Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage() ,Toast.LENGTH_SHORT).show();
                        }
                ));
    }
    private void suaSanPham() {
        Intent intent = new Intent(getApplicationContext(),ThemSPActivity.class);
        intent.putExtra("ten","Sửa Sản Phẩm");
        intent.putExtra("sua",sanPhamSuaXoa);
        startActivity(intent);

    }


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void evenSuaXoa(SuaXoaEvent suaXoaEvent){
        if(suaXoaEvent != null){
            sanPhamSuaXoa = suaXoaEvent.getSanPhamMoi();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        getSanPhamReload();

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}