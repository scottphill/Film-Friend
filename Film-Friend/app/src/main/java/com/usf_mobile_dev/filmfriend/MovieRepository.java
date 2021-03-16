package com.usf_mobile_dev.filmfriend;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executor;

import com.usf_mobile_dev.filmfriend.api.DiscoverResponse;
import com.usf_mobile_dev.filmfriend.api.GenreResponse;
import com.usf_mobile_dev.filmfriend.api.LanguageResponse;
import com.usf_mobile_dev.filmfriend.api.TMDBApi;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import retrofit2.Callback;

public class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<List<MovieListing>> mAllMovies;
    private final Executor threadExecutor;
    private final Handler resultHandler;

    public MovieRepository(Application application){
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mAllMovies = mMovieDao.getAllMovies();

        this.threadExecutor = ((MovieApplication)application).executorService;
        this.resultHandler = ((MovieApplication)application).mainThreadHandler;
    }

    public LiveData<List<MovieListing>> getAllMovies() {
        return mAllMovies;
    }

    public void insert (MovieListing movie) {
        new insertAsyncTask(mMovieDao).execute(movie);
    }

    private static class insertAsyncTask extends AsyncTask<MovieListing, Void, Void> {

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
