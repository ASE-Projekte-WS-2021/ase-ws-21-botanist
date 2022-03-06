package com.example.urbotanist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.urbotanist.database.DatabaseAdapterActivity;
import com.example.urbotanist.ui.plant.Plant;
import io.realm.Realm;
import java.util.ArrayList;

public class StartupActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_startup);
    SharedPreferences startupPreferences = getSharedPreferences("startupPreferences",
        Context.MODE_PRIVATE);
    String dbIsSetupKey = "DB_IS_SETUP";
    if (!startupPreferences.getBoolean(dbIsSetupKey, false)) {
      firstTimeSetupDb();
      startupPreferences.edit().putBoolean(dbIsSetupKey, true).apply();
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
  private void firstTimeSetupDb() {

    DatabaseAdapterActivity dbHelper = new DatabaseAdapterActivity(this);
    dbHelper.createDatabase();
    dbHelper.open();
    ArrayList<Plant> plants = dbHelper.getSearchResult("");
    dbHelper.close();
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(@NonNull Realm realm) {
        realm.copyToRealm(plants);
      }
    });
    realm.close();

  }
}