package com.usf_mobile_dev.filmfriend.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StringListToStringConverter {

    private static final Gson gson = new Gson();

    // Converts the JSON back to a string list
    @TypeConverter
    public static ArrayList<String> stringToIntBoolHashMap(String data) {
        if(data == null) {
            return new ArrayList<String>(Collections.emptyList());
        }

        Type listType = new TypeToken<ArrayList<String>>() {

        }.getType();

        return gson.fromJson(data, listType);
    }

    // Converts a string lsit to JSON
    @TypeConverter
    public static String intBoolHashMapToString(ArrayList<String> stringList) {
        return gson.toJson(stringList);
    }
}
