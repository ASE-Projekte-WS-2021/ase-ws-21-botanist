package com.example.urbotanist.ui.map;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.urbotanist.R;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class MapViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private ImageViewTouch map;
    public MapViewModel() {
        initGUI();
    }
    private void initGUI(){
        loadMap();
        loadButtons();
    }

    private void loadMap(){
        map = getView().findViewById(R.id.map);
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