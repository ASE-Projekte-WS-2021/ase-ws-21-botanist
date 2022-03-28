package com.example.urbotanist.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.urbotanist.ui.plant.Plant;
import java.io.IOException;
import java.util.ArrayList;

// realm by MongoDB https://realm.io/
import io.realm.RealmList;


// https://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application

public class DatabaseAdapterActivity {

  protected static final String TAG = "DataAdapter";

  private final Context context;
  private SQLiteDatabase sqLiteDatabase;
  private DatabaseHelper dbHelper;

  public DatabaseAdapterActivity(Context context) {
    this.context = context;
    dbHelper = new DatabaseHelper(this.context);
  }

  public DatabaseAdapterActivity createDatabase() throws SQLException {
    try {
      dbHelper.createDataBase();
    } catch (IOException ioException) {
      Log.e(TAG, ioException.toString() + "  UnableToCreateDatabase");
      throw new Error("UnableToCreateDatabase");
    }
    return this;
  }

  public DatabaseAdapterActivity open() throws SQLException {
    try {
      dbHelper.openDataBase();
      dbHelper.close();
      sqLiteDatabase = dbHelper.getReadableDatabase();
    } catch (SQLException sqlException) {
      Log.e(TAG, "open >>" + sqlException.toString());
      throw sqlException;
    }
    return this;
  }

  public void close() {
    dbHelper.close();
  }

  public ArrayList<Plant> getSearchResult(String searchTerm) {
    ArrayList<Plant> results = new ArrayList<Plant>();
    ArrayList<String> alreadySaved = new ArrayList<String>();
    try {
      String sql = "SELECT * FROM plantDatabase"
          + " WHERE plantDatabase.BESTAND LIKE '%+%'"
          + " AND (plantDatabase.GATTUNG LIKE '%" + searchTerm + "%'"
          + " OR plantDatabase.ART LIKE '%" + searchTerm + "%'"
          + " OR plantDatabase.FAMILIE LIKE '%" + searchTerm + "%'"
          + " OR plantDatabase.VOLKSNAMEN LIKE '%" + searchTerm + "%')";

      Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
      if (cursor != null) {
        while (cursor.moveToNext()) {
          // table contains plants previously in the garden, only those with "+"
          // in row 1 are currently planted
          int id = cursor.getPosition();
          String genus = cursor.getString(2);
          String type = cursor.getString(3);
          if (!alreadySaved.contains(genus + type)) {
            String family = cursor.getString(4);
            String plantNative = cursor.getString(6);
            //get all native names for a plant
            RealmList<String> nameCommon = getAllNativeNames(genus, type, 7);
            //String nameCommon = cursor.getString(7);
            String lifeForm = cursor.getString(8);
            RealmList<String> locationShort = getAllNativeNames(genus, type, 9);
            RealmList<String> locationLong = getAllNativeNames(genus, type, 10);
            //String locationShort = cursor.getString(9);
            //String locationLong = cursor.getString(10);
            boolean isFavored = false;

            results.add(
                new Plant(id, genus, type, family, locationShort, locationLong, plantNative,
                    nameCommon, lifeForm, isFavored));
            alreadySaved.add(genus + type);
          }
        }
      }
      return results;
    } catch (SQLException sqlException) {
      Log.e(TAG, "getTestData >>" + sqlException.toString());
      throw sqlException;
    }
  }

  private RealmList<String> getAllNativeNames(String genus, String type, int row) {
    RealmList<String> result = new RealmList<String>();
    try {
      String sql = "SELECT * FROM plantDatabase"
          + " WHERE plantDatabase.GATTUNG='" + genus
          + "' AND plantDatabase.ART='" + type
          + "' AND plantDatabase.BESTAND='+' ";
      Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
      if (cursor != null) {
        while (cursor.moveToNext()) {
          String name = cursor.getString(row);
          if (!result.contains(name) && name.length() > 0) {
            result.add(name);
          }
        }
        cursor.close();
      }
      return result;
    } catch (SQLException sqlException) {
      Log.e(TAG, "getTestData >>" + sqlException.toString());
      throw sqlException;
    }
  }

}

