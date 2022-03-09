package com.example.urbotanist.ui.map;

import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.ui.IconGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class MapViewModel extends ViewModel {

  private GoogleMap map;
  private MapMarkerMaker mapMaker;
  private IconGenerator iconGen;
  ArrayList<Marker> markerList = new ArrayList<Marker>();

  private static final int SELECTED_AREA_FILL_COLOR = 0x80FA6E6E;
  private static final int SELECTED_AREA_BORDER_COLOR = Color.parseColor("#FA6E6E");
  /*
  private static final int COLOR_AREA_A = 0x90FDAD02;
  private static final int COLOR_AREA_B = 0x80FFFFBF;
  private static final int COLOR_AREA_E = 0x80CDAA66;
  private static final int COLOR_AREA_M = 0x8038A700;
  private static final int COLOR_AREA_N = 0x8C38A700;
  private static final int COLOR_AREA_S = 0x80C8D79E;
  private static final int COLOR_AREA_H = 0x9938A700;
  private static final int COLOR_AREA_G = 0x80FF7F7E;
   */

  private ArrayList<Polygon> polygonList = new ArrayList<>();
  //private final HashMap<String, ArrayList<PolygonOptions>> polyOpList;
  private final ArrayList<PolygonInfo> polyInfoList;
  private static final int POLY_STROKE_WIDTH = 6;
  private static final int POLY_STROKE_WIDTH_PLANT_SELECTED = 18;


  public MapViewModel(IconGenerator iconGen) {
    this.iconGen = iconGen;
    PolygonMaker polyMaker = new PolygonMaker();
    //polyOpList = polyMaker.getPolyOptions();
    polyInfoList = polyMaker.getPolyInfoList();
    mapMaker = new MapMarkerMaker();
  }

  public void initData(GoogleMap googleMap) {
    map = googleMap;

    addPolygonsToMap();
    LatLng botanicGarden = new LatLng(48.993161, 12.090753);
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(botanicGarden, 18));


  }

  public void initInfoMarker() {
    ArrayList<MarkerInfo> markerInfoList = mapMaker.getMarkerInfoArray();

    for (MarkerInfo info : markerInfoList) {
      Marker infoMarker = map.addMarker(new MarkerOptions()
              .icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(info.getLocationName())))
              .title(info.getAreaName())
              .position(info.getLocation())
              .visible(false)
              .flat(true));
      markerList.add(infoMarker);
    }
  }

  public void toggleMarker() {
    for (Marker marker : markerList) {
      if (marker.isVisible()) {
        marker.setVisible(false);
      } else {
        marker.setVisible(true);
      }
    }
  }

  private void addPolygonsToMap() {
    //for (ArrayList<PolygonOptions> polygonOptList : polyOpList.values()) {
    for (PolygonInfo polygonInfo : polyInfoList) {
      ArrayList<PolygonOptions> polygonOptList = polygonInfo.getPolyOpList();
      for (PolygonOptions polygonOption : polygonOptList) {
        Polygon polygon = map.addPolygon(polygonOption);
        polygon.setStrokeWidth(POLY_STROKE_WIDTH);
        polygonList.add(polygon);
      }
    }
  }
/*
  public void setPlantLocation(String location) {
    if (polyOpList.get(location) != null) {
      for (PolygonOptions polygonOptions : Objects.requireNonNull(polyOpList.get(location))) {
        map.addPolygon(polygonOptions.fillColor(currentAreaColorCode).strokeWidth(POLY_STROKE_WIDTH_PLANT_SELECTED));
        currentUserArea(new LatLng(48.9933, 12.0908));
      }
    }
  }*/

  public void setPlantLocation(String location) {
    for (PolygonInfo polyInfo : polyInfoList) {
      if (polyInfo.getAreaName() != null) {
        if (polyInfo.getAreaName().equals(location)) {
          for (PolygonOptions polyOp : polyInfo.getPolyOpList()) {
            map.addPolygon(polyOp.fillColor(SELECTED_AREA_FILL_COLOR)
                    .strokeWidth(POLY_STROKE_WIDTH_PLANT_SELECTED)
                    .strokeColor(SELECTED_AREA_BORDER_COLOR));
          }
        }
      }
    }
  }

  //TODO finish return statement
  public String currentUserArea(LatLng currentUserLocation) {
    for (int i = 0; i < polygonList.size(); i++) {
      if (PolyUtil.containsLocation(currentUserLocation,polygonList.get(i).getPoints(), true)) {
        return ("A");
      };
    }
    return ("");

  }
}