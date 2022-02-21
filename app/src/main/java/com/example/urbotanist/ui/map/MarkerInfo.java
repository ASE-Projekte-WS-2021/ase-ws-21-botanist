package com.example.urbotanist.ui.map;

import com.google.android.gms.maps.model.LatLng;

public class MarkerInfo {

    private String locationName;
    private LatLng location;
    private String areaName;

    public MarkerInfo(String locationName, LatLng location, String areaName) {
        this.locationName = locationName;
        this.location = location;
        this.areaName = areaName;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public LatLng getLocation() {
        return this.location;
    }

    public String getAreaName() {
        return this.areaName;
    }
}
