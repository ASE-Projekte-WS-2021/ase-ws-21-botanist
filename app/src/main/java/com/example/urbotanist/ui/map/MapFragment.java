package com.example.urbotanist.ui.map;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MapFragment extends CurrentScreenFragment implements OnMapReadyCallback, ActivityResultCallback {

    private final LatLng SOUTH_WEST_MAP_BORDER = new LatLng(48.992262952936514,12.089423798024654);
    private final LatLng NORTH_EAST_MAP_BORDER = new LatLng(48.995638443353734,12.093880958855152);
    private final float MIN_ZOOM_LEVEL = 13.314879f;
    private final float MAX_ZOOM_LEVEL = 19.351759f;
    private final LatLngBounds MAP_BOUNDS = new LatLngBounds(SOUTH_WEST_MAP_BORDER,NORTH_EAST_MAP_BORDER);

    private String TAG = "MapFragment";
    private MapViewModel mViewModel;
    private MarkerMaker mMarker;
    //private ImageViewTouch map;
    private GoogleMap map;
    private MapView mapView;
    private String location;
    private Button showUserPositionButton;

    public MapFragment(String location) {
            this.location = location;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        mapView = v.findViewById(R.id.google_map);
        mapView.onCreate(savedInstanceState);
        showUserPositionButton = v.findViewById(R.id.show_user_position_button);
        showUserPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationPermissions();
            }
        });
        mViewModel = new MapViewModel();
        mMarker = new MarkerMaker();

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
            setPlantLocation();
        }

        /*LatLng botanic_garden = new LatLng(48.993161, 12.090753);
        IconGenerator iconGen = new IconGenerator(getActivity());
        MarkerOptions mOp = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon("E")))
                .title("Blabla")
                .position(botanic_garden)
                .flat(true);
        map.addMarker(mOp);*/

        addInfoMarker();
    }

    private void addInfoMarker() {
        ArrayList<MarkerInfo> markerInfos = mMarker.getMarkerInfoArray();
        IconGenerator iconGen = new IconGenerator(getActivity());

        for (MarkerInfo info : markerInfos) {
            MarkerOptions mOp = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(info.getLocationName())))
                    .title(info.getAreaName())
                    .position(info.getLocation())
                    .flat(true);
            map.addMarker(mOp);
        }
    }


    private void initMap() {
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setLatLngBoundsForCameraTarget(MAP_BOUNDS);
        map.setMaxZoomPreference(MAX_ZOOM_LEVEL);
        map.setMinZoomPreference(MIN_ZOOM_LEVEL);
    }

    public void requestLocationPermissions(){
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null) {
            if ((ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED)) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            }
            //else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) && mainActivity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
            //}
            else {
                showUserPositionButton.setVisibility(View.VISIBLE);
                requestPermissionLauncher.launch(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
            }

        }
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

    private void setPlantLocation() {
        String areaType = location.substring(0,1);
        mViewModel.setPlantLocation(areaType);
    }

    @Override
    public void onActivityResult(Object result) {

    }

    @SuppressLint("MissingPermission")
    private ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
        if(permissions.get(Manifest.permission.ACCESS_COARSE_LOCATION) && permissions.get(Manifest.permission.ACCESS_FINE_LOCATION)){
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            showUserPositionButton.setVisibility(View.GONE);
            }
        else{
            showUserPositionButton.setVisibility(View.VISIBLE);
        }
    });
}