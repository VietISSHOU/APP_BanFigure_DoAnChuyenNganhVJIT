package com.example.japanfigure.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.japanfigure.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.SimpleFormatter;

import okhttp3.internal.Util;
import retrofit2.http.Part;
import com.example.japanfigure.utils.*;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.japanfigure.Adapter.*;
 import  com.example.japanfigure.Model.*;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.appcompat.widget.Toolbar;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ImageView imagesend;
    EditText editmess;
    FirebaseFirestore db;
    ChatAdapter adapter;
    List<ChatMessage> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        Actionbar();
        initControl();
        listenmess();
        inserUser();
    }

    private void Actionbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void inserUser() {
        HashMap<String,Object> user  = new HashMap<>();
        user.put("id",Utils.user_current.getId());
        user.put("username",Utils.user_current.getUsername());
        db.collection("user").document(String.valueOf(Utils.user_current.getId())).set(user);

    }

    private void initControl() {
        imagesend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMesToFire();
            }
        });
    }

    private void sendMesToFire() {
        String str_mess= editmess.getText().toString().trim();
        if(TextUtils.isEmpty(str_mess)) {

        }else {
            HashMap<String, Object> message = new HashMap<>();
            message.put(Utils.SENDID,String.valueOf(Utils.user_current.getId()));
            message.put(Utils.RECEIVERID,String.valueOf(Utils.ID_RECEIVED));
            message.put(Utils.MESSAGE,str_mess);
            message.put(Utils.DATETIME, new Date());
            db.collection(Utils.PATH_CHAT).add(message);
            editmess.setText("");

        }
    }
    private  void listenmess(){
        db.collection(Utils.PATH_CHAT)
                .whereEqualTo(Utils.SENDID,String.valueOf(Utils.user_current.getId()))
                .whereEqualTo(Utils.RECEIVERID,Utils.ID_RECEIVED)
                .addSnapshotListener(eventListener);

        db.collection(Utils.PATH_CHAT)
                .whereEqualTo(Utils.SENDID,Utils.ID_RECEIVED)
                .whereEqualTo(Utils.RECEIVERID,String.valueOf(Utils.user_current.getId()))
                .addSnapshotListener(eventListener);
    }
    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
       if(error!= null){
           return;
       }
       if (value != null){
           int count = list.size();
           for (DocumentChange documentChange: value.getDocumentChanges()){
               if(documentChange.getType() == DocumentChange.Type.ADDED){
                   ChatMessage chatMessage = new ChatMessage();
                   chatMessage.idsend = documentChange.getDocument().getString(Utils.SENDID);
                   chatMessage.idreseived = documentChange.getDocument().getString(Utils.RECEIVERID);
                   chatMessage.mess = documentChange.getDocument().getString(Utils.MESSAGE);
                   chatMessage.dateobj = String.valueOf(documentChange.getDocument().getDate(Utils.DATETIME));
                   chatMessage.date = fotmat_date(documentChange.getDocument().getDate(Utils.DATETIME));
                   list.add(chatMessage);
               }
           }
           Collections.sort(list,(obj1,obj2)->obj1.dateobj.compareTo(obj2.dateobj));
           if(count == 0 ){
               adapter.notifyDataSetChanged();
           }else {
               adapter.notifyItemRangeInserted(list.size(),list.size());
               recyclerView.smoothScrollToPosition(list.size() -1 );
           }
       }
    });

    private String fotmat_date(Date date){
        return  new SimpleDateFormat("MMM dd,yyyy- hh:mm a", Locale.getDefault()).format(date);
    }
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        list = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recy_chat);
        imagesend = findViewById(R.id.imagechat);
        editmess = findViewById(R.id.editinput);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ChatAdapter(getApplicationContext(), list,String.valueOf(Utils.user_current.getId()));
        recyclerView.setAdapter(adapter);
    }
}