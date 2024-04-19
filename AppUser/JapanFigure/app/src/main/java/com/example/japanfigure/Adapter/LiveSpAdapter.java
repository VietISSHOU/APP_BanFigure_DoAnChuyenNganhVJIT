package com.example.japanfigure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.japanfigure.Activity.ChiTietActivity;
import com.example.japanfigure.Interface.ItemClickListener;
import com.example.japanfigure.Model.SanPhamMoi;
import com.example.japanfigure.R;
import com.example.japanfigure.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class LiveSpAdapter  extends RecyclerView.Adapter<LiveSpAdapter.MyViewHolder>{
    Context context;
    List<SanPhamMoi> array;

    public LiveSpAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public LiveSpAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nendoroid, parent,false);
        return new LiveSpAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveSpAdapter.MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.tensp.setText(sanPhamMoi.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.giasp.setText("Giá:" + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGia() )) + "VNĐ");
        holder.mota.setText(sanPhamMoi.getMota());
//        holder.txtgia.setText(sanPhamMoi.getGia());
        if(sanPhamMoi.getHinhanh().contains("http")){
            Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.hinhanh);

        }else {
            String hinhanh = Utils.BASE_URL + "images/" + sanPhamMoi.getHinhanh();
            Glide.with(context).load(hinhanh).into(holder.hinhanh);

        }
//        Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imghinhanh);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet", sanPhamMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
