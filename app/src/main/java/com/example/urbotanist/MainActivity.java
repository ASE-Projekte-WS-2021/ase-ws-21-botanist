package com.example.urbotanist;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.urbotanist.database.DatabaseAdapterActivity;
import com.example.urbotanist.ui.info.InfoFragment;
import com.example.urbotanist.ui.map.MapFragment;
import com.example.urbotanist.ui.plant.Plant;
import com.example.urbotanist.ui.plant.PlantFragment;
import com.example.urbotanist.ui.plant.PlantSelectedListener;
import com.example.urbotanist.ui.search.SearchFragment;
import com.example.urbotanist.ui.search.SearchListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.sileria.android.Kit;
import com.sileria.android.view.SlidingTray;
import io.realm.Case;
import io.realm.Realm;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity implements SearchListener,
    PlantSelectedListener, LocationSource.OnLocationChangedListener {

  DatabaseAdapterActivity dbHelper;

  public MapFragment mapFragment = new MapFragment("");
  public SearchFragment searchFragment = new SearchFragment();
  public InfoFragment infoFragment = new InfoFragment();
  public PlantFragment plantFragment = new PlantFragment();
  Plant currentPlant;
  private LatLng currentUserLocation;
  private FusedLocationProviderClient fusedLocationClient;
  private Button showMapButton;
  private Button searchButton;
  private Button infoButton;
  private GifImageView splashscreen;
  private SlidingTray slidingTrayDrawer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.noTransition);
    getSupportFragmentManager().beginTransaction().replace(R.id.content, plantFragment)
        .commit();
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
                  //TODO LatLng location to MapViewModel, search for right Polygon and get Location String.
                  mapFragment.getPlantsInUserArea(currentUserLocation);
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
    loadCurrentScreenFragment(mapFragment);
    ImageView handle =  findViewById(R.id.handle);
    handle.setX(handle.getX() + 200f); //TODO  Calculate right position for handle
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

  private void showMapWithArea(String location) {
    MapFragment locationFragment = new MapFragment(location);
    loadCurrentScreenFragment(locationFragment);
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
    plantFragment.setAreaSelectListener(new PlantSelectedListener() {
      @Override
      public void onAreaSelected(String location) {
        showMapWithArea(location);
      }
    });
  }

  private void focusButton(Button focusButton) {
    //highlights button of active UI-Fragment
    infoButton.getBackground().setAlpha(128);
    searchButton.getBackground().setAlpha(128);
    showMapButton.getBackground().setAlpha(128);
    focusButton.getBackground().setAlpha(255);
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
        .commit();
  }

  public void setCurrentPlant(Plant plant) {
    this.currentPlant = plant;
  }

  public Plant getCurrentPlant() {
    return this.currentPlant;
  }

  public void openDrawer() {
    slidingTrayDrawer.animateOpen();
  }

  @Override
  public List<Plant> searchPlant(String searchTerm) {
    ArrayList<Plant> result = new ArrayList<>();
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(@NonNull Realm realm) {
        // .freeze() is used to create an own object that doesn't reference the query
        result.addAll(realm.where(Plant.class)
            .beginsWith("fullName", searchTerm, Case.INSENSITIVE).findAll()
            .sort("fullName", Sort.ASCENDING).freeze());

        result.addAll(realm.where(Plant.class)
            .beginsWith("familyName", searchTerm, Case.INSENSITIVE)
            .or().beginsWith("commonName", searchTerm, Case.INSENSITIVE).findAll()
            .sort("fullName", Sort.ASCENDING).freeze());

        result.addAll(realm.where(Plant.class).contains("fullName", searchTerm, Case.INSENSITIVE)
            .or().contains("familyName", searchTerm, Case.INSENSITIVE)
            .or().contains("commonName", searchTerm, Case.INSENSITIVE).findAll()
            .sort("fullName", Sort.ASCENDING).freeze());
      }
    });

    try {
      Thread.sleep(100);
    } catch (Exception e) {
      Log.e("Exception", "Time couldn't wait, it waits for noone. searchPlant, MainActivity" + e);
    }

    /*try {
      betterResult.addAll(result);
      betterResult.stream().distinct().collect(Collectors.toList());
    } catch (Exception e) {
      Log.e("Exception", "Could not make results distinct " + e.toString());
    }*/

    return result;
  }

  public List<Plant> getPlantsInArea(String areaName) {
    ArrayList<Plant> result = new ArrayList<>();
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(@NonNull Realm realm) {
        // .freeze() is used to create an own object that doesn't reference the query
        result.addAll(
                realm.where(Plant.class).contains("location", areaName, Case.INSENSITIVE).findAll()
                        .sort("fullName", Sort.ASCENDING).freeze());
      }
    });

    try {
      Thread.sleep(100);
    } catch (Exception e) {
      Log.e("Exception", "Time couldn't wait,"
          + " it waits for noone. getPlantsInArea, MainActivity" + e);
    }

    return result;
  }

  @Override
  public void onLocationChanged(@NonNull Location location) {
    currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
    mapFragment.getPlantsInUserArea(currentUserLocation);
  }

  @Override
  public void onAreaSelected(String location) {
    showMapWithArea(location);
  }

}