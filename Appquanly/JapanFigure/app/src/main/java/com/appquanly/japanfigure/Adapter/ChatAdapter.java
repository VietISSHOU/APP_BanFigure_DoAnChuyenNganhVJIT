package com.appquanly.japanfigure.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appquanly.japanfigure.Model.ChatMessage;
import com.appquanly.japanfigure.Model.*;
import com.appquanly.japanfigure.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessagesList;
    private String sendid;
    private static final int TYPE_SEND  = 1 ;
    private static final int TYPE_RECEIVED  = 2 ;

    public ChatAdapter(Context context, List<ChatMessage> chatMessagesList, String sendid) {
        this.context = context;
        this.chatMessagesList = chatMessagesList;
        this.sendid = sendid;
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessagesList.get(position).idsend.equals(sendid)){
            return TYPE_SEND;
        }else {
            return TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType ==TYPE_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.item_send_text,parent,false);
            return new SendMessViewHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_receiver,parent,false);
            return new ReceivedViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)== TYPE_SEND){
            ((SendMessViewHolder) holder).txtmess.setText(chatMessagesList.get(position).mess);
            ((SendMessViewHolder) holder).datetime.setText(chatMessagesList.get(position).date);
        }else {
            ((ReceivedViewHolder) holder).txtmessR.setText(chatMessagesList.get(position).mess);
            ((ReceivedViewHolder) holder).datetimeR.setText(chatMessagesList.get(position).date);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessagesList.size();
    }

    class SendMessViewHolder extends RecyclerView.ViewHolder{
        TextView txtmess,datetime;
        public SendMessViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.txtmess);
            datetime = itemView.findViewById(R.id.datetime);
        }
    }
    class ReceivedViewHolder extends RecyclerView.ViewHolder{
        TextView txtmessR,datetimeR;
        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmessR = itemView.findViewById(R.id.txtmess);
            datetimeR = itemView.findViewById(R.id.datetime);
        }
    }

}
