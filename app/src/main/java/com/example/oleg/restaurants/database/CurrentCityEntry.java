package com.example.oleg.restaurants.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.oleg.restaurants.models.City;

/**
 * Created by oleg on 25.12.17.
 */


public class CurrentCityEntry implements BaseColumns {
    public static final String TABLE_NAME = "City";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ID = "column_id";
    public static final String COLUMN_NULL = "NULL";

    private static final String TEXT_TYPE = " TEXT";
    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + TEXT_TYPE +
                    " )";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static City getCurrentCity(Context context) {
        SQLiteOpenHelper dbHelper = new DataBaseHelper(context);
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String[] projection = {
                    COLUMN_ID,
                    COLUMN_NAME
            };
            Cursor cursor = db.query(
                    TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                int cityId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_ID)
                );
                String cityName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                City city = new City();
                city.setId(cityId);
                city.setName(cityName);
                return city;
            }
        }
        return null;
    }



    public static void setCurrentCity(Context context, City city) {
        SQLiteOpenHelper dbHelper = new DataBaseHelper(context);
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            db.delete(TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, city.getId());
            values.put(COLUMN_NAME, city.getName());
            db.insert(
                    TABLE_NAME,
                    COLUMN_NULL,
                    values
            );
        }
    }
}