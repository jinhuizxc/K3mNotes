package com.k3mshiro.k3mnotes.aconstant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConstantUtil {

    /*BaseEditActivity*/
    public static final int RESULT_CODE_SUCCESS = 1000;
    public static final int RESULT_CODE_DELETE = 1002;
    public static final int RESULT_CODE_FAILURE = 1001;


    /*Main Activity*/
    public static final int VERTICAL_ITEM_SPACE = 30;
    public static final int REQUEST_CODE_EDIT = 102;
    public static final int REQUEST_CODE_TRASH = 103;
    public static final int REQUEST_OPEN_CAMERA = 104;
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
    public static final String SNAPSHOT = "SNAPSHOT";

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

    public static String convertStringToHtml(String content) {
        String nullHtlm = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta name=\"viewport\" content=\"user-scalable=no\">\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"normalize.css\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">\n" +
                "</head>\n" +
                "<body>{CONTENT}\n" +
                "<div id=\"editor\" contenteditable=\"true\"></div>\n" +
                "<script type=\"text/javascript\" src=\"rich_editor.js\"></script>\n" +
                "</body>\n" +
                "</html>\n";

        return nullHtlm.replace("{CONTENT}", content);
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

    public static String getSnapshotPath(Bitmap finalBitmap) {
        String timeShot = getCurrentDateTime().replaceAll("/", "_").replaceAll(":", "_").trim();
        String html = "<body><img class=\"resize\" src='{IMAGE_PATH}' /></body>";
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SnapshotSave/";
        File image = new File(root);
        image.mkdirs();
        String imageName = timeShot + ".jpg";
        File file = new File(image, imageName);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        html = html.replace("{IMAGE_PATH}", root + imageName);
        return html;
    }
}
