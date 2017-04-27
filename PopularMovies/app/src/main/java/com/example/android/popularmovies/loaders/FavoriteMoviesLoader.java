package com.example.android.popularmovies.loaders;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.domain.Movie;

/**
 * Created by mateus on 25/04/17.
 */

public class FavoriteMoviesLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;

    private FavoriteMoviesListener favoriteMoviesListener;
    private FavoriteMovieByIdListener favoriteMovieByIdListener;

    private static final int LOAD_FAVORITE_MOVIES_ID = 104;
    private static final int LOAD_FAVORITE_MOVIE_BY_ID = 105;

    private LoaderManager loaderManager;

    private int movieId;

    private static final String[] MOVIE_PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH
    };

    public FavoriteMoviesLoader(Context context, LoaderManager loaderManager){
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

    public void loadFavoriteMovies(FavoriteMoviesListener listener){
        this.favoriteMoviesListener = listener;
        initLoader(LOAD_FAVORITE_MOVIES_ID);
    }


    public void loadFavoriteMovieById(int id, FavoriteMovieByIdListener listener){
        this.favoriteMovieByIdListener = listener;
        this.movieId = id;
        initLoader(LOAD_FAVORITE_MOVIE_BY_ID);
    }

    private void callbackOnLoadFinished(Cursor data){
        if(favoriteMoviesListener != null){
            favoriteMoviesListener.onFavoriteMoviesLoaded(data);
        }
    }

    private void callbackOnFavoriteMovieLoaded(Cursor data){
        if(favoriteMovieByIdListener != null){
            favoriteMovieByIdListener.onFavoriteMovieByIdLoaded(data);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        switch (id){
            case LOAD_FAVORITE_MOVIES_ID:
                uri = MovieContract.MovieEntry.CONTENT_URI;
                break;
            case LOAD_FAVORITE_MOVIE_BY_ID:
                uri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, movieId);
                break;
            default:
                return null;

        }
        return new CursorLoader(context, uri, MOVIE_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case LOAD_FAVORITE_MOVIES_ID:
                callbackOnLoadFinished(data);
                break;
            case LOAD_FAVORITE_MOVIE_BY_ID:
                callbackOnFavoriteMovieLoaded(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface FavoriteMoviesListener{
        void onFavoriteMoviesLoaded(Cursor cursor);
    }

    public interface FavoriteMovieByIdListener{
        void onFavoriteMovieByIdLoaded(Cursor data);
    }
}
