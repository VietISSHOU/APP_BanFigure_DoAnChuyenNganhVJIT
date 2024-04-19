package com.example.japanfigure.Adapter;

import  android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.japanfigure.Model.DonHang;
import com.example.japanfigure.R;

import java.util.List;
import  com.example.japanfigure.Retrofit.*;

import org.greenrobot.eventbus.EventBus;
import  com.example.japanfigure.Model.EventBus.*;
import com.example.japanfigure.Interface.ItemClickListener;
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
        holder.txtdonhang.setText("Đơn hàng: " + donHang.getId());
        holder.tranghaidonhang.setText(trangThaiDonHang(donHang.getTrangthai()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.reChitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItems().size());

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    EventBus.getDefault().postSticky(new DonHangEvent(donHang));
                }
            });


        //adapter chitiet
        ChitietAdapter chitietAdapter = new ChitietAdapter(context, donHang.getItems());
        holder.reChitiet.setLayoutManager(layoutManager);
        holder.reChitiet.setAdapter(chitietAdapter);
        holder.reChitiet.setRecycledViewPool(viewPool);
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView txtdonhang, tranghaidonhang;

        RecyclerView reChitiet;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            reChitiet = itemView.findViewById(R.id.recyclerview_chitiet);
            tranghaidonhang = itemView.findViewById(R.id.trangthaidonhang);
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
