package com.example.japanfigure.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.japanfigure.Model.GioHang;
import com.example.japanfigure.Model.SanPhamMoi;
import com.example.japanfigure.R;
import com.example.japanfigure.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ChiTietActivity extends AppCompatActivity {

    TextView tensp, giasp, mota;
    Button btnthem;
    ImageView imghinhanh;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;
    FrameLayout frameLayoutGioHang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
                Paper.book().write("giohang",Utils.manggiohang);
            }
        });
    }

    private void themGioHang() {
        if(Utils.manggiohang.size()>0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i=0; i<Utils.manggiohang.size(); i++){
                if(Utils.manggiohang.get(i).getIpsp() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());

                    flag = true;
                }
            }
            if (flag == false){
                long gia = Long.parseLong(sanPhamMoi.getGia()) ;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                gioHang.setSltonkho(sanPhamMoi.getSltonkho());
                Utils.manggiohang.add(gioHang);
            }
        }else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGia());
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            gioHang.setSltonkho(sanPhamMoi.getSltonkho());
            Utils.manggiohang.add(gioHang);
        }
        int totalItem = 0 ;
        for(int i = 0 ; i < Utils.manggiohang.size(); i++){
            totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        if(sanPhamMoi.getHinhanh().contains("http")){
            Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);

        }else {
            String hinhanh = Utils.BASE_URL + "images/" + sanPhamMoi.getHinhanh();
            Glide.with(getApplicationContext()).load(hinhanh).into(imghinhanh);

        }
      //  Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá:" + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGia() )) + "VNĐ");
//        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        List<Integer> so = new ArrayList<>();
        for(int i=1; i<sanPhamMoi.getSltonkho()+1;i++){
            so.add(i);
        }
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapterspin);
    }

    private void initView() {
        tensp = findViewById(R.id.txttensp);
        giasp = findViewById(R.id.txtgiasp);
        mota = findViewById(R.id.txtmotachitiet);
        btnthem = findViewById(R.id.btnthemvaogiohang);
        spinner = findViewById(R.id.spinner);
        imghinhanh = findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menu_sl);
        frameLayoutGioHang = findViewById(R.id.framegiohang);

        if(Utils.manggiohang != null){
            int totalItem = 0 ;
            for(int i = 0 ; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
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
        frameLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
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
}