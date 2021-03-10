package com.usf_mobile_dev.filmfriend.ui.discover;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usf_mobile_dev.filmfriend.DiscoverRecyclerAdapter;
import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.movieInfo.MovieInfoActivity;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DiscoverFragment extends Fragment {

    public final static int ENABLE_FINE_LOCATION = 1;
    public final static int ENABLE_COARSE_LOCATION = 2;

    private DiscoverViewModel discoverViewModel;
    private RecyclerView discoverRecyclerView;
    Spinner spinnerMileRadius;
    Double[] mileRadius;
    private double radius;
    private List<String> usersNearby = new ArrayList<>();

    GeoFire geoFire;
    GeoQuery geoQuery;
    FirebaseDatabase rootNode;
    DatabaseReference ref_geoFire;
    private FusedLocationProviderClient fusedLocationClient;
    //private Location loc;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        discoverViewModel =
                new ViewModelProvider(this).get(DiscoverViewModel.class);
        View root = inflater.inflate(R.layout.fragment_discover, container, false);

        rootNode = FirebaseDatabase.getInstance();
        ref_geoFire = rootNode.getReference("geoFire");
        geoFire = new GeoFire(ref_geoFire);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        discoverViewModel =
                new ViewModelProvider(this).get(DiscoverViewModel.class);
        //test = view.findViewById(R.id.nearestMovie);

        spinnerMileRadius = view.findViewById(R.id.spinner_miles);
        populateSpinnerMiles();

        discoverRecyclerView = (RecyclerView) view.findViewById(R.id.discover_recyclerview);
        DiscoverRecyclerAdapter adapter  = new DiscoverRecyclerAdapter(getContext());
        discoverRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        discoverRecyclerView.setAdapter(adapter);
        FragmentActivity activity = getActivity();

        spinnerMileRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                radius = (Double) adapterView.getItemAtPosition(i);
                discoverViewModel.getNearbyUsers(radius, activity);
                Log.d("NEARBY", String.valueOf(usersNearby));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        discoverViewModel.getAllMoviesNearby(usersNearby).observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> movies) {
                adapter.setMovies(movies);
            }
        });

        adapter.setOnItemClickListener(new DiscoverRecyclerAdapter.ClickListener()  {

            @Override
            public void onItemClick(View v, int position) {
                Movie movie = adapter.getMovieAtPosition(position);
                launchMovieInfoActivity(movie);
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
}