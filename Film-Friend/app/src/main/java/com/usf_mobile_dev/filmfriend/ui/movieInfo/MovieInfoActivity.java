package com.usf_mobile_dev.filmfriend.ui.movieInfo;

import android.graphics.Movie;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.usf_mobile_dev.filmfriend.R;

public class MovieInfoActivity extends AppCompatActivity {

    private MovieInfoViewModel movieInfoViewModel;

    private TextView mMovieTitle;
    private TextView mMovieRelease;
    private TextView mMovieDirector;
    private TextView mMovieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        //movieInfoViewModel = new ViewModelProvider(this).get(MovieInfoViewModel.class);

        mMovieTitle = findViewById(R.id.movie_info_title);
        mMovieRelease = findViewById(R.id.movie_info_release);
        mMovieDirector = findViewById(R.id.movie_info_director);
        mMovieOverview = findViewById(R.id.movie_info_overview);

        /*
        if(getIntent().getExtras() != null) {
            Movie randomMovie = (Movie) getIntent().getSerializableExtra("randomMovie");

            mMovieTitle = randomMovie.getTitle();
            mMovieRelease = randomMovie.getRelease();
            mMovieDirector = randomMovie.getDirector();
            mMovieOverview = randomMovie.getOverview();

        }
        //*/
    }
}
