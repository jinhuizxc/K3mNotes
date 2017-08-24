package com.k3mshiro.k3mnotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "NoteManagement";
    public static final int DB_VERSION = 2;
    public static final String TABLE_NOTE = "Note";

    public static final String COLUMN_NOTE_ID = "id";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_DATE = "date";
    public static final String COLUMN_NOTE_CONTENT = "content";
    public static final String COLUMN_NOTE_COLOR = "color";

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCommand = "CREATE TABLE " + TABLE_NOTE
                + " ( " + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOTE_TITLE + " TEXT, "
                + COLUMN_NOTE_DATE + " TEXT, "
                + COLUMN_NOTE_CONTENT + " TEXT, "
                + COLUMN_NOTE_COLOR + " TEXT ); ";

        db.execSQL(sqlCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlCommand = "DROP TABLE IF EXISTS " + TABLE_NOTE;
        db.execSQL(sqlCommand);
        onCreate(db);
    }
}

