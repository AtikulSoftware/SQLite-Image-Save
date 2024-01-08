package com.example.sqliteimagesave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "user_profile";
    public static final String DB_TABLE_NAME = "user_profile";
    public static final int DB_VERSION  = 1;
    byte[] imageBytes;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+DB_TABLE_NAME+" (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,image BLOB) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE_NAME);
    }

    public boolean insertData(String name, Bitmap image){
        SQLiteDatabase database = this.getWritableDatabase();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);


        imageBytes = outputStream.toByteArray();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("image",imageBytes);
        long result = database.insert(DB_TABLE_NAME, null, contentValues);
        if (result <= 0){
            return false;
        } else {
            return true;
        }

    } // insertData end here =========================

    public Cursor getUserProfile(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+DB_TABLE_NAME, null);
        if (cursor.getCount() != 0){
            return cursor;
        }
        return null;
    }

} // DatabaseHelper end here ===============
