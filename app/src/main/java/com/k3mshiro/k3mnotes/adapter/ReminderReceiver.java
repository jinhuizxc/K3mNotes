package com.k3mshiro.k3mnotes.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {

    private static final String TAG = ReminderReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        //String command = intent.getExtras().getString(BaseEditActivity.REMINDER_TRANSFER_KEY);
        Intent myIntent = new Intent(context, ReminderService.class);
        context.startService(myIntent);
    }
}
