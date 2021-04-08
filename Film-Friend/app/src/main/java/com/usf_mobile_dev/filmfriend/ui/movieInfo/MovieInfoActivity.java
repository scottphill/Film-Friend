package com.usf_mobile_dev.filmfriend.ui.movieInfo;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.usf_mobile_dev.filmfriend.data_sources.data_classes.Movie;
import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MovieListing;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.data_sources.local_db.RoomCallback;
import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MatchPreferences;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class MovieInfoActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    public static String INTENT_ACTION_LAUNCH_WITH_MOVIE_DATA = "com.usf_mobile_dev.filmfriend.intent.action.launch_with_movie_data";
    public static String INTENT_EXTRAS_MOVIE_DATA = "com.usf_mobile_dev.filmfriend.intent.extras.movie_data";

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
    private Movie newMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_movie_info);

        movieInfoViewModel = new ViewModelProvider(this).get(MovieInfoViewModel.class);

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

            //getMovie(newMovie.getTmdbMovieId());

            String mode = movieInfoViewModel.getActivityMode();
            Log.d("ACTIVITY_MODE",String.valueOf(mode));
            switch(mode){
                case MovieInfoViewModel.ACTIVITY_MODE_MATCH:
                    break;
                case MovieInfoViewModel.ACTIVITY_MODE_HISTORY:
                case MovieInfoViewModel.ACTIVITY_MODE_DISCOVER:
                    getSupportActionBar().setTitle(newMovie.getTitle());
                    break;
                default:
                    Log.e("ACTIVITY_MODE","Invalid activity mode");
                    break;
            }

            //historyViewModel.insert(newMovieListing);
        }

        //movieInfoViewModel.setupFirebasePermissions(this);
        movieInfoViewModel.setActivity(this);

        // Changes the movie in the UI whenever the movie in the ViewModel changes
        movieInfoViewModel.getCurrentMovie().observe(
                this,
                currentMovie -> {
                    if(currentMovie != null) {
                        setMovieDetails(currentMovie);
                        watchMovieBtn.setText(movieInfoViewModel
                                .getWatchMovieBtnText());
                        watchMovieBtn.setClickable(true);
                    }
                }
        );

        movieInfoViewModel.getWatchedMovies().observe(
                this,
                watchedMovies -> {
                    if(watchedMovies != null) {
                        watchMovieBtn.setText(movieInfoViewModel
                                .getWatchMovieBtnText());
                        watchMovieBtn.setClickable(true);
                    }
                }
        );

        // Sets the click handler and visibility for the new movie button
        newMovieBtn.setOnClickListener(movieInfoViewModel
                .getNewMovieBtnOnClickListener());
        newMovieBtn.setVisibility(movieInfoViewModel
                .getNewMovieBtnVisibility());
        watchMovieBtn.setOnClickListener(movieInfoViewModel
                .getWatchMovieBtnOnClickListener());
        watchMovieBtn.setVisibility(movieInfoViewModel
                .getWatchMovieBtnVisibility());
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        newMovieBtn.setVisibility(movieInfoViewModel
                .getNewMovieBtnVisibility());

        // Sets the click handler and visibility for the watch movie button
        watchMovieBtn.setOnClickListener(movieInfoViewModel
                .getWatchMovieBtnOnClickListener());
        watchMovieBtn.setVisibility(movieInfoViewModel
                .getWatchMovieBtnVisibility());

    }

  
    // Sets the UI elements of this activity to show info for a new movie
    public void setMovieDetails(Movie newMovie) {
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

        movieInfoViewModel.permissionResultHandler(requestCode, permissions, grantResults, this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
