package com.example.urbotanist.ui.map;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class MapFragment extends CurrentScreenFragment {

    private MapViewModel mViewModel;
    private ImageViewTouch map;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initGUI();
        Log.d("test", "newmapFragment");
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
    }

    public void initGUI(){
        loadMap();
    }

    private void loadMap(){
        map = getView().findViewById(R.id.map);
        map.setImageResource(R.drawable.garden_map);
        map.setDisplayType(ImageViewTouchBase.DisplayType.FIT_HEIGHT);
        map.setScrollEnabled(true);
        map.setQuickScaleEnabled(true);

    }

}