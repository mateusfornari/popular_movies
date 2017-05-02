package com.example.android.popularmovies.utilities;

import android.content.ContentUris;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.domain.Video;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by mateus on 13/03/17.
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    //TODO fill this string out with your API key.
    private static final String API_KEY = "";

    private static final String API_KEY_PARAMETER = "api_key";

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    private static final String IMAGE_SIZE = "w185";

    private static final String PATH_TRAILERS = "videos";
    private static final String PATH_REVIEWS = "reviews";

    private static final String YOUTUBE_BASE_URL = "https://youtu.be/";

    /**
     * Buid request URL
     * @param sort Sort method "popular" or "top_rated"
     * @return URL
     */
    public static URL buildUrl(String sort){
        Uri uri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(sort)
                .appendQueryParameter(API_KEY_PARAMETER, API_KEY)
                .build();

        Log.d(LOG_TAG, "URI: " + uri.toString());

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTrailersUrl(int id){
        Uri uri = ContentUris.withAppendedId(Uri.parse(BASE_URL), id)
                .buildUpon()
                .appendPath(PATH_TRAILERS)
                .appendQueryParameter(API_KEY_PARAMETER, API_KEY)
                .build();

        Log.d(LOG_TAG, "URI: " + uri.toString());

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewsUrl(int id){
        Uri uri = ContentUris.withAppendedId(Uri.parse(BASE_URL), id)
                .buildUpon()
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(API_KEY_PARAMETER, API_KEY)
                .build();

        Log.d(LOG_TAG, "URI: " + uri.toString());

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri buildYutubeUri(Video video){
        if(video.getSite().toLowerCase().equals("youtube")){
            Uri uri = Uri.parse(YOUTUBE_BASE_URL)
                    .buildUpon()
                    .appendPath(video.getKey())
                    .build();
            return uri;
        }
        return null;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Check if there is internet connection
     * @param context
     * @return
     *
     * FROM: http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static String buildImageUrl(Movie movie){
        return BASE_IMAGE_URL + IMAGE_SIZE + movie.getPosterPath();
    }

}
