package com.example.urbotanist.ui.area;

// Sileria, https://sileria.com/
import static com.sileria.android.Kit.getSystemService;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
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
import com.example.urbotanist.ui.search.PlantSearchAdapter;
import com.example.urbotanist.ui.search.SearchResultClickListener;
import java.util.Collections;
import java.util.List;

public class AreaFragment extends Fragment implements SearchResultClickListener {

  private AreaViewModel areaViewModel;
  private TextView areaFullNameView;
  private TextView areaShortNameView;
  private TextView noAreaSelectedView;
  private RecyclerView areaPlantListRecycler;
  private PlantSearchAdapter plantListAdapter;
  private ImageButton showAreaButton;


  private AreaSelectListener areaSelectListener;

  public static AreaFragment newInstance() {
    return new AreaFragment();
  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.area_fragment, container, false);
    areaFullNameView = v.findViewById(R.id.area_header);
    areaShortNameView = v.findViewById(R.id.area_short_name);
    noAreaSelectedView = v.findViewById(R.id.no_area_selected);
    showAreaButton = v.findViewById(R.id.show_area_button);
    plantListAdapter = new PlantSearchAdapter(Collections.emptyList(), this);
    areaPlantListRecycler = v.findViewById(R.id.area_plant_list_recycler);
    areaPlantListRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
    areaPlantListRecycler.setAdapter(plantListAdapter);
    areaPlantListRecycler.addItemDecoration(
        new DividerItemDecoration(areaPlantListRecycler.getContext(),
            DividerItemDecoration.VERTICAL));

    //alternativeLocationContainer = v.findViewById(R.id.alternative_locations_container);

    return v;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }


  @Override
  public void onStart() {
    super.onStart();
    //getDialog().getWindow().setWindowAnimations(R.style.CustomDialogAnim);
    areaViewModel = new ViewModelProvider(this).get(AreaViewModel.class);

    setupUi();
  }

  public void searchPlantsInArea() {

    List<Plant> plantsInArea = DatabaseRetriever.searchPlantsInArea(areaViewModel.selectedArea.areaName);
    plantListAdapter.foundPlantsList = plantsInArea;
    plantListAdapter.notifyDataSetChanged();

  }


  public void setupUi() {
    areaViewModel.setSelectedArea(((MainActivity) requireActivity()).getCurrentSelectedArea());
    if (areaViewModel.selectedArea != null) {
      searchPlantsInArea();

      areaFullNameView.setText(areaViewModel.selectedArea.areaNameLong);
      areaShortNameView.setText(areaViewModel.selectedArea.areaName);

      areaPlantListRecycler.setVisibility(View.VISIBLE);
      areaFullNameView.setVisibility(View.VISIBLE);
      areaShortNameView.setVisibility(View.VISIBLE);
      showAreaButton.setVisibility(View.VISIBLE);
      noAreaSelectedView.setVisibility(View.GONE);
      showAreaButton.setOnClickListener(view -> {
        areaSelectListener.onAreaSelected(areaViewModel.selectedArea.areaName);
        ((MainActivity)requireActivity()).closeDrawer();
      });

    } else {
      areaPlantListRecycler.setVisibility(View.GONE);
      areaFullNameView.setVisibility(View.GONE);
      areaShortNameView.setVisibility(View.GONE);
      showAreaButton.setVisibility(View.GONE);

      noAreaSelectedView.setVisibility(View.VISIBLE);
    }

  }


  public void setAreaSelectListener(AreaSelectListener listener) {
    this.areaSelectListener = listener;
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


    } else {
      Log.e("TAG", "Failed to open Fragment; MainActivity not found");
    }
  }
}