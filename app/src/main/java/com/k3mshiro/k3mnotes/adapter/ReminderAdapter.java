package com.k3mshiro.k3mnotes.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Html;

public class ReminderAdapter {
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
    public static final String NOTIFICATION_CONTENT = "NOTIFICATION_CONTENT";

    private AlarmManager alarmManager;
    private Intent intent;
    private PendingIntent pendingIntent;
    private Context mContext;
    private int reminderId;
    private String content;
    private long timeInMillis;

    public ReminderAdapter(Context mContext, int reminderId, long timeInMillis,
                           String notificationTitle, String notificationContent) {
        this.mContext = mContext;
        this.reminderId = reminderId;
        this.timeInMillis = timeInMillis;

        alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(mContext, ReminderReceiver.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            content = String.valueOf(Html.fromHtml(notificationContent, Html.FROM_HTML_MODE_LEGACY));
        } else {
            content = String.valueOf(Html.fromHtml(notificationContent));
        }

        intent.putExtra(NOTIFICATION_ID, reminderId);
        intent.putExtra(NOTIFICATION_TITLE, notificationTitle);
        intent.putExtra(NOTIFICATION_CONTENT, content);
        pendingIntent = PendingIntent.getBroadcast(mContext, reminderId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void registerReminder() {
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    public void unregisterReminder() {
        alarmManager.cancel(pendingIntent);
    }
}
