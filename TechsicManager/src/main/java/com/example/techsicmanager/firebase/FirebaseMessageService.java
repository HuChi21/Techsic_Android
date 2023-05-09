package com.example.techsicmanager.firebase;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.techsicmanager.R;
import com.example.techsicmanager.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if(message.getNotification() != null){
            showNotification(message.getNotification().getTitle(),message.getNotification().getBody());
        }
        super.onMessageReceived(message);
    }

    @SuppressLint("NotificationPermission")
    private void showNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String channelID = "notification";
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channelID)
                .setSmallIcon(R.drawable.techsiclogo)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000})
                .setOnlyAlertOnce(true);

        builder = builder.setContent(ViewCustom(title,body));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelID,"web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0,builder.build());
    }

    private RemoteViews ViewCustom(String title, String body){
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.push_notification);
        remoteViews.setTextViewText(R.id.txtTitle,title);
        remoteViews.setTextViewText(R.id.txtBody,body);
        remoteViews.setImageViewResource(R.id.imgNotif,R.drawable.techsiclogo);
        return remoteViews;
    }
}
