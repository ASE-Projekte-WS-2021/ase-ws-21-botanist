package com.example.urbotanist.ui.Plant;

import static android.view.WindowManager.*;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;

public class PlantFragment extends DialogFragment{

    private PlantViewModel mViewModel;
    private TextView plantFullNameView;
    private TextView plantFamilyNameView;
    private TextView plantTypeNameView;
    private Button plantLocationButton;
    private TextView plantCommonNameView;
    private TextView plantGenusNameView;

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

        plantLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPlantSelected(mViewModel.selectedPlant.location);
                }
            }
        });

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setWindowAnimations(R.style.CustomDialogAnim);
        mViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        mViewModel.setSelectedPlant(((MainActivity)getActivity()).getCurrentPlant());
        setupUiText();
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP|Gravity.RIGHT);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.75);
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

    public void setupUiText(){
        Log.d("test",mViewModel.selectedPlant.toString());
        plantFullNameView.setText(mViewModel.selectedPlant.fullName);
        plantGenusNameView.setText(mViewModel.selectedPlant.genusName);
        plantFamilyNameView.setText(mViewModel.selectedPlant.familyName);
        plantTypeNameView.setText(mViewModel.selectedPlant.typeName);
        plantLocationButton.setText(mViewModel.selectedPlant.location);
        plantCommonNameView.setText(mViewModel.selectedPlant.commonName);
    }

    public void setPlantSelectListener(PlantSelectedListener listener) {
        this.listener = listener;
    }



}