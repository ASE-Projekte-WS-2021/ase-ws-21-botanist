package com.example.urbotanist.database;

import androidx.annotation.NonNull;
import com.example.urbotanist.database.resultlisteners.DbFavouritesFoundListener;
import com.example.urbotanist.database.resultlisteners.DbIsPlantFavouriteListener;
import com.example.urbotanist.database.resultlisteners.DbPlantFoundListener;
import com.example.urbotanist.drawerfragments.favorites.FavouritePlant;
import com.example.urbotanist.drawerfragments.plant.Plant;
// realm by MongoDB https://realm.io/
import io.realm.Case;
import io.realm.Realm;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;


public class DatabaseRetriever {

  /**
   * Functions checks whether a given Plant Object is marked as favorite in the database
   * @param plant = Current Plant Object to be checked
   * @param dbIsPlantFavouriteListener = Listener that gets called when the query is finished
   */
  public static void checkIfPlantIsFavourite(Plant plant,
      DbIsPlantFavouriteListener dbIsPlantFavouriteListener) {
    final boolean[] isFavourite = new boolean[1];
    Realm realm = Realm.getDefaultInstance();
    if (plant != null) {
      realm.executeTransactionAsync(new Realm.Transaction() {
        @Override
        public void execute(@NonNull Realm realm) {
          isFavourite[0] =
              realm.where(FavouritePlant.class).equalTo("plantId", plant.id).count() > 0;
        }
      }, new Realm.Transaction.OnSuccess() {
        @Override
        public void onSuccess() {
          dbIsPlantFavouriteListener.onIsFavouriteResult(isFavourite[0]);
        }

      });
    }
  }

  /**
   * Function removes a Plant's favorite status with a given ID from the database
   * @param plantId = Plant Object's ID in the database
   */
  public static void removeFavouritePlant(int plantId) {
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(@NonNull Realm realm) {
        FavouritePlant favouriteToDelete = realm.where(FavouritePlant.class)
            .equalTo("plantId", plantId).findFirst();
        if (favouriteToDelete != null) {
          favouriteToDelete.deleteFromRealm();
        }
      }
    });
  }

  /**
   * Function searches for plants that match the search term provided by the user.
   * Additionally sorts results by relevance and alphabet and removes duplicates when necessary.
   * @param searchTerm = The query typed by the user into the search bar
   * @param dbPlantFoundListener = Listener gets called once the query finished
   */
  public static void searchPlants(String searchTerm,
      DbPlantFoundListener dbPlantFoundListener) {

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

        //remove duplicates without affecting the waiting duration
        if (result.size() <= 500) {
          Set<Plant> betterResult = new LinkedHashSet<>();
          betterResult.addAll(result);
          result.clear();
          result.addAll(betterResult);
        }

      }
    }, new Realm.Transaction.OnSuccess() {
      @Override
      public void onSuccess() {
        dbPlantFoundListener.onPlantFound(result);
      }

    });
  }

  /**
   * Function searches for all plants with a matching area name.
   * @param areaName = A given area name
   * @param dbPlantFoundListener = Listener gets called once the query finished
   */
  public static void searchPlantsInArea(String areaName,
      DbPlantFoundListener dbPlantFoundListener) {
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
    }, new Realm.Transaction.OnSuccess() {
      @Override
      public void onSuccess() {
        dbPlantFoundListener.onPlantFound(result);
      }
    });
  }

  /**
   * Function searches for all plants that were marked "favorite"
   * @param dbFavouritesFoundListener = Listener gets called once the query finished
   */
  public static void searchFavouritePlants(DbFavouritesFoundListener dbFavouritesFoundListener) {
    ArrayList<FavouritePlant> result = new ArrayList<>();
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(@NonNull Realm realm) {
        result.addAll(realm.where(FavouritePlant.class).findAll().freeze());
      }
    }, new Realm.Transaction.OnSuccess() {
      @Override
      public void onSuccess() {
        dbFavouritesFoundListener.onFavouritePlantsFound(result);
      }
    });
  }

  /**
   * Function updates a plant that has been marked favorite in the database.
   * @param plant = a given Plant Object
   */
  public static void addFavouritePlant(Plant plant) {
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
