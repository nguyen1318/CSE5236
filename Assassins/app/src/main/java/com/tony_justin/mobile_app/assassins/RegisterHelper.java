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

public class RegisterHelper extends SQLiteOpenHelper {

    private static final String TAG = "RegisterHelper";

    private static final String TABLE_NAME = "users";
    private static final String COL0 = "ID";
    private static final String COL1 = "Name";
    private static final String COL2 = "Email";
    private static final String COL3 = "Password";

    private SHA1 hash = new SHA1();
    public RegisterHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public RegisterHelper (Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL1 + " TEXT, "+ COL2 + " TEXT, " + COL3 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String name, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, name);
        contentValues.put(COL2, email);
        contentValues.put(COL3, hash.SHA1(password));
        Log.d(TAG, "addData: Adding user info to "+TABLE_NAME);
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

