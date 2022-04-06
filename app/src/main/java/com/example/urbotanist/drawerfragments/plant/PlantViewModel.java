package com.example.urbotanist.drawerfragments.plant;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import com.example.urbotanist.network.DownloadImageTask;
import com.example.urbotanist.network.retrofit.WikimediaApiCalls;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlantViewModel extends ViewModel {

  Plant selectedPlant;

  public void setSelectedPlant(Plant selectedPlant) {
    this.selectedPlant = selectedPlant;
  }

  public void checkForPlantImage(ImageDownloadListener imageDownloadListener) {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://en.wikipedia.org").build();
    WikimediaApiCalls wikimediaApiCall = retrofit.create(WikimediaApiCalls.class);
    Call<okhttp3.ResponseBody> call = wikimediaApiCall
        .checkForImage(selectedPlant.genusName + "_" + selectedPlant.typeName);
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
          Log.d("api", imageName);

          getImageLicenseData(imageName, imageUrl, imageDownloadListener);


        } catch (IOException | JSONException e) {
          e.printStackTrace();
          imageDownloadListener.onImageAvailabilityChecked(false, null, null);
        }

      }

      @Override
      public void onFailure(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Throwable t) {
        imageDownloadListener.onImageAvailabilityChecked(false, null, null);
      }
    });
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
              .onImageAvailabilityChecked(true, imageUrl, completeLicenseHMTLString);

        } catch (IOException | JSONException e) {
          e.printStackTrace();
          imageDownloadListener.onImageAvailabilityChecked(false, null, null);
        }

      }

      @Override
      public void onFailure(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Throwable t) {
        imageDownloadListener.onImageAvailabilityChecked(false, null, null);
      }
    });
  }

  public void downloadImage(ImageView plantImage, TextView imageLicenseView, String downloadUrl) {
    new DownloadImageTask(plantImage, imageLicenseView).execute(
        downloadUrl);
  }

}