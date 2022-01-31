package com.example.urbotanist;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.urbotanist.ui.Plant.Plant;

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

    public ArrayList<Plant> getSearchResult(String searchTerm) {
        ArrayList<Plant> results= new ArrayList<Plant>();
        try {
            String sql ="SELECT * FROM bestand1" +
                    " WHERE GATTUNG LIKE '%" + searchTerm + "%'" +
                    " OR ART LIKE '%" + searchTerm + "%'" +
                    " OR FAMILIE LIKE '%" + searchTerm + "%'" +
                    " OR VOLKSNAMEN LIKE '%" + searchTerm + "%'";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                while (mCur.moveToNext()) {
                    if(mCur.getString(1).equals("+")){
                        String genus = mCur.getString(2);
                        String type = mCur.getString(3);
                        String family = mCur.getString(4);
                        String location = mCur.getString(5);
                        String plant_native = mCur.getString(6);
                        String name_common = mCur.getString(7);
                        String life_form = mCur.getString(8);
                        results.add(new Plant(genus, type, family,location, plant_native, name_common, life_form));
                    }
                }
            }
            return results;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}

