package com.example.urbotanist.drawerfragments.plant;

import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import com.example.urbotanist.network.DownloadImageTask;
import com.example.urbotanist.network.retrofit.CheckForImageApiCall;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.annotation.Nullable;
import javax.xml.transform.Result;
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

  public void checkForPlantImage(CheckForImageListener listener) {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://en.wikipedia.org").build();
    CheckForImageApiCall checkForImageCall = retrofit.create(CheckForImageApiCall.class);
    Call<okhttp3.ResponseBody> call = checkForImageCall
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
          listener.onImageAvailabilityChecked(true, imageUrl);
          Log.d("apicall", "imageAvailable at: " + imageUrl);

        } catch (IOException | JSONException e) {
          e.printStackTrace();
          listener.onImageAvailabilityChecked(false, null);
          Log.d("apicall", "imageAvailable false ");
        }

      }

      @Override
      public void onFailure(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Throwable t) {
        listener.onImageAvailabilityChecked(false, null);
        Log.d("apicall", "imageAvailable false ");
      }
    });
  }

  public void downloadImage(ImageView plantImage, String downloadUrl) {
    new DownloadImageTask(plantImage).execute(
        downloadUrl);
    Log.d("apicall", "downloading image");
  }

}