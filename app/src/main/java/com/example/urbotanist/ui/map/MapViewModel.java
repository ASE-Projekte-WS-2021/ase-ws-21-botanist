package com.example.urbotanist.ui.map;

import androidx.lifecycle.ViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class MapViewModel extends ViewModel {

  private GoogleMap map;
  private MapMarkerMaker mapMaker;
  private IconGenerator iconGen;

  private static final int currentAreaColorCode = 0x80FDAD02;
  /*
  private static final int COLOR_AREA_B = 0x80FFFFBF;
  private static final int COLOR_AREA_E = 0x80CDAA66;
  private static final int COLOR_AREA_M = 0x8038A700;
  private static final int COLOR_AREA_N = 0x8C38A700;
  private static final int COLOR_AREA_S = 0x80C8D79E;
  private static final int COLOR_AREA_H = 0x9938A700;
  private static final int COLOR_AREA_G = 0x80FF7F7E;
   */

  private final HashMap<String, ArrayList<PolygonOptions>> polyOpList;
  private static final int POLY_STROKE_WIDTH = 6;


  public MapViewModel(IconGenerator iconGen) {
    this.iconGen = iconGen;
    PolygonMaker polyMaker = new PolygonMaker();
    polyOpList = polyMaker.getPolyOptions();
    mapMaker = new MapMarkerMaker();
  }

  public void initData(GoogleMap googleMap) {
    map = googleMap;

    addPolygonsToMap();
    LatLng botanicGarden = new LatLng(48.993161, 12.090753);
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(botanicGarden, 18));


  }

  public void initInfoMarker() {
    ArrayList<MarkerInfo> markerInfos = mapMaker.getMarkerInfoArray();

    for (MarkerInfo info : markerInfos) {
      MarkerOptions markerOptions = new MarkerOptions()
              .icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(info.getLocationName())))
              .title(info.getAreaName())
              .position(info.getLocation())
              .visible(false)
              .flat(true);
      map.addMarker(markerOptions);
    }
  }

  private void addPolygonsToMap() {
    for (ArrayList<PolygonOptions> polygonList : polyOpList.values()) {
      for (PolygonOptions polygonOption : polygonList) {
        Polygon polygon = map.addPolygon(polygonOption);
        polygon.setStrokeWidth(POLY_STROKE_WIDTH);
      }
    }
  }

  public void setPlantLocation(String location) {
    if (polyOpList.get(location) != null) {
      for (PolygonOptions polygonOptions : Objects.requireNonNull(polyOpList.get(location))) {
        map.addPolygon(polygonOptions.fillColor(currentAreaColorCode));
      }
    }
  }
}