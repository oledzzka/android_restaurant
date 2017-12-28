package com.example.oleg.restaurants.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oleg on 25.12.17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Restaurant.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CurrentCityEntry.SQL_CREATE_ENTRIES);
        db.execSQL(CategoryEntry.SQL_CREATE_ENTRIES);
        db.execSQL(VisitEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CurrentCityEntry.SQL_DELETE_ENTRIES);
        db.execSQL(CategoryEntry.SQL_DELETE_ENTRIES);
        db.execSQL(VisitEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
