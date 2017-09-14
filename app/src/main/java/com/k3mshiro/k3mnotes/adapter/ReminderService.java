package com.k3mshiro.k3mnotes.adapter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.activity.MainActivity;

public class ReminderService extends Service {

    private static final int SERVICE_REQUST_CODE = 10;
    private static final int NOTIFICATION_ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intentStartActivity = new Intent(this, MainActivity.class);
        intentStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, SERVICE_REQUST_CODE, intentStartActivity,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.do_note_icon)
                .setContentTitle("Title")
                .setContentText("Content")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager mNotificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationMgr.notify(NOTIFICATION_ID, notificationBuilder.build());
        return START_NOT_STICKY;
    }
}
