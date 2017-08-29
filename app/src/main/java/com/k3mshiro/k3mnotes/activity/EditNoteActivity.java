package com.k3mshiro.k3mnotes.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

public class EditNoteActivity extends BaseEditActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        editedNote = (NoteDTO) intent.getSerializableExtra(ListNotesActivity.EDIT_NOTE);
        edtTitle.setText(editedNote.getTitle());
        parseColor = editedNote.getColor();
        edtTitle.setTextColor(Color.parseColor(parseColor));
        edtContent.setText(editedNote.getContent());
        priority = editedNote.getPriority();

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
        } else if (priority == 1) {
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

        } else if (priority == 2) {
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

        } else if (priority == 3) {
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
        String newContent = edtContent.getText().toString();
        String newColor = parseColor;
        int newPriority = priority;

        if (editedNote.getTitle().compareTo(newTitle) == 0
                && editedNote.getContent().compareTo(newContent) == 0
                && editedNote.getColor().compareTo(newColor) == 0
                && editedNote.getPriority() == newPriority) {
            //back to list;
        } else {
            editedNote.setTitle(newTitle);
            editedNote.setContent(newContent);
            editedNote.setModifiedDate(getDateTime());
            editedNote.setColor(newColor);
            editedNote.setPriority(newPriority);
            boolean result = noteDAO.editNote(editedNote);
            if (result) {
                setResult(BaseEditActivity.RESULT_CODE_SUCCESS);
            } else {
                setResult(BaseEditActivity.RESULT_CODE_FAILURE);
            }
        }
    }
}
