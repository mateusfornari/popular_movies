package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.domain.Review;
import com.example.android.popularmovies.domain.Video;

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

    private static final String FIELD_TITLE = "original_title";
    private static final String FIELD_ID = "id";
    private static final String FIELD_RELEASE_DATE = "release_date";
    private static final String FIELD_SYNOPSIS = "overview";
    private static final String FIELD_RATING = "vote_average";
    private static final String FIELD_POSTER_PATH = "poster_path";

    private static final String FIELD_KEY = "key";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_SITE = "site";

    private static final String FIELD_AUTHOR = "author";
    private static final String FIELD_CONTENT = "content";



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

    public static List<Video> fetchVideoList(String json){

        List<Video> list = null;

        try {
            JSONObject root = new JSONObject(json);

            JSONArray results = root.getJSONArray(FIELD_RESULTS);

            list = new ArrayList<>();

            for(int i = 0; i < results.length(); i++){
                JSONObject item = results.getJSONObject(i);

                Video video = new Video();

                filloutVideo(video, item);

                list.add(video);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }


    public static List<Review> fetchReviewList(String json){

        List<Review> list = null;

        try {
            JSONObject root = new JSONObject(json);

            JSONArray results = root.getJSONArray(FIELD_RESULTS);

            list = new ArrayList<>();

            for(int i = 0; i < results.length(); i++){
                JSONObject item = results.getJSONObject(i);

                Review review = new Review();

                filloutReview(review, item);

                list.add(review);
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

    private static void filloutVideo(Video video, JSONObject item) throws JSONException {
        video.setKey(item.getString(FIELD_KEY));
        video.setName(item.getString(FIELD_NAME));
        video.setSite(item.getString(FIELD_SITE));
    }

    private static void filloutReview(Review review, JSONObject item) throws JSONException {
        review.setAuthor(item.getString(FIELD_AUTHOR));
        review.setContent(item.getString(FIELD_CONTENT));

    }

}
