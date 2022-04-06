package com.example.urbotanist.ui.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.OnboardingSupportFragment;

import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.StartupActivity;


/**
 * Tutorial used: https://developer.android.com/training/tv/playback/onboarding
 */
public class OnboardingFragment extends OnboardingSupportFragment {

  private String[] mTitles;
  private String[] mDescriptions;
  private int[] mImages;

  private View view;

  /**
   * Constructor
   * @return OnboardingFragment
   */
  public static OnboardingFragment newInstance() {
    return new OnboardingFragment();
  }

  /**
   * Function is called when the Fragment is attached.
   * Sets text Objects and other elements.
   * @param context
   */
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mTitles = new String[]{ getResources().getText(R.string.onboarding_title_page_1).toString(),
            getResources().getText(R.string.onboarding_title_page_2).toString(),
            getResources().getText(R.string.onboarding_title_page_3).toString() };
    mDescriptions = new String[]{getResources().getText(R.string.onboarding_description_page_1).toString(),
            getResources().getText(R.string.onboarding_description_page_2).toString(),
            getResources().getText(R.string.onboarding_description_page_3).toString() };
    mImages = new int[] {R.drawable.botanist_icon, R.drawable.onboarding_search, R.drawable.onboarding_map};

    setDotBackgroundColor(ContextCompat.getColor(context,R.color.green));
    setArrowColor(ContextCompat.getColor(context,R.color.light_green));
    setStartButtonText(getResources().getText(R.string.onboarding_button));
  }

  /**
   * @return Function returns the number of pages in the OnboardingFragment
   */
  @Override
  protected int getPageCount() {
    return mTitles.length;
  }

  /**
   * Function is called on the Creation of the OnboardingFragment and sets the BackgroundView
   * @param inflater
   * @param container
   * @return Function returns a view
   */
  @Nullable
  @Override
  protected View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container) {
    view = inflater.inflate(R.layout.onboarding_fragment, container, false);
    setViewDetails();
    return view;
  }

  /**
   * Function is called when the page in the Onboarding Fragment is changed.
   * @param newPage = the index of the page that the fragment has been changed to.
   * @param previousPage = the index of the page that the fragment has previously been.
   */
  @Override
  protected void onPageChanged(int newPage, int previousPage) {
    super.onPageChanged(newPage, previousPage);
    setViewDetails();
  }

  /**
   * Function sets the different View Elements of the different Onboarding Pages.
   */
  private void setViewDetails() {
    TextView descriptionText = view.findViewById(R.id.onboarding_description);
    descriptionText.setText(mDescriptions[getCurrentPageIndex()]);
    TextView titleText = view.findViewById(R.id.onboarding_title);
    titleText.setText(mTitles[getCurrentPageIndex()]);
    ImageView image = view.findViewById(R.id.onboarding_image);
    image.setImageResource(mImages[getCurrentPageIndex()]);
  }

  @Override
  protected CharSequence getPageTitle(int pageIndex) {
    return null;
  }

  @Override
  protected CharSequence getPageDescription(int pageIndex) {
    return null;
  }

  @Nullable
  @Override
  protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) { return null; }

  @Nullable
  @Override
  protected View onCreateForegroundView(LayoutInflater inflater, ViewGroup container) { return null; }

  /**
   * Function is called when the Onboarding Fragment is finished.
   * Onboarding is flagged as seen and starts the MainActivity
   */
  @Override
  protected void onFinishFragment() {
    super.onFinishFragment();
    SharedPreferences startupPreferences = requireActivity().getSharedPreferences("startupPreferences", Context.MODE_PRIVATE);
    startupPreferences.edit().putBoolean("ONBOARDING_SEEN", true).apply();

    Intent intent = new Intent(requireActivity(), MainActivity.class);
    startActivity(intent);
    getActivity().finish();
  }

}