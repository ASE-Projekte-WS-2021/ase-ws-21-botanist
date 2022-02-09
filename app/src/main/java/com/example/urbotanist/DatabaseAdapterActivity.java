package com.example.urbotanist;

import java.io.IOException;
import java.lang.reflect.Array;
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
    private DatabaseHelper mDbHelper;

    public DatabaseAdapterActivity(Context context) {
        this.mContext = context;
        mDbHelper = new DatabaseHelper(mContext);
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
        ArrayList<String> alreadySearched = new ArrayList<String>();
        ArrayList<Plant> results= new ArrayList<Plant>();
        try {
            String sql = "SELECT * FROM plantDatabase" +
                    " WHERE plantDatabase.BESTAND LIKE '%+%'" +
                    " AND (plantDatabase.GATTUNG LIKE '%" + searchTerm + "%'" +
                    " OR plantDatabase.ART LIKE '%" + searchTerm + "%'" +
                    " OR plantDatabase.FAMILIE LIKE '%" + searchTerm + "%'" +
                    " OR plantDatabase.VOLKSNAMEN LIKE '%" + searchTerm + "%')";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                while (mCur.moveToNext()) {
                    //if plant was already searched don't start new sql
                    if (!alreadySearched.contains(mCur.getString(2) +  mCur.getString(3))){
                        // table contains plants previously in the garden, only those with "+" in row 1 are currently planted
                        String genus = mCur.getString(2);
                        String type = mCur.getString(3);
                        String family = mCur.getString(4);
                        String plant_native = mCur.getString(6);
                        String name_common = mCur.getString(7);
                        String life_form = mCur.getString(8);
                        String location_short = mCur.getString(9);
                        String location_long = mCur.getString(10);

                        results.add(new Plant(genus, type, family, location_short, location_long, plant_native, name_common, life_form));
                        alreadySearched.add(genus + type);
                    }
                }
            }
            return results;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    // plantnames are unique by type and genus
    public ArrayList<String[]> getLocationsForPlant(String genus, String type) {
        ArrayList<String[]> locations= new ArrayList<String[]>();
        try {
            String sql = "SELECT * FROM plantDatabase" +
                    " WHERE plantDatabase.GATTUNG LIKE '%" + genus + "%'" +
                    " AND plantDatabase.ART LIKE '%" + type + "%'" +
                    " AND plantDatabase.BESTAND LIKE '%+%'";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                while (mCur.moveToNext()) {
                    String locShort = mCur.getString(9);
                    String locLong = mCur.getString(10);
                    locations.add(new String[]{ locShort , locLong});
                }
            }
            return locations;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}

