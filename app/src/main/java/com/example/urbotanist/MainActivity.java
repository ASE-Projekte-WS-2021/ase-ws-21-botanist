package com.example.urbotanist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.urbotanist.database.DatabaseAdapterActivity;
import com.example.urbotanist.ui.Plant.Plant;
import com.example.urbotanist.ui.Plant.PlantFragment;
import com.example.urbotanist.ui.Plant.PlantSelectedListener;
import com.example.urbotanist.ui.Search.SearchFragment;
import com.example.urbotanist.ui.Search.SearchListener;
import com.example.urbotanist.ui.info.InfoFragment;
import com.example.urbotanist.ui.map.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.android.HandlerDispatcher;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity implements SearchListener, PlantSelectedListener {
    DatabaseAdapterActivity mDbHelper;

    public MapFragment mapFragment = new MapFragment("");
    public SearchFragment searchFragment = new SearchFragment();
    public InfoFragment infoFragment = new InfoFragment();
    public PlantFragment plantFragment = new PlantFragment();
    Plant currentPlant;
    private Button showMapButton;
    private Button searchButton;
    private Button infoButton;
    private GifImageView splashscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSplashscreen();
        initDatabase();
        preloadViews();
        executeDelayedActions(8300);
    }

    private void preloadViews() {
        //every task which tooks precious time to prepare
        showMapButton = findViewById(R.id.map_button);
        searchButton = findViewById(R.id.search_button);
        infoButton = findViewById(R.id.bar_icon_background);
        loadCurrentScreenFragment(searchFragment);
        loadCurrentScreenFragment(mapFragment);
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
            }
        },delay);
    }

    private void showMapWithArea(String location){
        MapFragment locationFragment = new MapFragment(location);
        plantFragment.closeWindow();
        loadCurrentScreenFragment(locationFragment);
    }

    private void initDatabase() {
        mDbHelper = new DatabaseAdapterActivity(this);
        mDbHelper.createDatabase();
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
    private void focusButton(Button focusButton){
        //highlights button of active UI-Fragment
        infoButton.getBackground().setAlpha(128);
        searchButton.getBackground().setAlpha(128);
        showMapButton.getBackground().setAlpha(128);
        focusButton.getBackground().setAlpha(255);
    }


    public void loadCurrentScreenFragment(Fragment fragment){
        String fragmentName = fragment.getClass().getSimpleName();
        if (infoFragment.getClass().getSimpleName().equals(fragmentName)) {
            focusButton(infoButton);
        } else if (searchFragment.getClass().getSimpleName().equals(fragmentName)) {
            focusButton(searchButton);
        } else if (mapFragment.getClass().getSimpleName().equals(fragmentName)) {
            focusButton(showMapButton);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void setCurrentPlant(Plant plant){
        this.currentPlant = plant;
    }
    public Plant getCurrentPlant(){
        return this.currentPlant;
    }


    @Override
    public List<Plant> searchPlant(String searchTerm) {
        mDbHelper.open();
        ArrayList<Plant> foundPlants = mDbHelper.getSearchResult(searchTerm);
        mDbHelper.close();
        return foundPlants;
    }

    @Override
    public ArrayList<String> searchLocations(String genus, String type){
        mDbHelper.open();
        ArrayList<String> newLocations = mDbHelper.getLocationsForPlant(genus, type);
        mDbHelper.close();
        return newLocations;
    }

    @Override
    public void onAreaSelected(String location) {
        showMapWithArea(location);
        //for each location show on map
        /*
        for (int i = 0; i < locations.size(); i++){
            initPlantArea(locations.get(i)[0]);
        }
        */
    }
}