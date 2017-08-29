package com.k3mshiro.k3mnotes.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.dao.NoteDAO;
import com.k3mshiro.k3mnotes.dto.NoteDTO;
import com.k3mshiro.k3mnotes.fragment.DrawerInfoFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseEditActivity extends AppCompatActivity implements View.OnClickListener {
    public static final CharSequence RED = "red";
    public static final CharSequence ORANGE = "orange";
    public static final CharSequence YELLOW = "yellow";
    public static final CharSequence GREEN = "green";
    public static final CharSequence BLUE = "blue";
    public static final CharSequence INDIGO = "indigo";
    public static final CharSequence VIOLET = "violet";
    public static final CharSequence ALARM_SET_BTN = "ALARM_SET_BTN";
    public static final CharSequence INFO_LOOK_BTN = "INFO_LOOK_BTN";
    public static final int RESULT_CODE_SUCCESS = 1000;
    public static final int RESULT_CODE_FAILURE = 1001;
    public static final CharSequence BTN_NONE = "none";
    public static final CharSequence BTN_LOW = "low";
    public static final CharSequence BTN_MEDIUM = "medium";
    public static final CharSequence BTN_HIGH = "high";

    protected View colorPanel, priorityPanel;
    protected Button btnAlarmSet, btnInfoLook, btnRed, btnOrange, btnYellow,
            btnGreen, btnBlue, btnIndigo, btnViolet, btnPrioritySet,
            btnNone, btnLow, btnMedium, btnHigh;
    protected ImageView ivColorSet;
    protected FloatingActionButton fabEditNote;
    protected EditText edtTitle, edtContent;
    protected Toolbar createToolbar;

    protected NoteDAO noteDAO;
    protected String parseColor = "#4CAF50";
    protected NoteDTO editedNote;
    protected Fragment infoFragment;
    protected int priority;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        initViews();
        initNotes();
    }

    private void initViews() {
        createToolbar = (Toolbar) findViewById(R.id.create_toolbar);
        setSupportActionBar(createToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_check_red_800_24dp);

        View editSide = findViewById(R.id.edit_size);

        btnAlarmSet = (Button) editSide.findViewById(R.id.btn_alarm_set);
        btnAlarmSet.setContentDescription(ALARM_SET_BTN);

        btnInfoLook = (Button) editSide.findViewById(R.id.btn_info_look);
        btnAlarmSet.setContentDescription(INFO_LOOK_BTN);

        btnPrioritySet = (Button) editSide.findViewById(R.id.btn_priority_set);

        edtTitle = (EditText) editSide.findViewById(R.id.edt_note_title);
        edtTitle.setTextColor(Color.parseColor(parseColor));
        edtContent = (EditText) editSide.findViewById(R.id.edt_note_content);

        fabEditNote = (FloatingActionButton) findViewById(R.id.fab_edit_note);
        fabEditNote.setOnClickListener(this);

        btnAlarmSet.setOnClickListener(this);
        btnInfoLook.setOnClickListener(this);
        btnPrioritySet.setOnClickListener(this);

        ivColorSet = (ImageView) editSide.findViewById(R.id.iv_color_fill);
        ivColorSet.setBackgroundResource(R.drawable.green_circle_bg);
        ivColorSet.setOnClickListener(this);

        colorPanel = editSide.findViewById(R.id.color_panel);

        btnRed = (Button) colorPanel.findViewById(R.id.btn_red);
        btnRed.setContentDescription(RED);

        btnOrange = (Button) colorPanel.findViewById(R.id.btn_orange);
        btnOrange.setContentDescription(ORANGE);

        btnYellow = (Button) colorPanel.findViewById(R.id.btn_yellow);
        btnYellow.setContentDescription(YELLOW);

        btnGreen = (Button) colorPanel.findViewById(R.id.btn_green);
        btnGreen.setContentDescription(GREEN);

        btnBlue = (Button) colorPanel.findViewById(R.id.btn_blue);
        btnBlue.setContentDescription(BLUE);

        btnIndigo = (Button) colorPanel.findViewById(R.id.btn_indigo);
        btnIndigo.setContentDescription(INDIGO);

        btnViolet = (Button) colorPanel.findViewById(R.id.btn_violet);
        btnViolet.setContentDescription(VIOLET);

        priorityPanel = findViewById(R.id.priority_panel);

        btnNone = (Button) priorityPanel.findViewById(R.id.btn_none);
        btnNone.setContentDescription(BTN_NONE);

        btnLow = (Button) priorityPanel.findViewById(R.id.btn_low);
        btnLow.setContentDescription(BTN_LOW);

        btnMedium = (Button) priorityPanel.findViewById(R.id.btn_medium);
        btnMedium.setContentDescription(BTN_MEDIUM);

        btnHigh = (Button) priorityPanel.findViewById(R.id.btn_high);
        btnHigh.setContentDescription(BTN_HIGH);


        btnRed.setOnClickListener(this);
        btnOrange.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnIndigo.setOnClickListener(this);
        btnViolet.setOnClickListener(this);
        btnNone.setOnClickListener(this);
        btnLow.setOnClickListener(this);
        btnMedium.setOnClickListener(this);
        btnHigh.setOnClickListener(this);

    }

    private void initNotes() {
        noteDAO = new NoteDAO(BaseEditActivity.this);
        noteDAO.openDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_edit_note:
                edtContent.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(BaseEditActivity.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(edtContent, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.btn_alarm_set:
                break;
            case R.id.btn_info_look:
                showNoteInfo();
                break;
            case R.id.iv_color_fill:
                showColorBar();
                break;
            case R.id.btn_priority_set:
                showPriorityBar();
                break;
            default:
                break;
        }

        if (v.getContentDescription() == RED) {
            parseColor = "#F44336";
            ivColorSet.setBackgroundResource(R.drawable.red_circle_bg);
            hideColoBar();
        } else if (v.getContentDescription() == ORANGE) {
            parseColor = "#FB8C00";
            ivColorSet.setBackgroundResource(R.drawable.orange_circle_bg);
            hideColoBar();
        } else if (v.getContentDescription() == YELLOW) {
            parseColor = "#FFEA00";
            ivColorSet.setBackgroundResource(R.drawable.yellow_circle_bg);
            hideColoBar();
        } else if (v.getContentDescription() == GREEN) {
            parseColor = "#4CAF50";
            ivColorSet.setBackgroundResource(R.drawable.green_circle_bg);
            hideColoBar();
        } else if (v.getContentDescription() == BLUE) {
            parseColor = "#2196F3";
            ivColorSet.setBackgroundResource(R.drawable.blue_circle_bg);
            hideColoBar();
        } else if (v.getContentDescription() == INDIGO) {
            parseColor = "#3F51B5";
            ivColorSet.setBackgroundResource(R.drawable.indigo_circle_bg);
            hideColoBar();
        } else if (v.getContentDescription() == VIOLET) {
            parseColor = "#9C27B0";
            ivColorSet.setBackgroundResource(R.drawable.violet_circle_bg);
            hideColoBar();
        } else if (v.getContentDescription() == BTN_NONE) {
            priority = 0;
            btnPrioritySet.setText("None");
            btnPrioritySet.setTextColor(Color.parseColor("#2196F3"));
            btnNone.setBackgroundColor(Color.parseColor("#2196F3"));
            btnNone.setTextColor(Color.WHITE);
            btnLow.setBackgroundColor(Color.WHITE);
            btnLow.setTextColor(Color.parseColor("#2196F3"));
            btnMedium.setBackgroundColor(Color.WHITE);
            btnMedium.setTextColor(Color.parseColor("#2196F3"));
            btnHigh.setBackgroundColor(Color.WHITE);
            btnHigh.setTextColor(Color.parseColor("#2196F3"));
            hidePriorityBar();
        } else if (v.getContentDescription() == BTN_LOW) {
            priority = 1;
            btnPrioritySet.setText("Low");
            btnPrioritySet.setTextColor(Color.parseColor("#4CAF50"));
            btnNone.setBackgroundColor(Color.WHITE);
            btnNone.setTextColor(Color.parseColor("#2196F3"));
            btnLow.setBackgroundColor(Color.parseColor("#2196F3"));
            btnLow.setTextColor(Color.WHITE);
            btnMedium.setBackgroundColor(Color.WHITE);
            btnMedium.setTextColor(Color.parseColor("#2196F3"));
            btnHigh.setBackgroundColor(Color.WHITE);
            btnHigh.setTextColor(Color.parseColor("#2196F3"));
            hidePriorityBar();

        } else if (v.getContentDescription() == BTN_MEDIUM) {
            priority = 2;
            btnPrioritySet.setText("Medium");
            btnPrioritySet.setTextColor(Color.parseColor("#FFEA00"));
            btnNone.setBackgroundColor(Color.WHITE);
            btnNone.setTextColor(Color.parseColor("#2196F3"));
            btnLow.setBackgroundColor(Color.WHITE);
            btnLow.setTextColor(Color.parseColor("#2196F3"));
            btnMedium.setBackgroundColor(Color.parseColor("#2196F3"));
            btnMedium.setTextColor(Color.WHITE);
            btnHigh.setBackgroundColor(Color.WHITE);
            btnHigh.setTextColor(Color.parseColor("#2196F3"));
            hidePriorityBar();

        } else if (v.getContentDescription() == BTN_HIGH) {
            priority = 3;
            btnPrioritySet.setText("High");
            btnPrioritySet.setTextColor(Color.parseColor("#FB8C00"));
            btnNone.setBackgroundColor(Color.WHITE);
            btnNone.setTextColor(Color.parseColor("#2196F3"));
            btnLow.setBackgroundColor(Color.WHITE);
            btnLow.setTextColor(Color.parseColor("#2196F3"));
            btnMedium.setBackgroundColor(Color.WHITE);
            btnMedium.setTextColor(Color.parseColor("#2196F3"));
            btnHigh.setBackgroundColor(Color.parseColor("#2196F3"));
            btnHigh.setTextColor(Color.WHITE);
            hidePriorityBar();
        }
    }

    private void showPriorityBar() {
        priorityPanel.setVisibility(View.VISIBLE);
        edtTitle.setEnabled(false);
        edtContent.setEnabled(false);
        btnAlarmSet.setEnabled(false);
        btnInfoLook.setEnabled(false);
    }

    private void hidePriorityBar() {
        btnPrioritySet.setBackgroundColor(Color.TRANSPARENT);
        priorityPanel.setVisibility(View.INVISIBLE);
        edtTitle.setEnabled(true);
        edtContent.setEnabled(true);
        btnAlarmSet.setEnabled(true);
        btnInfoLook.setEnabled(true);
    }


    private void showNoteInfo() {
        infoFragment = new DrawerInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putString(DrawerInfoFragment.KEY_MODIFIED_DATE, editedNote.getModifiedDate());
        bundle.putString(DrawerInfoFragment.KEY_CREATED_DATE, editedNote.getDate());
        bundle.putString(DrawerInfoFragment.KEY_COLOR, editedNote.getColor());
        bundle.putInt(DrawerInfoFragment.KEY_PRIORITY, editedNote.getPriority());
        infoFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.createNote_act, infoFragment, BaseEditActivity.class.getName());
        fragmentTransaction.commit();
    }

    private void showColorBar() {
        colorPanel.setVisibility(View.VISIBLE);
        edtTitle.setEnabled(false);
        edtContent.setEnabled(false);
        btnAlarmSet.setEnabled(false);
        btnInfoLook.setEnabled(false);
    }

    private void hideColoBar() {
        colorPanel.setVisibility(View.INVISIBLE);
        edtTitle.setTextColor(Color.parseColor(parseColor));
        edtTitle.setEnabled(true);
        edtContent.setEnabled(true);
        btnAlarmSet.setEnabled(true);
        btnInfoLook.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    protected String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
