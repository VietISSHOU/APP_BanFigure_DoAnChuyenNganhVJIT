package com.appquanly.japanfigure.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appquanly.japanfigure.Model.Loai;
import com.bumptech.glide.Glide;
import com.appquanly.japanfigure.Model.*;
import java.util.List;
import com.appquanly.japanfigure.R;
public class LoaiSpAdapter extends BaseAdapter {
    List<Loai> listLoai ;
    Context context;

    public LoaiSpAdapter(Context context,List<Loai> listLoai ) {
        this.listLoai = listLoai;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listLoai.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }


    public class  ViewHolder{
        TextView tenSp;
        ImageView imgSp;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_loaisanpham,null);
            viewHolder.tenSp = convertView.findViewById(R.id.item_tenloai);
            viewHolder.imgSp = convertView.findViewById(R.id.item_image);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.tenSp.setText(listLoai.get(position).getTensanpham());
        Glide.with(context).load(listLoai.get(position).getHinhanh()).into(viewHolder.imgSp);
        return convertView;
    }
}
