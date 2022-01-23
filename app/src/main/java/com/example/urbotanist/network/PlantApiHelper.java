package com.example.urbotanist.network;

import android.util.Log;

import com.example.urbotanist.model.Plant;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlantApiHelper {
    public final PlantApi plantApi;


    public PlantApiHelper(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.floraweb.de/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        plantApi = retrofit.create(PlantApi.class);
    }

    public void getPlantsByName(String gat, String art) {
        Call<Plant> callObject = plantApi.getPlantsByName(gat, art);
        callObject.enqueue(new Callback<Plant>() {


            @Override
            public void onResponse(Call<Plant> call, Response<Plant> response) {
                if (response.isSuccessful()) {
                    Plant plant = response.body();
                }
            }

            @Override
            public void onFailure(Call<Plant> call, Throwable t) {
            }
        });
    }


}
