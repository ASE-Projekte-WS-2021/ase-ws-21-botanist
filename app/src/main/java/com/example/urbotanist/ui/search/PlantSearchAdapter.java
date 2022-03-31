package com.example.urbotanist.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.plant.Plant;
import java.util.List;

public class PlantSearchAdapter extends RecyclerView.Adapter<PlantSearchAdapter.ViewHolder> {

  public List<Plant> foundPlantsList;
  SearchResultClickListener searchResultClickListener;


  public PlantSearchAdapter(List<Plant> foundPlantsList,
      SearchResultClickListener searchResultClickListener) {
    this.foundPlantsList = foundPlantsList;
    this.searchResultClickListener = searchResultClickListener;
  }

  // Create new views (invoked by the layout manager)
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    // Create a new view, which defines the UI of the list item
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.search_result_item, viewGroup, false);
    return new ViewHolder(view);
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(ViewHolder viewHolder, final int position) {

    // Get element from your dataset at this position and replace the
    // contents of the view with that element

    //SpannableString fullName = new SpannableString(localDataSet.get(position).fullName);
    //fullName.setSpan(new UnderlineSpan(), 0, fullName.length(), 0);
    viewHolder.getAllViews()[0].setText(foundPlantsList.get(position).fullName);
    if (!foundPlantsList.get(position).commonName.isEmpty()) {
      String allNames = "";
      for (String name : foundPlantsList.get(position).commonName) {
        allNames += name + ", ";
      }
      allNames = allNames.substring(0, allNames.length() - 2);
      viewHolder.getAllViews()[1].setText("(" + allNames + ")");
      viewHolder.getAllViews()[1].setVisibility(View.VISIBLE);
    } else {
      viewHolder.getAllViews()[1].setVisibility(View.GONE);
    }
    viewHolder.getAllViews()[2]
        .setText("Familie der \"" + foundPlantsList.get(position).familyName + "\"");

    //for all textViews
    for (int i = 0; i < viewHolder.getAllViews().length; i++) {
      viewHolder.getAllViews()[i].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          searchResultClickListener
              .onPlantSelectedListener(foundPlantsList.get(viewHolder.getAdapterPosition()));
        }
      });
    }
  }

  // Return the size of your dataset (invoked by the layout manager)
  @Override
  public int getItemCount() {
    return foundPlantsList.size();
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

    public TextView[] getAllViews() {
      TextView[] allTextViews = new TextView[3];
      allTextViews[0] = fullNameView;
      allTextViews[1] = commonNameView;
      allTextViews[2] = familyNameView;
      return allTextViews;
    }
  }

}
