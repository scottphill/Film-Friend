package com.usf_mobile_dev.filmfriend.data_sources.data_classes;

import java.sql.Date;

public class MovieListingDate {
        private int movieID;
        private Date dateViewed;

        public MovieListingDate(int movieID, Date dateViewed) {
                this.movieID = movieID;
                this.dateViewed = dateViewed;
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
}
