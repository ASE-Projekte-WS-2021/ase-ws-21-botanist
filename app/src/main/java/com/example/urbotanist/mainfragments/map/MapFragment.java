package com.example.urbotanist.mainfragments.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.drawerfragments.area.Area;
import com.example.urbotanist.mainfragments.CurrentScreenFragment;
// Google Maps by Google, https://developers.google.com/maps
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.ui.IconGenerator;
import java.util.Objects;


public class MapFragment extends CurrentScreenFragment implements
    OnMapReadyCallback,
    ActivityResultCallback {

  private static final LatLng SW_MAP_BORDER = new LatLng(48.992262952936514, 12.089423798024654);
  private static final LatLng NE_MAP_BORDER = new LatLng(48.995638443353734, 12.093880958855152);
  private static final LatLngBounds mapBounds = new LatLngBounds(SW_MAP_BORDER,
      NE_MAP_BORDER);
  private static final float MAX_ZOOM_LEVEL = 22f;
  private static final float MIN_ZOOM_LEVEL = 13.314879f;
  private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

  private MapViewModel mapViewModel;
  private GoogleMap map;
  private MapView mapView;
  private Button showUserPositionButton;
  private ImageButton toggleMarkerButton;
  private String plantArea = "";

  public static MapFragment newInstance() {
    return new MapFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.map_fragment, container, false);

    Bundle mapViewBundle = null;
    if (savedInstanceState != null) {
      mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
    }
    mapView = v.findViewById(R.id.google_map);
    mapView.onCreate(mapViewBundle);

    showUserPositionButton = v.findViewById(R.id.show_user_position_button);
    showUserPositionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        requestLocationPermissions();
      }
    });
    toggleMarkerButton = v.findViewById(R.id.toggle_marker_button);
    toggleMarkerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mapViewModel.toggleMarkerVisibility();
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
  }


  /**
   * Initialises the displayed map.
   *
   * @param googleMap GoogleMap object as soon as Map is ready.
   */
  @SuppressLint("PotentialBehaviorOverride")
  @Override
  public void onMapReady(@NonNull GoogleMap googleMap) {
    map = googleMap;
    //setup map and get permissions
    initMap();

    //setup polygons
    mapViewModel.initData(map);
    mapViewModel.initInfoMarker();
    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

      @Override
      public void onInfoWindowClick(@NonNull Marker marker) {
        if (mapViewModel.markerInfoClickListener != null) {
          Area area = new Area(Objects.requireNonNull(marker.getTag()).toString().substring(0, 1),
              marker.getTitle());
          mapViewModel.markerInfoClickListener.onMarkerInfoClicked(area);
        }
      }
    });
    if (!this.plantArea.equals("")) {
      mapViewModel.setPlantArea(this.plantArea);
    }
    requestLocationPermissions();
  }


  /**
   * inits map regarding ui settings (zoomcontrol, location).
   */
  @SuppressLint("MissingPermission")
  private void initMap() {
    map.getUiSettings().setZoomControlsEnabled(true);
    map.setLatLngBoundsForCameraTarget(mapBounds);
    map.setMaxZoomPreference(MAX_ZOOM_LEVEL);
    map.setMinZoomPreference(MIN_ZOOM_LEVEL);
    setMarkerInfoClickListener(new MarkerInfoClickListener() {
      @Override
      public void onMarkerInfoClicked(Area markerArea) {
        ((MainActivity) requireActivity()).openDrawerWithAreaTag(markerArea);
      }
    });
  }

  /**
   * checks if location permission is granted and enables user location button in map if no
   * permission is granted, app will ask for permission.
   */
  public void requestLocationPermissions() {
    if ((ContextCompat.checkSelfPermission(
        requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
        requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED)) {
      locationServicesActivated();
    } else {
      requestPermissionLauncher.launch(
          new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
              Manifest.permission.ACCESS_COARSE_LOCATION});
    }


  }

  /**
   * Allows to set a listener for clicks on Marker info buttons.
   *
   * @param markerInfoClickListener listener to be called on markerInfoClick
   */
  public void setMarkerInfoClickListener(MarkerInfoClickListener markerInfoClickListener) {

    mapViewModel.markerInfoClickListener = markerInfoClickListener;
  }

  /**
   * Sends the current User Location to the MapViewModel to highlighting of an area if the user is
   * inside.
   *
   * @param currentUserLocation current user location as a LatLng.
   */
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
    Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
    if (mapViewBundle == null) {
      mapViewBundle = new Bundle();
      outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
    }
    super.onSaveInstanceState(mapViewBundle);
    mapView.onSaveInstanceState(mapViewBundle);
  }


  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  /**
   * Allows to set a value select a value for the plantArea.
   *
   * @param plantArea gets the area as a string, it gets cut to only the first letter
   */
  public void setPlantArea(String plantArea) {
    if (!plantArea.equals("")) {
      this.plantArea = plantArea.substring(0, 1);
    }
  }

  @SuppressLint("MissingPermission")
  private void locationServicesActivated() {
    MainActivity mainActivity = (MainActivity) requireActivity();
    map.setMyLocationEnabled(true);
    map.getUiSettings().setMyLocationButtonEnabled(true);
    mainActivity.initLocationServices();
    showUserPositionButton.setVisibility(View.GONE);
  }

  @SuppressLint("MissingPermission")
  private final ActivityResultLauncher<String[]> requestPermissionLauncher =
      registerForActivityResult(
          new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
            if (ActivityCompat
                .checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
              locationServicesActivated();
            } else {
              showUserPositionButton.setVisibility(View.VISIBLE);
            }
          });

  @Override
  public void onActivityResult(Object result) {

  }
}