package com.usf_mobile_dev.filmfriend.ui.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.HistoryRecyclerViewAdapter;
import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.R;

import java.util.List;

public class DiscoverFragment extends Fragment {

    private DiscoverViewModel discoverViewModel;
    private RecyclerView discoverRecyclerView;
    Spinner spinnerMileRadius;
    String[] mileRadius;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        discoverViewModel =
                new ViewModelProvider(this).get(DiscoverViewModel.class);
        View root = inflater.inflate(R.layout.fragment_discover, container, false);
        /*final TextView textView = root.findViewById(R.id.text_discover);
        discoverViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        discoverViewModel =
                new ViewModelProvider(this).get(DiscoverViewModel.class);

        discoverRecyclerView = (RecyclerView) view.findViewById(R.id.discover_recyclerview);
        HistoryRecyclerViewAdapter adapter  = new HistoryRecyclerViewAdapter(getContext());
        discoverRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        discoverRecyclerView.setAdapter(adapter);

        spinnerMileRadius = view.findViewById(R.id.spinner_miles);
        populateSpinnerMiles();

        discoverViewModel.getAllMovies().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> movies) {
                // Update the cached copy of the words in the adapter.
                adapter.setMovies(movies);
            }
        });
    }

    private void populateSpinnerMiles() {
        mileRadius = new String[]{"15", "30", "45", "60", "85", "100"};
        ArrayAdapter<String> rangeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mileRadius);
        rangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMileRadius.setAdapter(rangeAdapter);

    }
}