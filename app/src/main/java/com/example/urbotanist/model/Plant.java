package com.example.urbotanist.model;

import android.util.Log;

import com.example.urbotanist.network.PlantApiHelper;
import com.google.gson.annotations.SerializedName;

public class Plant {
    @SerializedName("name")
    public String name;


    public Plant(){
        Log.d("test", "plant created");
    }
}
