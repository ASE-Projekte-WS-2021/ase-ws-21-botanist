package com.example.urbotanist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.example.urbotanist.ui.Plant.Plant;
import com.example.urbotanist.ui.Plant.PlantFragment;
import com.example.urbotanist.ui.Search.SearchFragment;
import com.example.urbotanist.ui.Search.SearchListener;
import com.example.urbotanist.ui.info.InfoFragment;
import com.example.urbotanist.ui.map.MapFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SearchListener {
    DatabaseAdapterActivity mDbHelper;

    public MapFragment mapFragment = new MapFragment();
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

    }

    private void initDatabase() {
        mDbHelper = new DatabaseAdapterActivity(this);
        mDbHelper.createDatabase();

    }

    

    private void setUpButtons() {
        Button showMapButton = findViewById(R.id.map_button);

        showMapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadCurrentScreenFragment(mapFragment);
            }
        });

        Button searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadCurrentScreenFragment(searchFragment);
            }
        });

        Button infoButton = findViewById(R.id.bar_icon_background);

        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadCurrentScreenFragment(infoFragment);
            }
        });
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

}