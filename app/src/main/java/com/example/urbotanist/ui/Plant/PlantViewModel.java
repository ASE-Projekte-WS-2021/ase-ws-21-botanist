package com.example.urbotanist.ui.Plant;

import androidx.lifecycle.ViewModel;

public class PlantViewModel extends ViewModel {

    Plant selectedPlant;

    public void setSelectedPlant(Plant selectedPlant){
        this.selectedPlant = selectedPlant;
    };
}