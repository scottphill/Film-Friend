package com.usf_mobile_dev.filmfriend;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;

import com.usf_mobile_dev.filmfriend.api.DiscoverResponse;
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
            final MatchPreferences matchPreferences
            ) {
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                TMDBApi.getMovie(discoverCallback, callbackExecutor, matchPreferences);
                /*try {
                    ThreadResult<DiscoverResponse> discoverResult = getMovie();
                    notifyTMDBMovieResult(discoverResult, callback);
                } catch (Exception e) {
                    ThreadResult<DiscoverResponse> errorResult = new ThreadResult.Error<>(e);
                    notifyTMDBMovieResult(errorResult, callback);
                }*/
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
