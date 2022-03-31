package com.example.urbotanist.ui.favorites;

import androidx.lifecycle.ViewModel;
import com.example.urbotanist.database.DatabaseRetriever;
import java.util.List;

public class FavoritesViewModel extends ViewModel {


  public List<FavouritePlant> getFavouritePlants() {
    return DatabaseRetriever.searchFavouritePlants();
  }
}