package com.example.urbotanist.ui.map;

// Google Maps by Google, https://developers.google.com/maps
import com.google.android.gms.maps.model.LatLng;

public class MarkerInfo {

  private final String areaTag;
  private final LatLng location;
  private final String areaName;

  public MarkerInfo(String areaTag, LatLng location, String areaName) {
    this.areaTag = areaTag;
    this.location = location;
    this.areaName = areaName;
  }

  public String getAreaTag() {
    return this.areaTag;
  }

  public LatLng getLocation() {
    return this.location;
  }

  public String getAreaName() {
    return this.areaName;
  }
}
