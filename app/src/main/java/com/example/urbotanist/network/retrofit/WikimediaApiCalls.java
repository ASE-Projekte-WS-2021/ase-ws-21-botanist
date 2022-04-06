package com.example.urbotanist.network.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikimediaApiCalls {

  /**
   * https://www.mediawiki.org/wiki/Extension:PageImages
   * @param plantName Name of the plant (most likely "genusName + _ + typeName")
   * @return returns the responseBody of the Api call
   */
  @GET("w/api.php?action=query&prop=pageimages&format=json&piprop=original|name&pilicense=free")
  Call<ResponseBody> checkForImage(@Query("titles") String plantName);

  @GET("w/api.php?action=query&prop=imageinfo&format=json&iiprop=extmetadata")
  Call<ResponseBody> getImageLicenseData(@Query("titles") String wikimediaImageName);
}
