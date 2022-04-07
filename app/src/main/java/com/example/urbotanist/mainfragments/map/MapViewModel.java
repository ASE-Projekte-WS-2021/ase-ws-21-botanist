package com.example.urbotanist.mainfragments.map;

import android.graphics.Color;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
// Google Maps by Google, https://developers.google.com/maps
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.ui.IconGenerator;
import java.util.ArrayList;
import java.util.HashMap;

public class MapViewModel extends ViewModel {

  private GoogleMap map;
  private IconGenerator iconGen;
  private ArrayList<Marker> markerList = new ArrayList<Marker>();

  private static final int SELECTED_AREA_FILL_COLOR = 0x80FA6E6E;
  private static final int SELECTED_AREA_BORDER_COLOR = Color.parseColor("#FA6E6E");
  private static final int DEFAULT_POLYGON_Z_INDEX = 1;
  private static final int SELECTED_AREA_Z_INDEX = 2;
  private static final int POLY_STROKE_WIDTH = 6;
  private static final int POLY_STROKE_WIDTH_PLANT_SELECTED = 18;
  private static final int DEFAULT_ZOOM = 17;
  private static final int SELECTED_AREA_ZOOM = 18;

  private ArrayList<Polygon> polygonList = new ArrayList<>();
  private final HashMap<String, ArrayList<Polygon>> polyHashMap = new HashMap<>();
  private final HashMap<Polygon, String> reversedPolyHashMap = new HashMap<>();
  private final ArrayList<PolygonInfo> polyInfoList;

  /**
   * @param iconGen gets icon generator for custom marker icons
   *                instantiates area polygons and sets them in a ArrayList
   */
  public MapViewModel(IconGenerator iconGen) {
    this.iconGen = iconGen;
    PolygonMaker polyMaker = new PolygonMaker();
    polyInfoList = polyMaker.getPolyInfoList();
  }

  /**
   * @param googleMap gets GoogleMap object as soon as map is ready
   *        adds polygons to map and sets zoom to the middle of the garden
   */
  public void initData(GoogleMap googleMap) {
    map = googleMap;

    addPolygonsToMap();
    //circa middle of botanic garden for start zoom
    LatLng botanicGarden = new LatLng(48.993405, 12.091553);
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(botanicGarden, DEFAULT_ZOOM));

  }

  /**
   * instantiates all info markers for each area
   */
  public void initInfoMarker() {
    ArrayList<MarkerInfo> markerInfoList = MapMarkerSetup.setupMarkerCoordinatesAndNames();
    iconGen.setStyle(IconGenerator.STYLE_DEFAULT);

    for (MarkerInfo info : markerInfoList) {
      Marker infoMarker = map.addMarker(new MarkerOptions()
          .icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(info.getAreaTag())))
          .title(info.getAreaName())
          .position(info.getLocation())
          .visible(true)
          .flat(true));
      infoMarker.setTag(info.getAreaTag());
      markerList.add(infoMarker);
    }
  }

  /**
   * when toggle button is pressed this method will be called and toggles visibility of markers
   */
  public void toggleMarkerVisibility() {
    for (Marker marker : markerList) {
      if (marker.isVisible()) {
        marker.setVisible(false);
      } else {
        marker.setVisible(true);
      }
    }
  }

  /**
   * adds all polygons to map
   * map.addPolygon returns polygon, which will be set in a hashmap (area name as key)
   * a reversed hashmap is made, with polygon as key for search purposes
   */
  private void addPolygonsToMap() {
    for (PolygonInfo polygonInfo : polyInfoList) {
      ArrayList<PolygonOptions> polygonOptList = polygonInfo.getPolyOpList();
      for (PolygonOptions polygonOption : polygonOptList) {
        Polygon polygon = map.addPolygon(polygonOption);
        polygon.setStrokeWidth(POLY_STROKE_WIDTH);
        polygon.setZIndex(DEFAULT_POLYGON_Z_INDEX);
        polygonList.add(polygon);
        //check if polyHashMap already has a entry with area, else extend list
        if (polyHashMap.containsKey(polygonInfo.getAreaName())) {
          polyHashMap.get(polygonInfo.getAreaName()).add(polygon);
        } else {
          polyHashMap.put(polygonInfo.getAreaName(), new ArrayList<Polygon>());
          polyHashMap.get(polygonInfo.getAreaName()).add(polygon);
        }
        reversedPolyHashMap.put(polygon, polygonInfo.getAreaName());
      }
    }
  }

  /**
   * @param location gets a one letter location string
   *                 searches in reversedPolyHashMap for all polygons with this location
   *                 and highlights them with red color
   *                 also calls findPolygonCenter for zoom purposes
   */
  public void setPlantArea(String location) {
    for (String area : reversedPolyHashMap.values()) {
      if (area.equals(location)) {
        ArrayList<Polygon> polygonList = polyHashMap.get(area);
        for (Polygon polygon : polygonList) {
          polygon.setFillColor(SELECTED_AREA_FILL_COLOR);
          polygon.setStrokeWidth(POLY_STROKE_WIDTH_PLANT_SELECTED);
          polygon.setStrokeColor(SELECTED_AREA_BORDER_COLOR);
          findPolygonCenter(polygon);
          polygon.setZIndex(SELECTED_AREA_Z_INDEX);
        }
      }
    }
  }

  /**
   * @param polygon gets a Polygon, which should be zoomed at
   *                LatLngBounds.Builder takes all LatLngs from polygon
   *                and finds centerpoint, which is also a LatLng value
   *
   *                GoogleMap object then zooms at this center
   */
  private void findPolygonCenter(Polygon polygon) {
    LatLng centerPoint;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    // puts all LatLngs from polygon into boundsbuilder
    for (LatLng point : polygon.getPoints()) {
      builder.include(point);
    }
    LatLngBounds bounds = builder.build();
    centerPoint = bounds.getCenter();
    //zoom to selected area
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, SELECTED_AREA_ZOOM));

  }

  /**
   * method to highlight marker in area, in which user is located
   *
   * @param currentUserLocation as a LatLng, contains current user location
   *
   *   sets Highlight to default for 'not in polygon' case
   *   checks if user Position is in any area and highlights all the area markers
   */
   public void highlightMarker(LatLng currentUserLocation) {
    setMarkerDefault();
    String currentUserArea = "";
    for (ArrayList<Polygon> polygonList : polyHashMap.values()) {
      for (Polygon polygon : polygonList) {
        if (PolyUtil.containsLocation(currentUserLocation, polygon.getPoints(), true)) {
          currentUserArea = reversedPolyHashMap.get(polygon);
        }
      }
    }
    if (!currentUserArea.equals("")) {
      for (Marker marker : markerList) {
        setMarkerHighlight(marker, marker.getTag().toString().contains(currentUserArea));
      }
    }
  }

  /**
   * all markers will be set to default look
   */
  private void setMarkerDefault() {
    for (Marker marker : markerList) {
      setMarkerHighlight(marker, false);
    }
  }

  /**
   * @param marker Marker, which should be highlighted
   * @param highlight true: marker gets highlighted, false: default look
   */
  private void setMarkerHighlight(Marker marker, boolean highlight) {
    if (highlight) {
      iconGen.setStyle(IconGenerator.STYLE_GREEN);
    } else {
      iconGen.setStyle(IconGenerator.STYLE_DEFAULT);
    }
    marker.setIcon(BitmapDescriptorFactory
            .fromBitmap(iconGen.makeIcon(marker.getTag().toString())));
  }
}