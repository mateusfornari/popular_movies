package com.example.android.popularmovies.loaders;

/**
 * Created by mateus on 24/04/17.
 */

public interface MoviesLoaderListener {
    void onStartLoading();
    void onLoadFinished(String data);
}
