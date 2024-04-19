package com.example.japanfigure.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Region;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.japanfigure.R;
import androidx.appcompat.widget.Toolbar;
import com.example.japanfigure.Adapter.*;
import com.example.japanfigure.Model.*;
import com.example.japanfigure.utils.* ;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;
import com.example.japanfigure.Model.EventBus.*;
public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong,tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnmuahang;
    GiaHangAdapter gioHangAdapter;
    List<GioHang> gioHangList;
    long tongtiensp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        AnhXa();
        ActionBar();
        if(Utils.mangmuahang != null) {
            Utils.mangmuahang.clear();
        }
        total();
    }

    private void total() {
        tongtiensp = 0;
        for (int i= 0; i<Utils.mangmuahang.size();i++){
            tongtiensp += (Utils.mangmuahang.get(i).getGiasp() *Utils.mangmuahang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText( decimalFormat.format(tongtiensp) + "VNĐ");
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.manggiohang.size() ==0){
            giohangtrong.setVisibility(View.VISIBLE);
        }else {
            gioHangAdapter = new GiaHangAdapter(getApplicationContext(),Utils.manggiohang);
            recyclerView.setAdapter(gioHangAdapter);
        }

        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = false ;
                for(int i = 0 ; i< Utils.manggiohang.size(); i ++ ){
                    if(Utils.manggiohang.get(i).isCheked() == true){
                        checked =  Utils.manggiohang.get(i).isCheked();
                    }
                }

                if(Utils.manggiohang.size() == 0  ){
                    Toast.makeText(getApplicationContext(),"Vui lòng thêm sản phẩm vào giỏ hàng!" ,Toast.LENGTH_SHORT).show();
                }else if(checked == false){
                    Toast.makeText(getApplicationContext(),"Vui lòng chọn sản phẩm cần đặt hàng!" ,Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                    intent.putExtra("tongtien", tongtiensp);
                    //  Utils.manggiohang.clear();
                    startActivity(intent);
                }

            }
        });
    }

    private void AnhXa() {
        giohangtrong = findViewById(R.id.txtgiohangtrong);
        tongtien = findViewById(R.id.txttongtien);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerviewgiohang);
        btnmuahang = findViewById(R.id.btnmuahang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongTienEvent event){
        if(event != null){
            total();
        }
    }
}