package com.k3mshiro.k3mnotes.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.aconstant.ConstantUtil;
import com.k3mshiro.k3mnotes.adapter.ReminderAdapter;
import com.k3mshiro.k3mnotes.dto.NoteDTO;
import com.k3mshiro.k3mnotes.fragment.DrawerInfoFragment;

import java.util.Random;

public class CreateNoteActivity extends BaseEditActivity {

    private static final String TAG = CreateNoteActivity.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Random rd = new Random();
        reminderId = rd.nextInt();
        btnInfoLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoteInfo();
            }
        });
    }

    private void showNoteInfo() {
        infoFragment = new DrawerInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtil.KEY_MODIFIED_DATE, null);
        bundle.putString(ConstantUtil.KEY_CREATED_DATE, null);
        bundle.putString(ConstantUtil.KEY_COLOR, parseColor);
        bundle.putInt(ConstantUtil.KEY_PRIORITY, priority);
        bundle.putString(ConstantUtil.KEY_THEME, theme);
        infoFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.createNote_act, infoFragment, BaseEditActivity.class.getName());
        fragmentTransaction.commit();
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
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNewNote() {
        String title = edtTitle.getText().toString();
        String content;
        if (redtContent.getHtml() == null) {
            content = ConstantUtil.NULL_HTML;
        } else {
            content = redtContent.getHtml();
        }
        String date = ConstantUtil.getCurrentDateTime();
        String modifiedDate = ConstantUtil.getCurrentDateTime();
        NoteDTO newNote = new NoteDTO(title, date, content, parseColor, priority, modifiedDate,
                favorValue, timeInMillis, reminderId);

        boolean result = noteDAO.createNewNote(newNote);
        if (result) {
            if (timeInMillis > 0) {
                ReminderAdapter reminderAdapter = new ReminderAdapter(getApplicationContext(), reminderId,
                        timeInMillis, title, content);
                reminderAdapter.registerReminder();
            }
        }
    }
}
