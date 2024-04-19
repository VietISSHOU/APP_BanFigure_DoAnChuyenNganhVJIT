package com.example.japanfigure.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import  com.example.japanfigure.Model.*;

import java.text.DecimalFormat;
import java.util.List;
import com.example.japanfigure.R;
import com.example.japanfigure.Interface.*;

import org.greenrobot.eventbus.EventBus;
import com.example.japanfigure.Model.EventBus.*;
import com.example.japanfigure.utils.*;

import io.paperdb.Paper;
import okhttp3.internal.Util;

public class GiaHangAdapter extends RecyclerView.Adapter<GiaHangAdapter.MyViewHolder> {
    Context context ;
    List<GioHang> gioHangList;

    public GiaHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(gioHang.getTensp());
        holder.item_giohang_soluong.setText(gioHang.getSoluong() + " ");
        if(gioHang.getHinhsp().contains("http")){
            Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);

        }else {
            String hinhanh = Utils.BASE_URL + "images/" + gioHang.getHinhsp();
            Glide.with(context).load(hinhanh).into(holder.item_giohang_image);

        }
//        Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText("Giá:" + decimalFormat.format((gioHang.getGiasp())));
        long  gia = gioHang.getSoluong() * gioHang.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format((gioHang.getGiasp())) );
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Utils.manggiohang.get(holder.getAdapterPosition()).setCheked(true);
                    if(!Utils.mangmuahang.contains(gioHang)){
                        Utils.mangmuahang.add(gioHang);
                    }

                    EventBus.getDefault().postSticky(new TinhTongTienEvent());
                }else {
                    Utils.manggiohang.get(holder.getAdapterPosition()).setCheked(false);
                    for(int i = 0 ; i< Utils.mangmuahang.size() ; i++){
                        if(Utils.mangmuahang.get(i).getIpsp() == gioHang.getIpsp()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongTienEvent());
                        }
                    }
                }
            }
        });
        holder.checkBox.setChecked(gioHang.isCheked());

        holder.setListeImageClickUpDown(new ImageClick_Up_Down() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if(giatri == 1 ){
                    if(gioHangList.get(pos).getSoluong() > 1){
                        int soluongmoi = gioHangList.get(pos).getSoluong() -1;
                        gioHangList.get(pos).setSoluong(soluongmoi);

                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong() + " ");
                        long  gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongTienEvent());
                    }else if(gioHangList.get(pos).getSoluong() == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo!");
                        builder.setMessage("Bạn có chắc chắn sẽ xóa sản phẩm này ra khỏi giỏ hàng ?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.mangmuahang.remove(gioHang);
                                Utils.manggiohang.remove(pos);
                                Paper.book().write("giohang",Utils.manggiohang);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongTienEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                            builder.show();
                    }
                } else if (giatri == 2) {
                    if(gioHangList.get(pos).getSoluong() < gioHangList.get(pos).getSltonkho()){
                        int soluongmoi = gioHangList.get(pos).getSoluong() +1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong() + " ");
                    long  gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                    holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongTienEvent());
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_image,item_giohang_tru,item_giohang_cong;
        TextView item_giohang_tensp,item_giohang_gia,item_giohang_soluong,item_giohang_giasp2;
        ImageClick_Up_Down listeImageClickUpDown;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
            item_giohang_tru = itemView.findViewById(R.id.item_giohang_tru);
            item_giohang_cong = itemView.findViewById(R.id.item_giohang_cong);
            checkBox = itemView.findViewById(R.id.item_giohang_check);
            // sự kiên tăng giảm item
            item_giohang_cong.setOnClickListener(this);
            item_giohang_tru.setOnClickListener(this);
        }

        public void setListeImageClickUpDown(ImageClick_Up_Down listeImageClickUpDown) {
            this.listeImageClickUpDown = listeImageClickUpDown;
        }


        @Override
        public void onClick(View v) {
            if(v ==item_giohang_tru ){
                listeImageClickUpDown.onImageClick(v,getAdapterPosition(),1);
            }else if (v == item_giohang_cong){
                listeImageClickUpDown.onImageClick(v,getAdapterPosition(),2);

            }
        }
    }
}
