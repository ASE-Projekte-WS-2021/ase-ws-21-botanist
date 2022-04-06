package com.example.urbotanist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.urbotanist.database.DatabaseAdapterActivity;
import com.example.urbotanist.database.RealmMigrations;
import com.example.urbotanist.drawerfragments.plant.Plant;
// realm by MongoDB, https://realm.io/
import io.realm.Realm;
import io.realm.RealmConfiguration;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.GifTextView;

public class StartupActivity extends AppCompatActivity {

  private GifTextView splashscreen;
  String dbIsSetupKey = "DB_IS_SETUP";
  GifImageView dataBaseSetupLoadingBar;
  TextView databaseSetupText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_startup);
    dataBaseSetupLoadingBar = findViewById(R.id.data_base_setup_loadingbar);
    databaseSetupText = findViewById(R.id.data_base_setup_text);
    handleDataBaseMigration();
    SharedPreferences startupPreferences = getSharedPreferences("startupPreferences",
        Context.MODE_PRIVATE);

    boolean needsDbSetup;
    if (!startupPreferences.getBoolean(dbIsSetupKey, false)) {
      dataBaseSetupLoadingBar.setVisibility(View.VISIBLE);
      databaseSetupText.setVisibility(View.VISIBLE);
      needsDbSetup = true;
      new DbSetupBackgroundTask(this).execute();

    } else {
      needsDbSetup = false;
    }
    if (!needsDbSetup) {
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          startMainActivity();
        }
      }, 1500);

    }
  }

  private void handleDataBaseMigration() {
    final RealmConfiguration configuration = new RealmConfiguration.Builder().name("default.realm")
        .schemaVersion(2).migration(new RealmMigrations()).build();
    Realm.setDefaultConfiguration(configuration);
    Realm.getInstance(configuration);
  }


  private void startSplashscreenEnd() {
    splashscreen = findViewById(R.id.splashscreen);
    dataBaseSetupLoadingBar.setVisibility(View.GONE);
    databaseSetupText.setVisibility(View.GONE);
    splashscreen.setBackgroundResource(R.drawable.botanist_splashscreen_bigger_p2);
  }

  private void startMainActivity() {
    SharedPreferences startupPreferences = getSharedPreferences("startupPreferences",
        Context.MODE_PRIVATE);
    //startupPreferences.getBoolean("ONBOARDING_SEEN", false);
    startSplashscreenEnd();
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (!startupPreferences.getBoolean("ONBOARDING_SEEN", false)) {
          startActivity(new Intent(StartupActivity.this, OnboardingActivity.class));
        } else {
          Intent intent = new Intent(StartupActivity.this, MainActivity.class);
          startActivity(intent);
          finish();
        }
      }
    }, 1500);
  }


  private static class DbSetupBackgroundTask extends AsyncTask<Void, Void, String> {

    private final WeakReference<StartupActivity> activityReference;

    DbSetupBackgroundTask(StartupActivity context) {

      activityReference = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(final Void... params) {
      firstTimeSetupDb();

      SharedPreferences startupPreferences = activityReference.get()
          .getSharedPreferences("startupPreferences",
              Context.MODE_PRIVATE);
      startupPreferences.edit().putBoolean(activityReference.get().dbIsSetupKey, true).apply();

      return null;
    }

    @Override
    protected void onPostExecute(String backgroundResult) {
      activityReference.get().startMainActivity();

    }

    // Creating a Realm Database from our SqlLite Database
    private void firstTimeSetupDb() {

      DatabaseAdapterActivity dbHelper = new DatabaseAdapterActivity(activityReference.get());
      dbHelper.createDatabase();
      dbHelper.open();
      ArrayList<Plant> plants = dbHelper.getSearchResult("");
      dbHelper.close();
      Realm realm = Realm.getDefaultInstance();
      realm.executeTransactionAsync(new Realm.Transaction() {
        @Override
        public void execute(@NonNull Realm realm) {
          realm.deleteAll(); // delete incomplete setup from possible previous tries
          realm.copyToRealm(plants);
        }
      });
      realm.close();

    }
  }
}