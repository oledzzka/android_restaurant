package com.example.oleg.restaurants.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.oleg.restaurants.models.Category;

import java.util.List;

/**
 * Created by oleg on 25.12.17.
 */

public class CategoryEntry {


    public static final String TABLE_NAME = "Categories";
    public static final String COLUMN_NAME= "name";
    public static final String COLUMN_ID="column_id";
    public static final String COLUMN_CHOOSE="choose";
    public static final String COLUMN_NULL="NULL";

    private static final String TEXT_TYPE = " TEXT";
    private static final String BOOLEAN_TYPE = " BOOLEAN";
    private static final String COMMA_SEP = ",";
    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_CHOOSE + BOOLEAN_TYPE +
                    " )";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static Category getCurrentCategory(Context context) {
        SQLiteOpenHelper dbHelper = new DataBaseHelper(context);
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            Cursor cursor = db.query(
                    TABLE_NAME,
                    new String[]{
                            COLUMN_ID,
                            COLUMN_NAME
                    },
                    COLUMN_CHOOSE + " = ?",
                    new String[]{String.valueOf(true)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                int cityId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(CurrentCityEntry.COLUMN_ID)
                );
                String cityName = cursor.getString(cursor.getColumnIndexOrThrow(CurrentCityEntry.COLUMN_NAME));
                Category category = new Category();
                category.setId(cityId);
                category.setName(cityName);
                return category;
            }
        }
        return null;
    }

    public static void setCurrentCategory(Context context, Category category) {
        SQLiteOpenHelper dbHelper = new DataBaseHelper(context);
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CHOOSE, false);
            int c = db.update(
                    TABLE_NAME,
                    values,
                    null,
                    null);
            values = new ContentValues();
            values.put(COLUMN_CHOOSE, true);
            String selection = COLUMN_NAME + " = ?";
            String[] selectionArgs = { category.getName() };
            int c2 = db.update(
                    TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }

    public static void setListCategory(Context context, List<Category> categoryList) {
        SQLiteOpenHelper dbHelper = new DataBaseHelper(context);
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            try {
                db.delete(TABLE_NAME, null, null);
            } catch (SQLException ignored) {}
            ContentValues values = new ContentValues();
            for (Category category: categoryList) {
                values.put(COLUMN_ID, category.getId());
                values.put(COLUMN_NAME, category.getName());
                db.insert(
                        TABLE_NAME,
                        COLUMN_NULL,
                        values
                );
            }
        }
    }
}
