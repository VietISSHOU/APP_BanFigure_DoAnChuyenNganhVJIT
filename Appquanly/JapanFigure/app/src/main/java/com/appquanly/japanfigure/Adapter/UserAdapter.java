package com.appquanly.japanfigure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appquanly.japanfigure.Activity.ChatActivity;
import com.appquanly.japanfigure.Interface.ItemClickListener;
import com.appquanly.japanfigure.Model.User;
import com.appquanly.japanfigure.Model.UserMessage;
import com.appquanly.japanfigure.R;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context context;
    List<User> list ;

    public UserAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User userMessage = list.get(position);
        holder.txtUser.setText(userMessage.getUsername());
        holder.txtid.setText(userMessage.getId() + "");
        holder.userIcon.setText(userMessage.getUsername().substring(0, 1).toUpperCase());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent  = new Intent(context, ChatActivity.class);
                    intent.putExtra("id",userMessage.getId());
                    intent.putExtra("ten",userMessage.getUsername());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        TextView txtid,txtUser,userIcon;
        ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtid = itemView.findViewById(R.id.userid);
            txtUser= itemView.findViewById(R.id.username);
            userIcon = itemView.findViewById(R.id.user_icon);
            itemView.setOnClickListener(this);

        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v , getAdapterPosition(),false);

        }
    }
}

