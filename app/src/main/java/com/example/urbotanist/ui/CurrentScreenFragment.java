package com.example.urbotanist.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;

public abstract class CurrentScreenFragment extends Fragment {

  public CurrentScreenFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


  }

  @Override
  public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState);


  @Override
  public void onStart() {
    super.onStart();
    //TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,0);
    AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
    alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    alphaAnimation.setDuration(500);
    getView().startAnimation(alphaAnimation);
  }
}