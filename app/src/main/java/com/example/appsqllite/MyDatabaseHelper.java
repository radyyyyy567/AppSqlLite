package com.example.appsqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create User Table
        db.execSQL("CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "jurusan TEXT, " +
                "nama_ayah TEXT, " +
                "nama_ibu TEXT, " +
                "tanggal_lahir TEXT)");

        // Create Kota Table
        db.execSQL("CREATE TABLE kota (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT)");

        // Create Hobby Table
        db.execSQL("CREATE TABLE hobby (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT)");

        // Create User_Data Table
        db.execSQL("CREATE TABLE user_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "tempat_tinggal INTEGER, " +  // kotaId
                "tempat_lahir INTEGER, " +   // kotaId
                "hobby_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES user(id), " +
                "FOREIGN KEY(tempat_tinggal) REFERENCES kota(id), " +
                "FOREIGN KEY(tempat_lahir) REFERENCES kota(id), " +
                "FOREIGN KEY(hobby_id) REFERENCES hobby(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables
        db.execSQL("DROP TABLE IF EXISTS user_data");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS kota");
        db.execSQL("DROP TABLE IF EXISTS hobby");
        onCreate(db);
    }
}
