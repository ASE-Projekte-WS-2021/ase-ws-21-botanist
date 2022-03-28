package com.example.urbotanist;

import android.app.Application;

// realm by MongoDB, https://realm.io/
import io.realm.Realm;

public class BotanistApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);
  }
}
