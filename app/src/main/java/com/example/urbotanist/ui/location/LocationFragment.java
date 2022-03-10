package com.example.urbotanist.ui.location;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;

public class LocationFragment extends Fragment {

  private LocationViewModel locationViewModel;
  private TextView locationFullNameView;
  private TextView noLocationSelectedView;
  private ScrollView locationInfoScrollViewContainer;


  private LocationSelectedListener listener;

  public static LocationFragment newInstance() {
    return new LocationFragment();
  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.location_fragment, container, false);
    locationFullNameView = v.findViewById(R.id.location_header);
    noLocationSelectedView = v.findViewById(R.id.no_location_selected);
    locationInfoScrollViewContainer = v.findViewById(R.id.location_info_scroll_view_container);
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
    locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

    setupUi();
  }


  public void setupUi() {
    locationViewModel.setSelectedPlant(((MainActivity) getActivity()).getCurrentLocation());
    if (locationViewModel.selectedLocation != null) {

      locationFullNameView.setText(locationViewModel.selectedLocation.fullName);

      locationInfoScrollViewContainer.setVisibility(View.VISIBLE);
      locationFullNameView.setVisibility(View.VISIBLE);
      noLocationSelectedView.setVisibility(View.GONE);

    } else {
      locationInfoScrollViewContainer.setVisibility(View.GONE);
      locationFullNameView.setVisibility(View.GONE);

      noLocationSelectedView.setVisibility(View.VISIBLE);
    }

  }


  public void setAreaSelectListener(LocationSelectedListener listener) {
    this.listener = listener;
  }
}