package com.example.urbotanist.ui.info;

import static android.view.animation.AnimationUtils.loadAnimation;
import static com.sileria.android.Resource.getColor;
import static com.sileria.android.Resource.getInteger;

import static io.realm.Realm.getApplicationContext;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;

public class InfoFragment extends CurrentScreenFragment {
  //exists to give some basic information

  private TextView infoTitle;
  private TextView infoText;
  private ImageButton impButton;
  private TableLayout infoTable;
  private ImageView impArrow;
  private ScrollView scrollidoli;
  private ScrollView scrollidoli2;
  private ConstraintLayout titleConstraint;
  private int fragmentWidth;

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
    scrollidoli = (ScrollView) v.findViewById(R.id.scrollView2);
    scrollidoli2 = (ScrollView) v.findViewById(R.id.scrollView3);
    titleConstraint = (ConstraintLayout) v.findViewById(R.id.titleConstraint);
    v.post(new Runnable() {
      @Override
      public void run() {
        fragmentWidth = v.getMeasuredWidth();
      }
    });
    return v;
  }


  @SuppressWarnings("checkstyle:Indentation")
  @SuppressLint("ClickableViewAccessibility")
  @Override
  public void onStart() {
    super.onStart();
    impButton.setOnTouchListener((v, event) -> {
      if (event.getAction() == MotionEvent.ACTION_UP) {
        impArrow.getBackground().setTint(getColor(R.color.green));

      } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
        impArrow.getBackground().setTint(getColor(R.color.white));
      }
      return false;
    });
    impButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        titleConstraint.startAnimation(loadAnimation(getApplicationContext(),R.anim.anim_title_out));
        Handler handler = new Handler();
        if (!(getResources().getString(R.string.impressum) == infoTitle.getText())) {
          handler.postDelayed(() -> {
            infoTitle.setText(getResources().getString(R.string.impressum));
            titleConstraint.startAnimation(loadAnimation(getApplicationContext(),R.anim.anim_title_in));
          }, getInteger(android.R.integer.config_shortAnimTime));
          impArrow.setRotationY(180);
          slideIn(scrollidoli);
          slideIn(scrollidoli2);
        } else {
          handler.postDelayed(() -> {
            infoTitle.setText(getResources().getString(R.string.info));
            titleConstraint.startAnimation(loadAnimation(getApplicationContext(),R.anim.anim_title_in));
          }, getInteger(android.R.integer.config_shortAnimTime));
          impArrow.setRotationY(0);
          slideOut(scrollidoli);
          slideOut(scrollidoli2);
        }
      }
    });
  }

  private void slideIn(View fadeView) {
    fadeView.startAnimation(loadAnimation(getApplicationContext(),R.anim.animifnforight));
    fadeView.setX(fadeView.getX()-fragmentWidth);
  }

  private void slideOut(View fadeView) {
    /*TranslateAnimation trans = new TranslateAnimation(0, -fragmentWidth, 0, 0);
    trans.setDuration(250);
    fadeView.startAnimation(trans);*/
    fadeView.startAnimation(loadAnimation(getApplicationContext(),R.anim.animifnfoleft));
    fadeView.setX(fadeView.getX()+fragmentWidth);
  }

  private void slideTitle(View fadeView){
  }

}
