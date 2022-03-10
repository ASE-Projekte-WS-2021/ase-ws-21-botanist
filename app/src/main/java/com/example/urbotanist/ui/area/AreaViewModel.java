package com.example.urbotanist.ui.area;

import androidx.lifecycle.ViewModel;

public class AreaViewModel extends ViewModel {

  Area selectedArea;

  public void setSelectedPlant(Area selectedArea) {
    this.selectedArea = selectedArea;
  }

}