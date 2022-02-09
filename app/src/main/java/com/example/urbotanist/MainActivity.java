package com.example.urbotanist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity implements SearchListener, PlantSelectedListener {
    DatabaseAdapterActivity mDbHelper;

    public MapFragment mapFragment = new MapFragment("");
    public SearchFragment searchFragment = new SearchFragment();
    public InfoFragment infoFragment = new InfoFragment();
    public PlantFragment plantFragment = new PlantFragment();
    Plant currentPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setUpButtons();
        initDatabase();
        loadCurrentScreenFragment(mapFragment);

        plantFragment.setAreaSelectListener(new PlantSelectedListener() {
            @Override
            public void onAreaSelected(String location) {
                showMapWithArea(location);
            }
        });
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

    

    private void setUpButtons() {
        Button showMapButton = findViewById(R.id.map_button);
        Button searchButton = findViewById(R.id.search_button);
        Button infoButton = findViewById(R.id.bar_icon_background);
        Button[] allButtons = {showMapButton, searchButton, infoButton};
        focusButton(allButtons, showMapButton);
        showMapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                focusButton(allButtons, showMapButton);
                loadCurrentScreenFragment(mapFragment);
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                focusButton(allButtons, searchButton);
                loadCurrentScreenFragment(searchFragment);
            }
        });


        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                focusButton(allButtons, infoButton);
                loadCurrentScreenFragment(infoFragment);

            }
        });

    }
    private void resetButtons(Button[] buttons){
        for (Button button : buttons) {
            button.getBackground().setAlpha(128);
        }
    }

    private void focusButton(Button[] buttons, Button focusButton){
        resetButtons(buttons);
        focusButton.getBackground().setAlpha(255);
    }


    public void loadCurrentScreenFragment(Fragment fragment){
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