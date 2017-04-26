package com.example.android.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.android.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by mateus on 24/04/17.
 */

public class MoviesLoader implements LoaderManager.LoaderCallbacks<String> {

    private Context context;

    private MoviesLoaderListener listener;

    private static final int LOADER_ID = 100;

    private String sort;

    private LoaderManager loaderManager;

    public MoviesLoader(Context context, MoviesLoaderListener listener, LoaderManager loaderManager){
        this.context = context;
        this.listener = listener;
        this.loaderManager = loaderManager;
    }

    public void execute(String sort){
        this.sort = sort;
        Loader<String> loader = loaderManager.getLoader(LOADER_ID);
        if(loader == null){
            loaderManager.initLoader(LOADER_ID, null, this);
        }else{
            loaderManager.restartLoader(LOADER_ID, null, this);
        }
    }

    private void callbackOnStartLoading(){
        if(listener != null){
            listener.onStartLoading();
        }
    }

    private void callbackOnLoadFinished(String data){
        if(listener != null){
            listener.onLoadFinished(data);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(context) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                callbackOnStartLoading();
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {

                    URL url = NetworkUtils.buildUrl(sort);
                    return NetworkUtils.getResponseFromHttpUrl(url);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        callbackOnLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
