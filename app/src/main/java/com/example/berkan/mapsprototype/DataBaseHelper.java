package com.example.berkan.mapsprototype;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Berkan on 7-3-2015.
 * This is for the SQLite database that is going to be stored on the internal storage of the phone
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    static final String TABLE_Name = "tableName";
    private static final String DATABASE_NAME = "DBName.sqlite";
    private static final int DATABASE_VERSION = 1;
    static SQLiteDatabase sqlLitedatabase;
    private static String DB_PATH = "/data/data/package_name/databases/"; // moet naar de sql bestand in de assets folder pointen
    public Context context;


    /* http://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application/9109728#9109728   */


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

