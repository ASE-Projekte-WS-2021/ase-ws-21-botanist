package com.example.urbotanist;

import static android.view.animation.AnimationUtils.loadAnimation;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.urbotanist.drawerfragments.area.Area;
import com.example.urbotanist.drawerfragments.area.AreaFragment;
import com.example.urbotanist.drawerfragments.area.AreaSelectListener;
import com.example.urbotanist.drawerfragments.favorites.FavoritesFragment;
import com.example.urbotanist.drawerfragments.plant.Plant;
import com.example.urbotanist.drawerfragments.plant.PlantFragment;
import com.example.urbotanist.mainfragments.info.InfoFragment;
import com.example.urbotanist.mainfragments.map.MapFragment;
import com.example.urbotanist.mainfragments.map.MarkerInfoClickListener;
import com.example.urbotanist.mainfragments.search.SearchFragment;
// Google Maps by Google, https://developers.google.com/maps
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
// Sileria , https://sileria.com/
import com.google.android.gms.tasks.Task;
import com.sileria.android.Kit;
import com.sileria.android.view.SlidingTray;
// GIF API by DroidsOnRoids, https://github.com/DroidsOnRoids/API


public class MainActivity extends AppCompatActivity implements AreaSelectListener,
    LocationSource.OnLocationChangedListener, MarkerInfoClickListener {


  public MapFragment mapFragment = new MapFragment();
  public SearchFragment searchFragment = new SearchFragment();
  public InfoFragment infoFragment = new InfoFragment();
  public PlantFragment plantDrawerFragment = new PlantFragment();
  public AreaFragment areaDrawerFragment = new AreaFragment();
  public FavoritesFragment favoritesDrawerFragment = new FavoritesFragment();
  private Plant currentPlant;
  private Area currentSelectedArea;
  private LatLng currentUserLocation;
  private FusedLocationProviderClient fusedLocationClient;
  private LocationRequest locationRequest;
  private Button showMapButton;
  private Button searchButton;
  private Button infoButton;

  private SlidingTray slidingTrayDrawer;
  private ImageView drawerPlantButton;
  private ImageView drawerAreaButton;
  private ImageView drawerBackground;
  private ImageView drawerFavouritesButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.noTransition);
    Kit.init(this);
    setContentView(R.layout.activity_main);
    setupViews();
    initLocationServices();
    loadCurrentScreenFragment(mapFragment);
    loadCurrentDrawerFragment(favoritesDrawerFragment);

    setupButtonListeners();
    setTheme(R.style.Theme_URBotanist);
  }


  /**
   * Setup Views for main activity.
   */
  private void setupViews() {
    showMapButton = findViewById(R.id.map_button);
    searchButton = findViewById(R.id.search_button);
    infoButton = findViewById(R.id.bar_icon_background);
    slidingTrayDrawer = findViewById(R.id.drawer);
    drawerPlantButton = findViewById(R.id.drawer_plants_button);
    drawerAreaButton = findViewById(R.id.drawer_areas_button);
    drawerBackground = findViewById(R.id.black_overlay);
    drawerFavouritesButton = findViewById(R.id.drawer_favorite_button);

    //Setup Drawer Handle Position
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    ImageView handle = findViewById(R.id.handle);
    handle.setX(handle.getX() + (int) (displayMetrics.widthPixels * 0.28));
    handle.setY(handle.getY() - 30f);
  }

  /**
   * Loads the given Fragment into the Drawer.
   *
   * @param fragment fragment to be loaded
   */
  public void loadCurrentDrawerFragment(Fragment fragment) {
    String fragmentName = fragment.getClass().getSimpleName();
    if (fragmentName.equals(plantDrawerFragment.getClass().getSimpleName())) {
      drawerAreaButton.setBackground(null);
      drawerFavouritesButton.setBackground(null);
      //drawerPlantButton.setBackground(getDrawable(R.color.white));
      drawerPlantButton.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_drawerbutton));
    } else if (fragmentName.equals(areaDrawerFragment.getClass().getSimpleName())) {
      drawerPlantButton.setBackground(null);
      drawerFavouritesButton.setBackground(null);
      drawerAreaButton.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_drawerbutton));
    } else if (fragmentName.equals(favoritesDrawerFragment.getClass().getSimpleName())) {
      drawerPlantButton.setBackground(null);
      drawerAreaButton.setBackground(null);
      drawerFavouritesButton.setBackground(ContextCompat
          .getDrawable(this, R.drawable.ic_drawerbutton));
    }

    getSupportFragmentManager().beginTransaction().replace(R.id.drawer_fragment_container, fragment)
        .commitNow();
  }

  /**
   * Loads the given Fragment into the MainActivity.
   *
   * @param fragment fragment to be loaded
   */
  public void loadCurrentScreenFragment(Fragment fragment) {
    String fragmentName = fragment.getClass().getSimpleName();
    if (infoFragment.getClass().getSimpleName().equals(fragmentName)) {
      focusButton(infoButton);
    } else if (searchFragment.getClass().getSimpleName().equals(fragmentName)) {
      focusButton(searchButton);
    } else if (mapFragment.getClass().getSimpleName().equals(fragmentName)) {
      focusButton(showMapButton);
    }
    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
        .commitNow();
  }


  /**
   * Sets up Listeners for all buttons in drawer and MainActivity, outside of fragments.
   */
  private void setupButtonListeners() {
    showMapButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        loadCurrentScreenFragment(mapFragment);
      }
    });
    searchButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        loadCurrentScreenFragment(searchFragment);
      }
    });
    infoButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        loadCurrentScreenFragment(infoFragment);
      }
    });

    drawerPlantButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        loadCurrentDrawerFragment(plantDrawerFragment);
      }
    });

    drawerAreaButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        loadCurrentDrawerFragment(areaDrawerFragment);
      }
    });

    drawerFavouritesButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        loadCurrentDrawerFragment(favoritesDrawerFragment);
      }
    });

    plantDrawerFragment.setAreaSelectListener(new AreaSelectListener() {
      @Override
      public void onAreaSelected(String area) {
        showMapWithArea(area);
      }
    });

    areaDrawerFragment.setAreaSelectListener(new AreaSelectListener() {
      @Override
      public void onAreaSelected(String area) {
        showMapWithArea(area);
      }
    });

    mapFragment.setMarkerInfoClickListener(new MarkerInfoClickListener() {
      @Override
      public void onMarkerInfoClicked(Area markerArea) {
        openDrawerWithAreaTag(markerArea);
      }
    });

    slidingTrayDrawer.setOnDrawerCloseListener(new SlidingTray.OnDrawerCloseListener() {
      @Override
      public void onDrawerClosed() {
        fadeOut(drawerBackground);
      }
    });

    slidingTrayDrawer.setOnDrawerOpenListener(new SlidingTray.OnDrawerOpenListener() {
      @Override
      public void onDrawerOpened() {
        fadeIn(drawerBackground);
      }
    });

    slidingTrayDrawer.setOnDrawerScrollListener(new SlidingTray.OnDrawerScrollListener() {
      @Override
      public void onScrollStarted() {
        if (!slidingTrayDrawer.isOpened()) {
          fadeIn(drawerBackground);
        }
      }

      @Override
      public void onScrollEnded() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
          @Override
          public void run() {
            if (!slidingTrayDrawer.isOpened()) {
              fadeOut(drawerBackground);
            }
          }
        }, 600);
      }
    });

  }

  private void fadeIn(View fadeView) {
    if (fadeView.getVisibility() == View.INVISIBLE) {
      fadeView.setVisibility(View.VISIBLE);
      fadeView.setAnimation(loadAnimation(getApplicationContext(), R.anim.fade_in));
    }
  }

  private void fadeOut(View fadeView) {
    if (fadeView.getVisibility() == View.VISIBLE) {
      fadeView.setAnimation(loadAnimation(getApplicationContext(), R.anim.fade_out));
      fadeView.setVisibility(View.INVISIBLE);
    }
  }

  /**
   * Set "button pressed" style to the given button.
   *
   * @param focusButton button to be styled
   */
  private void focusButton(Button focusButton) {
    //highlights button of active UI-Fragment
    infoButton.getBackground().setAlpha(128);
    searchButton.getBackground().setAlpha(128);
    showMapButton.getBackground().setAlpha(128);
    focusButton.getBackground().setAlpha(255);
  }

  /**
   * Opens the drawer if its not allready open.
   */
  public void openDrawer() {
    if (!slidingTrayDrawer.isOpened()) {
      slidingTrayDrawer.animateOpen();
    }
  }

  /**
   * Opens the drawer with the area fragment and the given Area Selected.
   *
   * @param markerArea area to be selected in the area fragment
   */
  private void openDrawerWithAreaTag(Area markerArea) {
    setCurrentSelectedArea(markerArea);
    loadCurrentDrawerFragment(areaDrawerFragment);
    areaDrawerFragment.setupUi();
    openDrawer();
  }


  /**
   * Closes the drawer.
   */
  public void closeDrawer() {
    slidingTrayDrawer.animateClose();
  }

  /**
   * Overrides the back button to close drawer instead of closing app if drawer is open.
   */
  @Override
  public void onBackPressed() {
    if (slidingTrayDrawer.isOpened()) {
      closeDrawer();
    } else {
      super.onBackPressed();
    }
  }


  /**
   * Loads map fragement and set area, that in map fragment that gets then focused by the map
   * camera.
   *
   * @param area area to be focused by the map camera
   */
  private void showMapWithArea(String area) {
    mapFragment = new MapFragment();
    loadCurrentScreenFragment(mapFragment);
    mapFragment.setPlantArea(area);
  }

  ////////////
  // Setter and getter for selected area and plant
  ///////////

  /**
   * Sets the currently selected plant that can be referenced from all fragments.
   *
   * @param plant                   plant to be selected
   * @param shouldOpenPlantFragment opens drawer with plant fragment if true
   */
  public void setCurrentPlant(Plant plant, boolean shouldOpenPlantFragment) {
    this.currentPlant = plant;

    if (shouldOpenPlantFragment) {
      loadCurrentDrawerFragment(plantDrawerFragment);
      plantDrawerFragment.setupUi(plant);
      openDrawer();
    }
  }

  /**
   * returns the currently selected area.
   *
   * @return currently selected area
   */
  public Area getCurrentSelectedArea() {
    return this.currentSelectedArea;
  }

  /**
   * Sets the currently selected area that can be referenced from all fragments.
   *
   * @param area area to be selected
   */
  public void setCurrentSelectedArea(Area area) {
    this.currentSelectedArea = area;
  }

  /**
   * returns the currently selected plant.
   *
   * @return currently selected plant
   */
  public Plant getCurrentPlant() {
    return this.currentPlant;
  }

  ////////////////
  // Setup Location Services
  ///////////////

  /**
   * updated the currentUserLocation if its available.
   */
  private void getLastUserLocation() {
    fusedLocationClient = getFusedLocationProviderClient(this);
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    fusedLocationClient.getLastLocation()
        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
          @Override
          public void onSuccess(Location location) {
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
              currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
              mapFragment.highlightUserAreaMarker(currentUserLocation);
            }
          }
        });
  }

  /**
   * Inits location services, that allow to check for users current location periodicly, so that we
   * can mark areas on the map if the user is inside of them.
   */
  private void initLocationServices() {
    getLastUserLocation();
    getLocationUpdates();
  }

  /**
   * Initializes recurring location queries.
   */
  private void getLocationUpdates() {
    locationRequest = LocationRequest.create();
    locationRequest.setInterval(4000);
    locationRequest.setFastestInterval(2000);
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    mapFragment.requestLocationPermissions();
    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest);
    SettingsClient client = LocationServices.getSettingsClient(this);
    Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
    task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
      @Override
      public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
        startLocationUpdates();
      }
    });
  }

  /**
   * sets up onLocationChanged as listener for locationUpdates.
   */
  private void startLocationUpdates() {
    if (ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest,
        new LocationCallback() {
          @Override
          public void onLocationResult(LocationResult locationResult) {
            onLocationChanged(locationResult.getLastLocation());
          }
        },
        Looper.myLooper());
  }

  ////////////////
  // Listener Implementations
  ///////////////


  @Override
  public void onLocationChanged(@NonNull Location location) {
    currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
    mapFragment.highlightUserAreaMarker(currentUserLocation);
  }

  @Override
  public void onAreaSelected(String location) {
    //showMapWithArea(location);
  }

  @Override
  public void onMarkerInfoClicked(Area markerArea) {
    openDrawerWithAreaTag(markerArea);
  }
}