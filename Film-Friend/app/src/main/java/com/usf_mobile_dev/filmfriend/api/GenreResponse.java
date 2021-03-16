package com.usf_mobile_dev.filmfriend.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenreResponse {

    @SerializedName("genres")
    public List<Genre> genres = null;

    public class Genre {

        @SerializedName("id")
        public Integer id;
        @SerializedName("name")
        public String name;
    }
}
