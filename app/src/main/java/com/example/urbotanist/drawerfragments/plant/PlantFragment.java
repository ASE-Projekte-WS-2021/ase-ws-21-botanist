package com.example.urbotanist.drawerfragments.plant;

// Sileria , https://sileria.com/

import static com.sileria.android.Resource.getColor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.database.DatabaseRetriever;
import com.example.urbotanist.database.resultlisteners.DbIsPlantFavouriteListener;
import com.example.urbotanist.drawerfragments.area.Area;
import com.example.urbotanist.drawerfragments.area.AreaSelectListener;
// realm by MongoDB, https://realm.io/
import io.realm.RealmList;

public class PlantFragment extends Fragment {

  private MainActivity mainActivity;
  public PlantViewModel plantViewModel;
  private TextView plantFullNameView;
  private TextView plantFamilyNameView;
  private TextView plantTypeNameView;
  private TextView plantCommonNameView;
  private TextView plantGenusNameView;
  private TextView noPlantSelectedView;
  private GridLayout alternativeLocationGrid;
  private ConstraintLayout locationContainer;
  private ScrollView plantInfoScrollViewContainer;
  private AreaSelectListener areaSelectlistener;
  private ImageButton wikiButton;
  private int screenWidth;
  private ImageButton favButton;
  private boolean currentPlantIsFavourite = false;
  private ImageView plantImage;
  private TextView imageLicenseView;
  private Button downloadImageButton;
  private ProgressBar downloadImageSpinner;
  private TextView noImageAvailable;


  public static PlantFragment newInstance() {
    return new PlantFragment();
  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.plant_fragment, container, false);
    plantFullNameView = v.findViewById(R.id.plant_header);
    plantGenusNameView = v.findViewById(R.id.plant_genus_name);
    plantFamilyNameView = v.findViewById(R.id.plant_family_name);
    plantTypeNameView = v.findViewById(R.id.plant_type_name);
    plantCommonNameView = v.findViewById(R.id.plant_common_name);
    noPlantSelectedView = v.findViewById(R.id.no_plant_selected);
    plantInfoScrollViewContainer = v.findViewById(R.id.plant_info_scroll_view_container);
    locationContainer = v.findViewById(R.id.plant_footer);
    alternativeLocationGrid = v.findViewById(R.id.locations_container);
    wikiButton = v.findViewById(R.id.wiki_button);
    plantImage = v.findViewById(R.id.plant_image);
    imageLicenseView = v.findViewById(R.id.image_license_view);
    downloadImageButton = v.findViewById(R.id.download_plant_image_button);
    downloadImageSpinner = v.findViewById(R.id.plant_image_download_spinner);
    noImageAvailable = v.findViewById(R.id.no_plant_image_available);
    favButton = v.findViewById(R.id.fav_button);
    v.post(new Runnable() {
      @Override
      public void run() {

      }
    });

    return v;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
    mainActivity = (MainActivity) requireActivity();
  }

  @Override
  public void onStart() {
    super.onStart();
    DisplayMetrics displayMetrics = new DisplayMetrics();
    requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    screenWidth = displayMetrics.widthPixels;
    setupUi(((MainActivity) requireActivity()).getCurrentPlant());
  }

  /**
   * Connects Views with their layout counterparts and adapters.
   */
  public void setupUi(Plant plant) {

    plantImage.setVisibility(View.GONE);
    imageLicenseView.setVisibility(View.GONE);
    downloadImageSpinner.setVisibility(View.GONE);
    downloadImageButton.setVisibility(View.GONE);
    if (plantViewModel == null) {
      plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);

    }

    plantInfoScrollViewContainer.scrollTo(0, 0);

    plantViewModel.setSelectedPlant(plant);

    if (plantViewModel.selectedPlant != null) {
      plantFullNameView.setText(plantViewModel.selectedPlant.fullName);
      plantGenusNameView.setText(plantViewModel.selectedPlant.genusName);
      plantFamilyNameView.setText(plantViewModel.selectedPlant.familyName);
      plantTypeNameView.setText(plantViewModel.selectedPlant.typeName);
      noImageAvailable.setText("");
      plantViewModel.checkForPlantImage(new ImageDownloadListener() {
        @Override
        public void onImageAvailabilityChecked(boolean isAvailable, boolean needsDownload,
            String imageDownloadUrl,
            String licenseHtmlString, Bitmap image) {
          if (isAvailable) {
            if (!needsDownload) {
              plantImage.setImageBitmap(image);
              imageLicenseView.setText(HtmlCompat.fromHtml(licenseHtmlString, 0));
              plantImage.setVisibility(View.VISIBLE);
              imageLicenseView.setVisibility(View.VISIBLE);
              downloadImageButton.setVisibility(View.GONE);
            } else {
              downloadImageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                  plantViewModel.downloadImage(plantImage, imageLicenseView, downloadImageSpinner,
                      imageDownloadUrl, licenseHtmlString);
                  imageLicenseView.setText(HtmlCompat.fromHtml(licenseHtmlString, 0));
                  downloadImageButton.setVisibility(View.GONE);
                  downloadImageSpinner.setVisibility(View.VISIBLE);

                }
              });

              imageLicenseView.setMovementMethod(LinkMovementMethod.getInstance());
              downloadImageButton.setVisibility(View.VISIBLE);
            }

          } else {
            plantImage.setVisibility(View.GONE);
            imageLicenseView.setVisibility(View.GONE);
            downloadImageSpinner.setVisibility(View.GONE);
            noImageAvailable.setText(getResources().getText(R.string.no_image_available));

          }
        }
      });
      wikiButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Uri uri = Uri
              .parse(plantViewModel.selectedPlant.link); // missing 'http://' will cause crashed
          Intent intent = new Intent(Intent.ACTION_VIEW, uri);
          startActivity(intent);
        }
      });
      favButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          if (currentPlantIsFavourite) {
            DatabaseRetriever.removeFavouritePlant(plantViewModel
                .selectedPlant.id);
            currentPlantIsFavourite = false;
          } else {
            DatabaseRetriever.addFavouritePlant(plantViewModel.selectedPlant);
            currentPlantIsFavourite = true;
          }

          setFavouriteButtonState(currentPlantIsFavourite);
        }
      });
      checkIfPlantIsFavourite();
      setFavouriteButtonState(currentPlantIsFavourite);
      setupPlantCommonNames();

      plantInfoScrollViewContainer.setVisibility(View.VISIBLE);
      locationContainer.setVisibility(View.VISIBLE);
      plantFullNameView.setVisibility(View.VISIBLE);
      noPlantSelectedView.setVisibility(View.GONE);

      setupLocationButtons();
    } else {
      plantInfoScrollViewContainer.setVisibility(View.GONE);
      locationContainer.setVisibility(View.GONE);
      plantFullNameView.setVisibility(View.GONE);

      noPlantSelectedView.setVisibility(View.VISIBLE);
    }
  }


  /**
   * Checks if plant is marked as Favourite and set calls setFavouriteButtonState when check is
   * finished.
   */
  private void checkIfPlantIsFavourite() {
    DatabaseRetriever.checkIfPlantIsFavourite(plantViewModel.selectedPlant,
        new DbIsPlantFavouriteListener() {
          @Override
          public void onIsFavouriteResult(boolean isFavourite) {
            setFavouriteButtonState(isFavourite);
            currentPlantIsFavourite = isFavourite;
          }
        });
  }

  /**
   * Concatenates all Common names to the plant, to be seen in the plantCommonNameView.
   */
  private void setupPlantCommonNames() {
    String allNames = "";
    if (!plantViewModel.selectedPlant.commonName.isEmpty()) {
      for (String name : plantViewModel.selectedPlant.commonName) {
        allNames += name + ", ";
      }
      allNames = allNames.substring(0, allNames.length() - 2);
      plantCommonNameView.setText(allNames);
    } else {
      plantCommonNameView.setText("Nicht vorhanden");
    }
  }

  /**
   * Sets up buttons for all locations that are listed for the current plant in the database.
   */
  private void setupLocationButtons() {
    int buttonColumnCount = 3;
    int gridWidth = (int) (screenWidth * 0.40);
    int gridHeight = (int) (screenWidth * 0.15);
    alternativeLocationGrid.setColumnCount(buttonColumnCount);
    RealmList<String> areasForPlant = plantViewModel.selectedPlant.location;
    RealmList<String> areasForPlantLong = plantViewModel.selectedPlant.locationLong;
    alternativeLocationGrid.removeAllViews();
    int buttonWidth = (int) gridWidth / buttonColumnCount;
    int buttonMargin = (int) (gridWidth * 0.09 / buttonColumnCount);
    int buttonHeight = (int) gridHeight;
    if (areasForPlant.size() > buttonColumnCount) {
      buttonHeight = (int) (buttonHeight * 0.6);
    }
    for (int i = 0; i < areasForPlant.size(); i++) {
      Button areaButton = new Button(plantCommonNameView.getContext());
      areaButton.setBackgroundResource(R.drawable.button);
      ConstraintLayout.LayoutParams params =
          new ConstraintLayout.LayoutParams(buttonWidth, buttonHeight);
      params.setMargins(buttonMargin, (int) (buttonMargin / 2), buttonMargin,
          (int) (buttonMargin / 2));
      areaButton.setContentDescription("alternativeLocationButton");
      areaButton.setLayoutParams(params);
      areaButton.setPadding(0, 0, 0, buttonMargin);
      areaButton.setTextSize(20);
      areaButton.setTextColor(getColor(R.color.green));
      areaButton.setOutlineAmbientShadowColor(Color.TRANSPARENT);
      areaButton.setOutlineSpotShadowColor(Color.TRANSPARENT);
      String areaShortName = areasForPlant.get(i);
      String areaLongName = areasForPlantLong.get(i);
      areaButton.setText(areasForPlant.get(i));
      areaButton.setOnTouchListener((v, event) -> {
        if (event.getAction() == MotionEvent.ACTION_UP) {
          areaButton.setTextColor(getColor(R.color.green));
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
          areaButton.setTextColor(getColor(R.color.white));
        }
        return false;
      });
      areaButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          areaButton.setTextColor(plantCommonNameView.getContext().getColor(R.color.white));
          if (areaSelectlistener != null) {
            Area areaObject = new Area(areaShortName, areaLongName);
            ((MainActivity) requireActivity()).setCurrentSelectedArea(areaObject);
            ((MainActivity) requireActivity()).loadCurrentDrawerFragment(
                ((MainActivity) requireActivity()).areaDrawerFragment);
          }
        }
      });
      alternativeLocationGrid.addView(areaButton);
    }
  }

  /**
   * Changes the Favourite buttons image according to its status.
   *
   * @param isFavourite if plant is favourite
   */
  public void setFavouriteButtonState(boolean isFavourite) {
    if (isFavourite) {
      favButton.setBackground(mainActivity.getDrawable(R.drawable.ic_fav_wb_n2));
    } else {
      favButton.setBackground(mainActivity.getDrawable(R.drawable.ic_fav_wb));
    }
  }

  public void setAreaSelectListener(AreaSelectListener listener) {
    this.areaSelectlistener = listener;
  }

}