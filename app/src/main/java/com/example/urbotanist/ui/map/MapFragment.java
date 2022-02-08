package com.example.urbotanist.ui.map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class MapFragment extends CurrentScreenFragment implements OnMapReadyCallback {

    private MapViewModel mViewModel;
    //private ImageViewTouch map;
    private GoogleMap map;
    private MapView mapView;
    private String location;

    public MapFragment(String location) {
            this.location = location;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        mapView = (MapView) v.findViewById(R.id.google_map);
        mapView.onCreate(savedInstanceState);

        mViewModel = new MapViewModel();

        //create Map, TODO check permission
        mapView.getMapAsync(this);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        initGUI();
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
    }

    public void initGUI() {
        loadMap();
    }

    private void loadMap() {
        //old picture-map
        /**map = getView().findViewById(R.id.map);
         map.setImageResource(R.drawable.garden_map);
         map.setDisplayType(ImageViewTouchBase.DisplayType.FIT_HEIGHT);
         map.setScrollEnabled(true);
         map.setQuickScaleEnabled(true);*/

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        //setup map and get permissions
        initMap();

        //setup polygons
        mViewModel.initData(map);
        if (location != "") {
            setPlantLocation(location);
        }
    }

    private void initMap() {
        map.getUiSettings().setZoomControlsEnabled(true);

        //get user location
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void setPlantLocation(String location) {
        mViewModel.setPlantLocation(this.location);
    }
}