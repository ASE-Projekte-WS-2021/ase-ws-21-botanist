package com.example.urbotanist.ui.favorites;

// Sileria, https://sileria.com/
import static com.sileria.android.Kit.getSystemService;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.database.DatabaseRetriever;
import com.example.urbotanist.ui.plant.Plant;
import com.example.urbotanist.ui.search.SearchResultClickListener;
import java.util.Collections;
import java.util.List;

public class FavoritesFragment extends Fragment implements SearchResultClickListener {

  private FavoritesViewModel favoritesViewModel;
  private RecyclerView favouritePlantListRecycler;
  private FavouriteListAdapter favouriteListAdapter;
  private TextView noFavouritesSelectedView;

  public static FavoritesFragment newInstance() {
    return new FavoritesFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.favourite_fragment, container, false);
    noFavouritesSelectedView = v.findViewById(R.id.no_fav_selected);
    favouriteListAdapter =
        new FavouriteListAdapter(Collections.emptyList(), this);
    favouritePlantListRecycler = v.findViewById(R.id.favourite_plant_list_recycler);
    favouritePlantListRecycler.setAdapter(favouriteListAdapter);
    favouritePlantListRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
    favouritePlantListRecycler.addItemDecoration(
        new DividerItemDecoration(favouritePlantListRecycler.getContext(),
            DividerItemDecoration.VERTICAL));

    return v;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
  }

  @Override
  public void onStart() {
    super.onStart();
    initFavouritesList();
  }

  private void initFavouritesList() {
    List<FavouritePlant> newFavouritePlants = DatabaseRetriever.searchFavouritePlants();
    if (newFavouritePlants.size() > 0) {
      noFavouritesSelectedView.setVisibility(View.GONE);
    }
    favoritesViewModel.setFavouritePlants(newFavouritePlants);
    favouriteListAdapter.favouritePlantsList = favoritesViewModel.getFavouritePlants();
    favouriteListAdapter.notifyDataSetChanged();
  }


  @Override
  public void onSearchResultClick(Plant plant) {
    MainActivity mainActivity = (MainActivity) getActivity();
    if (mainActivity != null) {
      mainActivity.setCurrentPlant(plant);
      mainActivity.loadCurrentDrawerFragment(mainActivity.plantDrawerFragment);
      mainActivity.plantDrawerFragment.setupUi(plant);

      //Close The keyboard
      View view = mainActivity.getCurrentFocus();
      if (view != null) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
            Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
    }
  }
}