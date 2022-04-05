package com.example.urbotanist;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.urbotanist.ui.onboarding.OnboardingFragment;

public class OnboardingActivity extends AppCompatActivity {

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