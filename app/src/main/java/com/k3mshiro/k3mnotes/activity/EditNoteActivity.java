package com.k3mshiro.k3mnotes.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.dto.NoteDTO;
import com.k3mshiro.k3mnotes.fragment.DrawerInfoFragment;

public class EditNoteActivity extends BaseEditActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnInfoLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoteInfo();
            }
        });
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        editedNote = (NoteDTO) intent.getSerializableExtra(ListNotesActivity.EDIT_NOTE);
        edtTitle.setText(editedNote.getTitle());
        parseColor = editedNote.getColor();
        edtTitle.setTextColor(Color.parseColor(parseColor));
        redtContent.setHtml(editedNote.getContent());
        priority = editedNote.getPriority();
        favorValue = editedNote.getFavoriteValue();

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

        if (priority == 0) {
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
        } else if (priority == 1) {
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

        } else if (priority == 2) {
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

        } else if (priority == 3) {
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
        }

        if (favorValue == 1) {
            cbFavorite.setChecked(true);
        } else {
            cbFavorite.setChecked(false);
        }
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
                editNote();
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

    private void editNote() {
        String newTitle = edtTitle.getText().toString();
        String newContent = redtContent.getHtml();
        String newColor = parseColor;
        int newPriority = priority;
        int newFavorValue = favorValue;

        if (editedNote.getTitle().compareTo(newTitle) == 0
                && editedNote.getContent().compareTo(newContent) == 0
                && editedNote.getColor().compareTo(newColor) == 0
                && editedNote.getPriority() == newPriority
                && editedNote.getFavoriteValue() == newFavorValue) {
            //back to list;
        } else {
            editedNote.setTitle(newTitle);
            editedNote.setContent(newContent);
            editedNote.setModifiedDate(getDateTime());
            editedNote.setColor(newColor);
            editedNote.setPriority(newPriority);
            editedNote.setFavoriteValue(newFavorValue);
            boolean result = noteDAO.editNote(editedNote);
            if (result) {
                setResult(BaseEditActivity.RESULT_CODE_SUCCESS);
            } else {
                setResult(BaseEditActivity.RESULT_CODE_FAILURE);
            }
        }
    }

    private void showNoteInfo() {
        infoFragment = new DrawerInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putString(DrawerInfoFragment.KEY_MODIFIED_DATE, editedNote.getModifiedDate());
        bundle.putString(DrawerInfoFragment.KEY_CREATED_DATE, editedNote.getDate());
        bundle.putString(DrawerInfoFragment.KEY_COLOR, editedNote.getColor());
        bundle.putInt(DrawerInfoFragment.KEY_PRIORITY, editedNote.getPriority());
        bundle.putString(DrawerInfoFragment.KEY_THEME, theme);
        infoFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.createNote_act, infoFragment, BaseEditActivity.class.getName());
        fragmentTransaction.commit();
    }
}
