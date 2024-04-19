package com.example.japanfigure.Service;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.example.japanfigure.R;
import com.example.japanfigure.Activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagerReceiver extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if(message.getNotification() != null){
            showNotification(message.getNotification().getTitle(),message.getNotification().getBody());
        }
        super.onMessageReceived(message);
    }

    private void showNotification(String title, String body) {
        Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
        String chanelId = "noti";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent ,PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder   = new NotificationCompat.Builder(getApplicationContext() , chanelId).setSmallIcon(R.drawable.baseline_notifications_24)
                        .setAutoCancel(true).setVibrate(new long[]{1000,1000,1000,1000}).setOnlyAlertOnce(true).setContentIntent(pendingIntent);
        builder = builder.setContent(customView(title,body));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(chanelId , "AppFigre", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0,builder.build());
    }
    private RemoteViews customView(String title,String body){
        RemoteViews remoteViews = new RemoteViews(getApplication().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title_noti,title);
        remoteViews.setTextViewText(R.id.body_noti,body);
        remoteViews.setImageViewResource(R.id.imgnoti,R.drawable.baseline_notifications_24);
        return  remoteViews;
    }
}
