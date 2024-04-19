package com.example.japanfigure.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.japanfigure.R;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment;

public class LiveActivity extends AppCompatActivity {

    String userID, name, liveID;
    boolean isHost;
    TextView liveIDText;
    ImageView shareBtn,shopBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        liveIDText = findViewById(R.id.live_id_textview);
        shareBtn = findViewById(R.id.share_btn);
        shopBtn = findViewById(R.id.shop_btn);

        userID = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");
        liveID = getIntent().getStringExtra("live_id");
        //isHost = getIntent().getBooleanExtra("host", false);

        liveIDText.setText(liveID);

        addFragment();

        shareBtn.setOnClickListener((v)->{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Join my live in FigureLive app\n Live ID: "+liveID);
            startActivity(Intent.createChooser(intent, "Share via"));
        });
        shopBtn.setOnClickListener((v)->{
            BottomDialog dialog = new BottomDialog();
            dialog.show(getSupportFragmentManager(), dialog.getTag());
        });
    }
    void addFragment(){
        ZegoUIKitPrebuiltLiveStreamingConfig config;
        config = ZegoUIKitPrebuiltLiveStreamingConfig.audience();
        ZegoUIKitPrebuiltLiveStreamingFragment fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
                AppConstants.appId, AppConstants.appSign, userID, name, liveID, config);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frament_container, fragment)
                .commitNow();
    }
}