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
import android.widget.ProgressBar;
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
import com.example.urbotanist.database.resultlisteners.DbPlantFoundListener;
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
  private ProgressBar plantInAreaSearchOngoingSpinner;



  private AreaSelectListener areaSelectListener;

  public static AreaFragment newInstance() {
    return new AreaFragment();
  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.area_fragment, container, false);
    setupViews(v);

    return v;
  }

  @Override
  public void onStart() {
    super.onStart();
    areaViewModel = new ViewModelProvider(this).get(AreaViewModel.class);
    setupUi();
  }


  /**
   * Connects Views with their layout counterparts and adapters
   */
  public void setupViews(View v) {
    plantInAreaSearchOngoingSpinner = v.findViewById(R.id.area_plant_search_ongoing_spinner);
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
  }


  /**
   * Sets up the views according to the state of the ViewModel
   */
  public void setupUi() {
    areaViewModel.setSelectedArea(((MainActivity) requireActivity()).getCurrentSelectedArea());
    if (areaViewModel.selectedArea != null) {
      plantListAdapter.foundPlantsList = areaViewModel.searchPlantsInArea();
      plantListAdapter.notifyDataSetChanged();
      areaFullNameView.setText(areaViewModel.selectedArea.areaNameLong);
      areaShortNameView.setText(areaViewModel.selectedArea.areaName);
      areaPlantListRecycler.setVisibility(View.VISIBLE);
      areaFullNameView.setVisibility(View.VISIBLE);
      areaShortNameView.setVisibility(View.VISIBLE);
      showAreaButton.setVisibility(View.VISIBLE);
      noAreaSelectedView.setVisibility(View.GONE);
      showAreaButton.setOnClickListener(view -> {
        areaSelectListener.onAreaSelected(areaViewModel.selectedArea.areaName);
        ((MainActivity) requireActivity()).closeDrawer();
      });
    } else {
      areaPlantListRecycler.setVisibility(View.GONE);
      areaFullNameView.setVisibility(View.GONE);
      areaShortNameView.setVisibility(View.GONE);
      showAreaButton.setVisibility(View.GONE);

      noAreaSelectedView.setVisibility(View.VISIBLE);
    }

  }


  /**
   * Set an Listener that is called when a area is selected
   * @param listener Is called when an area ist selected
   */
  public void setAreaSelectListener(AreaSelectListener listener) {
    this.areaSelectListener = listener;
  }


  /**
   * Listener Method for clicks in the areaPlantListRecycler
   *
   * @param plant The plant that was selected from the RecyclerView
   */
  @Override
  public void onPlantSelectedListener(Plant plant) {
    MainActivity mainActivity = (MainActivity) getActivity();
    if (mainActivity != null) {
      mainActivity.setCurrentPlant(plant, true);
      View view = mainActivity.getCurrentFocus();

      //Close the Keyboard if possible
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