package com.arielfriedman.arminesweeperproject.specialClasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean notifEnabled = prefs.getBoolean("notifications_enabled", true);
        if (!notifEnabled) return;

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channelReminder";

        NotificationChannel channel =
                new NotificationChannel(channelId, "channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);

        Notification notification =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Reminder!")
                        .setContentText("Don't forget to come back and get a new record!")
                        .build();

        manager.notify(1, notification);
    }
}
