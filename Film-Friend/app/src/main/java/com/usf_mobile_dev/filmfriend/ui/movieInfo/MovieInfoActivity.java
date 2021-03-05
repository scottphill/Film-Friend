package com.usf_mobile_dev.filmfriend.ui.movieInfo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.R;

public class MovieInfoActivity extends AppCompatActivity {
    public static String INTENT_ACTION_LAUNCH_WITH_MOVIE_DATA = "com.usf_mobile_dev.filmfriend.intent.action.launch_with_movie_data";
    public static String INTENT_EXTRAS_MOVIE_DATA = "com.usf_mobile_dev.filmfriend.intent.extras.movie_data";

    private MovieInfoViewModel movieInfoViewModel;
    private Movie movie;

    private TextView mMovieTitle;
    private TextView mMovieRelease;
    private TextView mRating;
    private TextView mVoteCount;
    private TextView mMovieOverview;

    private ImageView mMovieBanner;
    private ImageView mMoviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        //movieInfoViewModel = new ViewModelProvider(this).get(MovieInfoViewModel.class);

        mMovieTitle = findViewById(R.id.movie_info_title);
        mMovieRelease = findViewById(R.id.movie_info_release);
        mRating = findViewById(R.id.rating);
        mVoteCount = findViewById(R.id.vote_count);
        mMovieOverview = findViewById(R.id.movie_info_overview);

        mMovieBanner = findViewById(R.id.movie_banner);
        mMoviePoster = findViewById(R.id.movie_poster);

        ///*
        if(getIntent().getExtras() != null) {
            movie = (Movie) getIntent().getSerializableExtra(INTENT_EXTRAS_MOVIE_DATA);

            mMovieTitle.setText(movie.getTitle());
            mMovieRelease.setText(movie.getReleaseYearAsStr());
            mRating.setText(movie.getRatingAsFormattedStr());
            mVoteCount.setText(movie.getVoteCountAsString());
            mMovieOverview.setText(movie.getOverview());

            //----Load images----

        }
        //*/
    }
}
