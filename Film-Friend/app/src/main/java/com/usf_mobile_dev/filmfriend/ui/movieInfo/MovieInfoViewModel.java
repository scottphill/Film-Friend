package com.usf_mobile_dev.filmfriend.ui.movieInfo;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.MovieRepository;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.api.DiscoverResponse;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieInfoViewModel extends AndroidViewModel {
    public static final String ACTIVITY_MODE_MATCH = "movie_info_mode_match";
    public static final String INTENT_ACTION_LAUNCH_WITH_MOVIE_DATA = "com.usf_mobile_dev.filmfriend.intent.action.launch_with_movie_data";
    public static final String INTENT_EXTRAS_MOVIE_DATA = "com.usf_mobile_dev.filmfriend.intent.extras.movie_data";
    public static final String INTENT_EXTRAS_ACTIVITY_MODE = "com.usf_mobile_dev.filmfriend.intent.extras.movie_info_activity_mode";
    public static final String INTENT_EXTRAS_MOVIE_PREFERENCES = "com.usf_mobile_dev.filmfriend.intent.extras.movie_preferences";

    private Stack<Movie> previousMovies;
    private Stack<Movie> nextMovies;
    private HashSet<Integer> viewedMovieIds;
    private HashSet<Integer> viewedPages;
    private MutableLiveData<Movie> currentMovie;
    private List<DiscoverResponse.MovieData> currentMoviePage;
    private MatchPreferences curMatchPreferences;
    private String activityMode;
    private MovieRepository movieRepository;
    private String api_key;

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
    }

    public void setCurrentMovie(Movie currentMovie) {
        viewedMovieIds.add(currentMovie.getTmdbMovieId());
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

    public void setCurMatchPreferences(MatchPreferences curMatchPreferences) {
        this.curMatchPreferences = curMatchPreferences;
    }

    public void setActivityMode(String activityMode) {
        this.activityMode = activityMode;
    }

    public void setCurrentMoviePage(
            List<DiscoverResponse.MovieData> newMoviePage,
            int page
    ) {
        this.currentMoviePage = newMoviePage;
        this.viewedPages.add(page);
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
        else {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            };
        }
    }

    public View.OnClickListener getWatchMovieOnClickListener() {
        if (ACTIVITY_MODE_MATCH.equals(activityMode)) {
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
}
