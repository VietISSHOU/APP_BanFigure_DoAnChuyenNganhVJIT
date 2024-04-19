package com.appquanly.japanfigure.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appquanly.japanfigure.Adapter.UserAdapter;
import com.appquanly.japanfigure.Model.User;
import com.appquanly.japanfigure.Model.UserMessage;
import com.appquanly.japanfigure.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserChatActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    UserAdapter userAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        initView();
        ActionBar();
        getUserFromFire();
    }

    private void getUserFromFire() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<User> userlist = new ArrayList<>();
                            for (QueryDocumentSnapshot  queryDocumentSnapshot : task.getResult()){
                                User  userMessage = new User();
                                userMessage.setId(queryDocumentSnapshot.getLong("id").intValue());
                                userMessage.setUsername(queryDocumentSnapshot.getString("username"));
                                userlist.add(userMessage);
                            }
                            if(userlist.size() > 0){
                                userAdapter = new UserAdapter(getApplicationContext(),userlist);
                                recyclerView.setAdapter(userAdapter);
                            }
                        }
                    }
                });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recy_user);
        RecyclerView.LayoutManager   layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
    private  void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}