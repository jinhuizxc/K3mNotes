package com.k3mshiro.k3mnotes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.k3mshiro.k3mnotes.aconstant.ConstantUtil;
import com.k3mshiro.k3mnotes.database.Database;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    private SQLiteDatabase mSQLiteDB;
    private Database db;
    private Context mContext;

    public NoteDAO(Context mContext) {
        db = new Database(mContext);
    }


    public void openDatabase() {
        mSQLiteDB = db.getWritableDatabase();
    }

    public void closeDatabase() {
        db.close();
    }

    public boolean createNewNote(NoteDTO newNote) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ConstantUtil.COLUMN_NOTE_TITLE, newNote.getTitle());
        contentValues.put(ConstantUtil.COLUMN_NOTE_DATE, newNote.getDate());
        contentValues.put(ConstantUtil.COLUMN_NOTE_CONTENT, newNote.getContent());
        contentValues.put(ConstantUtil.COLUMN_NOTE_PRIORITY, newNote.getPriority());
        contentValues.put(ConstantUtil.COLUMN_NOTE_MODIFIED_DATE, newNote.getModifiedDate());
        contentValues.put(ConstantUtil.COLUMN_NOTE_FAVORITE, newNote.getFavoriteValue());
        contentValues.put(ConstantUtil.COLUMN_NOTE_TIME_REMINDER, newNote.getTimeReminder());
        contentValues.put(ConstantUtil.COLUMN_NOTE_REMINDER_ID, newNote.getReminderId());
        contentValues.put(ConstantUtil.COLUMN_NOTE_COLOR, newNote.getColor());
        contentValues.put(ConstantUtil.COLUMN_NOTE_IS_IN_TRASH, 0);

        long idNhanVien = mSQLiteDB.insert(ConstantUtil.TABLE_NOTE, null, contentValues);

        return idNhanVien != 0;
    }


    public List<NoteDTO> getAllNotes(int viewMode) {
        List<NoteDTO> listNoteDTOs = new ArrayList<>();
        String sqlCommand = "";
        switch (viewMode) {
            case 1:
                sqlCommand = "SELECT * FROM " + ConstantUtil.TABLE_NOTE + " WHERE " +
                        ConstantUtil.COLUMN_NOTE_IS_IN_TRASH + " = 0";
                break;
            case 2:
                sqlCommand = "SELECT * FROM " + ConstantUtil.TABLE_NOTE + " WHERE " +
                        ConstantUtil.COLUMN_NOTE_IS_IN_TRASH + " = 0 and "
                        + ConstantUtil.COLUMN_NOTE_FAVORITE + " = 1";
                break;
            case 3:
                sqlCommand = "SELECT * FROM " + ConstantUtil.TABLE_NOTE + " WHERE " +
                        ConstantUtil.COLUMN_NOTE_IS_IN_TRASH + " = 0 and "
                        + ConstantUtil.COLUMN_NOTE_TIME_REMINDER + " > 0";
                break;
            default:
                sqlCommand = "SELECT * FROM " + ConstantUtil.TABLE_NOTE + " WHERE " +
                        ConstantUtil.COLUMN_NOTE_IS_IN_TRASH + " = 0";
                break;
        }
        Cursor cursor = mSQLiteDB.rawQuery(sqlCommand, null);
        int idIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_ID);
        int titleIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_TITLE);
        int dateIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_DATE);
        int contentIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_CONTENT);
        int colorIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_COLOR);
        int modifiedDateIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_MODIFIED_DATE);
        int favoriteIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_FAVORITE);
        int priorityIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_PRIORITY);
        int timeReminderIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_TIME_REMINDER);
        int reminderIdIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_REMINDER_ID);
        int isInTrashIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_IS_IN_TRASH);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(idIndex);
            String title = cursor.getString(titleIndex);
            String date = cursor.getString(dateIndex);
            String content = cursor.getString(contentIndex);
            String modifiedDate = cursor.getString(modifiedDateIndex);
            int priority = cursor.getInt(priorityIndex);
            String color = cursor.getString(colorIndex);
            int isFavorite = cursor.getInt(favoriteIndex);
            long timeReminder = cursor.getLong(timeReminderIndex);
            int reminderId = cursor.getInt(reminderIdIndex);
            int isInTrash = cursor.getInt(isInTrashIndex);

            NoteDTO newNote = new NoteDTO(id, title, date, content, color, priority, modifiedDate,
                    isFavorite, timeReminder, reminderId, isInTrash);

            listNoteDTOs.add(newNote);

            cursor.moveToNext();
        }

        return listNoteDTOs;
    }


    public List<NoteDTO> getNotesInTrash() {
        List<NoteDTO> listNoteDTOs = new ArrayList<>();
        String sqlCommand = "SELECT * FROM " + ConstantUtil.TABLE_NOTE + " WHERE " +
                ConstantUtil.COLUMN_NOTE_IS_IN_TRASH + " = 1";
        Cursor cursor = mSQLiteDB.rawQuery(sqlCommand, null);

        int idIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_ID);
        int titleIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_TITLE);
        int dateIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_DATE);
        int contentIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_CONTENT);
        int colorIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_COLOR);
        int modifiedDateIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_MODIFIED_DATE);
        int favoriteIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_FAVORITE);
        int priorityIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_PRIORITY);
        int timeReminderIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_TIME_REMINDER);
        int reminderIdIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_REMINDER_ID);
        int isInTrashIndex = cursor.getColumnIndex(ConstantUtil.COLUMN_NOTE_IS_IN_TRASH);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(idIndex);
            String title = cursor.getString(titleIndex);
            String date = cursor.getString(dateIndex);
            String content = cursor.getString(contentIndex);
            String modifiedDate = cursor.getString(modifiedDateIndex);
            int priority = cursor.getInt(priorityIndex);
            String color = cursor.getString(colorIndex);
            int isFavorite = cursor.getInt(favoriteIndex);
            long timeReminder = cursor.getLong(timeReminderIndex);
            int reminderId = cursor.getInt(reminderIdIndex);
            int isInTrash = cursor.getInt(isInTrashIndex);

            NoteDTO newNote = new NoteDTO(id, title, date, content, color, priority, modifiedDate,
                    isFavorite, timeReminder, reminderId, isInTrash);

            listNoteDTOs.add(newNote);

            cursor.moveToNext();
        }

        return listNoteDTOs;
    }

    public boolean deleteNote(NoteDTO deletedNote) {
        int result = mSQLiteDB.delete(ConstantUtil.TABLE_NOTE, ConstantUtil.COLUMN_NOTE_ID + " = " + deletedNote.getId(),
                null);
        return result != 0;
    }

    public boolean emptyTrash() {
        int result = mSQLiteDB.delete(ConstantUtil.TABLE_NOTE, ConstantUtil.COLUMN_NOTE_IS_IN_TRASH + " = 1",
                null);
        return result != 0;
    }

    public void updateToTrash(NoteDTO deletedNote, int trashValue) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ConstantUtil.COLUMN_NOTE_IS_IN_TRASH, trashValue);

        mSQLiteDB.update(ConstantUtil.TABLE_NOTE,
                contentValues,
                ConstantUtil.COLUMN_NOTE_ID + " = " + deletedNote.getId(),
                null);
    }

    public boolean editNote(NoteDTO editedNote) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ConstantUtil.COLUMN_NOTE_TITLE, editedNote.getTitle());
        contentValues.put(ConstantUtil.COLUMN_NOTE_DATE, editedNote.getDate());
        contentValues.put(ConstantUtil.COLUMN_NOTE_CONTENT, editedNote.getContent());
        contentValues.put(ConstantUtil.COLUMN_NOTE_COLOR, editedNote.getColor());
        contentValues.put(ConstantUtil.COLUMN_NOTE_MODIFIED_DATE, editedNote.getModifiedDate());
        contentValues.put(ConstantUtil.COLUMN_NOTE_FAVORITE, editedNote.getFavoriteValue());
        contentValues.put(ConstantUtil.COLUMN_NOTE_PRIORITY, editedNote.getPriority());
        contentValues.put(ConstantUtil.COLUMN_NOTE_TIME_REMINDER, editedNote.getTimeReminder());

        int result = mSQLiteDB.update(ConstantUtil.TABLE_NOTE,
                contentValues,
                ConstantUtil.COLUMN_NOTE_ID + " = " + editedNote.getId(),
                null);

        return result != 0;
    }
}
