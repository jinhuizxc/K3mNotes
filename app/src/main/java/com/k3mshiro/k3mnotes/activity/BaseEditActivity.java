package com.k3mshiro.k3mnotes.activity;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.adapter.ReminderReceiver;
import com.k3mshiro.k3mnotes.customview.SquareButton;
import com.k3mshiro.k3mnotes.customview.SquareImageView;
import com.k3mshiro.k3mnotes.customview.dialog.ColorSetDialog;
import com.k3mshiro.k3mnotes.customview.dialog.PrioritySetDialog;
import com.k3mshiro.k3mnotes.customview.popupmenu.ReminderPopup;
import com.k3mshiro.k3mnotes.customview.richeditor.RichEditor;
import com.k3mshiro.k3mnotes.dao.NoteDAO;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BaseEditActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, ColorSetDialog.OnCallBack, PrioritySetDialog.OnCallBack, ReminderPopup.OnPopupSendCalendarToActivity {
    public static final int RESULT_CODE_SUCCESS = 1000;
    public static final int RESULT_CODE_FAILURE = 1001;
    public static final int REMINDER_REQUEST_CODE = 200;
    public static final String REMINDER_TRANSFER_KEY = "REMINDER_TRANSFER_KEY";
    public static final String REMINDER_SET = "REMINDER_SET";
    public static final String REMINDER_DONE = "REMINDER_DONE";
    public static final String REMINDER_REMOVE = "REMINDER_REMOVE";

    protected View editSide, formatBar;
    protected Button btnPrioritySet;
    protected SquareButton btnAlarmSet, btnInfoLook;
    protected SquareImageView ivColorSet, ivUndo, ivRedo, ivBold, ivItalic, ivUnderline, ivStrike,
            ivBullets, ivNumbers, ivIndent, ivOutdent, ivCbAdd;
    protected ImageView ivTextFormat;
    protected FloatingActionButton fabEditNote;
    protected EditText edtTitle;
    protected RichEditor redtContent;
    protected Toolbar createToolbar;
    protected CheckBox cbFavorite;
    protected PrioritySetDialog priorityDialog;
    protected ColorSetDialog colorSetDialog;
    protected ReminderPopup reminderPopup;
    protected TextView tvTimeInfo;

    protected String theme;
    protected NoteDAO noteDAO;
    protected String parseColor = "#4CAF50";
    protected NoteDTO editedNote;
    protected Fragment infoFragment;
    protected int priority;
    protected int favorValue = 0;
    protected long timeInMillis = 0;
    protected AlarmManager alarmManager;
    protected Intent intent;
    protected PendingIntent pendingIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        theme = getSharedPreferences(MainActivity.THEME_PREFERENCES, MODE_PRIVATE).getString(MainActivity.THEME_SAVED, MainActivity.LIGHTTHEME);
        if (theme.equals(MainActivity.LIGHTTHEME)) {
            setTheme(R.style.CustomStyle_LightTheme);
        } else {
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        initViews();
        initColorBar();
        initPriorityBar();
        initTextFormat();
        initReminderPopup();
        initNotes();
    }

    protected void initReminder() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        intent = new Intent(getApplicationContext(), ReminderReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REMINDER_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void initViews() {
        createToolbar = (Toolbar) findViewById(R.id.create_toolbar);
        setSupportActionBar(createToolbar);

        final Drawable check = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check_white_24dp);
        if (check != null) {
            if (theme.equals(MainActivity.DARKTHEME)) {
                check.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.cyan_600), PorterDuff.Mode.SRC_ATOP);
            } else {
                check.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            }
        }

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(check);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        cbFavorite = (CheckBox) createToolbar.findViewById(R.id.cbFavorite);
        cbFavorite.setVisibility(View.VISIBLE);
        cbFavorite.setOnCheckedChangeListener(this);

        editSide = findViewById(R.id.edit_side);

        btnAlarmSet = (SquareButton) editSide.findViewById(R.id.btn_alarm_set);
        btnAlarmSet.setOnClickListener(this);

        btnInfoLook = (SquareButton) editSide.findViewById(R.id.btn_info_look);
        btnInfoLook.setOnClickListener(this);

        btnPrioritySet = (Button) editSide.findViewById(R.id.btn_priority_set);
        btnPrioritySet.setOnClickListener(this);

        ivColorSet = (SquareImageView) editSide.findViewById(R.id.iv_color_fill);
        ivColorSet.setBackgroundResource(R.drawable.green_circle_bg);
        ivColorSet.setOnClickListener(this);

        ivTextFormat = (ImageView) createToolbar.findViewById(R.id.iv_textFormat);
        if (theme.equals(MainActivity.DARKTHEME)) {
            ivTextFormat.setBackgroundResource(R.drawable.lv_text_format_dark);
        }
        ivTextFormat.setVisibility(View.VISIBLE);
        ivTextFormat.setOnClickListener(this);

        edtTitle = (EditText) editSide.findViewById(R.id.edt_note_title);
        edtTitle.setTextColor(Color.parseColor(parseColor));

        redtContent = (RichEditor) editSide.findViewById(R.id.rich_editor);
        redtContent.setBackgroundColor(Color.TRANSPARENT);
        redtContent.setEditorHeight(400);
        redtContent.setEditorFontSize(18);
        redtContent.setEditorFontColor(ContextCompat.getColor(getApplicationContext(), R.color.grey600));
        redtContent.setPadding(8, 8, 0, 0);
        redtContent.setPlaceholder("What do you thinking about....");

        tvTimeInfo = (TextView) editSide.findViewById(R.id.tv_timeInfo);

        fabEditNote = (FloatingActionButton) findViewById(R.id.fab_edit_note);
        fabEditNote.setOnClickListener(this);
    }

    private void initColorBar() {
        colorSetDialog = new ColorSetDialog(BaseEditActivity.this);

        WindowManager.LayoutParams windowLayout = colorSetDialog.getWindow()
                .getAttributes();
        windowLayout.verticalMargin = -0.16F;
        colorSetDialog.getWindow().setAttributes(windowLayout);

        colorSetDialog.setmColorListener(this);
    }

    private void initPriorityBar() {
        priorityDialog = new PrioritySetDialog(BaseEditActivity.this);

        WindowManager.LayoutParams windowLayout = priorityDialog.getWindow()
                .getAttributes();
        windowLayout.verticalMargin = -0.17F;
        priorityDialog.getWindow().setAttributes(windowLayout);

        priorityDialog.setmPriorityListener(this);
    }

    private void initTextFormat() {
        formatBar = editSide.findViewById(R.id.layout_textFormat);

        ivUndo = (SquareImageView) editSide.findViewById(R.id.iv_setUndo);
        ivRedo = (SquareImageView) editSide.findViewById(R.id.iv_setRedo);
        ivBold = (SquareImageView) editSide.findViewById(R.id.iv_setBold);
        ivItalic = (SquareImageView) editSide.findViewById(R.id.iv_setItalic);
        ivUnderline = (SquareImageView) editSide.findViewById(R.id.iv_setUnderline);
        ivStrike = (SquareImageView) editSide.findViewById(R.id.iv_setStrikeThrough);
        ivBullets = (SquareImageView) editSide.findViewById(R.id.iv_insertBullet);
        ivNumbers = (SquareImageView) editSide.findViewById(R.id.iv_insertNumber);
        ivIndent = (SquareImageView) editSide.findViewById(R.id.iv_setIndent);
        ivOutdent = (SquareImageView) editSide.findViewById(R.id.iv_setOutdent);
        ivCbAdd = (SquareImageView) editSide.findViewById(R.id.iv_insertCheckbox);

        ivUndo.setOnClickListener(this);
        ivRedo.setOnClickListener(this);
        ivBold.setOnClickListener(this);
        ivItalic.setOnClickListener(this);
        ivUnderline.setOnClickListener(this);
        ivStrike.setOnClickListener(this);
        ivBullets.setOnClickListener(this);
        ivNumbers.setOnClickListener(this);
        ivIndent.setOnClickListener(this);
        ivOutdent.setOnClickListener(this);
        ivCbAdd.setOnClickListener(this);
    }

    private void initNotes() {
        noteDAO = new NoteDAO(BaseEditActivity.this);
        noteDAO.openDatabase();
    }

    private void initReminderPopup() {
        reminderPopup = new ReminderPopup(BaseEditActivity.this, btnAlarmSet);
        reminderPopup.setmReminderListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_edit_note:
                redtContent.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(BaseEditActivity.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(redtContent, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.btn_alarm_set:
                showReminderPopup();
                break;

            case R.id.iv_color_fill:
                showColorSetBar();
                break;

            case R.id.btn_priority_set:
                showPriorityBar();
                break;

            case R.id.iv_textFormat:
                if (formatBar.getVisibility() == View.GONE) {
                    showTextFormatBar();
                    ivTextFormat.setImageLevel(1);
                } else if (formatBar.getVisibility() == View.VISIBLE) {
                    hideTextFormatBar();
                    ivTextFormat.setImageLevel(0);
                }
                break;

            case R.id.iv_setUndo:
                redtContent.undo();
                break;

            case R.id.iv_setRedo:
                redtContent.redo();
                break;

            case R.id.iv_setBold:
                redtContent.setBold();
                break;

            case R.id.iv_setItalic:
                redtContent.setItalic();
                break;

            case R.id.iv_setUnderline:
                redtContent.setUnderline();
                break;

            case R.id.iv_setStrikeThrough:
                redtContent.setStrikeThrough();
                break;

            case R.id.iv_insertBullet:
                redtContent.setBullets();
                break;

            case R.id.iv_insertNumber:
                redtContent.setNumbers();
                break;

            case R.id.iv_setIndent:
                redtContent.setIndent();
                break;

            case R.id.iv_setOutdent:
                redtContent.setOutdent();
                break;

            case R.id.iv_insertCheckbox:
                redtContent.insertTodo();
                break;

            default:
                break;
        }

    }

    private void showReminderPopup() {
        reminderPopup.show();
    }

    private void showColorSetBar() {
        colorSetDialog.show();
    }

    private void showPriorityBar() {
        priorityDialog.show();
    }

    private void showTextFormatBar() {
        formatBar.setVisibility(View.VISIBLE);
    }

    private void hideTextFormatBar() {
        formatBar.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            favorValue = 1;
        } else {
            favorValue = 0;
        }
    }

    @Override
    public void setColor(String parseColor) {
        //get parseColor from ColorSetDialog
        this.parseColor = parseColor;
        edtTitle.setTextColor(Color.parseColor(parseColor));
    }

    @Override
    public void setDrawableResource(int resId) {
        //get resId from ColorSetDialog
        ivColorSet.setBackgroundResource(resId);
    }

    @Override
    public void setPriorityText(int textResId) {
        //get textResId from PrioritySetDialog
        btnPrioritySet.setText(getText(textResId));
    }

    @Override
    public void setPriorityTextColor(String textColor) {
        //get textColor from PrioritySetDialog
        btnPrioritySet.setTextColor(Color.parseColor(textColor));
    }

    @Override
    public void setPriorityValue(int priorityValue) {
        //get priorityValue from PrioritySetDialog
        priority = priorityValue;
    }

    @Override
    public void sendCalendar(long timeInMillis) {
        //get calendar from ReminderPopup
        this.timeInMillis = timeInMillis;
        if (timeInMillis > 0) {
            String timeText = "Time set for " + getCurrentDateTime(timeInMillis);
            tvTimeInfo.setText(timeText);
        } else {
            tvTimeInfo.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    protected String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    protected String getCurrentDateTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return dateFormat.format(calendar.getTime());
    }
}
