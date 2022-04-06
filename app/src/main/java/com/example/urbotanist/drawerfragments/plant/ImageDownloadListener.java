package com.example.urbotanist.drawerfragments.plant;

import android.text.Html;
import com.example.urbotanist.drawerfragments.favorites.FavouritePlant;
import java.util.List;

public interface ImageDownloadListener {

  void onImageAvailabilityChecked(boolean isAvailable, String imageDownloadUrl,
      String licenseString);

}
