package com.example.berkan.mapsprototype;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by Berkan on 7-3-2015.
 * This is for the SQLite database that is going to be stored on the internal storage of the phone
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    static final String TABLE_Name = "tableName";
    private static final String DATABASE_NAME = "DBName.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH = DataBaseHelper.class.getSimpleName(); // Safer then hardcoding it
    static SQLiteDatabase mDatabase;
    // moet naar de sql bestand in de assets folder pointen
    public Context context;


    public DataBaseHelper(Context context) { //lol
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /* http://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application/9109728#9109728
       * the default android api description is way to abstract for me to understand */
    public void createDatabase() throws IOException {
        boolean exist = databaseExists();
        if (!exist) {
            this.getWritableDatabase(); // get an readable instance of the database
            this.close(); // close connection
            try {
                cloneDatabase(); // lets clone the database


            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Clone database", "clone failed" + "Failed to clone db");
            }
        }
    }

    public boolean databaseExists() {
        File dbFile = new File(DB_PATH + DATABASE_NAME);
        Log.d("databaseFile", "databaseFile" + dbFile.exists());
        return dbFile.exists(); // returns true if the database file exists or false if it does not exist
    }

    public void cloneDatabase() throws IOException {
        InputStream is = context.getAssets().open(DATABASE_NAME);
        String outputFileName = DB_PATH + DATABASE_NAME;
        OutputStream os = new FileOutputStream(outputFileName);
        byte[] buffer = new byte[1024];  // buffer van 1024 bytes
        int len;
        while ((len = is.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        is.close();
    }

    public void openDatabase() throws SQLException {
        String path = DB_PATH + DATABASE_NAME;
        mDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }
    @Override
    public synchronized void close() {
        if (mDatabase != null) {
            mDatabase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // onnodig in onze applicatie, omdat we al een database hebben hiermee zou je de columns etc kunnen defineren
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // zelfde als hierboven

    }
}

