package com.usf_mobile_dev.filmfriend;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import java.util.List;

import com.usf_mobile_dev.filmfriend.api.TMDBApi;

public class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<List<MovieListing>> mAllMovies;

    public MovieRepository(Application application){
     MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mAllMovies = mMovieDao.getAllMovies();
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

    public void getMovie(){
        TMDBApi.getMovie();
    }
}
