package com.example.urbotanist.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
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
import java.util.ArrayList;


public class MapFragment extends CurrentScreenFragment implements OnMapReadyCallback,
    ActivityResultCallback {

  private final LatLng southWestMapBorder = new LatLng(48.992262952936514, 12.089423798024654);
  private final LatLng northEastMapBorder = new LatLng(48.995638443353734, 12.093880958855152);
  private final LatLngBounds mapBounds = new LatLngBounds(southWestMapBorder,
      northEastMapBorder);

  private MapViewModel mapViewModel;
  private MapMaker mapMaker;
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
    mapViewModel = new MapViewModel();
    mapMaker = new MapMaker();

    //create Map, TODO check permission
    mapView.getMapAsync(this);

    return v;
  }


  @Override
  public void onStart() {
    super.onStart();
    mapView.onStart();
    initGui();
    mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
  }

  public void initGui() {
    loadMap();
  }

  private void loadMap() {
    //old picture-map
    /*map = getView().findViewById(R.id.map);
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
    mapViewModel.initData(map);
    if (location != "") {
      setPlantLocation();
    }

    addInfoMarker();
  }

  private void addInfoMarker() {
    ArrayList<MarkerInfo> markerInfos = mapMaker.getMarkerInfoArray();
    IconGenerator iconGen = new IconGenerator(getActivity());

    for (MarkerInfo info : markerInfos) {
      MarkerOptions markerOptions = new MarkerOptions()
          .icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(info.getLocationName())))
          .title(info.getAreaName())
          .position(info.getLocation())
          .flat(true);
      map.addMarker(markerOptions);
    }
  }


  private void initMap() {
    map.getUiSettings().setZoomControlsEnabled(true);
    map.setLatLngBoundsForCameraTarget(mapBounds);
    float maxZoomLevel = 19.351759f;
    map.setMaxZoomPreference(maxZoomLevel);
    float minZoomLevel = 13.314879f;
    map.setMinZoomPreference(minZoomLevel);
  }

  public void requestLocationPermissions() {
    MainActivity mainActivity = (MainActivity) getActivity();
    if (mainActivity != null) {
      if ((ContextCompat.checkSelfPermission(
          getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
          == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
          getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
          == PackageManager.PERMISSION_GRANTED)) {
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
      } else {
        showUserPositionButton.setVisibility(View.VISIBLE);
        requestPermissionLauncher.launch(
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION});
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
    String areaType = location.substring(0, 1);
    mapViewModel.setPlantLocation(areaType);
  }

  @Override
  public void onActivityResult(Object result) {

  }

  @SuppressLint("MissingPermission")
  private final ActivityResultLauncher<String[]> requestPermissionLauncher =
      registerForActivityResult(
        new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
          if (permissions.get(Manifest.permission.ACCESS_COARSE_LOCATION) && permissions
              .get(Manifest.permission.ACCESS_FINE_LOCATION)) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            showUserPositionButton.setVisibility(View.GONE);
          } else {
            showUserPositionButton.setVisibility(View.VISIBLE);
          }
        });
}