package com.example.urbotanist.ui.info;

import androidx.fragment.app.DialogFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;

public class InfoFragment extends CurrentScreenFragment {

//    private InfoViewModel mViewModel;
    private TextView infoTitle;
    private TextView infoText;
    private Button impButton;

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.info_fragment, container, false);
        infoTitle = (TextView) v.findViewById(R.id.infoTitle);
        infoText = (TextView) v.findViewById(R.id.infoText);
        impButton = (Button)v.findViewById(R.id.impressumButton);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        impButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!(getResources().getString(R.string.impressum) == infoTitle.getText())) {
                    infoTitle.setText(getResources().getString(R.string.impressum));
                    infoText.setText(getResources().getString(R.string.impressumContent));
                    impButton.setText(getResources().getString(R.string.info));
                } else {
                    infoTitle.setText(getResources().getString(R.string.info));
                    infoText.setText(getResources().getString(R.string.infoContent));
                    impButton.setText(getResources().getString(R.string.impressum));
                }
            }
        });
//        mViewModel = new ViewModelProvider(this).get(InfoViewModel.class);
    }

}
