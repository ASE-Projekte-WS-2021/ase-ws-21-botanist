package com.example.urbotanist.ui.onboarding;

import android.content.Context;
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

import com.example.urbotanist.R;

public class OnboardingFragment extends OnboardingSupportFragment {

  private String[] mTitles;
  private String[] mDescriptions;
  private int[] mImages;

  private View view;

  public static OnboardingFragment newInstance() {
    return new OnboardingFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mTitles = new String[]{"URBotanist", "Funktionen", "Erklärungen"};
    mDescriptions = new String[]{"Willkommen in der App URBotanist, dem kleinen handlichen Begleiter für Ihren Besuch im botanischen Garten der Universität Regensburg",
                                 "In dieser App können Sie Ihren Standort im botanischen Garten einsehen, nach Pflanzen suchen und über sie lernen. Ebenso können Sie andere Pflanzen im selben Bereich erkunden und Ihre Favoriten speichern.",
                                 "Kurz vorab: Die Kürzel der Bereiche sind dieselben Kürzel, die auch auf den Karten im botanischen Garten gefunden werden können, also wenn ein Bereich dort besonders gefällt, kann man ihn in der App erkunden!"};
    mImages = new int[] {R.drawable.botanist_icon, R.drawable.fav_button, R.drawable.botanist_icon};
    //mDescriptions = getResources().getStringArray(R.array.onboarding_page_descriptions);
    setDotBackgroundColor(ContextCompat.getColor(context,R.color.green));
    setArrowColor(ContextCompat.getColor(context,R.color.light_green));
    setStartButtonText("Auf geht's!");
  }

  @Override
  protected int getPageCount() {
    return mTitles.length;
  }

  @Nullable
  @Override
  protected View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container) {
    view = inflater.inflate(R.layout.onboarding_fragment, container, false);
    setViewDetails();
    return view;
  }

  @Override
  protected void onPageChanged(int newPage, int previousPage) {
    super.onPageChanged(newPage, previousPage);
    setViewDetails();
  }

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
    getActivity().finish();
  }

}