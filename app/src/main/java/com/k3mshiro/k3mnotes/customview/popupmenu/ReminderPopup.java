package com.k3mshiro.k3mnotes.customview.popupmenu;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.customview.dialog.TimeSetDialog;

/*
* ReminderPopup is a mediate object which help connect and send calendar(year, month, date, hour, minute)
* from TimeSetDialog to BaseEditActivity
* */

public class ReminderPopup extends PopupMenu implements PopupMenu.OnMenuItemClickListener, TimeSetDialog.OnCallBack, DialogInterface.OnCancelListener {

    private long timeInMillis;
    private OnPopupSendCalendarToActivity mReminderListener;
    private TimeSetDialog timeSetDialog;
    private Context mContext;

    public interface OnPopupSendCalendarToActivity {
        void sendCalendar(long timeInMillis);
    }

    public void setmReminderListener(OnPopupSendCalendarToActivity mReminderListener) {
        this.mReminderListener = mReminderListener;
    }

    public ReminderPopup(@NonNull Context context, @NonNull View anchor) {
        super(context, anchor);
        mContext = context;
        initViews();
    }

    private void initViews() {
        getMenuInflater().inflate(R.menu.menu_reminder, getMenu());
        setOnMenuItemClickListener(this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_add_reminder:
                showTimeSetDialog();
                break;

            case R.id.item_check_done_reminder:
                timeInMillis = 0;
                mReminderListener.sendCalendar(timeInMillis);
                break;

            case R.id.item_remove_reminder:
                timeInMillis = -1;
                mReminderListener.sendCalendar(timeInMillis);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void setDateTime(long timeInMillis) {
        this.timeInMillis = timeInMillis; // get calendar from TimeSetDialog
    }

    private void showTimeSetDialog() {
        timeSetDialog = new TimeSetDialog(mContext);
        timeSetDialog.setmTimeListener(this);
        timeSetDialog.show();

        timeSetDialog.setOnCancelListener(this);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mReminderListener.sendCalendar(timeInMillis);
    }
}
