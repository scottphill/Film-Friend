package com.usf_mobile_dev.filmfriend.ui.movieInfo;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.FirebaseInstallations;
import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.MovieListing;
import com.usf_mobile_dev.filmfriend.MovieRepository;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.RoomCallback;
import com.usf_mobile_dev.filmfriend.api.DiscoverResponse;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.usf_mobile_dev.filmfriend.R.string.watch_this_movie;


public class MovieInfoViewModel extends AndroidViewModel {
    public static final String ACTIVITY_MODE_MATCH = "movie_info_mode_match";
    public static final String ACTIVITY_MODE_HISTORY = "movie_info_mode_history";
    public static final String ACTIVITY_MODE_DISCOVER = "movie_info_mode_discover";
    public static final String INTENT_ACTION_LAUNCH_WITH_MOVIE_DATA = "com.usf_mobile_dev.filmfriend.intent.action.launch_with_movie_data";
    public static final String INTENT_EXTRAS_MOVIE_DATA = "com.usf_mobile_dev.filmfriend.intent.extras.movie_data";
    public static final String INTENT_EXTRAS_ACTIVITY_MODE = "com.usf_mobile_dev.filmfriend.intent.extras.movie_info_activity_mode";
    public static final String INTENT_EXTRAS_MOVIE_PREFERENCES = "com.usf_mobile_dev.filmfriend.intent.extras.movie_preferences";
    public final static int ENABLE_FINE_LOCATION = 1;
    public final static int ENABLE_COARSE_LOCATION = 2;

    private Stack<Movie> previousMovies;
    private Stack<Movie> nextMovies;
    private HashSet<Integer> viewedMovieIds;
    private HashSet<Integer> viewedPages;
    private MutableLiveData<Movie> currentMovie;
    private MutableLiveData<HashSet<Integer>> watchedMovies;
    private Observer<List<MovieListing>> allMoviesObserver;
    private List<DiscoverResponse.MovieData> currentMoviePage;
    private MatchPreferences curMatchPreferences;
    private String activityMode;
    private MovieRepository movieRepository;
    private String api_key;
    private int willWatch;
    private Activity activity;

    FirebaseDatabase rootNode;
    DatabaseReference ref_user;
    DatabaseReference ref_movies;
    DatabaseReference ref_geoFire;
    GeoFire geoFire;
    String FID;
    Task<String> firebaseInstallation;

    private FusedLocationProviderClient fusedLocationClient;
    private Location loc;

    public MovieInfoViewModel(Application application) {
        super(application);

        previousMovies = new Stack<>();
        nextMovies = new Stack<>();
        viewedMovieIds = new HashSet<>();
        viewedPages = new HashSet<>();
        api_key = application.getString(R.string.tmdb_api_key);
        currentMoviePage = new ArrayList<>();
        activityMode = "";
        currentMovie = new MutableLiveData<Movie>();
        movieRepository = new MovieRepository(application);
        watchedMovies = new MutableLiveData<>();

        // Updates the watched movies hashset when the observed data changes
        allMoviesObserver = new Observer<List<MovieListing>>() {
            @Override
            public void onChanged(List<MovieListing> movieListings) {
                watchedMovies.setValue(
                        movieListings
                                .stream()
                                .mapToInt(MovieListing::getMovieID)
                                .boxed()
                                .collect(Collectors.toCollection(HashSet::new))
                );
            }
        };
        movieRepository.getWatchList().observeForever(allMoviesObserver);

        //Set up firebase instance/references
        rootNode = FirebaseDatabase.getInstance();
        ref_user = rootNode.getReference("users");
        ref_movies = rootNode.getReference("movies");
        ref_geoFire = rootNode.getReference("geoFire");
        geoFire = new GeoFire(ref_geoFire);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
    }

    public void setActivity(Activity a){
        activity = a;
    }

    public void setCurrentMovie(Movie currentMovie) {
        viewedMovieIds.add(currentMovie.getTmdbMovieId());
        updateMovieHistory(currentMovie);
        this.currentMovie.postValue(currentMovie);
    }
    public void setCurrentMovie(DiscoverResponse.MovieData currentMovieData) {
        Movie newMovie = new Movie();
        newMovie.setTitle(currentMovieData.title);
        newMovie.setOverview(currentMovieData.overview);
        newMovie.setReleaseYear(Integer.valueOf(
                currentMovieData
                        .releaseDate
                        .split("-")[0]));
        newMovie.setRating(currentMovieData.voteAverage);
        newMovie.setVoteCount(currentMovieData.voteCount);
        newMovie.setTmdbMovieId(currentMovieData.id);
        newMovie.setPosterPath(currentMovieData.posterPath);
        newMovie.setBackdropPath(currentMovieData.backdropPath);

        setCurrentMovie(newMovie);
    }

    public MutableLiveData<Movie> getCurrentMovie() {
        return currentMovie;
    }

    public MutableLiveData<HashSet<Integer>> getWatchedMovies() {
        return watchedMovies;
    }

    public void setCurMatchPreferences(MatchPreferences curMatchPreferences) {
        this.curMatchPreferences = curMatchPreferences;
    }

    public void setActivityMode(String activityMode) {
        this.activityMode = activityMode;
    }
    public String getActivityMode() {return activityMode;}

    public void setCurrentMoviePage(
            List<DiscoverResponse.MovieData> newMoviePage,
            int page
    ) {
        this.currentMoviePage = newMoviePage;
        this.viewedPages.add(page);
    }

    public void updateMovieDatabase(MovieListing movieListing) {
        movieRepository.update(movieListing);
    }

    public void getMovie(int id , final RoomCallback callback)
    {
        movieRepository.getMovie(id, callback);
    }

    public void updateMovieHistory(Movie movie) {

            // Inserts the movie into the top of the history table
            long millis = System.currentTimeMillis();
            MovieListing newMovieListing = new MovieListing(
                    movie.getTmdbMovieId(),
                    new Date(millis),
                    movie,
                    0
            );
            movieRepository.insertOrUpdateMovie(newMovieListing);
    }

    public void setupFirebasePermissions(Activity context) {
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(context, new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, ENABLE_FINE_LOCATION);
            ActivityCompat.requestPermissions(context, new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, ENABLE_COARSE_LOCATION);
        }
        else if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                loc = location;
                                Log.d("MovieInfo", "DEBUG1");
                                addDataToFirebase();
                            }
                        }
                    });
        }
    }

    public void permissionResultHandler(int requestCode, @NotNull String[] permissions,
                                        @NotNull int[] grantResults, Activity context) {
        switch (requestCode) {
            case MovieInfoViewModel.ENABLE_FINE_LOCATION:
            case MovieInfoViewModel.ENABLE_COARSE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            loc = location;
                                            Log.d("MovieInfo", "DEBUG1");
                                            addDataToFirebase();
                                        }
                                    }
                                });
                    }
                }
                else
                {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Log.d("MovieInfo", "DEBUG2");
                    //addDataToFirebase();//adds without location.
                }
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    public void addDataToFirebase() {
        firebaseInstallation = FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            FID = task.getResult();
                            ref_user.child(FID).child("recentMatch").setValue(currentMovie.getValue().getTmdbMovieId());
                            ref_user.child(FID).child("time").setValue(new java.util.Date().getTime());
                            ref_movies.child(currentMovie.getValue().getTmdbMovieIdAsStr()).setValue(currentMovie.getValue());
                            if(loc != null) {
                                geoFire.setLocation(FID, new GeoLocation(loc.getLatitude(), loc.getLongitude()));
                                Log.d("Location", "Added location");
                            }
                            else
                                Log.d("Location", "Added without location");
                        }
                        else
                        {
                            Log.e("Installations", "Unable to get Installation ID");
                        }
                    }
                });
    }

    /*public View.OnClickListener getWatchMovieOnClickListener() {
        if (ACTIVITY_MODE_MATCH.equals(activityMode)) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentMovie.getValue() != null) {
                        long millis = System.currentTimeMillis();
                        MovieListing newMovieListing = new MovieListing(
                                currentMovie.getValue().getTmdbMovieId(),
                                new Date(millis),
                                currentMovie.getValue(),
                                1);
                        Log.d("WATCHLIST", "Inserting with willWatch value");
                        movieRepository.update(newMovieListing);
                    }
                }
            };
        }
        else {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            };
        }
    }*/

    private void getNextMovie(Context context) {

        if(currentMoviePage == null || currentMoviePage.isEmpty()) {
            movieRepository.getNextTMDBMovie(
                    new Callback<DiscoverResponse>() {
                        @Override
                        public void onResponse(
                                Call<DiscoverResponse> call,
                                Response<DiscoverResponse> response
                        ) {
                            Random randGen = new Random();

                            DiscoverResponse results = response.body();
                            int numMovies = results.movieData.size();
                            int movieChoiceIndex = -1;
                            // Pops a toast if there aren't any movies returned
                            if (numMovies == 0) {
                                Log.d("No Matching Movies Found", "NOMATCHINGMOVIESFOUND");

                                Toast.makeText(
                                        context,
                                        "No Matching Movies!",
                                        Toast.LENGTH_LONG
                                ).show();
                                setCurrentMoviePage(null, 0);
                            }
                            // Pops a toast if all the pages have been searched,
                            //   and therefore no new movies have been found.
                            else if (viewedPages.contains(results.page)) {
                                Log.d("No More Movies Found", "NOMOVIESFOUND");
                                Toast.makeText(
                                        context,
                                        "No More Movies Found!",
                                        Toast.LENGTH_LONG
                                ).show();
                                setCurrentMoviePage(null, 0);
                            } else {
                                Log.d("Movies Found", "MOVIESFOUND");
                                // Shuffles the page of movies returned and sets
                                //   it as the current page of movies.
                                Collections.shuffle(results.movieData);
                                setCurrentMoviePage(results.movieData, results.page);
                                getNextMovie(context);
                            }
                        }

                        @Override
                        public void onFailure(Call<DiscoverResponse> call, Throwable t) {
                            call.cancel();
                            Toast.makeText(
                                    context,
                                    "SEARCH FAILED",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    },
                    context.getMainExecutor(),
                    curMatchPreferences,
                    this.api_key,
                    viewedPages);
        }
        else if(currentMoviePage != null){
            boolean movieFound = false;
            while(!currentMoviePage.isEmpty()) {
                DiscoverResponse.MovieData nextMovieData = currentMoviePage.get(0);
                currentMoviePage.remove(0);

                if(!viewedMovieIds.contains(nextMovieData.id)) {
                    setCurrentMovie(nextMovieData);
                    movieFound = true;
                    break;
                }
            }
            if(!movieFound)
                getNextMovie(context);

        }
    }

    public void watchToast(int type, Context context)
    {
        CharSequence text;
        if(type == 1)
            text = "Added to Watchlist";
        else
            text = "Removed from Watchlist";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public View.OnClickListener getNewMovieBtnOnClickListener() {
        if (ACTIVITY_MODE_MATCH.equals(activityMode)) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("newMovieBtnClickTester", "IN NEW MOVIE CLICK LISTENER");
                    getNextMovie(v.getContext());
                }
            };
        }
        else if (ACTIVITY_MODE_HISTORY.equals(activityMode)) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            };
        }
        else {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            };
        }
    }

    public View.OnClickListener getWatchMovieBtnOnClickListener() {
        if (ACTIVITY_MODE_MATCH.equals(activityMode)) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentMovie.getValue() != null) {
                        if (watchedMovies.getValue().contains(
                                currentMovie.getValue().getTmdbMovieId()
                        )) // In watchlist
                        {
                            long millis = System.currentTimeMillis();
                            MovieListing newMovieListing = new MovieListing(
                                    currentMovie.getValue().getTmdbMovieId(),
                                    new Date(millis),
                                    currentMovie.getValue(),
                                    0);
                            Log.d("WATCHLIST", "Removing from watch list");
                            updateMovieDatabase(newMovieListing);
                            ((Button) v).setText(R.string.empty_string);
                            ((Button) v).setClickable(false);
                            watchToast(0, v.getContext());
                        }
                        // Not in watchList
                        else {
                            //addDataToFirebase();
                            setupFirebasePermissions(activity);
                            long millis = System.currentTimeMillis();
                            MovieListing newMovieListing = new MovieListing(
                                    currentMovie.getValue().getTmdbMovieId(),
                                    new Date(millis),
                                    currentMovie.getValue(),
                                    1);
                            Log.d("WATCHLIST", "Inserting to watchlist");
                            updateMovieDatabase(newMovieListing);
                            ((Button) v).setText(R.string.empty_string);
                            ((Button) v).setClickable(false);
                            watchToast(1, v.getContext());
                        }
                    }
                }
            };
        }
        else if (ACTIVITY_MODE_HISTORY.equals(activityMode)) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentMovie.getValue() != null) {
                        if (watchedMovies.getValue().contains(
                                currentMovie.getValue().getTmdbMovieId()
                        )) // In watchlist
                        {
                            long millis = System.currentTimeMillis();
                            MovieListing newMovieListing = new MovieListing(
                                    currentMovie.getValue().getTmdbMovieId(),
                                    new Date(millis),
                                    currentMovie.getValue(),
                                    0);
                            Log.d("WATCHLIST", "Removing from watch list");
                            updateMovieDatabase(newMovieListing);
                            ((Button) v).setText(R.string.empty_string);
                            ((Button) v).setClickable(false);
                            watchToast(0, v.getContext());
                        }
                        // Not in watchList
                        else {
                            long millis = System.currentTimeMillis();
                            MovieListing newMovieListing = new MovieListing(
                                    currentMovie.getValue().getTmdbMovieId(),
                                    new Date(millis),
                                    currentMovie.getValue(),
                                    1);
                            Log.d("WATCHLIST", "Inserting to watchlist");
                            updateMovieDatabase(newMovieListing);
                            ((Button) v).setText(R.string.empty_string);
                            ((Button) v).setClickable(false);
                            watchToast(1, v.getContext());
                        }
                    }
                }
            };
        }
        else {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentMovie.getValue() != null) {
                        if (watchedMovies.getValue().contains(
                                currentMovie.getValue().getTmdbMovieId()
                        )) // In watchlist
                        {
                            long millis = System.currentTimeMillis();
                            MovieListing newMovieListing = new MovieListing(
                                    currentMovie.getValue().getTmdbMovieId(),
                                    new Date(millis),
                                    currentMovie.getValue(),
                                    0);
                            Log.d("WATCHLIST", "Removing from watch list");
                            updateMovieDatabase(newMovieListing);
                            ((Button) v).setText(R.string.empty_string);
                            ((Button) v).setClickable(false);
                            watchToast(0, v.getContext());
                        }
                        // Not in watchList
                        else {
                            long millis = System.currentTimeMillis();
                            MovieListing newMovieListing = new MovieListing(
                                    currentMovie.getValue().getTmdbMovieId(),
                                    new Date(millis),
                                    currentMovie.getValue(),
                                    1);
                            Log.d("WATCHLIST", "Inserting to watchlist");
                            updateMovieDatabase(newMovieListing);
                            ((Button) v).setText(R.string.empty_string);
                            ((Button) v).setClickable(false);
                            watchToast(1, v.getContext());
                        }
                    }
                }
            };
        }
    }

    public int getNewMovieBtnVisibility() {
        if (ACTIVITY_MODE_MATCH.equals(activityMode)) {
            return Button.VISIBLE;
        }
        else if (ACTIVITY_MODE_HISTORY.equals(activityMode)) {
            return Button.GONE;
        }
        else {
            return Button.GONE;
        }
    }

    public int getWatchMovieBtnVisibility() {
        if (ACTIVITY_MODE_MATCH.equals(activityMode)) {
            return Button.VISIBLE;
        }
        else if (ACTIVITY_MODE_HISTORY.equals(activityMode)) {
            return Button.VISIBLE;
        }
        else {
            return Button.VISIBLE;
        }
    }

    public int getWatchMovieBtnText() {
        if(currentMovie.getValue() != null
                && watchedMovies.getValue() != null) {
            if (watchedMovies.getValue().contains(
                    currentMovie.getValue().getTmdbMovieId())
            )
                return R.string.remove_from_watchlist;
            else
                return watch_this_movie;
        }
        else
            return R.string.empty_string;
    }

    // Removes the LiveData observers on the repository before this
    //   ViewModel is destroyed.
    @Override
    protected void onCleared() {
        super.onCleared();

        movieRepository.getAllMovies().removeObserver(allMoviesObserver);
    }


}
