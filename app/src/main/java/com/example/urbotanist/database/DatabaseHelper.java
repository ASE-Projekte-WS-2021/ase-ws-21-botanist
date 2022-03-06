package com.example.urbotanist.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DatabaseHelper extends SQLiteOpenHelper {

  private Context context;
  private SQLiteDatabase dataBase;
  private final File dbFile;
  public static final String DATABASE_NAME = "database.sqlite";
  public static final int DATABASE_VERSION = 1;

  public static String TAG = "DATABASEACTIVITY";

  public DatabaseHelper(@Nullable Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    dbFile = context.getDatabasePath(DATABASE_NAME);
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
    boolean dataBaseExists = checkDataBase();
    if (!dataBaseExists) {
      this.getReadableDatabase();
      this.close();
      try {
        // Copy the database from assests
        copyDataBase();
        //joinDataBase();
      } catch (IOException ioException) {
        throw new Error("ErrorCopyingDataBase");
      }
    }
  }

  // Check that the database file exists in databases folder
  private boolean checkDataBase() {
    return dbFile.exists();
  }

  private void copyDataBase() throws IOException {
    InputStream inputStream = context.getAssets().open(DATABASE_NAME);
    OutputStream outputStream = new FileOutputStream(dbFile);
    byte[] buffer = new byte[1024];
    int length;
    while ((length = inputStream.read(buffer)) > 0) {
      outputStream.write(buffer, 0, length);
    }
    outputStream.flush();
    outputStream.close();
    inputStream.close();
  }

  public boolean openDataBase() throws SQLException {
    dataBase = SQLiteDatabase
        .openDatabase(dbFile.getPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
    return dataBase != null;
  }

  @Override
  public synchronized void close() {
    if (dataBase != null) {
      dataBase.close();
    }
    super.close();
  }
}
