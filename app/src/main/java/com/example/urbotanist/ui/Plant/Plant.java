package com.example.urbotanist.ui.Plant;

import android.util.Log;

public class Plant {

    public String fullName;
    public String genusName;
    public String typeName;
    public String familyName;
    public String location;
    public String location_long;
    public String plantNative;
    public String commonName;
    public String life_form;

    public Plant(String genus, String type, String family, String location_short, String location_long, String plant_native, String name_common, String life_form) {
        genusName = genus;
        typeName = type;
        familyName = family;
        this.location = location_short;
        this.location_long = location_long;
        plantNative = plant_native;
        this.commonName = name_common;
        this.life_form = life_form;

        //Log.d("taggrio", genus + " " + type + " " + family + " " + location_short + " " + location_long + " " + plant_native + " " + name_common + " " + life_form);
        fullName = genus + " " + type;

    }
}
