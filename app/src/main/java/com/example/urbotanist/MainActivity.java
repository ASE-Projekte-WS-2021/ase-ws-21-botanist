package com.example.urbotanist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.widget.ConstraintLayout;



import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class MainActivity extends AppCompatActivity {
    private ImageViewTouch map;
    private Button pivotButton;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pivotButton = new Button(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_main);
        initGUI();

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
        map.setOnViewMatrixChangedListener(new ImageViewTouchBase.OnViewMatrixChangeListener(){
            @Override
            public void onMatrixChanged(Matrix newMatrix){
                //pivotButton.setX(pivotButton.getX()+);
            }
        });

    }

    private void fireAnalyticsLog(String eventName, String target){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, target);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, eventName);

        mFirebaseAnalytics.logEvent("myEvent", bundle);
        Log.d("firebase","Log Sent");



    }

    private void loadButtons(){
        Button infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fireAnalyticsLog("ButtonClicked", "MapInfoButton");
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.height= 35;
        params.width = 130;
        pivotButton.setLayoutParams(params);
        pivotButton.setBackgroundResource(R.drawable.round_button);

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.activityLayout);
        layout.addView(pivotButton);
        pivotButton.setX(305);
        pivotButton.setY(1115);
    }


}