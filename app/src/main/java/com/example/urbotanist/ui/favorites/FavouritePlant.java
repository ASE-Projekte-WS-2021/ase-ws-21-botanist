package com.example.urbotanist.ui.favorites;

import com.example.urbotanist.ui.plant.Plant;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.time.Instant;
import java.util.Date;

public class FavouritePlant extends RealmObject {

  @PrimaryKey
  public int plantId;
  public Plant plant;
  public Date favouriteTime;

  public FavouritePlant() {
  }

  public FavouritePlant(Plant plant) {
    this.plant = plant;
    plantId = plant.id;
    this.favouriteTime = Date.from(Instant.now());

  }

  public void setPlantId(int plantId) {
    this.plantId = plantId;
  }

  public int getPlantId() {
    return plantId;
  }

  public void setPlant(Plant plant) {
    this.plant = plant;
  }

  public Plant getPlant() {
    return plant;
  }

  public void setFavouriteTime(Date favouriteTime) {
    this.favouriteTime = favouriteTime;
  }

  public Date getFavouriteTime() {
    return favouriteTime;
  }

}
