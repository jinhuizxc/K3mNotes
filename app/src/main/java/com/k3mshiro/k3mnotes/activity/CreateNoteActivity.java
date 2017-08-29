package com.k3mshiro.k3mnotes.activity;

import android.view.Menu;
import android.view.MenuItem;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

public class CreateNoteActivity extends BaseEditActivity {

    private static final String TAG = CreateNoteActivity.class.getName();

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

    private void createNewNote() {
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        String date = getDateTime();
        String modifiedDate = getDateTime();
        NoteDTO newNote = new NoteDTO(title, date, content, parseColor, priority, modifiedDate);
        boolean result = noteDAO.createNewNote(newNote);

        if (result) {
            setResult(RESULT_CODE_SUCCESS);
        } else {
            setResult(RESULT_CODE_FAILURE);
        }
    }
}
