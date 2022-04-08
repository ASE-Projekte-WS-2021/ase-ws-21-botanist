package com.example.urbotanist.onboarding;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.onboarding.OnboardingFragment;

public class OnboardingActivity extends FragmentActivity {

  /**
   * Onboarding Activity used as parent for the Onboarding Fragment.
   *
   * @param savedInstanceState Saved Instance of this Activity
   */
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

