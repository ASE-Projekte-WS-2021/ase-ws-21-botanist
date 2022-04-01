package com.example.urbotanist.database.resultlisteners;

import com.example.urbotanist.ui.favorites.FavouritePlant;
import java.util.List;

public interface DbFavouritesFoundListener {
  void onFavouritePlantsFound(List<FavouritePlant> favouritePlants);
}
