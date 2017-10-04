package com.tony_justin.mobile_app.assassins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tony Nguyen on 9/29/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "locations";
    private static final String COL0 = "ID";
    private static final String COL1 = "Photo";
    private static final String COL2 = "Caption";
    private static final String COL3 = "Lat";
    private static final String COL4 = "Lon";
    private static final String COL5 = "Time";
    //private static final String COL4 = "Title";

    public DatabaseHelper (Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL1 + " TEXT, "+ COL2 + " TEXT, " + COL3 + " REAL, "+ COL4 + " REAL, " + COL5 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String photo, String caption, double lat, double lon, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, photo);
        contentValues.put(COL2, caption);
        contentValues.put(COL3, lat);
        contentValues.put(COL4, lon);
        contentValues.put(COL5, date);

        Log.d(TAG, "addData: Adding location and timestamp to "+TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
