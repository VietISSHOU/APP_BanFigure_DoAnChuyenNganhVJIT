package com.appquanly.japanfigure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.appquanly.japanfigure.Model.EventBus.SuaXoaEvent;
import com.appquanly.japanfigure.Model.SanPhamMoi;
import com.appquanly.japanfigure.utils.Utils;
import com.bumptech.glide.Glide;
import com.appquanly.japanfigure.Activity.ChiTietActivity;
import com.appquanly.japanfigure.Interface.ItemClickListener;
import  com.appquanly.japanfigure.Model.*;
import com.appquanly.japanfigure.R;

import org.greenrobot.eventbus.EventBus;

public class NendoroidAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> array;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    public NendoroidAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nendoroid, parent, false);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            SanPhamMoi sanPham = array.get(position);
            myViewHolder.tensp.setText(sanPham.getTensp());
            myViewHolder.giasp.setText(sanPham.getGia() + " VNĐ");
            myViewHolder.mota.setText(sanPham.getMota());
            if(sanPham.getHinhanh().contains("http")){
                Glide.with(context).load(sanPham.getHinhanh()).into(myViewHolder.hinhanh);

            }else {
                String hinhanh = Utils.BASE_URL + "images/" + sanPham.getHinhanh();
                Glide.with(context).load(hinhanh).into(myViewHolder.hinhanh);

            }
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("chitiet", sanPham);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else {
                        EventBus.getDefault().postSticky(new SuaXoaEvent(sanPham));
                    }
                }
            });

        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder (@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView tensp,giasp,mota, idsp;
        ImageView hinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.item_ten);
            giasp = itemView.findViewById(R.id.item_gia);
            mota = itemView.findViewById(R.id.item_mota);
            //idsp = itemView.findViewById(R.id.item_idsp);
            hinhanh = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);

        }



        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,0,getAdapterPosition(),"Sửa");
            menu.add(0,0,getAdapterPosition(),"Xóa");
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return false;
        }
    }

}