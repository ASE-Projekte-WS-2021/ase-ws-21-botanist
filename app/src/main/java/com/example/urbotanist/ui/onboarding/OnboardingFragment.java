package com.example.urbotanist.ui.onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.leanback.app.OnboardingSupportFragment;

import com.example.urbotanist.R;

public class OnboardingFragment extends OnboardingSupportFragment{
    private String[] mTitles;
    private String[] mDescriptions;

    public static OnboardingFragment newInstance() {
        return new OnboardingFragment();
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        mTitles = new String[]{"1", "2"};
        mDescriptions = new String[]{"1", "2"};
        //mDescriptions = getResources().getStringArray(R.array.onboarding_page_descriptions);
    }

    @Override
    protected int getPageCount() {
        return mTitles.length;
    }

    @Override
    protected CharSequence getPageTitle(int pageIndex) {
        return mTitles[pageIndex];
    }

    @Override
    protected CharSequence getPageDescription(int pageIndex) {
        return mDescriptions[pageIndex];
    }

    @Nullable
    @Override
    protected View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Nullable
    @Override
    protected View onCreateForegroundView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Override
    protected void onFinishFragment() {
        super.onFinishFragment();
        // User has seen OnboardingSupportFragment, so mark our SharedPreferences
        // flag as completed so that we don't show our OnboardingSupportFragment
        // the next time the user launches the app.
        /*
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        sharedPreferencesEditor.putBoolean(
                COMPLETED_ONBOARDING_PREF_NAME

                , true);
        sharedPreferencesEditor.apply();

         */
    }

}