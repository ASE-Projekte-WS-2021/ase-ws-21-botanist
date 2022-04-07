package com.example.urbotanist.network.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikimediaApiCalls {

  /**
   * https://www.mediawiki.org/wiki/Extension:PageImages
   *
   * @param plantName Name of the plant (most likely "genusName + _ + typeName")
   * @return returns the responseBody of the Api call
   */
  @GET("w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail|name&pilicense=free&pithumbsize=600")
  Call<ResponseBody> checkForImage(@Query("titles") String plantName);

  /**
   * https://www.mediawiki.org/wiki/API:Imageinfo
   *
   * @param mediaWikiImageName Name of the file from the MediaWiki Page
   * @return returns the responseBody of the Api call
   */
  @GET("w/api.php?action=query&prop=imageinfo&format=json&iiprop=extmetadata")
  Call<ResponseBody> getImageLicenseData(@Query("titles") String mediaWikiImageName);
}
