package com.example.urbotanist;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.urbotanist.ui.Plant.Plant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private SQLiteDatabase dataBase;
    private final File DB_FILE;
    public static final String DATABASE_NAME = "database.sqlite";
    public static final int DATABASE_VERSION = 1;

    public static String TAG = "DATABASEACTIVITY";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DB_FILE = context.getDatabasePath(DATABASE_NAME);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDataBase() throws IOException {
        // If the database does not exist, copy it from the assets.
        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                // Copy the database from assests
                copyDataBase();
                //joinDataBase();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    /*private void joinDataBase() {
        Log.d(TAG, "wir try ");
        try {
            Log.d(TAG, "und wir tryen weiter");
            String sql = "CREATE TABLE [IF NOT EXISTS] .plantDatabase (" +
                    "ID INTEGER PRIMARY KEY, BESTAND TEXT, " +
                    "GATTUNG TEXT, ART string, FAMILIE TEXT, " +
                    "STANDORT TEXT, VORKOMMEN TEXT, VOLKSNAMEN TEXT, LEBENSFORM TEXT, " +
                    "standortKurz TEXT, standortLang TEXT, alterBegriff TEXT)";
            dataBase.execSQL(sql);
            Log.d(TAG, "anderes b");

            String joinSql =
                    " SELECT database_bestand1.BESTAND, database_bestand1.GATTUNG, database_bestand1.ART," +
                    " database_bestand1.FAMILIE, database_bestand1.STANDORT_G," +
                    " database_bestand1.VORKOMMEN, database_bestand1.VOLKSNAMEN, database_bestand1.LEBENSFORM," +
                    " database_standorte.kurz, database_standorte.lang, database_standorte.alterBegriff" +
                    " FROM database_standorte" +
                    " INNER JOIN database_bestand1 ON database_bestand1.STANDORT_G=database_standorte.alterBegriff";
            Cursor c = dataBase.query("plantDatabase", null, null, null, null, null, null);
            Log.d(TAG, "anderes aaaaaah");
        } catch (SQLException mSQLException) {
            Log.d(TAG, "aaaaaah");
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    } */

    // Check that the database file exists in databases folder
    private boolean checkDataBase() {
        return DB_FILE.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream mInput = context.getAssets().open(DATABASE_NAME);
        OutputStream mOutput = new FileOutputStream(DB_FILE);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        dataBase = SQLiteDatabase.openDatabase(DB_FILE.getPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return dataBase != null;
    }

    @Override
    public synchronized void close() {
        if(dataBase != null) {
            dataBase.close();
        }
        super.close();
    }
}
