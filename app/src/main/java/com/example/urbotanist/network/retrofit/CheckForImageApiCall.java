package com.example.urbotanist.network.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CheckForImageApiCall {

  @GET("w/api.php?action=query&prop=pageimages&format=json&piprop=original&pilicense=free")
  Call<ResponseBody> checkForImage(@Query("titles") String plantName);
}
