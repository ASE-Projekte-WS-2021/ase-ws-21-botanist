package com.example.urbotanist.drawerfragments.plant;

// realm by MongoDB, https://realm.io/
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
  public RealmList<String> location;
  public RealmList<String> locationLong;
  public String plantNative;
  public RealmList<String> commonName;
  public String lifeForm;
  public boolean isFavored;
  public String link;

  public Plant() {

  }

  public Plant(int id, String genus, String type, String family, RealmList<String> locationShort,
      RealmList<String> locationLong, String plantNative, RealmList<String> nameCommon,
      String lifeForm, boolean isFavored) {
    this.id = id;
    genusName = genus;
    typeName = type;
    familyName = family;
    this.location = locationShort;
    this.locationLong = locationLong;
    this.plantNative = plantNative;
    this.commonName = nameCommon;
    this.lifeForm = lifeForm;
    this.isFavored = isFavored;
    fullName = genus + " " + type;
    link = getPlantLink(fullName);


  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setGenusName(String genusName) {
    this.genusName = genusName;
  }

  public String getGenusName() {
    return genusName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setLocation(RealmList<String> location) {
    this.location = location;
  }

  public RealmList<String> getLocation() {
    return location;
  }

  public void setLocationLong(RealmList<String> locationLong) {
    this.locationLong = locationLong;
  }

  public RealmList<String> getLocationLong() {
    return locationLong;
  }

  public void setPlantNative(String plantNative) {
    this.plantNative = plantNative;
  }

  public String getPlantNative() {
    return plantNative;
  }

  public void setCommonName(RealmList<String> commonName) {
    this.commonName = commonName;
  }

  public RealmList<String> getCommonName() {
    return commonName;
  }

  public void setLifeForm(String lifeForm) {
    this.lifeForm = lifeForm;
  }

  public String getLifeForm() {
    return lifeForm;
  }

  public boolean isFavored() {
    return isFavored;
  }

  public void setFavored(boolean favored) {
    isFavored = favored;
  }

  public String getPlantLink(String fullName) {
    String name =  fullName.replaceAll(" ", "_");
    return "https://en.wikipedia.org/w/index.php?title=" + name;
  }

}

