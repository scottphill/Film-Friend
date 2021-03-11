package com.usf_mobile_dev.filmfriend;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usf_mobile_dev.filmfriend.api.DiscoverResponse;
import com.usf_mobile_dev.filmfriend.api.TMDBApi;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import retrofit2.Callback;

public class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<List<Movie>> mAllMovies;
    private List<Movie> mAllMoviesInRadius;
    private int movieCheck;
    private final Executor threadExecutor;
    private final Handler resultHandler;

    public MovieRepository(Application application){
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mAllMovies = mMovieDao.getAllMovies();

        this.threadExecutor = ((MovieApplication)application).executorService;
        this.resultHandler = ((MovieApplication)application).mainThreadHandler;
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }

    public void insert (Movie movie) {
        new insertAsyncTask(mMovieDao).execute(movie);
    }

    private static class insertAsyncTask extends android.os.AsyncTask<Movie, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public List<Movie> getAllMoviesNearby(List<String> usersNearby)
    {
        //do firebase query
        FirebaseDatabase.getInstance().getReference("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            //Log.d("movieID",snapshot.getValue().toString());
                            mAllMoviesInRadius = findUserMatches(usersNearby, snapshot);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        Log.d("NearbyMovies:", String.valueOf(mAllMoviesInRadius));
        return mAllMoviesInRadius;
    }

    public List<Movie> findUserMatches(List<String> usersNearby, DataSnapshot snapshot)
    {
        List<Movie> movieList = new ArrayList<>();
        List<String> movieIDs = new ArrayList<>();

        //Grab the recentMatch of all nearby users
        for(String FID: usersNearby)
        {
            movieIDs.add(Objects.requireNonNull(snapshot.child(FID).child("recentMatch").getValue()).toString());
        }
        Log.d("movieIDs", String.valueOf(movieIDs));

        FirebaseDatabase.getInstance().getReference("movies").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    for(String movieID: movieIDs)
                    {
                        movieList.add(task.getResult().child(movieID).getValue(Movie.class));
                        Log.d("movieTitle", String.valueOf(task.getResult().child(movieID).getValue(Movie.class).getTitle()));
                    }
                }
            }
        });

        return movieList;
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
