package com.k3mshiro.k3mnotes.adapter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.activity.MainActivity;

public class ReminderService extends Service {

    private static final int SERVICE_REQUST_CODE = 10;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int notificationId = intent.getExtras().getInt(ReminderAdapter.NOTIFICATION_ID);
        String notificationTitle = intent.getExtras().getString(ReminderAdapter.NOTIFICATION_TITLE);
        String notificationContent = intent.getExtras().getString(ReminderAdapter.NOTIFICATION_CONTENT);

        Intent intentStartActivity = new Intent(this, MainActivity.class);
        intentStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, SERVICE_REQUST_CODE,
                intentStartActivity, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.do_note_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.do_note_icon))
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationMgr.notify(notificationId, notificationBuilder.build());

        return START_NOT_STICKY;
    }
}
