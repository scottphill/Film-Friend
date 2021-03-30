package com.usf_mobile_dev.filmfriend.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;


public class IntBoolHashMapToStringConverter {

    private static final Gson gson = new Gson();

    // Converts the JSON back to an Integer:Boolean HashMap
    @TypeConverter
    public static HashMap<Integer, Boolean> stringToIntBoolHashMap(String data) {
        if(data == null) {
            return new HashMap<Integer, Boolean>(Collections.emptyMap());
        }

        Type hashMapType = new TypeToken<HashMap<Integer, Boolean>>() {

        }.getType();

        return gson.fromJson(data, hashMapType);
    }

    // Converts an Integer:Boolean HashMap to JSON
    @TypeConverter
    public static String intBoolHashMapToString(HashMap<Integer, Boolean> hashMap) {
        return gson.toJson(hashMap);
    }
}
