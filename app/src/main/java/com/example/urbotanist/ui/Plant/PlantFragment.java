package com.example.urbotanist.ui.plant;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.search.SearchListener;
import io.realm.RealmList;

public class PlantFragment extends Fragment {

  private PlantViewModel plantViewModel;
  private TextView plantFullNameView;
  private TextView plantFamilyNameView;
  private TextView plantTypeNameView;
  private TextView plantCommonNameView;
  private TextView plantGenusNameView;
  private TextView noPlantSelectedView;
  private GridLayout alternativeLocationContainer;
  private SearchListener searchListener;
  private ScrollView plantInfoScrollViewContainer;


  private PlantSelectedListener listener;

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
    alternativeLocationContainer = v.findViewById(R.id.alternative_locations_container);

    return v;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      searchListener = (SearchListener) context;
    } catch (ClassCastException e) {
      Log.e("CastException", "Activity must extend searchListener: " + e.getLocalizedMessage());
    }
  }


  @Override
  public void onStart() {
    super.onStart();
    //getDialog().getWindow().setWindowAnimations(R.style.CustomDialogAnim);
    plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);

    setupUi();
  }


  public void setupUi() {
    plantViewModel.setSelectedPlant(((MainActivity) getActivity()).getCurrentPlant());
    if (plantViewModel.selectedPlant != null) {

      plantFullNameView.setText(plantViewModel.selectedPlant.fullName);
      plantGenusNameView.setText(plantViewModel.selectedPlant.genusName);
      plantFamilyNameView.setText(plantViewModel.selectedPlant.familyName);
      plantTypeNameView.setText(plantViewModel.selectedPlant.typeName);

      plantInfoScrollViewContainer.setVisibility(View.VISIBLE);
      plantFullNameView.setVisibility(View.VISIBLE);
      noPlantSelectedView.setVisibility(View.GONE);


      // plantCommonNameView.setText(mViewModel.selectedPlant.commonName);
      setupAlternativeLocations();
      //the window overlap setup:
      /*Window window = getDialog().getWindow();
      window.setGravity(Gravity.TOP | Gravity.RIGHT);
      int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
      int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
      window.setLayout(width, height);
      WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
      p.y = (int) (getResources().getDisplayMetrics().heightPixels * 0.04);
      getDialog().getWindow().setAttributes(p);
      getDialog().setCanceledOnTouchOutside(true);*/
    } else {
      plantInfoScrollViewContainer.setVisibility(View.GONE);
      plantFullNameView.setVisibility(View.GONE);

      noPlantSelectedView.setVisibility(View.VISIBLE);
    }


  }


  private void setupAlternativeLocations() {
    alternativeLocationContainer.setColumnCount(3);
    RealmList<String> alternativeLocations =
        plantViewModel.selectedPlant.location;

    for (String location : alternativeLocations) {
      Button locationButton = new Button(getContext());
      locationButton.setText(location);
      locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (listener != null) {
                listener.onAreaSelected(location);
              }
            }
        });
      alternativeLocationContainer.addView(locationButton);
    }
  }


  public void setAreaSelectListener(PlantSelectedListener listener) {
    this.listener = listener;
  }


}