package com.example.urbotanist.drawerfragments.plant;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import com.example.urbotanist.BotanistApplication;
import com.example.urbotanist.network.DownloadImageListener;
import com.example.urbotanist.network.DownloadImageTask;
import com.example.urbotanist.network.retrofit.WikimediaApiCalls;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlantViewModel extends ViewModel {

  Plant selectedPlant;
  String plantName;
  static String plantImagePath = "plantImages";

  public void setSelectedPlant(Plant selectedPlant) {
    this.selectedPlant = selectedPlant;
  }

  public void checkForPlantImage(ImageDownloadListener imageDownloadListener) {
    plantName = selectedPlant.genusName + "_" + selectedPlant.typeName;
    Bitmap plantImageFromStorage = loadImageFromStorage(plantImagePath, plantName);
    if (plantImageFromStorage != null) {
      SharedPreferences plantImageLicensePreferences = BotanistApplication.context
          .getSharedPreferences("PlantImageLicenses",
              Context.MODE_PRIVATE);
      String plantLicenseString = plantImageLicensePreferences.getString(plantName, null);
      imageDownloadListener.onImageAvailabilityChecked(true, false,
          null, plantLicenseString, plantImageFromStorage);

    } else {

      Retrofit retrofit = new Retrofit.Builder().baseUrl("https://en.wikipedia.org").build();
      WikimediaApiCalls wikimediaApiCall = retrofit.create(WikimediaApiCalls.class);
      Call<okhttp3.ResponseBody> call = wikimediaApiCall
          .checkForImage(plantName);
      call.enqueue(new Callback<okhttp3.ResponseBody>() {
        @Override
        public void onResponse(@NonNull Call<okhttp3.ResponseBody> call,
            @NonNull Response<okhttp3.ResponseBody> response) {
          JSONObject responseObject;
          try {
            assert response.body() != null;
            responseObject = new JSONObject(response.body().string());
            JSONObject pages = responseObject.getJSONObject("query").getJSONObject("pages");
            String imageUrl =
                "" + pages.getJSONObject(pages.keys().next()).getJSONObject("original")
                    .get("source");
            String imageName = "" + pages.getJSONObject(pages.keys().next()).get("pageimage");

            getImageLicenseData(imageName, imageUrl, imageDownloadListener);


          } catch (IOException | JSONException e) {
            e.printStackTrace();
            imageDownloadListener.onImageAvailabilityChecked(false, false, null, null, null);
          }

        }

        @Override
        public void onFailure(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Throwable t) {
          imageDownloadListener.onImageAvailabilityChecked(false, false, null, null, null);
        }
      });
    }
  }

  public void getImageLicenseData(String imageFileName, String imageUrl,
      ImageDownloadListener imageDownloadListener) {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://en.wikipedia.org").build();
    WikimediaApiCalls wikimediaApiCall = retrofit.create(WikimediaApiCalls.class);
    Call<okhttp3.ResponseBody> call = wikimediaApiCall
        .getImageLicenseData("File:" + imageFileName);
    call.enqueue(new Callback<okhttp3.ResponseBody>() {
      @Override
      public void onResponse(@NonNull Call<okhttp3.ResponseBody> call,
          @NonNull Response<okhttp3.ResponseBody> response) {
        JSONObject responseObject;
        try {
          assert response.body() != null;
          responseObject = new JSONObject(response.body().string());
          JSONObject pages = responseObject.getJSONObject("query").getJSONObject("pages");
          JSONObject metaData = pages.getJSONObject(pages.keys().next()).getJSONArray("imageinfo")
              .getJSONObject(0).getJSONObject("extmetadata");
          String licenseShortname =
              "" + metaData.getJSONObject("LicenseShortName").get("value");
          String author = "" + metaData.getJSONObject("Artist")
              .get("value");
          String regex = "//commons";
          author = author.replace(regex, "https://commons");
          String licenseUrl =
              "" + metaData.getJSONObject("LicenseUrl")
                  .get("value");

          // <a href="https://commons.wikimedia.org/wiki/File:Abies_pinsapo_var._tazaotana,_Wakehurst_Place,_UK_-_Diliff.jpg">Diliff</a>, <a href="https://creativecommons.org/licenses/by-sa/3.0">CC BY-SA 3.0</a>, via Wikimedia Commons
          String completeLicenseHMTLString =
              author
                  + "</a>, <a href='" + licenseUrl + "'>" + licenseShortname
                  + "</a>, via Wikimedia Commons";
          imageDownloadListener
              .onImageAvailabilityChecked(true, true, imageUrl, completeLicenseHMTLString,
                  null);

        } catch (IOException | JSONException e) {
          e.printStackTrace();
          imageDownloadListener.onImageAvailabilityChecked(false, false, null, null, null);
        }

      }

      @Override
      public void onFailure(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Throwable t) {
        imageDownloadListener.onImageAvailabilityChecked(false, false, null, null, null);
      }
    });
  }

  public void downloadImage(ImageView plantImage, TextView imageLicenseView,
      ProgressBar downloadImageSpinner, String downloadUrl, String imageLicenseString) {
    downloadImageSpinner.setVisibility(View.VISIBLE);
    new DownloadImageTask(new DownloadImageListener() {
      @Override
      public void onImageDownloadFinished(Bitmap image) {
        if (image != null) {
          plantImage.setImageBitmap(image);
          plantImage.setVisibility(View.VISIBLE);
          imageLicenseView.setVisibility(View.VISIBLE);
          saveImageToInternalStorage(image, plantImagePath, plantName);
          SharedPreferences plantImageLicensePreferences = BotanistApplication.context
              .getSharedPreferences("PlantImageLicenses",
                  Context.MODE_PRIVATE);
          plantImageLicensePreferences.edit().putString(plantName, imageLicenseString).apply();

        } else {
          downloadImageSpinner.setVisibility(View.GONE);
        }
      }
    }).execute(
        downloadUrl);


  }

  private String saveImageToInternalStorage(Bitmap bitmapImage, String Path, String filename) {
    Context context = BotanistApplication.context;
    ContextWrapper cw = new ContextWrapper(context);
    // path to /data/data/yourapp/app_data/imageDir
    File directory = cw.getDir("plantImages", Context.MODE_PRIVATE);
    // Create imageDir
    File mypath = new File(directory, filename + ".png");
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(mypath);
      // Use the compress method on the BitMap object to write image to the OutputStream
      bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        fos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return directory.getAbsolutePath();
  }

  public Bitmap loadImageFromStorage(String path, String filename) {
    Context context = BotanistApplication.context;
    ContextWrapper cw = new ContextWrapper(context);
    try {
      File directory = cw.getDir("plantImages", Context.MODE_PRIVATE);
      File f = new File(directory, filename + ".png");
      Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
      return b;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }

  }

}