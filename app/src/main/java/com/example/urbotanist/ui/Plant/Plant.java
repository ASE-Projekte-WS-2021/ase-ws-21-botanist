package com.example.urbotanist.ui.Plant;

public class Plant {

    public String full_Name;
    public String name_genus;
    public String name_type;
    public String name_family;
    public String location;
    public String plant_native;
    public String name_common;
    public String lifeform;

    public Plant(String genus, String type, String family, String location) {
        name_genus = genus;
        name_type = type;
        name_family = family;
        full_Name = genus + " " + type + " " + family;
        this.location = location;

    }
}
