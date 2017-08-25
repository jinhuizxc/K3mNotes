package com.k3mshiro.k3mnotes.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.dao.NoteDAO;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CreateNoteActivity.class.getName();
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

    private View colorPanel;
    private Button btnAlarmSet, btnInfoLook, btnRed, btnOrange, btnYellow, btnGreen, btnBlue, btnIndigo, btnViolet;
    private ImageView ivColorSet;
    private FloatingActionButton fabEditNote;
    private EditText edtTitle, edtContent;
    private Toolbar createToolbar;

    private NoteDAO noteDAO;
    private String parseColor = "#4CAF50";
    private Intent intent;
    private NoteDTO editedNote;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        initViews();
        initNotes();
    }

    private void getData() {
        editedNote = (NoteDTO) intent.getSerializableExtra(ListNotesActivity.EDIT_NOTE);
        edtTitle.setText(editedNote.getTitle());
        parseColor = editedNote.getColor();
        edtTitle.setTextColor(Color.parseColor(parseColor));
        edtContent.setText(editedNote.getContent());
        if (parseColor.compareTo("#F44336") == 0) {
            ivColorSet.setBackgroundResource(R.drawable.red_circle_bg);
        } else if (parseColor.compareTo("#FB8C00") == 0) {
            ivColorSet.setBackgroundResource(R.drawable.orange_circle_bg);
        } else if (parseColor.compareTo("#FFEA00") == 0) {
            ivColorSet.setBackgroundResource(R.drawable.yellow_circle_bg);
        } else if (parseColor.compareTo("#4CAF50") == 0) {
            ivColorSet.setBackgroundResource(R.drawable.green_circle_bg);
        } else if (parseColor.compareTo("#2196F3") == 0) {
            ivColorSet.setBackgroundResource(R.drawable.blue_circle_bg);
        } else if (parseColor.compareTo("#3F51B5") == 0) {
            ivColorSet.setBackgroundResource(R.drawable.indigo_circle_bg);
        } else {
            ivColorSet.setBackgroundResource(R.drawable.violet_circle_bg);
        }
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

        edtTitle = (EditText) editSide.findViewById(R.id.edt_note_title);
        edtTitle.setTextColor(Color.parseColor(parseColor));
        edtContent = (EditText) editSide.findViewById(R.id.edt_note_content);

        fabEditNote = (FloatingActionButton) findViewById(R.id.fab_edit_note);
        fabEditNote.setOnClickListener(this);

        btnAlarmSet.setOnClickListener(this);
        btnInfoLook.setOnClickListener(this);

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

        btnRed.setOnClickListener(this);
        btnOrange.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnIndigo.setOnClickListener(this);
        btnViolet.setOnClickListener(this);

    }

    private void initNotes() {
        noteDAO = new NoteDAO(CreateNoteActivity.this);
        noteDAO.openDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                createNewNote();
                finish();
                break;
            case R.id.item_delete:
                break;
            case R.id.item_statistics:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_edit_note:
                edtContent.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(CreateNoteActivity.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(edtContent, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.btn_alarm_set:
                break;
            case R.id.btn_info_look:
                break;
            case R.id.iv_color_fill:
                showColorBar();
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
        }
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


    private void createNewNote() {
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(c.getTime()).toString();
        String color = parseColor;

        NoteDTO newNote = new NoteDTO(title, date, content, color);
        boolean result = noteDAO.createNewNote(newNote);

        if (result) {
            setResult(RESULT_CODE_SUCCESS);
        } else {
            setResult(RESULT_CODE_FAILURE);
        }
    }

    private void editNote() {
        editedNote.setTitle(edtTitle.getText().toString());
        editedNote.setContent(edtContent.getText().toString());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(c.getTime()).toString();
        editedNote.setDate(date);
        editedNote.setColor(parseColor);

        boolean result = noteDAO.editNote(editedNote);
        if (result) {
            setResult(RESULT_CODE_SUCCESS);
        } else {
            setResult(RESULT_CODE_FAILURE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
