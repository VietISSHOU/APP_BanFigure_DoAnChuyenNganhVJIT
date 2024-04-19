package com.appquanly.japanfigure.Adapter;

import  android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appquanly.japanfigure.Interface.ItemClickListener;
import com.appquanly.japanfigure.Model.DonHang;
import com.appquanly.japanfigure.Model.EventBus.DonHangEvent;
import com.appquanly.japanfigure.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listdonhang;
    public DonHangAdapter(Context context, List<DonHang> listdonhang) {
        this.context = context;
        this.listdonhang = listdonhang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listdonhang.get(position);
        holder.txtdonhang.setText("Mã đơn hàng: " + donHang.getId());
        holder.usename.setText("Tên người đặt hàng: " + donHang.getUsername());
        holder.trangthai.setText(trangThaiDonHang(donHang.getTrangthai()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.reChitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItems().size());
        //adapter chitiet
        ChitietAdapter chitietAdapter = new ChitietAdapter(context, donHang.getItems());
        holder.reChitiet.setLayoutManager(layoutManager);
        holder.reChitiet.setAdapter(chitietAdapter);
        holder.reChitiet.setRecycledViewPool(viewPool);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(isLongClick){
                    EventBus.getDefault().postSticky(new DonHangEvent(donHang));

                }
            }
        });
    }
    private String trangThaiDonHang(int status){
        String result = "";
        switch (status){
            case 0 :
                result = "ĐƠN HÀNG ĐANG ĐƯỢC XỬ LÝ";
                break;
            case 1 :
                result = "ĐƠN HÀNG ĐÃ XỬ LÝ THÀNH CÔNG";
                break;
            case 2 :
                result = "ĐƠN HÀNG ĐÃ ĐƯỢC GIAO ĐẾN ĐƠN VỊ VẬN CHUYỂN";
                break;
            case 3 :
                result = "THÀNH CÔNG";
                break;
            case 4:
                result = "ĐƠN HÀNG ĐÃ HỦY";
                break;
        }
        return result;
    }

    @Override
    public int getItemCount() {

        return listdonhang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnLongClickListener {
        TextView txtdonhang,usename,trangthai;
        RecyclerView reChitiet;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            usename = itemView.findViewById(R.id.username);
            reChitiet = itemView.findViewById(R.id.recyclerview_chitiet);
            trangthai = itemView.findViewById(R.id.trangthai);
            itemView.setOnLongClickListener(this);
        }


        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }



        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return false;
        }
    }
}
