package com.usf_mobile_dev.filmfriend.ui.qr;

import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

public class MPJSONHandling {

    public static String mpToJSON(MatchPreferences mp)
            throws JsonProcessingException
    {
        ObjectMapper obj_mapper = new ObjectMapper();
        String json = obj_mapper.writeValueAsString(mp);

        return json;
    }

    public static MatchPreferences JSONToMP(String json)
            throws JsonProcessingException
    {
        ObjectMapper obj_mapper = new ObjectMapper();
        MatchPreferences mp = obj_mapper.readValue(json, MatchPreferences.class);

        return mp;
    }

    // Printing Purposes ONLY!
    public static String mpToPrettyJSON(MatchPreferences mp)
            throws JsonProcessingException
    {
        ObjectMapper obj_mapper = new ObjectMapper();
        String pretty_json = "";

        pretty_json = obj_mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mp);

        return pretty_json;
    }

    // Printing Purposes ONLY!
    public static String JSONToPrettyJSON(String json)
            throws JsonProcessingException
    {
        ObjectMapper obj_mapper = new ObjectMapper();

        MatchPreferences mp = obj_mapper.readValue(json, MatchPreferences.class);

        String pretty_json = obj_mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mp);

        return pretty_json;
    }
}
