package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.domain.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mateus on 13/03/17.
 */

public class JsonUtils {

    private static final String FIELD_RESULTS = "results";

    private static final String FIELD_ID = "id";
    private static final String FIELD_TITLE = "original_title";
    private static final String FIELD_SYNOPSIS = "overview";
    private static final String FIELD_RELEASE_DATE = "release_date";
    private static final String FIELD_RATING = "vote_avarage";
    private static final String FIELD_POSTER_PATH = "poster_path";

    public static List<Movie> fetchMovieList(String json){

        List<Movie> list = null;

        try {
            JSONObject root = new JSONObject(json);

            JSONArray results = root.getJSONArray(FIELD_RESULTS);

            list = new ArrayList<>();

            for(int i = 0; i < results.length(); i++){
                JSONObject item = results.getJSONObject(i);

                Movie movie = new Movie();

                filloutMovie(movie, item);

                list.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static void filloutMovie(Movie movie, JSONObject item) throws JSONException {
        movie.setId(item.getInt(FIELD_ID));
        movie.setTitle(item.getString(FIELD_TITLE));
        movie.setSynopsis(item.getString(FIELD_SYNOPSIS));
        movie.setReleaseDate(item.getString(FIELD_RELEASE_DATE));
        movie.setRating(item.getDouble(FIELD_RATING));
        movie.setPosterPath(item.getString(FIELD_POSTER_PATH));
    }

}
