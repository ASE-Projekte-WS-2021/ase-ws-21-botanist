package com.example.urbotanist.ui.info;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
  private ImageButton impButton;
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
    impButton = (ImageButton) v.findViewById(R.id.impressumButton);
    impArrow = (ImageView) v.findViewById(R.id.impressumArrow);
    return v;
  }


  @Override
  public void onStart() {
    super.onStart();
    impButton.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
          impArrow.getBackground().setTint(getContext().getColor(R.color.green));

      }
      else if(event.getAction() == MotionEvent.ACTION_DOWN){
          impArrow.getBackground().setTint(getContext().getColor(R.color.light_grey));
      }
        return false;
      }
    });
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
