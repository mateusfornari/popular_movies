package com.example.android.popularmovies.utilities;

import android.database.Cursor;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by mateus on 23/04/17.
 */

public class MovieCursorUtils {

    public static Movie movieFromCursor(Cursor cursor, int position){
        Movie movie = null;

        if(cursor.moveToPosition(position)){
            movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE)));
            movie.setSynopsis(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_SYNOPSIS)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE)));
            movie.setRating(cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_RATING)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH)));
        }

        return movie;
    }
}
