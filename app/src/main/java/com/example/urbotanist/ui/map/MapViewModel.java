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

    public MapViewModel() {
        polyMaker = new PolygonMaker();
        polyOpList = polyMaker.getPolyOptions();
    }

   public void initData (GoogleMap googleMap) {
       /**set marker
       LatLng botanic_garden = new LatLng(48.993161, 12.090753);

       Polygon area = map.addPolygon(polyOpA);
       area.setFillColor(0x80C8D79E);**/
       map = googleMap;

       LatLng botanic_garden = new LatLng(48.993161, 12.090753);

       map.addMarker(new MarkerOptions()
               .position(botanic_garden)
               .title("Marker"));

       addPolygonsToMap();
       
       map.moveCamera(CameraUpdateFactory.newLatLngZoom(botanic_garden, 18));


    }

    private void addPolygonsToMap() {
        int test = polyOpList.size();
        for (int i = 0; i < test; i++) {
            map.addPolygon(polyOpList.get(i));
        }
    }
}