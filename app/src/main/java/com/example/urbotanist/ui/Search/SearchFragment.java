package com.example.urbotanist.ui.Search;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urbotanist.R;
import com.example.urbotanist.ui.CurrentScreenFragment;
import com.example.urbotanist.ui.Plant.Plant;

import java.util.List;

public class SearchFragment extends CurrentScreenFragment {

    private SearchViewModel mViewModel;
    private SearchView searchView;
    private SearchListener searchListener;
    private RecyclerView searchListRecycler;
    private PlantSearchAdapter searchListAdapter;

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
        searchListAdapter = new PlantSearchAdapter(new String[0]);
        searchListRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
        searchListRecycler.setAdapter(searchListAdapter);
        initSearch();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        initSearch();
    }


    public void initSearch(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Plant> foundPlants =  searchListener.searchPlant(newText);
                String[] plantNames = new String[foundPlants.size()];
                for(int i = 0; i < foundPlants.size(); i++){
                    //Log.d("searchrestults:", plant.full_Name);
                    plantNames[i] = foundPlants.get(i).full_Name;
                }
                //searchListAdapter = new PlantSearchAdapter(plantNames);
                searchListAdapter.localDataSet = plantNames;
                searchListAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }
}