package com.example.urbotanist.ui.search;

import com.example.urbotanist.ui.plant.Plant;
import java.util.List;

public interface SearchListener {

  public List<Plant> searchPlant(String searchTerm);

  public List<Plant> searchPlantsInArea(String areaName);
}
