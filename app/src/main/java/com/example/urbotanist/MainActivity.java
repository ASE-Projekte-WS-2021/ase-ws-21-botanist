package com.example.urbotanist;

import static android.view.animation.AnimationUtils.loadAnimation;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.urbotanist.database.DatabaseAdapterActivity;
import com.example.urbotanist.ui.area.Area;
import com.example.urbotanist.ui.area.AreaFragment;
import com.example.urbotanist.ui.area.AreaSelectListener;
import com.example.urbotanist.ui.favorites.FavoritesFragment;
import com.example.urbotanist.ui.info.InfoFragment;
import com.example.urbotanist.ui.map.MapFragment;
import com.example.urbotanist.ui.map.MarkerInfoClickListener;
import com.example.urbotanist.ui.plant.Plant;
import com.example.urbotanist.ui.plant.PlantFragment;
import com.example.urbotanist.ui.search.SearchFragment;
// Google Maps by Google, https://developers.google.com/maps
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
// Sileria , https://sileria.com/
import com.sileria.android.Kit;
import com.sileria.android.view.SlidingTray;
// GIF API by DroidsOnRoids, https://github.com/DroidsOnRoids/API
import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity implements AreaSelectListener,
        LocationSource.OnLocationChangedListener, MarkerInfoClickListener {

  DatabaseAdapterActivity dbHelper;

  public MapFragment mapFragment = new MapFragment("");
  public SearchFragment searchFragment = new SearchFragment();
  public InfoFragment infoFragment = new InfoFragment();
  public PlantFragment plantDrawerFragment = new PlantFragment();
  public AreaFragment areaDrawerFragment = new AreaFragment();
  public FavoritesFragment favoritesDrawerFragment = new FavoritesFragment();
  private Plant currentPlant;
  private Area currentSelectedArea;
  private LatLng currentUserLocation;
  private FusedLocationProviderClient fusedLocationClient;
  private Button showMapButton;
  private Button searchButton;
  private Button infoButton;
  private GifImageView splashscreen;
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
    setupSplashscreen();
    initDatabase();
    preloadViews();
    getLastUserLocation();
    executeDelayedActions(4000);
  }

  @SuppressLint("MissingPermission")
  private void getLastUserLocation() {
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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

  private void preloadViews() {
    //every task which tooks precious time to prepare
    showMapButton = findViewById(R.id.map_button);
    searchButton = findViewById(R.id.search_button);
    infoButton = findViewById(R.id.bar_icon_background);
    slidingTrayDrawer = findViewById(R.id.drawer);
    drawerPlantButton = findViewById(R.id.drawer_plants_button);
    drawerAreaButton = findViewById(R.id.drawer_areas_button);
    drawerBackground = findViewById(R.id.black_overlay);
    drawerFavouritesButton = findViewById(R.id.drawer_favorite_button);

    loadCurrentScreenFragment(searchFragment);
    loadCurrentScreenFragment(mapFragment);

    // need to load the plant fragment for a short time to  set it up correctly
    openDrawer();
    loadCurrentDrawerFragment(favoritesDrawerFragment);
    loadCurrentDrawerFragment(areaDrawerFragment);
    loadCurrentDrawerFragment(plantDrawerFragment);
    // needs short delay for correct getting display Metrics
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        closeDrawer();
      }
    }, 60);

    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    ImageView handle =  findViewById(R.id.handle);
    handle.setX(handle.getX() + (int) (displayMetrics.widthPixels * 0.28));
    handle.setY(handle.getY() - 30f);
  }

  private void setupSplashscreen() {
    //setting up the splashscreen
    splashscreen = findViewById(R.id.splashscreen);
    splashscreen.setVisibility(View.VISIBLE);
  }

  private void executeDelayedActions(int delay) {
    //everything here should happen after a given delay
    // - e.g. the End of the Splashscreen & showing requests
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        splashscreen.setVisibility(View.INVISIBLE);
        mapFragment.requestLocationPermissions();
        setupListeners();
        setTheme(R.style.Theme_URBotanist);
      }
    }, delay);
  }

  private void showMapWithArea(String area) {
    mapFragment = new MapFragment(area);
    loadCurrentScreenFragment(mapFragment);
  }

  private void initDatabase() {
    dbHelper = new DatabaseAdapterActivity(this);
    dbHelper.createDatabase();
  }

  private void setupListeners() {
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
      fadeView.setAnimation(loadAnimation(getApplicationContext(),R.anim.fade_in));
    }
  }

  private void fadeOut(View fadeView) {
    if (fadeView.getVisibility() == View.VISIBLE) {
      fadeView.setAnimation(loadAnimation(getApplicationContext(),R.anim.fade_out));
      fadeView.setVisibility(View.INVISIBLE);
    }
  }

  private void focusButton(Button focusButton) {
    //highlights button of active UI-Fragment
    infoButton.getBackground().setAlpha(128);
    searchButton.getBackground().setAlpha(128);
    showMapButton.getBackground().setAlpha(128);
    focusButton.getBackground().setAlpha(255);
  }

  private void openDrawerWithAreaTag(Area markerArea) {
    setCurrentSelectedArea(markerArea);
    loadCurrentDrawerFragment(areaDrawerFragment);
    areaDrawerFragment.setupUi();
    openDrawer();
  }

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

  public void setCurrentPlant(Plant plant) {
    this.currentPlant = plant;
  }

  public Area getCurrentSelectedArea() {
    return this.currentSelectedArea;
  }

  public void setCurrentSelectedArea(Area area) {
    this.currentSelectedArea = area;
  }

  public Plant getCurrentPlant() {
    return this.currentPlant;
  }

  public void openDrawer() {
    slidingTrayDrawer.animateOpen();
  }


  public void closeDrawer() {
    slidingTrayDrawer.animateClose();
  }

  @Override
  public void onLocationChanged(@NonNull Location location) {
    currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
    mapFragment.highlightUserAreaMarker(currentUserLocation);
  }

  @Override
  public void onAreaSelected(String location) {
    showMapWithArea(location);
  }

  // Override back button to close drawer instead of closing app if drawer is open
  @Override
  public void onBackPressed() {
    if (slidingTrayDrawer.isOpened()) {
      closeDrawer();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void onMarkerInfoClicked(Area markerArea) {
    openDrawerWithAreaTag(markerArea);
  }
}