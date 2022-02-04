package com.example.urbotanist.ui.Plant;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;

public class PlantFragment extends CurrentScreenFragment {

    private PlantViewModel mViewModel;
    private TextView plantFullNameView;
    private TextView plantFamilyNameView;
    private TextView plantTypeNameView;
    private TextView plantLocationView;
    private TextView plantCommonNameView;
    private TextView plantGenusNameView;

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
        plantLocationView = v.findViewById(R.id.plant_location);
        plantCommonNameView = v.findViewById(R.id.plant_common_name);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        mViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        mViewModel.setSelectedPlant(((MainActivity)getActivity()).getCurrentPlant());
        setupUiText();

    }

    public void setupUiText(){
        Log.d("test",mViewModel.selectedPlant.toString());
        plantFullNameView.setText(mViewModel.selectedPlant.fullName);
        plantGenusNameView.setText(mViewModel.selectedPlant.genusName);
        plantFamilyNameView.setText(mViewModel.selectedPlant.familyName);
        plantTypeNameView.setText(mViewModel.selectedPlant.typeName);
        plantLocationView.setText(mViewModel.selectedPlant.location);
        plantCommonNameView.setText(mViewModel.selectedPlant.commonName);
    }

}