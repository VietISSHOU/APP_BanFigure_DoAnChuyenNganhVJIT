package com.appquanly.japanfigure.Activity;

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

import com.appquanly.japanfigure.Model.GioHang;
import com.appquanly.japanfigure.utils.Utils;
import com.bumptech.glide.Glide;
import com.appquanly.japanfigure.Model.SanPhamMoi;
import com.appquanly.japanfigure.R;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

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
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá:" + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGia() )) + "VNĐ");

    }

    private void initView() {
        tensp = findViewById(R.id.txttensp);
        giasp = findViewById(R.id.txtgiasp);
        mota = findViewById(R.id.txtmotachitiet);
        imghinhanh = findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toolbar);
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
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}