package com.example.urbotanist.ui.Search;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urbotanist.MainActivity;
import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;
import com.example.urbotanist.ui.Plant.Plant;

import java.util.Collections;
import java.util.List;

public class SearchFragment extends CurrentScreenFragment implements SearchResultClickListener {

    private SearchViewModel mViewModel;
    private SearchView searchView;
    private SearchListener searchListener;
    private RecyclerView searchListRecycler;
    private PlantSearchAdapter searchListAdapter;
    private String TAG = this.getClass().getSimpleName();

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            searchListener = (SearchListener) context;
        }
        catch(ClassCastException castException){
            Log.e("castException","Activity must extend SearchListener:" + castException.getLocalizedMessage());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, container, false);
        searchView = v.findViewById(R.id.search_bar);
        searchListRecycler = v.findViewById(R.id.searchListRecycler);
        searchListAdapter = new PlantSearchAdapter(Collections.emptyList(),this::onSearchResultClick);
        searchListRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
        searchListRecycler.setAdapter(searchListAdapter);
        searchListRecycler.addItemDecoration(new DividerItemDecoration(searchListRecycler.getContext(), DividerItemDecoration.VERTICAL));
        initSearch();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }

    public void initSearch(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Plant> foundPlants =  searchListener.searchPlant(query);
                //searchListAdapter = new PlantSearchAdapter(plantNames);
                searchListAdapter.localDataSet = foundPlants;
                searchListAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Plant> foundPlants =  searchListener.searchPlant(newText);
                //searchListAdapter = new PlantSearchAdapter(plantNames);
                searchListAdapter.localDataSet = foundPlants;
                searchListAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public void onSearchResultClick(Plant plant) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null) {
            mainActivity.setCurrentPlant(plant);
            mainActivity.plantFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
            mainActivity.plantFragment.show(mainActivity.getSupportFragmentManager(), "Info");
            //mainActivity.loadCurrentScreenFragment(mainActivity.plantFragment);

        }
        else{
            Log.e("TAG", "Failed to open Fragment; MainActivity not found");
        }
    }
}