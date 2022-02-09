package com.example.urbotanist.ui.Search;

import com.example.urbotanist.ui.Plant.Plant;

import java.util.ArrayList;
import java.util.List;

public interface SearchListener {
    public List<Plant> searchPlant(String searchTerm);
    public ArrayList<String[]> searchLocations(String genus, String type);
}
