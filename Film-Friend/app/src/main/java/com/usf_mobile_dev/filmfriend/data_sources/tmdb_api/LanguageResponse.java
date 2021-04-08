package com.usf_mobile_dev.filmfriend.data_sources.tmdb_api;

import com.google.gson.annotations.SerializedName;

public class LanguageResponse {

    @SerializedName("iso_639_1")
    public String iso_code;
    @SerializedName("english_name")
    public String english_name;
    @SerializedName("name")
    public String name;
}
