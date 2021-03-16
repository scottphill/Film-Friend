package com.usf_mobile_dev.filmfriend.ui.movieInfo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;


public class MovieInfoActivity extends AppCompatActivity {

    private MovieInfoViewModel movieInfoViewModel;
    private TextView mMovieTitle;
    private TextView mMovieRelease;
    private TextView mRating;
    private TextView mVoteCount;
    private TextView mMovieOverview;
    private ImageView mMovieBanner;
    private ImageView mMoviePoster;
    private Button newMovieBtn;
    private Button watchMovieBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        movieInfoViewModel = new ViewModelProvider(this).get(MovieInfoViewModel.class);

        mMovieTitle = findViewById(R.id.movie_info_title);
        mMovieRelease = findViewById(R.id.movie_info_release);
        mRating = findViewById(R.id.rating);
        mVoteCount = findViewById(R.id.vote_count);
        mMovieOverview = findViewById(R.id.movie_info_overview);
        mMovieBanner = findViewById(R.id.movie_banner);
        mMoviePoster = findViewById(R.id.movie_poster);
        newMovieBtn = findViewById(R.id.button_new_movie);
        watchMovieBtn = findViewById(R.id.button_watch_movie);

        if(getIntent().getExtras() != null) {
            Movie newMovie = (Movie)getIntent()
                    .getSerializableExtra(MovieInfoViewModel.INTENT_EXTRAS_MOVIE_DATA);
            setMovieDetails(newMovie);
            movieInfoViewModel.setCurrentMovie(newMovie);
            movieInfoViewModel.setActivityMode(getIntent()
                    .getStringExtra(MovieInfoViewModel.INTENT_EXTRAS_ACTIVITY_MODE));
            movieInfoViewModel.setCurMatchPreferences((MatchPreferences)getIntent()
                    .getSerializableExtra(MovieInfoViewModel.INTENT_EXTRAS_MOVIE_PREFERENCES));
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
        watchMovieBtn.setOnClickListener(movieInfoViewModel
                .getWatchMovieOnClickListener());
    }

    // Sets the UI elements of this activity to show info for a new movie
    public void setMovieDetails(Movie newMovie) {

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
}
