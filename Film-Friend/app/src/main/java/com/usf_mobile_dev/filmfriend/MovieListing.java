package com.usf_mobile_dev.filmfriend;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "movies_history")
public class MovieListing {

    @PrimaryKey
    private int movieID;

    private Date dateViewed;

    @Embedded
    private Movie movie;

    @Ignore
    public MovieListing() {
        this.movieID = 0;
        this.dateViewed = null;
        this.movie = null;
    }

    public MovieListing(int movieID, Date dateViewed, Movie movie)
    {
        this.movieID = movieID;
        this.dateViewed = dateViewed;
        this.movie = movie;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public Date getDateViewed() {
        return dateViewed;
    }

    public void setDateViewed(Date dateViewed) {
        this.dateViewed = dateViewed;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
