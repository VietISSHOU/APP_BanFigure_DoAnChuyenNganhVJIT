package com.example.japanfigure.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.japanfigure.Adapter.SanPhamMoiAdapter;
import com.example.japanfigure.R;
import com.example.japanfigure.Retrofit.ApiBanHang;
import com.example.japanfigure.Retrofit.RetrofitClient;
import com.example.japanfigure.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.example.japanfigure.Adapter.*;
import com.example.japanfigure.Model.*;

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
}