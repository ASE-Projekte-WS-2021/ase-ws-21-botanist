package com.example.urbotanist.ui.search;

import com.example.urbotanist.ui.favorites.FavouritePlant;
import com.example.urbotanist.ui.plant.Plant;
import java.util.List;

public interface DatabaseListener {

  public List<Plant> searchPlant(String searchTerm);

  public List<Plant> searchPlantsInArea(String areaName);

  public List<FavouritePlant> searchFavouritePlants();

  public boolean checkIfPlantIsFavourite(Plant plant);

  public void removeFavouritePlant(int plantId);

  public void addFavouritePlant(Plant plant);
}
