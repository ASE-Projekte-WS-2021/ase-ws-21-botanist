package com.example.urbotanist.ui.area;

import androidx.lifecycle.ViewModel;
import com.example.urbotanist.database.DatabaseRetriever;
import com.example.urbotanist.ui.plant.Plant;
import java.util.List;

public class AreaViewModel extends ViewModel {

  Area selectedArea;

  public void setSelectedArea(Area selectedArea) {
    this.selectedArea = selectedArea;
  }

  public List<Plant> searchPlantsInArea() {

    List<Plant> plantsInArea =
        DatabaseRetriever.searchPlantsInArea(selectedArea.areaName);
    return plantsInArea;

  }

}