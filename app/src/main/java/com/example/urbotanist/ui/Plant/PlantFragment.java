package com.example.urbotanist.ui.Plant;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.Search.SearchListener;

import java.util.ArrayList;

public class PlantFragment extends DialogFragment{

    private PlantViewModel mViewModel;
    private TextView plantFullNameView;
    private TextView plantFamilyNameView;
    private TextView plantTypeNameView;
    private Button plantLocationButton;
    private TextView plantCommonNameView;
    private TextView plantGenusNameView;
    private LinearLayout alternativeLocationContainer;
    private SearchListener searchListener;


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
        plantLocationButton = v.findViewById(R.id.plant_location);
        plantCommonNameView = v.findViewById(R.id.plant_common_name);
        alternativeLocationContainer = v.findViewById(R.id.alternative_locations_container);


        plantLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAreaSelected(mViewModel.selectedPlant.location);
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            searchListener = (SearchListener) context;
        }catch (ClassCastException e){
            Log.e("CastException", "Activity must extend searchListener: " + e.getLocalizedMessage());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setWindowAnimations(R.style.CustomDialogAnim);
        mViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        mViewModel.setSelectedPlant(((MainActivity)getActivity()).getCurrentPlant());
        setupUi();
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP|Gravity.RIGHT);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        window.setLayout(width, height);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.y = (int)(getResources().getDisplayMetrics().heightPixels*0.04);
        getDialog().getWindow().setAttributes(p);
        getDialog().setCanceledOnTouchOutside(true);
        /*TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        getView().setAnimation(animation);
        animation.setDuration(500);
        animation.start();*/

    }

    public void closeWindow() {
        getDialog().cancel();
    }

    public void setupUi(){
        Log.d("test",mViewModel.selectedPlant.toString());
        plantFullNameView.setText(mViewModel.selectedPlant.fullName);
        plantGenusNameView.setText(mViewModel.selectedPlant.genusName);
        plantFamilyNameView.setText(mViewModel.selectedPlant.familyName);
        plantTypeNameView.setText(mViewModel.selectedPlant.typeName);

        plantLocationButton.setText(mViewModel.selectedPlant.location);
        plantCommonNameView.setText(mViewModel.selectedPlant.commonName);
        setupAlternativeLocations();

    }

    private void setupAlternativeLocations() {
        ArrayList<String> alternativeLocations = searchListener.searchLocations(mViewModel.selectedPlant.genusName, mViewModel.selectedPlant.typeName);
        for(String location : alternativeLocations){
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