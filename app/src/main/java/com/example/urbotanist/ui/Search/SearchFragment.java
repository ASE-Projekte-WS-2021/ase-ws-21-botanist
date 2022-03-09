package com.example.urbotanist.ui.search;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
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
  private SearchListener searchListener;
  private PlantSearchAdapter searchListAdapter;
  private TextView noSearchResultsText;

  public static SearchFragment newInstance() {
    return new SearchFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      searchListener = (SearchListener) context;
    } catch (ClassCastException castException) {
      Log.e("castException",
          "Activity must extend SearchListener:" + castException.getLocalizedMessage());
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
        List<Plant> foundPlants = searchListener.searchPlant(query);
        searchListAdapter.localDataSet = foundPlants;
        if (foundPlants.size() > 0) {
          noSearchResultsText.setVisibility(View.GONE);
        } else {
          noSearchResultsText.setVisibility(View.VISIBLE);
        }
        searchListAdapter.notifyDataSetChanged();
        return false;
      }
    });
  }

  @Override
  public void onSearchResultClick(Plant plant) {
    MainActivity mainActivity = (MainActivity) getActivity();
    if (mainActivity != null) {
      mainActivity.setCurrentPlant(plant);
      mainActivity.plantFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
      mainActivity.plantFragment.show(mainActivity.getSupportFragmentManager(), "Info");
      //mainActivity.loadCurrentScreenFragment(mainActivity.plantFragment);

    } else {
      Log.e("TAG", "Failed to open Fragment; MainActivity not found");
    }
  }
}