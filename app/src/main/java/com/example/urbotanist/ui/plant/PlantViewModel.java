package com.example.urbotanist.ui.plant;

import androidx.lifecycle.ViewModel;

public class PlantViewModel extends ViewModel {

  Plant selectedPlant;

  public void setSelectedPlant(Plant selectedPlant) {
    this.selectedPlant = selectedPlant;
  }

}