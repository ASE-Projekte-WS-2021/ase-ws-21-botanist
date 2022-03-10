package com.example.urbotanist.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;

public class InfoFragment extends CurrentScreenFragment {
  //exists to give some basic information

  private TextView infoTitle;
  private TextView infoText;
  private Button impButton;
  private TableLayout infoTable;
  private ImageView impArrow;

  public static InfoFragment newInstance() {
    return new InfoFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.info_fragment, container, false);
    infoTitle = (TextView) v.findViewById(R.id.infoTitle);
    infoTable = (TableLayout) v.findViewById(R.id.infoTableOpeningHours);
    infoText = (TextView) v.findViewById(R.id.infoText);
    impButton = (Button) v.findViewById(R.id.impressumButton);
    impArrow = (ImageView) v.findViewById(R.id.impressumArrow);
    return v;
  }


  @Override
  public void onStart() {
    super.onStart();
    impButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        if (!(getResources().getString(R.string.impressum) == infoTitle.getText())) {
          infoTitle.setText(getResources().getString(R.string.impressum));
          infoText.setText(getResources().getString(R.string.impressumContent));
          infoTable.setVisibility(View.GONE);
          impArrow.setRotationY(180);
        } else {
          infoTitle.setText(getResources().getString(R.string.info));
          infoTable.setVisibility(View.VISIBLE);
          infoText.setText(getResources().getString(R.string.infoContent));
          impArrow.setRotationY(0);
        }
      }
    });
  }

}
