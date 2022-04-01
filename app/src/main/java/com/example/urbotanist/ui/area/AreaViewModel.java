package com.example.urbotanist.ui.area;

import androidx.lifecycle.ViewModel;
import com.example.urbotanist.database.DatabaseRetriever;
import com.example.urbotanist.database.resultlisteners.DbPlantFoundListener;
import com.example.urbotanist.ui.plant.Plant;
import java.util.List;

public class AreaViewModel extends ViewModel {

  Area selectedArea;

  public void setSelectedArea(Area selectedArea) {
    this.selectedArea = selectedArea;
  }

}