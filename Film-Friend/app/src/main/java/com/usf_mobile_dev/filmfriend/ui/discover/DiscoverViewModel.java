package com.usf_mobile_dev.filmfriend.ui.discover;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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
import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.MovieRepository;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DiscoverViewModel extends AndroidViewModel {

    public final static int ENABLE_FINE_LOCATION = 1;
    public final static int ENABLE_COARSE_LOCATION = 2;

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;
    private MutableLiveData<List<Movie>> mAllMovies;
    private FusedLocationProviderClient fusedLocationClient;

    GeoFire geoFire;
    GeoQuery geoQuery;
    FirebaseDatabase rootNode;
    DatabaseReference ref_geoFire;
    private List<String> usersNearby = new ArrayList<>();

    public DiscoverViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is the Discover fragment");
        movieRepository = new MovieRepository(application);
        rootNode = FirebaseDatabase.getInstance();
        ref_geoFire = rootNode.getReference("geoFire");
        geoFire = new GeoFire(ref_geoFire);


    }

    void getNearbyUsers (double radius, FragmentActivity activity)
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        if (ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, ENABLE_FINE_LOCATION);
            ActivityCompat.requestPermissions(activity, new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, ENABLE_COARSE_LOCATION);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), radius);
                            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                @Override
                                public void onKeyEntered(String key, GeoLocation location) {
                                    usersNearby.add(key);//add the FID of all the users within range.
                                }

                                @Override
                                public void onKeyExited(String key) {

                                }

                                @Override
                                public void onKeyMoved(String key, GeoLocation location) {

                                }

                                @Override
                                public void onGeoQueryReady() {
                                    for(String key: usersNearby)
                                    {
                                        Log.d("FID", key);
                                    }
                                    getAllMoviesNearby(usersNearby);
                                }

                                @Override
                                public void onGeoQueryError(DatabaseError error) {

                                }
                            });
                        }
                    }
                });
    }

    MutableLiveData<List<Movie>> getAllMoviesNearby(List<String> users) {
        mAllMovies = movieRepository.getAllMoviesNearby(users);
        return mAllMovies;
    }

    public LiveData<String> getText() {
        return mText;
    }
}