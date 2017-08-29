package com.k3mshiro.k3mnotes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.k3mshiro.k3mnotes.database.Database;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        contentValues.put(Database.COLUMN_NOTE_TITLE, newNote.getTitle());
        contentValues.put(Database.COLUMN_NOTE_DATE, newNote.getDate());
        contentValues.put(Database.COLUMN_NOTE_CONTENT, newNote.getContent());
        contentValues.put(Database.COLUMN_NOTE_PRIORITY, newNote.getPriority());
        contentValues.put(Database.COLUMN_MODIFIED_DATE, newNote.getModifiedDate());
        contentValues.put(Database.COLUMN_NOTE_COLOR, newNote.getColor());

        long idNhanVien = mSQLiteDB.insert(Database.TABLE_NOTE, null, contentValues);

        return idNhanVien != 0;
    }


    public List<NoteDTO> getListNotes() {

        List<NoteDTO> listNoteDTOs = new ArrayList<>();
        String sqlCommand = "SELECT * FROM " + Database.TABLE_NOTE;
        Cursor cursor = mSQLiteDB.rawQuery(sqlCommand, null);

        int idIndex = cursor.getColumnIndex(Database.COLUMN_NOTE_ID);
        int titleIndex = cursor.getColumnIndex(Database.COLUMN_NOTE_TITLE);
        int dateIndex = cursor.getColumnIndex(Database.COLUMN_NOTE_DATE);
        int contentIndex = cursor.getColumnIndex(Database.COLUMN_NOTE_CONTENT);
        int colorIndex = cursor.getColumnIndex(Database.COLUMN_NOTE_COLOR);
        int modifiedDateIndex = cursor.getColumnIndex(Database.COLUMN_MODIFIED_DATE);
        int priorityIndex = cursor.getColumnIndex(Database.COLUMN_NOTE_PRIORITY);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(idIndex);
            String title = cursor.getString(titleIndex);
            String date = cursor.getString(dateIndex);
            String content = cursor.getString(contentIndex);
            String modifiedDate = cursor.getString(modifiedDateIndex);
            int priority = cursor.getInt(priorityIndex);
            String color = cursor.getString(colorIndex);

            NoteDTO newNote = new NoteDTO(id, title, date, content, color, priority, modifiedDate);

            listNoteDTOs.add(newNote);

            cursor.moveToNext();
        }

        return listNoteDTOs;
    }

    public boolean deleteNote(NoteDTO deletedNote) {

        int result = mSQLiteDB.delete(Database.TABLE_NOTE, Database.COLUMN_NOTE_ID + " = " + deletedNote.getId(),
                null);


        return result != 0;
    }

    public boolean editNote(NoteDTO editedNote) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(Database.COLUMN_NOTE_TITLE, editedNote.getTitle());
        contentValues.put(Database.COLUMN_NOTE_DATE, editedNote.getDate());
        contentValues.put(Database.COLUMN_NOTE_CONTENT, editedNote.getContent());
        contentValues.put(Database.COLUMN_NOTE_COLOR, editedNote.getColor());
        contentValues.put(Database.COLUMN_MODIFIED_DATE, editedNote.getModifiedDate());
        contentValues.put(Database.COLUMN_NOTE_PRIORITY, editedNote.getPriority());

        int result = mSQLiteDB.update(Database.TABLE_NOTE,
                contentValues,
                Database.COLUMN_NOTE_ID + " = " + editedNote.getId(),
                null);

        return result != 0;
    }
}
