package com.example.urbotanist.ui.location;

import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {

  Location selectedLocation;

  public void setSelectedPlant(Location selectedLocation) {
    this.selectedLocation = selectedLocation;
  }

}