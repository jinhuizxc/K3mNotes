package com.k3mshiro.k3mnotes.aconstant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConstantUtil {

    public static final String NULL_HTML = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta name=\"viewport\" content=\"user-scalable=no\">\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"normalize.css\">\n" +
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">\n" +
            "</head>\n" +
            "<body>\n" +
            "<div id=\"editor\" contenteditable=\"true\"></div>\n" +
            "<script type=\"text/javascript\" src=\"rich_editor.js\"></script>\n" +
            "</body>\n" +
            "</html>\n";

    /*BaseEditActivity*/
    public static final int RESULT_CODE_SUCCESS = 1000;
    public static final int RESULT_CODE_DELETE = 1002;
    public static final int RESULT_CODE_FAILURE = 1001;


    /*Main Activity*/
    public static final int VERTICAL_ITEM_SPACE = 30;
    public static final int REQUEST_CODE_EDIT = 102;
    public static final int REQUEST_CODE_TRASH = 103;
    public static final String EDIT_NOTE = "EDIT_NOTE";
    public static final String THEME_PREFERENCES = "THEME_PREFERENCES";
    public static final String VIEW_PREFERENCES = "VIEW_PREFERENCES";
    public static final String RECREATE_ACTIVITY = "RECREATE_ACTIVITY";
    public static final String THEME_SAVED = "THEME_SAVED";
    public static final String VIEWMODE_SAVED = "VIEWMODE_SAVED";
    public static final String DARKTHEME = "DARKTHEME";
    public static final String LIGHTTHEME = "LIGHTTHEME";
    public static final String ALLMODE = "ALLMODE";
    public static final String FAVORITEMODE = "FAVORITEMODE";
    public static final String REMINDERMODE = "REMINDERMODE";

    /*Database NoteDAO*/
    public static final String DB_NAME = "NoteManagement";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NOTE = "Note";
    public static final String COLUMN_NOTE_ID = "id";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_DATE = "date";
    public static final String COLUMN_NOTE_CONTENT = "content";
    public static final String COLUMN_NOTE_COLOR = "color";
    public static final String COLUMN_NOTE_PRIORITY = "priority";
    public static final String COLUMN_NOTE_MODIFIED_DATE = "modifiedDate";
    public static final String COLUMN_NOTE_FAVORITE = "favorite";
    public static final String COLUMN_NOTE_TIME_REMINDER = "timeReminder";
    public static final String COLUMN_NOTE_REMINDER_ID = "reminderId";
    public static final String COLUMN_NOTE_IS_IN_TRASH = "isInTrash";

    /*DrawerInfo Fragment*/
    public static final String KEY_CREATED_DATE = "KEY_CREATED_DATE";
    public static final String KEY_MODIFIED_DATE = "KEY_MODIFIED_DATE";
    public static final String KEY_PRIORITY = "KEY_PRIORITY";
    public static final String KEY_COLOR = "KEY_COLOR";
    public static final String KEY_THEME = "KEY_THEME";

    public static final String PASS_PREFERENCES = "PASS_PREFERENCES";
    public static final String PASSWORD_STATE = "PASSWORD_STATE";
    public static final String PASS_CODE = "PASS_CODE";

    public static String convertStringToHtml(String content) {
        String nullHtlm = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta name=\"viewport\" content=\"user-scalable=no\">\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"normalize.css\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>" + content + "</p>" +
                "<div id=\"editor\" contenteditable=\"true\"></div>\n" +
                "<script type=\"text/javascript\" src=\"rich_editor.js\"></script>\n" +
                "</body>\n" +
                "</html>\n";

        return nullHtlm;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public static String getCurrentDateTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return dateFormat.format(calendar.getTime());
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
