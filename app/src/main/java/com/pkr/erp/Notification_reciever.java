package com.pkr.erp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.util.Calendar;

/**
 * Created by Prashant on 6/2/2017.
 */

public class Notification_reciever extends BroadcastReceiver {

    int date, month, year, monthBuffer;
    private static final String TAG = "MainActivity";

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DAY_OF_MONTH);
        monthBuffer = calendar.get(Calendar.MONTH);
        month = monthBuffer+1;
        year = calendar.get(Calendar.YEAR);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_activity = new Intent(context, MainActivity.class);
        repeating_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_activity,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
//                .setVibrate(new long[]{100,100,100,100})
                .setContentTitle("Notification title")
                .setContentText("Notification text")
//                .setSound(alarmSound)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(100, builder.build());
    }
}
