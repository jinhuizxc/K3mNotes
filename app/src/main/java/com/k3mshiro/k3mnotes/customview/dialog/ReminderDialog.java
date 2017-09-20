package com.k3mshiro.k3mnotes.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.aconstant.ConstantUtil;
import com.k3mshiro.k3mnotes.adapter.ReminderAdapter;
import com.k3mshiro.k3mnotes.dao.NoteDAO;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.util.Random;

public class ReminderDialog extends Dialog implements TimeSetDialog.OnCallBack, View.OnClickListener {

    private EditText edtReminderContent;
    private Button btnSetTime, btnSave, btnCancel;
    private TimeSetDialog timeSetDialog;

    private long timeMillis;
    private NoteDAO noteDAO;
    private int reminderId;
    private String title = "Reminder!";

    public ReminderDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.reminder_layout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initNote();
        initViews();
    }

    private void initNote() {
        noteDAO = new NoteDAO(getContext());
        noteDAO.openDatabase();
    }

    private void initViews() {
        edtReminderContent = (EditText) findViewById(R.id.edt_reminderContent);
        btnSetTime = (Button) findViewById(R.id.btn_setTime);
        btnSave = (Button) findViewById(R.id.btnSaveReminder);
        btnCancel = (Button) findViewById(R.id.btnCancelReminder);
        timeSetDialog = new TimeSetDialog(getContext());
        timeSetDialog.setmTimeListener(this);

        btnSetTime.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        Random rd = new Random();
        reminderId = rd.nextInt();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setTime:
                timeSetDialog.show();
                break;
            case R.id.btnSaveReminder:
                setReminder();
                cancel();
                break;
            case R.id.btnCancelReminder:
                cancel();
                break;
            default:
                break;
        }
    }

    private void setReminder() {
        String date = ConstantUtil.getCurrentDateTime();
        String content = ConstantUtil.convertStringToHtml(edtReminderContent.getText().toString());
        NoteDTO newNote = new NoteDTO(title, date, content, "#4CAF50", 0, date,
                0, timeMillis, reminderId);
        boolean result = noteDAO.createNewNote(newNote);
        if (result) {
            ReminderAdapter reminderAdapter = new ReminderAdapter(getContext(), reminderId,
                    timeMillis, title, content);
            reminderAdapter.registerReminder();
        }
    }

    @Override
    public void setDateTime(long timeInMillis) {
        timeMillis = timeInMillis;
    }
}
