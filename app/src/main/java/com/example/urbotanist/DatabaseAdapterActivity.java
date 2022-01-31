package com.example.urbotanist;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

// https://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application

public class DatabaseAdapterActivity {
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseHelperActivity mDbHelper;

    public DatabaseAdapterActivity(Context context) {
        this.mContext = context;
        mDbHelper = new DatabaseHelperActivity(mContext);
    }

    public DatabaseAdapterActivity createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DatabaseAdapterActivity open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor getTestData() {
        try {
            String sql ="SELECT * FROM bestand1" +
                    " WHERE GATTUNG LIKE '%erica%'" +
                    " OR ART LIKE '%erica%'" +
                    " OR FAMILIE LIKE '%erica%'" +
                    " OR VOLKSNAMEN LIKE '%erica%'";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                while (mCur.moveToNext()) {
                    Log.d("1 TAG", mCur.getString(1));
                    Log.d("2 TAG", mCur.getString(2));
                    Log.d("3 TAG", mCur.getString(3));
                    Log.d("6 TAG", mCur.getString(6));
                }
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}

