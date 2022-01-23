package com.example.urbotanist.network;

import com.example.urbotanist.model.Plant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlantApi {
    //https://www.floraweb.de/xsql/taxnames_json.xsql?gat={gat}&art={art}
    @GET("/xsql/taxnames_json.xsql")
    Call<Plant> getPlantsByName(@Query("gat") String gat, @Query("art") String art);
}
