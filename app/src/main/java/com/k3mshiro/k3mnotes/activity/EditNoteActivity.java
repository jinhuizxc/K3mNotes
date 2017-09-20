package com.k3mshiro.k3mnotes.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.aconstant.ConstantUtil;
import com.k3mshiro.k3mnotes.adapter.ReminderAdapter;
import com.k3mshiro.k3mnotes.dto.NoteDTO;
import com.k3mshiro.k3mnotes.fragment.DrawerInfoFragment;

public class EditNoteActivity extends BaseEditActivity {
    private long currentTimeMillis;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnInfoLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoteInfo();
            }
        });
        getNoteDatas();
    }

    private void getNoteDatas() {
        Intent intent = getIntent();
        editedNote = (NoteDTO) intent.getSerializableExtra(ConstantUtil.EDIT_NOTE);
        edtTitle.setText(editedNote.getTitle());
        parseColor = editedNote.getColor();
        edtTitle.setTextColor(Color.parseColor(parseColor));
        redtContent.setHtml(editedNote.getContent());
        priority = editedNote.getPriority();
        favorValue = editedNote.getFavoriteValue();
        timeInMillis = editedNote.getTimeReminder();
        reminderId = editedNote.getReminderId();

        currentTimeMillis = timeInMillis;

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
        } else if (priority == 1) {
            btnPrioritySet.setText(getText(R.string.low_priority));
            btnPrioritySet.setTextColor(Color.parseColor("#4CAF50"));

        } else if (priority == 2) {
            btnPrioritySet.setText(getText(R.string.medium_priority));
            btnPrioritySet.setTextColor(Color.parseColor("#FFEA00"));

        } else if (priority == 3) {
            btnPrioritySet.setText(getText(R.string.high_priority));
            btnPrioritySet.setTextColor(Color.parseColor("#FB8C00"));
        }

        if (favorValue == 1) {
            cbFavorite.setChecked(true);
        } else {
            cbFavorite.setChecked(false);
        }

        if (timeInMillis > 0) {
            String timeText = "Time set for " + ConstantUtil.getCurrentDateTime(timeInMillis);
            tvTimeInfo.setText(timeText);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete: " + editedNote.getTitle());
                builder.setMessage("Are you sure you want to delete this note?");
                builder.setIcon(R.drawable.do_note_icon);
                builder.setCancelable(false);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(ConstantUtil.RESULT_CODE_DELETE);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
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

        long newTimeReminder = timeInMillis;

        if (editedNote.getTitle().compareTo(newTitle) == 0
                && editedNote.getContent().compareTo(newContent) == 0
                && editedNote.getColor().compareTo(newColor) == 0
                && editedNote.getPriority() == newPriority
                && editedNote.getFavoriteValue() == newFavorValue
                && editedNote.getTimeReminder() == newTimeReminder) {
            //back to list;
        } else {
            editedNote.setTitle(newTitle);
            editedNote.setContent(newContent);
            editedNote.setModifiedDate(ConstantUtil.getCurrentDateTime());
            editedNote.setColor(newColor);
            editedNote.setPriority(newPriority);
            editedNote.setFavoriteValue(newFavorValue);
            editedNote.setTimeReminder(newTimeReminder);
            boolean result = noteDAO.editNote(editedNote);
            if (result) {
                setResult(ConstantUtil.RESULT_CODE_SUCCESS);
                if (timeInMillis > 0 && editedNote.getTimeReminder() != currentTimeMillis) {
                    ReminderAdapter reminderAdapter = new ReminderAdapter(getApplicationContext(),
                            reminderId, timeInMillis, editedNote.getTitle(), editedNote.getContent());
                    reminderAdapter.registerReminder();
                } else if (timeInMillis == -1 || timeInMillis == 0) {
                    ReminderAdapter reminderAdapter = new ReminderAdapter(getApplicationContext(),
                            reminderId, timeInMillis, editedNote.getTitle(), editedNote.getContent());
                    reminderAdapter.unregisterReminder();
                }
            } else {
                setResult(ConstantUtil.RESULT_CODE_FAILURE);
            }
        }
    }

    private void showNoteInfo() {
        infoFragment = new DrawerInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtil.KEY_MODIFIED_DATE, editedNote.getModifiedDate());
        bundle.putString(ConstantUtil.KEY_CREATED_DATE, editedNote.getDate());
        bundle.putString(ConstantUtil.KEY_COLOR, editedNote.getColor());
        bundle.putInt(ConstantUtil.KEY_PRIORITY, editedNote.getPriority());
        bundle.putString(ConstantUtil.KEY_THEME, theme);
        infoFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.createNote_act, infoFragment, BaseEditActivity.class.getName());
        fragmentTransaction.commit();
    }
}
