package com.example.urbotanist;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DatabaseHelperActivity extends SQLiteOpenHelper {

    private Context context;
    private SQLiteDatabase dataBase;
    private final File DB_FILE;
    public static final String DATABASE_NAME = "database.sqlite";
    public static final int DATABASE_VERSION = 1;

    public static String TAG = "DATABASEACTIVITY";

    public static final String TABLE_NAME = "bestand1";
    public static final String COLUMN_ID = "PRIMS";
    public static final String COLUMN_GENUS = "GATTUNG";
    public static final String COLUMN_TYPE = "ART";
    public static final String COLUMN_FAMILY = "FAMILIE";
    public static final String COLUMN_LOCATION = "STANDORT";
    public static final String COLUMN_NATIVE = "VORKOMMEN";
    public static final String COLUMN_COMMON = "VOLKSNAME";
    public static final String COLUMN_LIFEFORM = "LEBENSFORM";


    public DatabaseHelperActivity(@Nullable Context context) {
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
                Log.e(TAG, "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

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
        // Log.v("DB_PATH", DB_FILE.getAbsolutePath());
        dataBase = SQLiteDatabase.openDatabase(DB_FILE.getPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        // mDataBase = SQLiteDatabase.openDatabase(DB_FILE, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
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
