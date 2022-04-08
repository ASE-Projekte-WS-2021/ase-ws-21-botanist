package com.example.urbotanist.drawerfragments.plant;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
  static String PlantImagePath = "plantImages";
  static String PlantImageSharedPreferences = "PlantImageLicenses";

  public void setSelectedPlant(Plant selectedPlant) {
    this.selectedPlant = selectedPlant;
  }


  /**
   * Checks if a Image for the selected plant is available in devices storage and calls the listener
   * with necessary parameters; if not, checks if a image is available on wikipedia. If image is
   * available on wikipedia getImageLicenseData is called to check for the images license which then
   * calls the listener when it checked for license data.
   *
   * @param imageDownloadListener gets called when checks are finished
   */
  public void checkForPlantImage(ImageDownloadListener imageDownloadListener) {
    plantName = selectedPlant.genusName + "_" + selectedPlant.typeName;

    ////////////
    //Check for image in Local Storage
    ///////////
    Bitmap plantImageFromStorage = loadImageFromStorage(PlantImagePath, plantName);
    if (plantImageFromStorage != null) {
      SharedPreferences plantImageLicensePreferences = BotanistApplication.context
          .getSharedPreferences(PlantImageSharedPreferences,
              Context.MODE_PRIVATE);
      String plantLicenseString = plantImageLicensePreferences.getString(plantName, null);
      imageDownloadListener.onImageAvailabilityChecked(true, false,
          null, plantLicenseString, plantImageFromStorage);

    } else {

      ///////////
      //Check for image on Wikipedia
      //////////
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
                "" + pages.getJSONObject(pages.keys().next()).getJSONObject("thumbnail")
                    .get("source");
            String imageName = "" + pages.getJSONObject(pages.keys().next()).get("pageimage");

            //////////
            // If image is found on Wikipedia, check the Images License next
            //////////
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

  /**
   * Checks if the image as a free license and gernerates the license string with attribution. Then
   * calls the listener with all needed data, or with isAvailable false and no other data.
   *
   * @param imageFileName         the filename of the image
   * @param imageUrl              the images download url
   * @param imageDownloadListener the listener that should be called after the check is finished
   */
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

          //////////////////////////
          // EXAMPLE LICENSE STRING:
          // <a href="https://commons.wikimedia.org/wiki/File:Abies_pinsapo_var._tazaotana,
          // _Wakehurst_Place,_UK_-_Diliff.jpg">Diliff</a>,
          // <a href="https://creativecommons.org/licenses/by-sa/3.0">
          // CC BY-SA 3.0</a>, via Wikimedia Commons
          //////////////////////////
          String completeLicenseHtmlString =
              author
                  + "</a>, <a href='" + licenseUrl + "'>" + licenseShortname
                  + "</a>, via Wikimedia Commons";
          imageDownloadListener
              .onImageAvailabilityChecked(true, true, imageUrl, completeLicenseHtmlString,
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

  /**
   * Downloads the Image and handles the given views visibilities accordingly.
   *
   * @param plantImage           The view that the image should be load into
   * @param imageLicenseView     The view that holds the license string
   * @param downloadImageSpinner the progressbar that should be shown while downloading
   * @param downloadUrl          The url to download the image from
   * @param imageLicenseString   the license string for the image
   */
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
          saveImageToInternalStorage(image, PlantImagePath, plantName);
          SharedPreferences plantImageLicensePreferences = BotanistApplication.context
              .getSharedPreferences(PlantImageSharedPreferences,
                  Context.MODE_PRIVATE);
          plantImageLicensePreferences.edit().putString(plantName, imageLicenseString).apply();

        } else {
          downloadImageSpinner.setVisibility(View.GONE);
        }
      }
    }).execute(
        downloadUrl);


  }

  /**
   * Save a bitmap image to local storage.
   *
   * @param bitmapImage the image to be saved
   * @param path        the Path where to save it to
   * @param filename    the filename that the image file should get
   */
  private void saveImageToInternalStorage(Bitmap bitmapImage, String path, String filename) {
    Context context = BotanistApplication.context;
    ContextWrapper cw = new ContextWrapper(context);
    // path to /data/data/yourapp/app_data/imageDir
    File directory = cw.getDir(path, Context.MODE_PRIVATE);
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
        assert fos != null;
        fos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Loads an Bitmap image from local storage.
   *
   * @param path     the path where to load from
   * @param filename the name of the file to load from the path
   * @return returns the bitmap, or null if the specified image was not found
   */
  public Bitmap loadImageFromStorage(String path, String filename) {
    Context context = BotanistApplication.context;
    ContextWrapper cw = new ContextWrapper(context);
    try {
      File directory = cw.getDir(path, Context.MODE_PRIVATE);
      File f = new File(directory, filename + ".png");
      Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
      return b;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }

  }

}