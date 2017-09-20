package com.k3mshiro.k3mnotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.k3mshiro.k3mnotes.aconstant.ConstantUtil;

public class Database extends SQLiteOpenHelper {
    public Database(Context context) {
        super(context, ConstantUtil.DB_NAME, null, ConstantUtil.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCommand = "CREATE TABLE " + ConstantUtil.TABLE_NOTE
                + " ( " + ConstantUtil.COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ConstantUtil.COLUMN_NOTE_TITLE + " TEXT, "
                + ConstantUtil.COLUMN_NOTE_REMINDER_ID + " INTEGER, "
                + ConstantUtil.COLUMN_NOTE_DATE + " TEXT, "
                + ConstantUtil.COLUMN_NOTE_CONTENT + " TEXT, "
                + ConstantUtil.COLUMN_NOTE_PRIORITY + " INTEGER, "
                + ConstantUtil.COLUMN_NOTE_MODIFIED_DATE + " TEXT, "
                + ConstantUtil.COLUMN_NOTE_FAVORITE + " INTEGER, "
                + ConstantUtil.COLUMN_NOTE_TIME_REMINDER + " LONG, "
                + ConstantUtil.COLUMN_NOTE_IS_IN_TRASH + " INTEGER, "
                + ConstantUtil.COLUMN_NOTE_COLOR + " TEXT ); ";

        db.execSQL(sqlCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlCommand = "DROP TABLE IF EXISTS " + ConstantUtil.TABLE_NOTE;
        db.execSQL(sqlCommand);
        onCreate(db);
    }
}

