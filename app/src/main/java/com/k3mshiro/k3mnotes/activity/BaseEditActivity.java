package com.k3mshiro.k3mnotes.activity;

import android.app.Fragment;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.customview.SquareButton;
import com.k3mshiro.k3mnotes.customview.SquareImageView;
import com.k3mshiro.k3mnotes.customview.richeditor.RichEditor;
import com.k3mshiro.k3mnotes.dao.NoteDAO;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseEditActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final int RESULT_CODE_SUCCESS = 1000;
    public static final int RESULT_CODE_FAILURE = 1001;

    protected View colorPanel, priorityPanel, editSide, formatBar;
    protected Button btnPrioritySet,
            btnNone, btnLow, btnMedium, btnHigh;
    protected SquareButton btnAlarmSet, btnInfoLook, btnRed, btnOrange, btnYellow,
            btnGreen, btnBlue, btnIndigo, btnViolet;
    protected SquareImageView ivColorSet, ivUndo, ivRedo, ivBold, ivItalic, ivUnderline, ivStrike,
            ivBullets, ivNumbers, ivIndent, ivOutdent, ivCbAdd;
    protected ImageView ivTextFormat;
    protected FloatingActionButton fabEditNote;
    protected EditText edtTitle;
    protected RichEditor redtContent;
    protected Toolbar createToolbar;
    protected CheckBox cbFavorite;

    protected String theme;
    protected NoteDAO noteDAO;
    protected String parseColor = "#4CAF50";
    protected NoteDTO editedNote;
    protected Fragment infoFragment;
    protected int priority;
    protected int favorValue = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        theme = getSharedPreferences(ListNotesActivity.THEME_PREFERENCES, MODE_PRIVATE).getString(ListNotesActivity.THEME_SAVED, ListNotesActivity.LIGHTTHEME);
        if (theme.equals(ListNotesActivity.LIGHTTHEME)) {
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
        initNotes();
    }

    private void initViews() {
        createToolbar = (Toolbar) findViewById(R.id.create_toolbar);
        setSupportActionBar(createToolbar);

        final Drawable check = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check_white_24dp);
        if (check != null) {
            if (theme.equals(ListNotesActivity.DARKTHEME)) {
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
        if (theme.equals(ListNotesActivity.DARKTHEME)) {
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

        fabEditNote = (FloatingActionButton) findViewById(R.id.fab_edit_note);
        fabEditNote.setOnClickListener(this);
    }

    private void initPriorityBar() {
        priorityPanel = editSide.findViewById(R.id.priority_panel);
        btnNone = (Button) priorityPanel.findViewById(R.id.btn_none);
        btnLow = (Button) priorityPanel.findViewById(R.id.btn_low);
        btnMedium = (Button) priorityPanel.findViewById(R.id.btn_medium);
        btnHigh = (Button) priorityPanel.findViewById(R.id.btn_high);

        btnNone.setOnClickListener(this);
        btnLow.setOnClickListener(this);
        btnMedium.setOnClickListener(this);
        btnHigh.setOnClickListener(this);
    }

    private void initColorBar() {
        colorPanel = editSide.findViewById(R.id.color_panel);
        btnRed = (SquareButton) colorPanel.findViewById(R.id.btn_red);
        btnOrange = (SquareButton) colorPanel.findViewById(R.id.btn_orange);
        btnYellow = (SquareButton) colorPanel.findViewById(R.id.btn_yellow);
        btnGreen = (SquareButton) colorPanel.findViewById(R.id.btn_green);
        btnBlue = (SquareButton) colorPanel.findViewById(R.id.btn_blue);
        btnIndigo = (SquareButton) colorPanel.findViewById(R.id.btn_indigo);
        btnViolet = (SquareButton) colorPanel.findViewById(R.id.btn_violet);

        btnRed.setOnClickListener(this);
        btnOrange.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnIndigo.setOnClickListener(this);
        btnViolet.setOnClickListener(this);
    }

    private void initTextFormat() {
        formatBar = editSide.findViewById(R.id.layout_textFormat);

        ivUndo = (SquareImageView) formatBar.findViewById(R.id.iv_setUndo);
        ivRedo = (SquareImageView) formatBar.findViewById(R.id.iv_setRedo);
        ivBold = (SquareImageView) formatBar.findViewById(R.id.iv_setBold);
        ivItalic = (SquareImageView) formatBar.findViewById(R.id.iv_setItalic);
        ivUnderline = (SquareImageView) formatBar.findViewById(R.id.iv_setUnderline);
        ivStrike = (SquareImageView) formatBar.findViewById(R.id.iv_setStrikeThrough);
        ivBullets = (SquareImageView) formatBar.findViewById(R.id.iv_insertBullet);
        ivNumbers = (SquareImageView) formatBar.findViewById(R.id.iv_insertNumber);
        ivIndent = (SquareImageView) formatBar.findViewById(R.id.iv_setIndent);
        ivOutdent = (SquareImageView) formatBar.findViewById(R.id.iv_setOutdent);
        ivCbAdd = (SquareImageView) formatBar.findViewById(R.id.iv_insertCheckbox);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_edit_note:
                redtContent.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(BaseEditActivity.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(redtContent, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.btn_alarm_set:
                break;
            case R.id.iv_color_fill:
                showColorBar();
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
            case R.id.btn_red:
                parseColor = "#F44336";
                ivColorSet.setBackgroundResource(R.drawable.red_circle_bg);
                hideColoBar();
                break;
            case R.id.btn_orange:
                parseColor = "#FB8C00";
                ivColorSet.setBackgroundResource(R.drawable.orange_circle_bg);
                hideColoBar();
                break;
            case R.id.btn_yellow:
                parseColor = "#FFEA00";
                ivColorSet.setBackgroundResource(R.drawable.yellow_circle_bg);
                hideColoBar();
                break;
            case R.id.btn_green:
                parseColor = "#4CAF50";
                ivColorSet.setBackgroundResource(R.drawable.green_circle_bg);
                hideColoBar();
                break;
            case R.id.btn_blue:
                parseColor = "#2196F3";
                ivColorSet.setBackgroundResource(R.drawable.blue_circle_bg);
                hideColoBar();
                break;
            case R.id.btn_indigo:
                parseColor = "#3F51B5";
                ivColorSet.setBackgroundResource(R.drawable.indigo_circle_bg);
                hideColoBar();
                break;
            case R.id.btn_violet:
                parseColor = "#9C27B0";
                ivColorSet.setBackgroundResource(R.drawable.violet_circle_bg);
                hideColoBar();
                break;
            case R.id.btn_none:
                priority = 0;
                btnPrioritySet.setText(getText(R.string.none_priority));
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
                break;
            case R.id.btn_low:
                priority = 1;
                btnPrioritySet.setText(getText(R.string.low_priority));
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
                break;
            case R.id.btn_medium:
                priority = 2;
                btnPrioritySet.setText(getText(R.string.medium_priority));
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
                break;
            case R.id.btn_high:
                priority = 3;
                btnPrioritySet.setText(getText(R.string.high_priority));
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


    private void showTextFormatBar() {
        formatBar.setVisibility(View.VISIBLE);
    }

    private void hideTextFormatBar() {
        formatBar.setVisibility(View.GONE);
    }

    private void showPriorityBar() {
        hideTextFormatBar();
        priorityPanel.setVisibility(View.VISIBLE);
        edtTitle.setEnabled(false);
        redtContent.setEnabled(false);
        btnAlarmSet.setEnabled(false);
        btnInfoLook.setEnabled(false);
    }

    private void hidePriorityBar() {
        btnPrioritySet.setBackgroundColor(Color.TRANSPARENT);
        priorityPanel.setVisibility(View.INVISIBLE);
        edtTitle.setEnabled(true);
        redtContent.setEnabled(true);
        btnAlarmSet.setEnabled(true);
        btnInfoLook.setEnabled(true);
    }

    private void showColorBar() {
        hideTextFormatBar();
        colorPanel.setVisibility(View.VISIBLE);
        edtTitle.setEnabled(false);
        redtContent.setEnabled(false);
        btnAlarmSet.setEnabled(false);
        btnInfoLook.setEnabled(false);
    }

    private void hideColoBar() {
        colorPanel.setVisibility(View.INVISIBLE);
        edtTitle.setTextColor(Color.parseColor(parseColor));
        edtTitle.setEnabled(true);
        redtContent.setEnabled(true);
        btnAlarmSet.setEnabled(true);
        btnInfoLook.setEnabled(true);
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
