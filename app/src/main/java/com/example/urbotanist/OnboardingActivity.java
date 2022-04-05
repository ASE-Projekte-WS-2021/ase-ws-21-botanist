package com.example.urbotanist;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.example.urbotanist.ui.onboarding.OnboardingFragment;

public class OnboardingActivity extends FragmentActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.onboarding_activity);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.container, OnboardingFragment.newInstance())
          .commitNow();
    }
  }
}