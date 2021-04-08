package com.usf_mobile_dev.filmfriend.ui.discover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.ui.recycler_adapters.DiscoverRecyclerAdapter;
import com.usf_mobile_dev.filmfriend.data_sources.data_classes.Movie;
import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MovieListing;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.tutorial_popups.Tutorial;
import com.usf_mobile_dev.filmfriend.ui.movieInfo.MovieInfoActivity;
import com.usf_mobile_dev.filmfriend.ui.movieInfo.MovieInfoViewModel;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {



    private DiscoverViewModel discoverViewModel;
    private RecyclerView discoverRecyclerView;
    Spinner spinnerMileRadius;
    Double[] mileRadius;
    private double radius;
    private List<String> usersNearby = new ArrayList<>();
    private DiscoverRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        discoverViewModel =
                new ViewModelProvider(requireActivity()).get(DiscoverViewModel.class);
        View root = inflater.inflate(R.layout.fragment_discover, container, false);
        adapter  = new DiscoverRecyclerAdapter(getContext());
        discoverRecyclerView = (RecyclerView) root.findViewById(R.id.discover_recyclerview);
        discoverRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        discoverRecyclerView.setAdapter(adapter);

        final Observer<List<MovieListing>> movieListObserver = new Observer<List<MovieListing>>() {
            @Override
            public void onChanged(List<MovieListing> movies) {
                Log.d("onChanged:", "Movie list has changed");
                adapter.setMovies(movies);
                Log.d("onChanged:", "Adapter set");
            }
        };

        discoverViewModel.getDiscoverMovieList().observe(getViewLifecycleOwner(), movieListObserver);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerMileRadius = view.findViewById(R.id.spinner_miles);
        populateSpinnerMiles();

        spinnerMileRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                radius = (Double) adapterView.getItemAtPosition(i);
                usersNearby.clear();

                Log.d("hasObserver", String.valueOf(discoverViewModel.getDiscoverMovieList().hasObservers()));
                discoverViewModel.getAllMoviesNearby(radius, requireActivity());
                Log.d("hasObserver", String.valueOf(discoverViewModel.getDiscoverMovieList().hasObservers()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapter.setOnItemClickListener(new DiscoverRecyclerAdapter.ClickListener()  {

            @Override
            public void onItemClick(View v, int position) {
                MovieListing movie = adapter.getMovieAtPosition(position);
                launchMovieInfoActivity(movie.getMovie());
            }
        });
    }

    

    public void launchMovieInfoActivity( Movie movie) {
        Context context = getActivity();
        Intent movieActivityIntent = new Intent(
                context,
                MovieInfoActivity.class
        );
        movieActivityIntent.putExtra(
                MovieInfoActivity.INTENT_EXTRAS_MOVIE_DATA,
                movie);
        movieActivityIntent.putExtra(
                MovieInfoViewModel.INTENT_EXTRAS_ACTIVITY_MODE,
                MovieInfoViewModel.ACTIVITY_MODE_DISCOVER
        );
        if (context != null) {
            context.startActivity(movieActivityIntent);
        }
    }

    private void populateSpinnerMiles() {
        mileRadius = new Double[]{15.0, 30.0, 45.0, 60.0, 85.0, 100.0};
        ArrayAdapter<Double> rangeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mileRadius);
        rangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMileRadius.setAdapter(rangeAdapter);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.tutorial_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Do nothing
        if (item.getItemId() == R.id.tutorial) {
            Tutorial t = new Tutorial();
            t.launchPageTutorial(this.getContext(), "Discover");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}