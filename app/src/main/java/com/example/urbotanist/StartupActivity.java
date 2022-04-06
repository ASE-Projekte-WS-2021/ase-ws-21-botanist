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
import com.example.urbotanist.database.DatabaseAdapter;
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
          startNextActivity();
        }
      }, 1500);

    }
  }

  /**
   * Migrates the installed database to the version defined in schemaVersion(VERSION).
   */
  private void handleDataBaseMigration() {
    final RealmConfiguration configuration = new RealmConfiguration.Builder().name("default.realm")
        .schemaVersion(2).migration(new RealmMigrations()).build();
    Realm.setDefaultConfiguration(configuration);
    Realm.getInstance(configuration);
  }


  /**
   * Sets the the ending animation of the splashscreen visible.
   */
  private void startSplashscreenEnd() {
    splashscreen = findViewById(R.id.splashscreen);
    dataBaseSetupLoadingBar.setVisibility(View.GONE);
    databaseSetupText.setVisibility(View.GONE);
    splashscreen.setBackgroundResource(R.drawable.botanist_splashscreen_bigger_p2);
  }

  /**
   * Starts the next activity: MainActivty or OnboardingActivty(on first app start).
   */
  private void startNextActivity() {
    SharedPreferences startupPreferences = getSharedPreferences("startupPreferences",
        Context.MODE_PRIVATE);

    startSplashscreenEnd();
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent intent;
        if (!startupPreferences.getBoolean("ONBOARDING_SEEN", false)) {
          intent = new Intent(StartupActivity.this, OnboardingActivity.class);
        } else {
          intent = new Intent(StartupActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
      }
    }, 1500);
  }


  /**
   * Executes the Realm DB Setup as AsyncTask.
   */
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
      activityReference.get().startNextActivity();

    }

    /**
     * Realm Database Setup, by copying Objects from the SQLite database.
     */
    // Creating a Realm Database from our SqlLite Database
    private void firstTimeSetupDb() {

      DatabaseAdapter dbHelper = new DatabaseAdapter(activityReference.get());
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