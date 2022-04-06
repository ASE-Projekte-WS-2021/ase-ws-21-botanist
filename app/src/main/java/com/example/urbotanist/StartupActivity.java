package com.example.urbotanist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
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
import javax.xml.transform.Result;
import pl.droidsonroids.gif.GifImageView;

public class StartupActivity extends AppCompatActivity {

  private GifImageView splashscreen;
  String dbIsSetupKey = "DB_IS_SETUP";
  ProgressBar databaseSetupSpinner;
  TextView databaseSetupText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_startup);
    databaseSetupSpinner = findViewById(R.id.data_base_setup_spinner);
    databaseSetupText = findViewById(R.id.data_base_setup_text);
    setupSplashscreen();
    handleDataBaseMigration();
    SharedPreferences startupPreferences = getSharedPreferences("startupPreferences",
        Context.MODE_PRIVATE);

    boolean needsDbSetup;
    if (!startupPreferences.getBoolean(dbIsSetupKey, false)) {
      databaseSetupSpinner.setVisibility(View.VISIBLE);
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


  private void setupSplashscreen() {
    //setting up the splashscreen
    splashscreen = findViewById(R.id.splashscreen);
    splashscreen.setVisibility(View.VISIBLE);
  }

  private void startMainActivity() {
    SharedPreferences startupPreferences = getSharedPreferences("startupPreferences", Context.MODE_PRIVATE);
    //startupPreferences.getBoolean("ONBOARDING_SEEN", false);

    if(!startupPreferences.getBoolean("ONBOARDING_SEEN", false)) {
      startActivity(new Intent(this, OnboardingActivity.class));
    } else {
      Intent intent = new Intent(StartupActivity.this, MainActivity.class);
      startActivity(intent);
      finish();
    }
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