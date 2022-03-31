package com.example.urbotanist.ui.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.plant.Plant;
import com.example.urbotanist.ui.search.DatabaseListener;
import com.example.urbotanist.ui.search.SearchResultClickListener;
import java.util.List;

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.ViewHolder> {

  List<FavouritePlant> favouritePlantsList;
  SearchResultClickListener searchResultClickListener;


  public FavouriteListAdapter(List<FavouritePlant> favouritePlantsList,
      SearchResultClickListener searchResultClickListener) {
    this.favouritePlantsList = favouritePlantsList;
    this.searchResultClickListener = searchResultClickListener;

  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.favourite_list_item, viewGroup, false);
    return new FavouriteListAdapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    // Get element from your dataset at this position and replace the
    // contents of the view with that element
    Plant plant = favouritePlantsList.get(position).plant;
    viewHolder.fullNameView.setText(plant.fullName);

    // Set up all Common Names for that plant
    if (!plant.commonName.isEmpty()) {
      String allNames = "";
      for (String name : plant.commonName) {
        allNames += name + ", ";
      }
      allNames = allNames.substring(0, allNames.length() - 2);

      viewHolder.commonNameView.setText("(" + allNames + ")");
      viewHolder.commonNameView.setVisibility(View.VISIBLE);
    } else {
      viewHolder.commonNameView.setVisibility(View.GONE);
    }

    // Set up family name View
    viewHolder.familyNameView
        .setText(viewHolder.itemView.getContext()
            .getString(R.string.family_of_string, plant.familyName));

    // Connect the itemView to the onPlantSelectedListener
    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        searchResultClickListener
            .onPlantSelectedListener(plant);
      }
    });

  }

  @Override
  public int getItemCount() {
    return favouritePlantsList.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    private final TextView fullNameView;
    private final TextView commonNameView;
    private final TextView familyNameView;

    public ViewHolder(View view) {
      super(view);
      // Define click listener for the ViewHolder's View
      fullNameView = (TextView) view.findViewById(R.id.display_full_name);
      commonNameView = (TextView) view.findViewById(R.id.display_common_name);
      familyNameView = (TextView) view.findViewById(R.id.display_family_name);
    }
  }

}
