package com.usf_mobile_dev.filmfriend;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashSet;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.FirebaseInstallations;
import com.usf_mobile_dev.filmfriend.api.DiscoverResponse;
import com.usf_mobile_dev.filmfriend.api.GenreResponse;
import com.usf_mobile_dev.filmfriend.api.LanguageResponse;
import com.usf_mobile_dev.filmfriend.api.TMDBApi;
import com.usf_mobile_dev.filmfriend.ui.discover.DiscoverViewModel;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import retrofit2.Callback;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MovieRepository {

    public final static int ENABLE_FINE_LOCATION = 1;
    public final static int ENABLE_COARSE_LOCATION = 2;

    private MovieDao mMovieDao;
    private List<MovieListing> mAllMovies;
    private List<String> usersNearby;
    private final Executor threadExecutor;
    private final Handler resultHandler;
    private DiscoverViewModel discoverViewModel;

    GeoFire geoFire;
    GeoQuery geoQuery;
    FirebaseDatabase rootNode;
    DatabaseReference ref_geoFire;
    private FusedLocationProviderClient fusedLocationClient;
    String FID;
    Task<String> firebaseInstallation;

    public MovieRepository(Application application){
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        //mAllMovies = mMovieDao.getAllMovies();
        //Log.d("MOVIELIST", String.valueOf(mAllMovies.getValue()));

        this.threadExecutor = ((MovieApplication)application).executorService;
        this.resultHandler = ((MovieApplication)application).mainThreadHandler;

        rootNode = FirebaseDatabase.getInstance();
        ref_geoFire = rootNode.getReference("geoFire");
        geoFire = new GeoFire(ref_geoFire);
        usersNearby = new ArrayList<>();
    }

    public void getAllMovies(final RoomCallback callback) {
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<MovieListing> movieListings;
                    movieListings =  mMovieDao.getAllMovies();

                    callback.onComplete(movieListings);
                } catch (Exception e){
                    Log.e("ALLMOVIES", String.valueOf(e));
                }
            }
        });
    }

    public void getMovie(int id, final RoomCallback callback) {
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    MovieListing movieListing;
                    movieListing =  mMovieDao.getMovie(id);

                    callback.onComplete(movieListing);
                } catch (Exception e){
                    Log.e("ALLMOVIES", String.valueOf(e));
                }
            }
        });
    }

    public void getWatchList(final RoomCallback callback) {
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("WATCHLIST", "In watchlist thread");
                    List<MovieListing> movieListings;
                    movieListings =  mMovieDao.getWatchList();

                    callback.onComplete(movieListings);
                } catch (Exception e){
                    Log.e("WATCHLIST", String.valueOf(e));
                }
            }
        });
    }

    public void insert (MovieListing movieListing) {
        new insertAsyncTask(mMovieDao).execute(movieListing);
    }

    public void update(MovieListing movieListing) {
        new updateAsyncTask(mMovieDao).execute(movieListing);
    }

    private static class insertAsyncTask extends android.os.AsyncTask<MovieListing, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieListing... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends android.os.AsyncTask<MovieListing, Void, Void> {

        private MovieDao mAsyncTaskDao;

        updateAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieListing... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    public void getAllMoviesNearby(double radius, FragmentActivity discoverActivity)
    {

        firebaseInstallation = FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            FID = task.getResult();
                            usersNearby.clear();
                            fusedLocationClient = LocationServices.getFusedLocationProviderClient(discoverActivity);

                            if (ActivityCompat.checkSelfPermission(discoverActivity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(discoverActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(discoverActivity, new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, ENABLE_FINE_LOCATION);
                                ActivityCompat.requestPermissions(discoverActivity, new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, ENABLE_COARSE_LOCATION);
                            }
                            fusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(discoverActivity, new OnSuccessListener<Location>() {
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
                                                        discoverViewModel = new ViewModelProvider(discoverActivity).get(DiscoverViewModel.class);
                                                        FirebaseDatabase.getInstance().getReference("users")
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if(snapshot.exists()) {
                                                                            //Log.d("movieID",snapshot.getValue().toString());
                                                                            findUserMatches(usersNearby, snapshot);
                                                                        }

                                                                    }
                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                    }

                                                    @Override
                                                    public void onGeoQueryError(DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            Log.e("Installations", "Unable to get Installation ID");
                        }
                    }
                });

        //do firebase query

        //Log.d("NearbyMovies:", String.valueOf(mAllMoviesInRadius));
        //return mAllMoviesInRadius;
    }

    public void findUserMatches(List<String> usersNearby, DataSnapshot snapshot)
    {
        List<Movie> movieList = new ArrayList<>();
        List<String> movieIDs = new ArrayList<>();
        usersNearby.remove(FID);

        //Grab the recentMatch of all nearby users
        for(String FID: usersNearby)
        {
            movieIDs.add(Objects.requireNonNull(snapshot.child(FID).child("recentMatch").getValue()).toString());
        }
        Log.d("movieIDs", String.valueOf(movieIDs));

        FirebaseDatabase.getInstance().getReference("movies").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    for(String movieID: movieIDs)
                    {
                        movieList.add(task.getResult().child(movieID).getValue(Movie.class));
                        Log.d("movieTitle", String.valueOf(task.getResult().child(movieID).getValue(Movie.class).getTitle()));
                    }
                    discoverViewModel.getDiscoverMovieList().postValue(movieList);
                }
            }
        });

        //Log.d("NearbyMovies:", String.valueOf(movieList));
        //return movieList;
    }

    public void getTMDBMovie(
            final Callback<DiscoverResponse> discoverCallback,
            final Executor callbackExecutor,
            final MatchPreferences matchPreferences,
            final String api_key
            ) {
                threadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        TMDBApi.getMovie(discoverCallback, callbackExecutor,
                                matchPreferences, api_key, null);
                    }
                });
    }

    public void getNextTMDBMovie(
            final Callback<DiscoverResponse> discoverCallback,
            final Executor callbackExecutor,
            final MatchPreferences curMatchPreferences,
            final String api_key,
            final HashSet<Integer> viewedPages
    ) {
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                TMDBApi.getMovie(discoverCallback, callbackExecutor,
                        curMatchPreferences, api_key, viewedPages);
            }
        });
    }

    public void getTMDBGenres(
            final Callback<GenreResponse> genreCallback,
            final Executor callbackExecutor,
            final String api_key
    ) {
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                TMDBApi.getGenres(genreCallback, callbackExecutor, api_key);
            }
        });
    }

    public void getTMDBLanguages(
            final Callback<List<LanguageResponse>> languageCallback,
            final Executor callbackExecutor,
            final String api_key
    ) {
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                TMDBApi.getLanguages(languageCallback, callbackExecutor, api_key);
            }
        });
    }

    /*private void notifyTMDBMovieResult(
            final ThreadResult<DiscoverResponse> discoverResult,
            final RepositoryCallback<DiscoverResponse> discoverCallback
    ) {
        this.resultHandler.post(new Runnable() {
            @Override
            public void run() {
                discoverCallback.onComplete(discoverResult);
            }
        });
    }

    private ThreadResult<DiscoverResponse> getTMDBMovie(){
        try {

        } catch (Exception e) {
            return new ThreadResult.Error<DiscoverResponse>(e);
        }
    }*/
}
