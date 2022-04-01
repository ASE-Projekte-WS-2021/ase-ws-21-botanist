package com.example.urbotanist.database.resultlisteners;

import com.example.urbotanist.ui.plant.Plant;
import java.util.List;

public interface DbPlantFoundListener {

  void onPlantFound(List<Plant> plants);

}
