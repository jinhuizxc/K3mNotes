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
import android.widget.Button;
import android.widget.EditText;

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
    private static final CharSequence ALARM_SET_BTN = "ALARM_SET_BTN";
    private static final CharSequence INFO_LOOK_BTN = "INFO_LOOK_BTN";
    public static final int RESULT_CODE_SUCCESS = 1000;
    public static final int RESULT_CODE_FAILURE = 1001;

    private Button btnAlarmSet, btnInfoLook, btnRed, btnOrange, btnYellow, btnGreen, btnBlue, btnIndigo, btnViolet;
    private FloatingActionButton fabEditNote;
    private EditText edtTitle, edtContent;
    private Toolbar createToolbar;

    private NoteDAO noteDAO;
    private String parseColor = "#4CAF50";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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

        View colorSetBar = editSide.findViewById(R.id.color_set_bar);

        btnRed = (Button) colorSetBar.findViewById(R.id.btn_red);
        btnRed.setContentDescription(RED);

        btnOrange = (Button) colorSetBar.findViewById(R.id.btn_orange);
        btnOrange.setContentDescription(ORANGE);

        btnYellow = (Button) colorSetBar.findViewById(R.id.btn_yellow);
        btnYellow.setContentDescription(YELLOW);

        btnGreen = (Button) colorSetBar.findViewById(R.id.btn_green);
        btnGreen.setContentDescription(GREEN);

        btnBlue = (Button) colorSetBar.findViewById(R.id.btn_blue);
        btnBlue.setContentDescription(BLUE);

        btnIndigo = (Button) colorSetBar.findViewById(R.id.btn_indigo);
        btnIndigo.setContentDescription(INDIGO);

        btnViolet = (Button) colorSetBar.findViewById(R.id.btn_violet);
        btnViolet.setContentDescription(VIOLET);

        edtTitle = (EditText) editSide.findViewById(R.id.edt_note_title);
        edtContent = (EditText) editSide.findViewById(R.id.edt_note_content);

        fabEditNote = (FloatingActionButton) findViewById(R.id.fab_edit_note);
        fabEditNote.setVisibility(View.GONE);

        btnAlarmSet.setOnClickListener(this);
        btnInfoLook.setOnClickListener(this);

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
            case R.id.btn_alarm_set:
                break;
            case R.id.btn_info_look:
                break;
            default:
                break;
        }

        if (v.getContentDescription() == RED) {
            parseColor = "#F44336";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == ORANGE) {
            parseColor = "#FB8C00";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == YELLOW) {
            parseColor = "#FFEA00";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == GREEN) {
            parseColor = "#4CAF50";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == BLUE) {
            parseColor = "#2196F3";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == INDIGO) {
            parseColor = "#673AB7";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == VIOLET) {
            parseColor = "#9C27B0";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        }
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

        Intent intent = new Intent(CreateNoteActivity.this, ListNotesActivity.class);

        if (result) {
            setResult(RESULT_CODE_SUCCESS);
        } else {
            setResult(RESULT_CODE_FAILURE);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
