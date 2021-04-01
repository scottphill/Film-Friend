package com.usf_mobile_dev.filmfriend.ui.movieInfo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
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
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.RoomCallback;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;
import com.usf_mobile_dev.filmfriend.ui.history.HistoryViewModel;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MovieInfoActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    public static String INTENT_ACTION_LAUNCH_WITH_MOVIE_DATA = "com.usf_mobile_dev.filmfriend.intent.action.launch_with_movie_data";
    public static String INTENT_EXTRAS_MOVIE_DATA = "com.usf_mobile_dev.filmfriend.intent.extras.movie_data";
    public final static int ENABLE_FINE_LOCATION = 1;
    public final static int ENABLE_COARSE_LOCATION = 2;

    FirebaseDatabase rootNode;
    DatabaseReference ref_user;
    DatabaseReference ref_movies;
    DatabaseReference ref_geoFire;
    GeoFire geoFire;
    String FID;
    Task<String> firebaseInstallation;

    private FusedLocationProviderClient fusedLocationClient;
    private Location loc;

    private MovieInfoViewModel movieInfoViewModel;
    private TextView mMovieTitle;
    private TextView mMovieRelease;
    private TextView mRating;
    private TextView mVoteCount;
    private TextView mMovieOverview;
    private ImageView mMovieBanner;
    private ImageView mMoviePoster;
    private int mWillWatch;
    private Button newMovieBtn;
    private Button watchMovieBtn;
  

    private HistoryViewModel historyViewModel;
    private Movie newMovie;
    private MovieListing newMovieListing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        movieInfoViewModel = new ViewModelProvider(this).get(MovieInfoViewModel.class);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        mMovieTitle = findViewById(R.id.movie_info_title);
        mMovieRelease = findViewById(R.id.movie_info_release);
        mRating = findViewById(R.id.rating);
        mVoteCount = findViewById(R.id.vote_count);
        mMovieOverview = findViewById(R.id.movie_info_overview);
        mMoviePoster = findViewById(R.id.movie_poster);
        mMovieBanner = findViewById(R.id.movie_banner);
        newMovieBtn = findViewById(R.id.button_new_movie);
        watchMovieBtn = findViewById(R.id.button_watch_movie);

        if(getIntent().getExtras() != null) {
            newMovie = (Movie)getIntent()
                    .getSerializableExtra(MovieInfoViewModel.INTENT_EXTRAS_MOVIE_DATA);
            setMovieDetails(newMovie);
            movieInfoViewModel.setCurrentMovie(newMovie);
            movieInfoViewModel.setActivityMode(getIntent()
                    .getStringExtra(MovieInfoViewModel.INTENT_EXTRAS_ACTIVITY_MODE));
            movieInfoViewModel.setCurMatchPreferences((MatchPreferences)getIntent()
                    .getSerializableExtra(MovieInfoViewModel.INTENT_EXTRAS_MOVIE_PREFERENCES));

            getMovie(newMovie.getTmdbMovieId());

            String mode = movieInfoViewModel.getActivityMode();
            Log.d("ACTIVITY_MODE",String.valueOf(mode));
            switch(mode){
                case MovieInfoViewModel.ACTIVITY_MODE_MATCH:
                    break;
                case MovieInfoViewModel.ACTIVITY_MODE_HISTORY:
                case MovieInfoViewModel.ACTIVITY_MODE_DISCOVER:
                    newMovieBtn.setVisibility(View.GONE);
                    getSupportActionBar().setTitle(newMovie.getTitle());
                    //watchMovieBtn.setVisibility(View.GONE);
                    break;
                default:
                    Log.e("ACTIVITY_MODE","Invalid activity mode");
                    break;
            }

            //historyViewModel.insert(newMovieListing);

            //Set up firebase instance/references
            rootNode = FirebaseDatabase.getInstance();
            ref_user = rootNode.getReference("users");
            ref_movies = rootNode.getReference("movies");
            ref_geoFire = rootNode.getReference("geoFire");
            geoFire = new GeoFire(ref_geoFire);

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, ENABLE_FINE_LOCATION);
                ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, ENABLE_COARSE_LOCATION);
            }
            else if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
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

        movieInfoViewModel.getCurrentMovie().observe(
                this,
                currentMovie -> {
                    if(currentMovie != null)
                        setMovieDetails(currentMovie);
                }
        );

        newMovieBtn.setOnClickListener(movieInfoViewModel
                .getNewMovieBtnOnClickListener());

        watchMovieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie movie = movieInfoViewModel.getCurrentMovie().getValue();
                if(movie != null) {
                    if(mWillWatch == 0)//Not in watchlist
                    {
                        long millis = System.currentTimeMillis();
                        MovieListing newMovieListing = new MovieListing(
                                movie.getTmdbMovieId(),
                                new Date(millis),
                                movie,
                                1);
                        Log.d("WATCHLIST", "Inserting to watchlist");
                        movieInfoViewModel.updateMovieDatabase(newMovieListing);
                        watchMovieBtn.setText(R.string.remove_from_watchlist);
                        mWillWatch = 1;
                        watchToast(1);
                    }
                    else if(mWillWatch == 1)
                    {
                        long millis = System.currentTimeMillis();
                        MovieListing newMovieListing = new MovieListing(
                                movie.getTmdbMovieId(),
                                new Date(millis),
                                movie,
                                0);
                        Log.d("WATCHLIST", "Removing from watch list");
                        movieInfoViewModel.updateMovieDatabase(newMovieListing);
                        watchMovieBtn.setText(R.string.watch_this_movie);
                        mWillWatch = 0;
                        watchToast(0);
                    }
                }

            }
        });
    }

    public void watchToast(int type)
    {
        Context context = getApplicationContext();
        CharSequence text;
        if(type == 1)
            text = "Added to Watchlist";
        else
            text = "Removed from Watchlist";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void getMovie(int id)
    {
        movieInfoViewModel.getMovie(id , new RoomCallback() {
            @Override
            public void onComplete(List<MovieListing> result) {

            }

            @Override
            public void onComplete(MovieListing result) {
                if(result != null) {
                    mWillWatch = result.getWillWatch();
                    Log.d("WATCHLIST", String.valueOf(mWillWatch));
                    if (mWillWatch == 1) {
                        watchMovieBtn.setText("Remove from Watchlist");
                    }
                }
            }
        });
    }

  
    // Sets the UI elements of this activity to show info for a new movie
    public void setMovieDetails(Movie newMovie) {

        long millis = System.currentTimeMillis();
        newMovieListing = new MovieListing(newMovie.getTmdbMovieId(), new Date(millis), newMovie, 0);
        historyViewModel.insert(newMovieListing);
        this.newMovie = newMovie;

        mMovieTitle.setText(newMovie.getTitle());
        mMovieRelease.setText(newMovie.getReleaseYearAsStr());
        mRating.setText(newMovie.getRatingAsFormattedStr());
        mVoteCount.setText(newMovie.getVoteCountAsString());
        mMovieOverview.setText(newMovie.getOverview());

        String posterUrl = getString(R.string.tmdb_image_base_url)
                + getString(R.string.tmdb_poster_size_3)
                + newMovie.getPosterPath();
        String backdropUrl = getString(R.string.tmdb_image_base_url)
                + getString(R.string.tmdb_backdrop_size_2)
                + newMovie.getBackdropPath();

        Glide.with(this)
                .load(posterUrl)
                .into(mMoviePoster);

        Glide.with(this)
                .load(backdropUrl)
                .into(mMovieBanner);
  
    }

  
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ENABLE_FINE_LOCATION:
            case ENABLE_COARSE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
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
                    addDataToFirebase();//adds without location.
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
                            ref_user.child(FID).child("recentMatch").setValue(newMovie.getTmdbMovieId());
                            ref_user.child(FID).child("time").setValue(new java.util.Date().getTime());
                            ref_movies.child(newMovie.getTmdbMovieIdAsStr()).setValue(newMovie);
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
}
