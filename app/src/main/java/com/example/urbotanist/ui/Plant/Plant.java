package com.example.urbotanist.ui.Plant;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Plant extends RealmObject {

    @PrimaryKey
    public int id;
    public String fullName;
    public String genusName;
    public String typeName;
    public String familyName;
    public RealmList<String>  location;
    public RealmList<String>  locationLong;
    public String plantNative;
    public RealmList<String> commonName;
    public String lifeForm;

    public Plant(){

    };

    public Plant(int id, String genus, String type, String family, RealmList<String>  location_short, RealmList<String>  locationLong, String plant_native, RealmList<String> name_common, String lifeForm) {
        this.id = id;
        genusName = genus;
        typeName = type;
        familyName = family;
        this.location = location_short;
        this.locationLong = locationLong;
        plantNative = plant_native;
        this.commonName = name_common;
        this.lifeForm = lifeForm;

        //Log.d("taggrio", genus + " " + type + " " + family + " " + location_short + " " + location_long + " " + plant_native + " " + name_common + " " + life_form);
        fullName = genus + " " + type;

    }

    public void setFullName(String fullName){
        this.fullName =  fullName;
    }

    public String getFullName(){
        return fullName;
    }

    public void setGenusName(String genusName){
        this.genusName =  genusName;
    }
    public String getGenusName(){
        return genusName;
    }

    public void setTypeName(String typeName){
        this.typeName =  typeName;
    }
    public String getTypeName(){
        return typeName;
    }

    public void setFamilyName(String familyName){
        this.familyName =  familyName;
    }
    public String getFamilyName(){
        return familyName;
    }

    public void setLocation(RealmList<String> location){
        this.location =  location;
    }
    public RealmList<String>  getLocation(){
        return location;
    }

    public void setLocationLong(RealmList<String>  locationLong){
        this.locationLong = locationLong;
    }
    public RealmList<String>  getLocationLong(){
        return locationLong;
    }

    public void setPlantNative(String plantNative){
        this.plantNative =  plantNative;
    }
    public String getPlantNative(){
        return plantNative;
    }

    public void setCommonName(RealmList<String>  commonName){
        this.commonName =  commonName;
    }
    public RealmList<String>  getCommonName(){ return commonName; }

    public void setLifeForm(String life_form){
        this.lifeForm =  life_form;
    }
    public String getLifeForm(){
        return lifeForm;
    }
}
