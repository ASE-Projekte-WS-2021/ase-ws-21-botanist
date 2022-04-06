package com.example.urbotanist.drawerfragments.plant;

import android.graphics.Bitmap;
import android.text.Html;
import com.example.urbotanist.drawerfragments.favorites.FavouritePlant;
import java.util.List;

public interface ImageDownloadListener {

  void onImageAvailabilityChecked(boolean isAvailable,boolean needsDownload, String imageDownloadUrl,
      String licenseString, Bitmap image );

}
