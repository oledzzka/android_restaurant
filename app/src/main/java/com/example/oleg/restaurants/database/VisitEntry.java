package com.example.oleg.restaurants.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oleg on 25.12.17.
 */

public class VisitEntry {
    public static final String TABLE_NAME = "Visit";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE="date";
    public static final String COLUMN_NULL="NULL";

    private static final String DATE_TIME_TYPE = " DATETIME";
    private static final String COMMA_SEP = ",";
    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_DATE + DATE_TIME_TYPE + " DEFAULT CURRENT_TIMESTAMP" +
                    " )";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static Long getLastVisit(Context context) {
        SQLiteOpenHelper dbHelper = new DataBaseHelper(context);
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            Cursor cursor = db.query(
                    TABLE_NAME,
                    new String[]{COLUMN_DATE},
                    null,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                long dateTime = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                return dateTime;
            }
        }
        return -1l;
    }



    public static void setVisit(Context context) {
        SQLiteOpenHelper dbHelper = new DataBaseHelper(context);
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            try {
                db.delete(TABLE_NAME, null, null);
            } catch (SQLException ignored) {}
            ContentValues values = new ContentValues();
            values.put(COLUMN_DATE, System.currentTimeMillis());
            db.insert(
                    TABLE_NAME,
                    COLUMN_NULL,
                    values
            );
        }
    }
}
