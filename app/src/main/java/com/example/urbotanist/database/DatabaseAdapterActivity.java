package com.example.urbotanist.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.urbotanist.drawerfragments.plant.Plant;
// realm by MongoDB https://realm.io/
import io.realm.RealmList;
import java.io.IOException;
import java.util.ArrayList;



// https://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application

public class DatabaseAdapterActivity {

  protected static final String TAG = "DataAdapter";

  private final Context context;
  private SQLiteDatabase sqLiteDatabase;
  private DatabaseHelper dbHelper;

  /**
   * Constructor
   * @param context
   */
  public DatabaseAdapterActivity(Context context) {
    this.context = context;
    dbHelper = new DatabaseHelper(this.context);
  }

  /**
   * Calls the DatabaseHelper Class and creates the SQL Database
   * @return
   * @throws SQLException
   */
  public DatabaseAdapterActivity createDatabase() throws SQLException {
    try {
      dbHelper.createDataBase();
    } catch (IOException ioException) {
      Log.e(TAG, ioException.toString() + "  UnableToCreateDatabase");
      throw new Error("UnableToCreateDatabase");
    }
    return this;
  }

  /**
   * Opens the SQLDatabase in the Helper class.
   * @return
   * @throws SQLException
   */
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

  /**
   * Closes the DatabaseHelper Object
   */
  public void close() {
    dbHelper.close();
  }

  /**
   * Function used to transfer all plants saved in the SQL database to be transfered into the Realm
   * Database on first start of the app.
   * @param searchTerm = to initialize the database, the searchTerm is an empty string.
   * @return Function returns a list of all plants in the database
   */
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
            String lifeForm = cursor.getString(8);
            RealmList<String> locationShort = getAllNativeNames(genus, type, 9);
            RealmList<String> locationLong = getAllNativeNames(genus, type, 10);

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

  /**
   * Function returns a list with different native names that share a biological name.
   * Alternatively also returns a list of locations for a plant with its single biological name that appears in different locations.
   * @param genus & @param type are used to identify a single plant via its unique biological name
   * @param row = used to determine the use case, as described in the function's description (row 7: native names, row 9/10: locations)
   * @return Function returns a String List containing all native names
   */
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

