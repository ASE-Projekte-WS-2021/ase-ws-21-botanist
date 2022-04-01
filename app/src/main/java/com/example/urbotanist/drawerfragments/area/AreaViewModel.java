package com.example.urbotanist.drawerfragments.area;

import androidx.lifecycle.ViewModel;

public class AreaViewModel extends ViewModel {

  Area selectedArea;

  public void setSelectedArea(Area selectedArea) {
    this.selectedArea = selectedArea;
  }

}