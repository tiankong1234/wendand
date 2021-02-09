package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataSQl extends SQLiteOpenHelper {
    public static final String CREATE_DATA = "create table Data ("
            + "id integer primary key autoincrement, "
            + "title text, "
            + "content text, "
            + "date text,"
            + "titlecolor integer,"
            + "contentcolor integer,"
            + "bordercolor integer,"
            + "typename text,"
            + "typeposition integer,"
            + "checked integer,"
            + "startdate integer,"
            + "startdatestr text)";
    public DataSQl( Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
