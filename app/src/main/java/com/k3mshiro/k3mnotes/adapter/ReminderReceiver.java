package com.k3mshiro.k3mnotes.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {

    private static final String TAG = ReminderReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt(ReminderAdapter.NOTIFICATION_ID);
        String notificationTitle = intent.getExtras().getString(ReminderAdapter.NOTIFICATION_TITLE);
        String notificationContent = intent.getExtras().getString(ReminderAdapter.NOTIFICATION_CONTENT);
        Intent myIntent = new Intent(context, ReminderService.class);
        myIntent.putExtra(ReminderAdapter.NOTIFICATION_ID, notificationId);
        myIntent.putExtra(ReminderAdapter.NOTIFICATION_TITLE, notificationTitle);
        myIntent.putExtra(ReminderAdapter.NOTIFICATION_CONTENT, notificationContent);
        context.startService(myIntent);
    }
}
