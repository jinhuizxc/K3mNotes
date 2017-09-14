package com.k3mshiro.k3mnotes.customview.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.k3mshiro.k3mnotes.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeSetDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = TimeSetDialog.class.getName();
    private EditText edtSetDate, edtSetTime;
    private Button btnCancel, btnSave;
    private Calendar calendar;
    private int cDate, cMonth, cYear, cHour, cMinute; // current datetime variables
    private int alarmDate, alarmMonth, alarmYear, alarmHour, alarmMinute; // chosen datetime variables from Dialog
    private OnCallBack mTimeListener;
    private long timeInMillis;

    public void setmTimeListener(OnCallBack mTimeListener) {
        this.mTimeListener = mTimeListener;
    }

    public interface OnCallBack {
        void setDateTime(long timeInMillis);
    }

    private void getCurrentDateTime() {
        calendar = Calendar.getInstance();
        cDate = calendar.get(Calendar.DAY_OF_MONTH);
        cMonth = calendar.get(Calendar.MONTH);
        cYear = calendar.get(Calendar.YEAR);
        cHour = calendar.get(Calendar.HOUR_OF_DAY);
        cMinute = calendar.get(Calendar.MINUTE);

        alarmDate = cDate;
        alarmMonth = cMonth;
        alarmYear = cYear;
        alarmHour = cHour;
        alarmMinute = cMinute;
    }

    public TimeSetDialog(@NonNull Context context) {
        super(context);
        getCurrentDateTime();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setContentView(R.layout.time_set_layout);
        initViews();
    }

    private void initViews() {
        edtSetDate = (EditText) findViewById(R.id.edt_setDate);
        edtSetDate.setOnClickListener(this);

        edtSetTime = (EditText) findViewById(R.id.edt_setTime);
        edtSetTime.setOnClickListener(this);

        btnSave = (Button) findViewById(R.id.btn_save_time);
        btnSave.setOnClickListener(this);

        btnCancel = (Button) findViewById(R.id.btn_cancel_time);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_setDate:
                setDate();
                break;

            case R.id.edt_setTime:
                setTime();
                break;

            case R.id.btn_save_time:
                saveTime();
                break;

            case R.id.btn_cancel_time:
                cancelDialog();
                break;
            default:
                break;
        }
    }

    private void setDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                alarmDate = dayOfMonth;
                alarmMonth = month;
                alarmYear = year;
                calendar.set(alarmYear, alarmMonth, alarmDate);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                edtSetDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, cYear, cMonth, cDate);

        datePickerDialog.show();
    }

    private void setTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                alarmHour = hourOfDay;
                alarmMinute = minute;
                calendar.set(alarmYear, alarmMonth, alarmDate, alarmHour, alarmMinute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                edtSetTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, cHour, cMinute, true);

        timePickerDialog.show();
    }

    private void saveTime() {
        calendar.set(Calendar.SECOND, 0);
        timeInMillis = calendar.getTimeInMillis();
        mTimeListener.setDateTime(timeInMillis);
        cancel();
    }

    private void cancelDialog() {
        mTimeListener.setDateTime(-1);
        cancel();
    }
}
