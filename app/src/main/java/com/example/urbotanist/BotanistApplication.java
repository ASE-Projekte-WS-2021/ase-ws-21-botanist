package com.example.urbotanist;

import android.app.Application;
import android.content.Context;
// realm by MongoDB, https://realm.io/
import io.realm.Realm;

public class BotanistApplication extends Application {

  public static Context context;

  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);
    context = getApplicationContext();
  }
}
