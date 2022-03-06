package com.example.urbotanist.database;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.urbotanist.ui.Plant.Plant;

import io.realm.Realm;
import io.realm.RealmList;

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
      Log.e(TAG, "open >>" + mSQLException.toString());
      throw mSQLException;
    }
    return this;
  }

  public void close() {
    mDbHelper.close();
  }

  public ArrayList<Plant> getSearchResult(String searchTerm) {
    ArrayList<Plant> results = new ArrayList<Plant>();
    ArrayList<String> alreadySaved = new ArrayList<String>();
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
          // table contains plants previously in the garden, only those with "+" in row 1 are currently planted
          int id = mCur.getPosition();
          String genus = mCur.getString(2);
          String type = mCur.getString(3);
          if (!alreadySaved.contains(genus + type)) {
            String family = mCur.getString(4);
            String plant_native = mCur.getString(6);
            //get all native names for a plant
            RealmList<String> name_common = getAllNativeNames(genus, type, 7);
            //String name_common = mCur.getString(7);
            String life_form = mCur.getString(8);
            RealmList<String> location_short = getAllNativeNames(genus, type, 9);
            RealmList<String> location_long = getAllNativeNames(genus, type, 10);
            //String location_short = mCur.getString(9);
            //String location_long = mCur.getString(10);

            results.add(
                new Plant(id, genus, type, family, location_short, location_long, plant_native,
                    name_common, life_form));
            alreadySaved.add(genus + type);
          }
        }
      }
      return results;
    } catch (SQLException mSQLException) {
      Log.e(TAG, "getTestData >>" + mSQLException.toString());
      throw mSQLException;
    }
  }

  private RealmList<String> getAllNativeNames(String genus, String type, int row) {
    RealmList<String> result = new RealmList<String>();
    try {
      String sql = "SELECT * FROM plantDatabase" +
          " WHERE plantDatabase.GATTUNG='" + genus +
          "' AND plantDatabase.ART='" + type +
          "' AND plantDatabase.BESTAND='+' ";
      Cursor mCur = mDb.rawQuery(sql, null);
      if (mCur != null) {
        while (mCur.moveToNext()) {
          String name = mCur.getString(row);
          if (!result.contains(name) && name.length() > 0) {
            result.add(name);
          }
        }
      }
      return result;
    } catch (SQLException mSQLException) {
      Log.e(TAG, "getTestData >>" + mSQLException.toString());
      throw mSQLException;
    }
  }

}

