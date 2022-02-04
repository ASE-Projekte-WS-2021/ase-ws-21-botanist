package com.example.urbotanist.ui.Plant;

public class Plant {

    public String fullName;
    public String genusName;
    public String typeName;
    public String familyName;
    public String location;
    public String plantNative;
    public String commonName;
    public String lifeForm;

    public Plant(String genus, String type, String family, String location, String plantNative, String commonName, String lifeForm) {
        genusName = genus;
        typeName = type;
        familyName = family;
        this.location = location;
        this.plantNative = plantNative;
        this.commonName = commonName;
        this.lifeForm = lifeForm;

        //Log.d("tag", genus + " " + type + " " + family + " " + location  + " " + plant_native + " " + name_common + " " + life_form);
        fullName = genus + " " + type;

    }
}
