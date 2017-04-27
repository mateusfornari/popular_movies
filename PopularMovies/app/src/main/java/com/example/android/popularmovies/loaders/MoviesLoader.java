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

public class MoviesLoader implements LoaderManager.LoaderCallbacks<String>{

    private Context context;

    private LoadMoviesListener loadMoviesListener;
    private LoadTrailersListener loadTrailersListener;
    private LoadReviewsListener loadReviewsListener;

    private static final int LOAD_MOVIES_ID = 100;
    private static final int LOAD_TRAILERS_ID = 101;
    private static final int LOAD_REVIEWS_ID = 102;

    private String sort;

    private int movieId;

    private LoaderManager loaderManager;

    public MoviesLoader(Context context, LoaderManager loaderManager){
        this.context = context;
        this.loaderManager = loaderManager;
    }

    private void initLoader(int id){
        Loader<String> loader = loaderManager.getLoader(id);
        if(loader == null){
            loaderManager.initLoader(id, null, this);
        }else{
            loaderManager.restartLoader(id, null, this);
        }
    }

    public void loadMovies(String sort, LoadMoviesListener listener){
        this.sort = sort;
        this.loadMoviesListener = listener;
        initLoader(LOAD_MOVIES_ID);
    }

    public void loadTrailers(int id, LoadTrailersListener listener){
        this.movieId = id;
        this.loadTrailersListener = listener;
        initLoader(LOAD_TRAILERS_ID);
    }

    public void loadReviews(int id, LoadReviewsListener listener){
        this.movieId = id;
        this.loadReviewsListener = listener;
        initLoader(LOAD_REVIEWS_ID);
    }

    private void callbackOnStartLoadingMovies(){
        if(loadMoviesListener != null){
            loadMoviesListener.onStartLoadingMovies();
        }
    }

    private void callbackOnMoviesLoaded(String data){
        if(loadMoviesListener != null){
            loadMoviesListener.onMoviesLoaded(data);
        }
    }


    private void callbackOnStartLoadingTrailers(){
        if(loadTrailersListener != null){
            loadTrailersListener.onStartLoadingTrailers();
        }
    }

    private void callbackOnTrailersLoaded(String data){
        if(loadTrailersListener != null){
            loadTrailersListener.onTrailersLoaded(data);
        }
    }


    private void callbackOnStartLoadingReviews(){
        if(loadReviewsListener != null){
            loadReviewsListener.onStartLoadingReviews();
        }
    }

    private void callbackOnReviewsLoaded(String data){
        if(loadReviewsListener != null){
            loadReviewsListener.onReviewsLoaded(data);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOAD_MOVIES_ID:
                return new LoadMovies(context, this);
            case LOAD_REVIEWS_ID:
                return new LoadReviews(context, this);
            case LOAD_TRAILERS_ID:
                return new LoadTrailers(context, this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        switch (loader.getId()) {
            case LOAD_MOVIES_ID:
                callbackOnMoviesLoaded(data);
                break;
            case LOAD_REVIEWS_ID:
                callbackOnReviewsLoaded(data);
                break;
            case LOAD_TRAILERS_ID:
                callbackOnTrailersLoaded(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    static class LoadMovies extends AsyncTaskLoader<String>{

        private MoviesLoader loader;

        public LoadMovies(Context context, MoviesLoader loader){
            super(context);
            this.loader = loader;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            loader.callbackOnStartLoadingMovies();
            forceLoad();
        }

        @Override
        public String loadInBackground() {
            try {

                URL url = NetworkUtils.buildUrl(loader.sort);
                return NetworkUtils.getResponseFromHttpUrl(url);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    static class LoadTrailers extends AsyncTaskLoader<String>{

        private MoviesLoader loader;

        public LoadTrailers(Context context, MoviesLoader loader){
            super(context);
            this.loader = loader;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            loader.callbackOnStartLoadingTrailers();
            forceLoad();
        }

        @Override
        public String loadInBackground() {
            try {

                URL url = NetworkUtils.buildTrailersUrl(loader.movieId);
                return NetworkUtils.getResponseFromHttpUrl(url);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    static class LoadReviews extends AsyncTaskLoader<String>{

        private MoviesLoader loader;

        public LoadReviews(Context context, MoviesLoader loader){
            super(context);
            this.loader = loader;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            loader.callbackOnStartLoadingReviews();
            forceLoad();
        }

        @Override
        public String loadInBackground() {
            try {

                URL url = NetworkUtils.buildReviewsUrl(loader.movieId);
                return NetworkUtils.getResponseFromHttpUrl(url);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public interface LoadMoviesListener{
        void onStartLoadingMovies();
        void onMoviesLoaded(String data);
    }

    public interface LoadTrailersListener{
        void onStartLoadingTrailers();
        void onTrailersLoaded(String data);
    }

    public interface LoadReviewsListener{
        void onStartLoadingReviews();
        void onReviewsLoaded(String data);
    }
}
