package com.example.urbotanist.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;

public class FavoritesFragment extends CurrentScreenFragment {

  private FavoritesViewModel favoritesViewModel;

  public static FavoritesFragment newInstance() {
    return new FavoritesFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.inventory_favorites_fragment, container, false);
  }

  @Override
  public void onStart() {
    super.onStart();
    favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
  }

}