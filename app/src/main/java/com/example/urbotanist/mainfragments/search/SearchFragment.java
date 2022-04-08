package com.example.urbotanist.mainfragments.search;

// Sileria , https://sileria.com/
import static com.sileria.android.Kit.getSystemService;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.database.DatabaseRetriever;
import com.example.urbotanist.database.resultlisteners.DbPlantFoundListener;
import com.example.urbotanist.drawerfragments.plant.Plant;
import com.example.urbotanist.mainfragments.CurrentScreenFragment;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends CurrentScreenFragment implements SearchResultClickListener {

  private SearchView searchView;
  private PlantSearchAdapter searchListAdapter;
  private TextView noSearchResultsText;
  private ProgressBar searchOngoingSpinner;

  public static SearchFragment newInstance() {
    return new SearchFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.search_fragment, container, false);
    searchView = v.findViewById(R.id.search_bar);
    searchOngoingSpinner = v.findViewById(R.id.search_ongoing_spinner);
    RecyclerView searchListRecycler = v.findViewById(R.id.search_list_recycler);
    noSearchResultsText = v.findViewById(R.id.no_search_results_text);
    searchListAdapter = new PlantSearchAdapter(Collections.emptyList(), this);
    searchListRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
    searchListRecycler.setAdapter(searchListAdapter);
    searchListRecycler.addItemDecoration(
        new DividerItemDecoration(searchListRecycler.getContext(), DividerItemDecoration.VERTICAL));
    initSearch();
    sendNewSearchQuery("");

    return v;
  }

  @Override
  public void onStart() {
    super.onStart();

  }

  /**
   * Sets up input listeners for the search field.
   */
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
        sendNewSearchQuery(query);
        return false;
      }
    });
  }

  /**
   * Sends a new search query to the database and sets a listener for the result. Also updates
   * The views accordingly
   *
   * @param query string for the query to search plants with
   */
  private void sendNewSearchQuery(String query) {
    searchOngoingSpinner.setVisibility(View.VISIBLE);
    noSearchResultsText.setVisibility(View.GONE);
    DatabaseRetriever.searchPlants(query, new DbPlantFoundListener() {
      @Override
      public void onPlantFound(List<Plant> plants) {
        searchOngoingSpinner.setVisibility(View.GONE);
        searchListAdapter.foundPlantsList = plants;
        if (plants.size() > 0) {
          noSearchResultsText.setVisibility(View.GONE);
        } else {
          noSearchResultsText.setVisibility(View.VISIBLE);
        }
        searchListAdapter.notifyDataSetChanged();
      }
    });

  }

  @Override
  public void onPlantSelectedListener(Plant plant) {
    MainActivity mainActivity = (MainActivity) getActivity();
    if (mainActivity != null) {
      mainActivity.setCurrentPlant(plant, true);

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