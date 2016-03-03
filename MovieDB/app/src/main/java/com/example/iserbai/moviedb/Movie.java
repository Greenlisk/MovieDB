package com.example.iserbai.moviedb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by green on 3/1/16.
 */
public class Movie {

    public String posterPath;
    public String overview;
    public String releaseDate;
    public long id;
    public String originalTitle;
    public float voteAverage;

    Movie(JSONObject movieDescr) {
        try {
            getDataFromJSON(movieDescr);
        } catch (JSONException jsex) {

        }
    }

    private void getDataFromJSON(JSONObject obj) throws JSONException{
        final String GET_POSTER = "poster_path";
        final String GET_OVER = "overview";
        final String GET_RDATE = "release_date";
        final String GET_ID = "id";
        final String GET_TITLE = "original_title";
        final String GET_VOTE = "vote_average";

        posterPath = obj.getString(GET_POSTER);

        overview = obj.getString(GET_OVER);
        releaseDate = obj.getString(GET_RDATE);
        id = obj.getInt(GET_ID);
        originalTitle = obj.getString(GET_TITLE);
        voteAverage = (float)obj.getDouble(GET_VOTE);
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(originalTitle + " ");
        buffer.append(id + " ");
        buffer.append(releaseDate + " ");
        buffer.append(voteAverage + " ");
        return buffer.toString();
    }

}
