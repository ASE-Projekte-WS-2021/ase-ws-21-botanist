package com.example.urbotanist;

import android.app.Application;

import io.realm.Realm;

public class BotanistApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
    }
}
