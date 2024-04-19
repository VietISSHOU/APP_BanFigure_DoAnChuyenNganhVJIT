package com.example.japanfigure.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.japanfigure.Adapter.LoaiSpAdapter;
import com.example.japanfigure.Adapter.SanPhamMoiAdapter;
import com.example.japanfigure.Model.Loai;
import com.example.japanfigure.Model.SanPhamMoi;
import com.example.japanfigure.Retrofit.ApiBanHang;
import com.example.japanfigure.Retrofit.RetrofitClient;
import com.example.japanfigure.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import com.example.japanfigure.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import  com.example.japanfigure.Adapter.*;

public class BottomDialog extends BottomSheetDialogFragment {
    BottomSheetBehavior bottomSheetBehavior ;
    ApiBanHang apiBanHang;
    List<SanPhamMoi> sanPhamMoiList ;
    //SanPhamMoiAdapter sanPhamMoiAdapter;
    LiveSpAdapter sanPhamMoiAdapter;
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        sanPhamMoiList = new ArrayList<>();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(),R.layout.bottom_sheet,null);
        recyclerView = view.findViewById(R.id.recy);
        LinearLayoutManager layoutManager = new  LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        getSanPhamMoi();
        dialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }

    private void getSanPhamMoi() {
        compositeDisposable.add(apiBanHang.getSpLive()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                sanPhamMoiList = sanPhamMoiModel.getResult();
                                sanPhamMoiAdapter = new  LiveSpAdapter(getContext(), sanPhamMoiList);
                                recyclerView.setAdapter(sanPhamMoiAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(), "Không kết nối được với sever" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }
    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
