package com.example.urbotanist.ui.Plant;

public class Plant {

    public String full_Name;
    public String name_genus;
    public String name_type;
    public String name_family;
    public String location_short;
    public String location_long;
    public String plant_native;
    public String name_common;
    public String life_form;

    public Plant(String genus, String type, String family, String location_short, String location_long, String plant_native, String name_common, String life_form) {
        name_genus = genus;
        name_type = type;
        name_family = family;
        this.location_short = location_short;
        this.location_long = location_long;
        this.plant_native = plant_native;
        this.name_common = name_common;
        this.life_form = life_form;

        Log.d("tag", genus + " " + type + " " + family + " " + location_short + " " + location_long + " " + plant_native + " " + name_common + " " + life_form);
        full_Name = genus + " " + type;

    }
}
