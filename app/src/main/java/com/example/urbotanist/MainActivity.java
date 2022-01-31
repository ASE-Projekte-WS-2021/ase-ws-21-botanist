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
import com.example.urbotanist.ui.Search.SearchFragment;
import com.example.urbotanist.ui.map.MapFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    MapFragment mapFragment = new MapFragment();
    SearchFragment searchFragment = new SearchFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUpButtons();
        initDatabase();

    }

    private void initDatabase() {
        DatabaseAdapterActivity mDbHelper = new DatabaseAdapterActivity(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        ArrayList<Plant> testdata = mDbHelper.getSearchResult("erica");

        mDbHelper.close();
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
    }


    public void loadCurrentScreenFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        TranslateAnimation animation = new TranslateAnimation(0,0,30,60);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }



}