package com.example.urbotanist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.urbotanist.database.DatabaseAdapterActivity;
import com.example.urbotanist.ui.Plant.Plant;

import java.util.ArrayList;

import io.realm.Realm;

public class StartupActivity extends AppCompatActivity {

  String DB_IS_SETUP_KEY = "DB_IS_SETUP";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_startup);
    SharedPreferences startupPreferences = getSharedPreferences("startupPreferences",
        Context.MODE_PRIVATE);
    if (!startupPreferences.getBoolean(DB_IS_SETUP_KEY, false)) {
      firstTimeSetupDB();
      startupPreferences.edit().putBoolean(DB_IS_SETUP_KEY, true).apply();
    }
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent intent = new Intent(StartupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
      }
    }, 1500);
  }


  // Creating a Realm Database from our SqlLite Database
  private void firstTimeSetupDB() {
    Realm realm = Realm.getDefaultInstance();
    DatabaseAdapterActivity mDbHelper = new DatabaseAdapterActivity(this);
    mDbHelper.createDatabase();
    mDbHelper.open();
    ArrayList<Plant> plants = mDbHelper.getSearchResult("");
    mDbHelper.close();
    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealm(plants);
      }
    });
    realm.close();

  }
}