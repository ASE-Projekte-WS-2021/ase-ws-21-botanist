package com.example.urbotanist.database;

import android.util.Log;
import androidx.annotation.NonNull;

import com.example.urbotanist.ui.favorites.FavouritePlant;
import com.example.urbotanist.ui.plant.Plant;
import com.example.urbotanist.ui.search.DatabaseListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import io.realm.Case;
import io.realm.Realm;
import io.realm.Sort;

public class DatabaseRetriever implements DatabaseListener {

    public DatabaseRetriever() { }

    @Override
    public boolean checkIfPlantIsFavourite(Plant plant) {
        final boolean[] isFavourite = new boolean[1];
        Realm realm = Realm.getDefaultInstance();
        if (plant != null) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    isFavourite[0] =
                            realm.where(FavouritePlant.class).equalTo("plantId", plant.id).count() > 0;
                }
            });

            try {
                Thread.sleep(100);
            } catch (Exception e) {
                Log.e("Exception", "Time couldn't wait,"
                        + " it waits for noone. getPlantsInArea, MainActivity" + e);
            }
        }
        return isFavourite[0];
    }

    @Override
    public void removeFavouritePlant(int plantId) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                FavouritePlant favouriteToDelete = realm.where(FavouritePlant.class)
                        .equalTo("plantId",plantId).findFirst();
                favouriteToDelete.deleteFromRealm();
            }
        });
    }

    @Override
    public List<Plant> searchPlant(String searchTerm) {
        ArrayList<Plant> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                // .freeze() is used to create an own object that doesn't reference the query
                result.addAll(realm.where(Plant.class)
                        .beginsWith("fullName", searchTerm, Case.INSENSITIVE).findAll()
                        .sort("fullName", Sort.ASCENDING).freeze());

                result.addAll(realm.where(Plant.class)
                        .beginsWith("familyName", searchTerm, Case.INSENSITIVE)
                        .or().beginsWith("commonName", searchTerm, Case.INSENSITIVE).findAll()
                        .sort("fullName", Sort.ASCENDING).freeze());

                result.addAll(realm.where(Plant.class).contains("fullName", searchTerm, Case.INSENSITIVE)
                        .or().contains("familyName", searchTerm, Case.INSENSITIVE)
                        .or().contains("commonName", searchTerm, Case.INSENSITIVE).findAll()
                        .sort("fullName", Sort.ASCENDING).freeze());
            }
        });

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            Log.e("Exception", "Time couldn't wait, it waits for noone. searchPlant, MainActivity" + e);
        }

        //remove duplicates without affecting the waiting duration
        if (result.size() <= 500) {
            Set<Plant> betterResult = new LinkedHashSet<>();
            betterResult.addAll(result);
            result.clear();
            result.addAll(betterResult);
        }
        return result;
    }

    @Override
    public List<Plant> searchPlantsInArea(String areaName) {
        ArrayList<Plant> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                // .freeze() is used to create an own object that doesn't reference the query
                result.addAll(
                        realm.where(Plant.class).contains("location", areaName, Case.INSENSITIVE).findAll()
                                .sort("fullName", Sort.ASCENDING).freeze());
            }
        });

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            Log.e("Exception", "Time couldn't wait,"
                    + " it waits for noone. getPlantsInArea, MainActivity" + e);
        }
        return result;
    }

    @Override
    public List<FavouritePlant> searchFavouritePlants() {
        ArrayList<FavouritePlant> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                result.addAll(realm.where(FavouritePlant.class).findAll().freeze());
            }
        });
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            Log.e("Exception", "Time couldn't wait,"
                    + " it waits for noone. searchFavouritePlants, MainActivity" + e);
        }
        return result;
    }

    @Override
    public void addFavouritePlant(Plant plant) {
        FavouritePlant newFavouritePlant = new FavouritePlant(plant);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.copyToRealmOrUpdate(newFavouritePlant);
            }
        });
    }
}
