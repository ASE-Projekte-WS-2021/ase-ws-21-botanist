package com.example.urbotanist.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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
import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;
import com.example.urbotanist.ui.area.Area;
import com.example.urbotanist.ui.area.AreaSelectListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.ui.IconGenerator;


public class MapFragment extends CurrentScreenFragment implements OnMapReadyCallback,
    ActivityResultCallback {

  private static final LatLng SW_MAP_BORDER = new LatLng(48.992262952936514, 12.089423798024654);
  private static final LatLng NE_MAP_BORDER = new LatLng(48.995638443353734, 12.093880958855152);
  private static final LatLngBounds mapBounds = new LatLngBounds(SW_MAP_BORDER,
      NE_MAP_BORDER);
  private static final float MAX_ZOOM_LEVEL = 19.351759f;
  private static final float MIN_ZOOM_LEVEL = 13.314879f;

  private MapViewModel mapViewModel;
  private GoogleMap map;
  private MapView mapView;
  private String plantLocation;
  private Button showUserPositionButton;
  private MarkerInfoClickListener markerInfoClickListener;

  public MapFragment(String plantLocation) {
    this.plantLocation = plantLocation;
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
    IconGenerator iconGen = new IconGenerator(getActivity());
    mapViewModel = new MapViewModel(iconGen);

    //create Map
    mapView.getMapAsync(this);

    return v;
  }


  @Override
  public void onStart() {
    super.onStart();
    mapView.onStart();
    //TODO error
    //"Cannot create an instance of class com.example.urbotanist.ui.map.MapViewModel"
    //mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
  }


  @Override
  public void onMapReady(@NonNull GoogleMap googleMap) {
    map = googleMap;
    //setup map and get permissions
    initMap();

    //setup polygons
    mapViewModel.initData(map);
    if (plantLocation != "") {
      setPlantLocation();
    }

    mapViewModel.initInfoMarker();
    mapViewModel.setShowMarker(true);

    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
      @Override
      public void onInfoWindowClick(@NonNull Marker marker) {
        if (markerInfoClickListener != null) {
          Area area = new Area(marker.getTag().toString().substring(0,1), marker.getTitle());
          markerInfoClickListener.onMarkerInfoClicked(area);
        }
      }
    });
  }


  private void initMap() {
    map.getUiSettings().setZoomControlsEnabled(true);
    map.setLatLngBoundsForCameraTarget(mapBounds);
    map.setMaxZoomPreference(MAX_ZOOM_LEVEL);
    map.setMinZoomPreference(MIN_ZOOM_LEVEL);
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

  public void setMarkerInfoClickListener(MarkerInfoClickListener listener) {
    this.markerInfoClickListener = listener;
  }

  public void highlightUserAreaMarker(LatLng currentUserLocation) {
    mapViewModel.highlightMarker(currentUserLocation);
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
    String areaType = plantLocation.substring(0, 1);
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