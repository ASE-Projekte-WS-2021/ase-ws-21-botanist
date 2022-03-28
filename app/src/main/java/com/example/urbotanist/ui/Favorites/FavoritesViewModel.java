package com.example.urbotanist.ui.favorites;

import androidx.lifecycle.ViewModel;
import java.util.List;

public class FavoritesViewModel extends ViewModel {
  private List<FavouritePlant> favouritePlants;

  public void setFavouritePlants(List<FavouritePlant> favouritePlants) {
    this.favouritePlants = favouritePlants;
  }

  public List<FavouritePlant> getFavouritePlants() {
    return favouritePlants;
  }
}