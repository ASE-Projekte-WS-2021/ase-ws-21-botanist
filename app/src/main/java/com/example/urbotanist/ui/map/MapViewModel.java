package com.example.urbotanist.ui.map;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.HashMap;


public class MapViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private String TAG = "MAPVIEWMODEL";
    private GoogleMap map;
    private PolygonMaker polyMaker;

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

    private HashMap<String,ArrayList<PolygonOptions>> polyOpList;
    private static final int POLY_STROKE_WIDTH = 6;
    /*
    private static final int INDEX_T_ONE = 17;
    private static final int INDEX_T_TWO = 18;
    private static final int INDEX_U_ONE = 19;
    private static final int INDEX_U_TWO = 20;
    private static final int INDEX_U_THREE = 21;
     */


    public MapViewModel() {
        polyMaker = new PolygonMaker();
        polyOpList = polyMaker.getPolyOptions();
    }

   public void initData (GoogleMap googleMap) {
       map = googleMap;
       
       addPolygonsToMap();
       LatLng botanic_garden = new LatLng(48.993161, 12.090753);
       map.moveCamera(CameraUpdateFactory.newLatLngZoom(botanic_garden, 18));


    }

    private void addPolygonsToMap() {
        for (ArrayList<PolygonOptions> polygonList : polyOpList.values()){
            for(PolygonOptions polygonOption : polygonList) {
                Polygon polygon = map.addPolygon(polygonOption);
                polygon.setStrokeWidth(POLY_STROKE_WIDTH);
            }
        }
    }

    public void setPlantLocation(String location){
        if(polyOpList.get(location) != null) {
            for (PolygonOptions polygonOptions : polyOpList.get(location)) {
                map.addPolygon(polygonOptions.fillColor(currentAreaColorCode));
            }
        }else{
            Log.e(TAG, "plant location not found in "+TAG);
        }
    }
}