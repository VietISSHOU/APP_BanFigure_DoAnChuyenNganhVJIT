package com.appquanly.japanfigure.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;


import com.appquanly.japanfigure.R;
import com.appquanly.japanfigure.Retrofit.ApiBanHang;
import com.appquanly.japanfigure.Retrofit.RetrofitClient;
import com.appquanly.japanfigure.utils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import  com.appquanly.*;
import com.google.firebase.firestore.model.ObjectValue;

public class ThongKeActivity extends AppCompatActivity {
    Toolbar toolbar;
    PieChart pieChart;
    BarChart barChart;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        initControl();
        ActionBar();
        getDataChart();
        settingBarChart();
    }

    private void settingBarChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(12);
        YAxis yAxisr = barChart.getAxisRight();
        yAxisr.setAxisMinimum(0);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menuthongke,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.tkdanhthu:
                getTkThang();
                return true;
            case R.id.tkmathang:
                getDataChart();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void getTkThang() {
        barChart.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.GONE);
        compositeDisposable.add(apiBanHang.getThongKeThang().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        thongKeModel -> {
                            if(thongKeModel.isSuccess()){
                                List<BarEntry> listdata = new ArrayList<>();
                                for(int i = 0 ; i<thongKeModel.getResult().size() ; i++){
                                    String tongtien = thongKeModel.getResult().get(i).getTongtienthang();
                                    String thang  = thongKeModel.getResult().get(i).getThang();
                                    listdata.add(new BarEntry(Integer.parseInt(thang),Float.parseFloat(tongtien)));
                                }
                                BarDataSet barDataSet = new BarDataSet(listdata,"Thống kê.");
                                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                barDataSet.setValueTextSize(14f);
                                barDataSet.setValueTextColor(Color.RED);

                                BarData barData = new BarData(barDataSet);
                                barChart.setData(barData);
                                barChart.invalidate();
                                barChart.animateXY(2000,2000);
                            }
                        },
                        throwable -> {
                            Log.d("logg",throwable.getMessage());
                        }

                ));

    }

    private void getDataChart() {
        barChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.VISIBLE);
        List<PieEntry> listdata = new ArrayList<>();
        compositeDisposable.add(apiBanHang.getThongKe().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                   thongKeModel -> {
                        if(thongKeModel.isSuccess()){
                            for(int i = 0 ; i < thongKeModel.getResult().size();i++){
                                String tensp = thongKeModel.getResult().get(i).getTensp();
                                int tong = thongKeModel.getResult().get(i).getTong();
                                listdata.add(new PieEntry(tong,tensp));
                            }
                            PieDataSet pieDataSet = new PieDataSet(listdata,"Thống kê");
                            PieData pieData = new PieData();
                            pieData.setDataSet(pieDataSet);
                            pieData.setValueTextSize(15f);
                            pieData.setValueFormatter(new PercentFormatter(pieChart));
                            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            pieChart.setData(pieData);
                            pieChart.animateXY(2000,2000);
                            pieChart.setUsePercentValues(true);
                            pieChart.getDescription().setEnabled(false);
                            pieChart.invalidate();
                        }
                   } ,
                   throwable -> {
                       Log.d("logg",throwable.getMessage());
                   }
                ));
    }



    private void initControl() {
        toolbar = findViewById(R.id.toolbar);
        pieChart = findViewById(R.id.piechart);
        barChart = findViewById(R.id.barchart);
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
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}