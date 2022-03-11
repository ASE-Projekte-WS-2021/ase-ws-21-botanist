package com.example.urbotanist.ui.plant;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.area.Area;
import com.example.urbotanist.ui.area.AreaSelectListener;
import com.example.urbotanist.ui.search.SearchListener;
import io.realm.RealmList;

public class PlantFragment extends Fragment {

  public PlantViewModel plantViewModel;
  private TextView plantFullNameView;
  private TextView plantFamilyNameView;
  private TextView plantTypeNameView;
  private TextView plantCommonNameView;
  private TextView plantGenusNameView;
  private TextView noPlantSelectedView;
  private GridLayout alternativeLocationGrid;
  private ConstraintLayout locationContainer;
  private SearchListener searchListener;
  private ScrollView plantInfoScrollViewContainer;
  private AreaSelectListener areaSelectlistener;

  public static PlantFragment newInstance() {
    return new PlantFragment();
  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.plant_fragment, container, false);
    plantFullNameView = v.findViewById(R.id.plant_header);
    plantGenusNameView = v.findViewById(R.id.plant_genus_name);
    plantFamilyNameView = v.findViewById(R.id.plant_family_name);
    plantTypeNameView = v.findViewById(R.id.plant_type_name);
    plantCommonNameView = v.findViewById(R.id.plant_common_name);
    noPlantSelectedView = v.findViewById(R.id.no_plant_selected);
    plantInfoScrollViewContainer = v.findViewById(R.id.plant_info_scroll_view_container);
    locationContainer = v.findViewById(R.id.plant_footer);
    alternativeLocationGrid = v.findViewById(R.id.alternative_locations_container);

    return v;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);

  }


  @Override
  public void onStart() {
    super.onStart();
    setupUi(((MainActivity) requireActivity()).getCurrentPlant());
    //getDialog().getWindow().setWindowAnimations(R.style.CustomDialogAnim);

  }


  public void setupUi(Plant plant) {
    if  (plantViewModel == null) {
      plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
    }
    plantViewModel.setSelectedPlant(plant);
    if (plantViewModel.selectedPlant != null) {

      plantFullNameView.setText(plantViewModel.selectedPlant.fullName);
      plantGenusNameView.setText(plantViewModel.selectedPlant.genusName);
      plantFamilyNameView.setText(plantViewModel.selectedPlant.familyName);
      plantTypeNameView.setText(plantViewModel.selectedPlant.typeName);
      setupPlantCommonNames();

      plantInfoScrollViewContainer.setVisibility(View.VISIBLE);
      locationContainer.setVisibility(View.VISIBLE);
      plantFullNameView.setVisibility(View.VISIBLE);
      noPlantSelectedView.setVisibility(View.GONE);

      setupAlternativeLocations();
    } else {
      plantInfoScrollViewContainer.setVisibility(View.GONE);
      locationContainer.setVisibility(View.GONE);
      plantFullNameView.setVisibility(View.GONE);

      noPlantSelectedView.setVisibility(View.VISIBLE);
    }

  }

  private void setupPlantCommonNames() {
    String allNames = "";
    if (!plantViewModel.selectedPlant.commonName.isEmpty()) {
      for (String name : plantViewModel.selectedPlant.commonName) {
        allNames += name + ", ";
      }
      allNames = allNames.substring(0, allNames.length() - 2);
      plantCommonNameView.setText(allNames);
    } else {
      plantCommonNameView.setText("Nicht vorhanden");
    }
  }
  
  private void setupAlternativeLocations() {
    alternativeLocationGrid.setColumnCount(3);
    RealmList<String> areasForPlant = plantViewModel.selectedPlant.location;
    RealmList<String> areasForPlantLong = plantViewModel.selectedPlant.locationLong;
    alternativeLocationGrid.removeAllViews();
    for (int i = 0; i < areasForPlant.size();i++) {
      Button areaButton = new Button(plantCommonNameView.getContext());
      String  areaShortName = areasForPlant.get(i);
      String areaLongName = areasForPlantLong.get(i);
      areaButton.setText(areasForPlant.get(i));
      areaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (areaSelectlistener != null) {
                Area areaObject = new Area(areaShortName, areaLongName);
                areaSelectlistener.onAreaSelected(areaShortName);
                ((MainActivity)requireActivity()).setCurrentSelectedArea(areaObject);
                ((MainActivity)requireActivity()).loadCurrentDrawerFragment(
                    ((MainActivity)requireActivity()).areaDrawerFragment);
              }
            }
        });
      alternativeLocationGrid.addView(areaButton);
    }
  }

  public void setAreaSelectListener(AreaSelectListener listener) {
    this.areaSelectlistener = listener;
  }
}