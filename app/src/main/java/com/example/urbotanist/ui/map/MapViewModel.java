package com.example.urbotanist.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.urbotanist.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class MapViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private GoogleMap map;
    private PolygonMaker polyMaker;

    private static final int COLOR_AREA_A = 0x80FDAD02;
    private static final int COLOR_AREA_B = 0x80FFFFBF;
    private static final int COLOR_AREA_E = 0x80CDAA66;
    private static final int COLOR_AREA_M = 0x8038A700;
    private static final int COLOR_AREA_N = 0x8C38A700;
    private static final int COLOR_AREA_S = 0x80C8D79E;
    private static final int COLOR_AREA_H = 0x9938A700;
    private static final int COLOR_AREA_G = 0x80FF7F7E;

    private List<PolygonOptions> polyOpList;
    private static final int POLY_STROKE_WIDTH = 6;
    private static final int INDEX_T_ONE = 17;
    private static final int INDEX_T_TWO = 18;
    private static final int INDEX_U_ONE = 19;
    private static final int INDEX_U_TWO = 20;
    private static final int INDEX_U_THREE = 21;


    public MapViewModel() {
        polyMaker = new PolygonMaker();
        polyOpList = polyMaker.getPolyOptions();
    }

   public void initData (GoogleMap googleMap) {
       map = googleMap;

       LatLng botanic_garden = new LatLng(48.993161, 12.090753);

       map.addMarker(new MarkerOptions()
               .position(botanic_garden)
               .title("Marker"));

       addPolygonsToMap();

       map.moveCamera(CameraUpdateFactory.newLatLngZoom(botanic_garden, 18));


    }

    private void addPolygonsToMap() {
        int size = polyOpList.size();
        for (int i = 0; i < size; i++) {
            Polygon polygon = map.addPolygon(polyOpList.get(i));
            polygon.setStrokeWidth(POLY_STROKE_WIDTH);
        }
    }

    public void setPlantLocation(String location){
        switch (location) {
            case "A":
                map.addPolygon(polyOpList.get(0).fillColor(COLOR_AREA_A));
                break;
            case "B":
                map.addPolygon(polyOpList.get(1).fillColor(COLOR_AREA_A));
                break;
            case "C":
                map.addPolygon(polyOpList.get(2).fillColor(COLOR_AREA_A));
                break;
            case "D":
                map.addPolygon(polyOpList.get(3).fillColor(COLOR_AREA_A));
                break;
            case "E":
                map.addPolygon(polyOpList.get(4).fillColor(COLOR_AREA_A));
                break;
            case "F":
                map.addPolygon(polyOpList.get(5).fillColor(COLOR_AREA_A));
                break;
            case "G":
                map.addPolygon(polyOpList.get(6).fillColor(COLOR_AREA_A));
                break;
            case "H":
                map.addPolygon(polyOpList.get(7).fillColor(COLOR_AREA_A));
                break;
            case "I":
                map.addPolygon(polyOpList.get(8).fillColor(COLOR_AREA_A));
                break;
            case "J":
                map.addPolygon(polyOpList.get(9).fillColor(COLOR_AREA_A));
                break;
            case "K":
                map.addPolygon(polyOpList.get(10).fillColor(COLOR_AREA_A));
                break;
            case "L":
                map.addPolygon(polyOpList.get(11).fillColor(COLOR_AREA_A));
                break;
            case "M":
                map.addPolygon(polyOpList.get(12).fillColor(COLOR_AREA_A));
                break;
            case "N":
                map.addPolygon(polyOpList.get(13).fillColor(COLOR_AREA_A));
                break;
            case "P":
                map.addPolygon(polyOpList.get(14).fillColor(COLOR_AREA_A));
                break;
            case "R":
                map.addPolygon(polyOpList.get(15).fillColor(COLOR_AREA_A));
                break;
            case "S":
                map.addPolygon(polyOpList.get(16).fillColor(COLOR_AREA_A));
                break;
            case "T":
                map.addPolygon(polyOpList.get(17).fillColor(COLOR_AREA_A));
                map.addPolygon(polyOpList.get(18).fillColor(COLOR_AREA_A));
                break;
            case "U":
                map.addPolygon(polyOpList.get(19).fillColor(COLOR_AREA_A));
                map.addPolygon(polyOpList.get(20).fillColor(COLOR_AREA_A));
                map.addPolygon(polyOpList.get(21).fillColor(COLOR_AREA_A));
                break;
            case "V":
                map.addPolygon(polyOpList.get(22).fillColor(COLOR_AREA_A));
                break;
            case "X":
                map.addPolygon(polyOpList.get(23).fillColor(COLOR_AREA_A));
                break;
            case "Y":
                map.addPolygon(polyOpList.get(24).fillColor(COLOR_AREA_A));
                break;
            case "Z":
                map.addPolygon(polyOpList.get(25).fillColor(COLOR_AREA_A));
                break;
        }

        //Polygon polygon = map.addPolygon(polyOpList.get(10));
        //polygon.setFillColor(COLOR_AREA_A);
        //Log.d("Farbe: ", location);
    }
}