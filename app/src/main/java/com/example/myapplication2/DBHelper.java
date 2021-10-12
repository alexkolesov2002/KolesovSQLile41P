package com.example.myapplication2;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "contactDb";
    public static final String TABLE_MOTORS = "motors";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_MODEL = "model";
    public static final String KEY_VOLTAGE = "voltage";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_MOTORS + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_MODEL + " text," +  KEY_VOLTAGE + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_MOTORS);

        onCreate(db);

    }
}