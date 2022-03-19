package com.example.urbotanist.ui.search;


import static com.sileria.android.Kit.getSystemService;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;
import com.example.urbotanist.ui.plant.Plant;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends CurrentScreenFragment implements SearchResultClickListener {

  private SearchView searchView;
  private DatabaseListener databaseListener;
  private PlantSearchAdapter searchListAdapter;
  private TextView noSearchResultsText;

  public static SearchFragment newInstance() {
    return new SearchFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      databaseListener = (DatabaseListener) context;
    } catch (ClassCastException castException) {
      Log.e("castException",
          "Activity must extend DatabaseListener:" + castException.getLocalizedMessage());
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.search_fragment, container, false);
    searchView = v.findViewById(R.id.search_bar);
    RecyclerView searchListRecycler = v.findViewById(R.id.search_list_recycler);
    noSearchResultsText = v.findViewById(R.id.no_search_results_text);
    searchListAdapter = new PlantSearchAdapter(Collections.emptyList(), this);
    searchListRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
    searchListRecycler.setAdapter(searchListAdapter);
    searchListRecycler.addItemDecoration(
        new DividerItemDecoration(searchListRecycler.getContext(), DividerItemDecoration.VERTICAL));
    initSearch();
    updatePlantWithQuery("");

    return v;
  }

  @Override
  public void onStart() {
    super.onStart();

  }

  public void initSearch() {
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return handleSearch(query);
      }

      @Override
      public boolean onQueryTextChange(String query) {
        return handleSearch(query);
      }

      public boolean handleSearch(String query) {
        updatePlantWithQuery(query);
        return false;
      }
    });
  }

  private void updatePlantWithQuery(String query) {
    List<Plant> foundPlants = databaseListener.searchPlant(query);
    searchListAdapter.foundPlantsList = foundPlants;
    if (foundPlants.size() > 0) {
      noSearchResultsText.setVisibility(View.GONE);
    } else {
      noSearchResultsText.setVisibility(View.VISIBLE);
    }
    searchListAdapter.notifyDataSetChanged();
  }

  @Override
  public void onSearchResultClick(Plant plant) {
    MainActivity mainActivity = (MainActivity) getActivity();
    if (mainActivity != null) {
      mainActivity.setCurrentPlant(plant);
      mainActivity.loadCurrentDrawerFragment(mainActivity.plantDrawerFragment);
      mainActivity.openDrawer();

      //Close The keyboard
      View view = mainActivity.getCurrentFocus();
      if (view != null) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }


    } else {
      Log.e("TAG", "Failed to open Fragment; MainActivity not found");
    }
  }
}