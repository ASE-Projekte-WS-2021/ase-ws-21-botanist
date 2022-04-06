package com.example.urbotanist.drawerfragments.plant;

import com.example.urbotanist.drawerfragments.favorites.FavouritePlant;
import java.util.List;

public interface CheckForImageListener {
  void onImageAvailabilityChecked(boolean isAvailable, String imageDownloadUrl);

}
