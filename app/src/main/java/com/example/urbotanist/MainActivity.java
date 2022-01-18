package com.example.urbotanist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class MainActivity extends AppCompatActivity {
    private ImageViewTouch map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGUI();
        initDatabase();

    }

    private void initDatabase() {
        DatabaseAdapterActivity mDbHelper = new DatabaseAdapterActivity(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor testdata = mDbHelper.getTestData();

        mDbHelper.close();
    }

    private void initGUI(){
        loadMap();
        loadButtons();
    }

    private void loadMap(){
        map = findViewById(R.id.map);
        map.setImageResource(R.drawable.garden_map);
        map.setDisplayType(ImageViewTouchBase.DisplayType.FIT_HEIGHT);
        map.setScrollEnabled(true);
        map.setQuickScaleEnabled(true);

    }

    private void loadButtons(){
        Button infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("pivot", "x: "+map.getScrollY());
                Log.d("pivot", "y: "+map.getClipBounds().centerX());
            }
        });

        Button pivotButton = new Button(this);
        pivotButton.setBackgroundResource(R.drawable.round_button);

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.activityLayout);
        layout.addView(pivotButton);
        pivotButton.setX(map.getTranslationX());
        pivotButton.setY(map.getPivotY());


    }
}