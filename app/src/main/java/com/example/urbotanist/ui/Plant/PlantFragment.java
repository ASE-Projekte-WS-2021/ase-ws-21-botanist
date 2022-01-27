package com.example.urbotanist.ui.Plant;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;

public class PlantFragment extends CurrentScreenFragment {

    private PlantViewModel mViewModel;

    public static PlantFragment newInstance() {
        return new PlantFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inventory_item_fragment, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        mViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
    }

}