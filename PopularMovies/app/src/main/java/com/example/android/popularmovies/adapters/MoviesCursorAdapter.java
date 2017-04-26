package com.example.android.popularmovies.adapters;

import android.database.Cursor;
import android.view.View;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.utilities.MovieCursorUtils;

/**
 * Created by mateus on 23/04/17.
 */

public class MoviesCursorAdapter extends MoviesAdapter {

    private Cursor mCursor;

    public MoviesCursorAdapter(Cursor cursor){
        super(null);
        mCursor = cursor;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        Movie movie = MovieCursorUtils.movieFromCursor(mCursor, position);
        if (movie != null){
            holder.bind(movie);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    class MoviesCursorViewHolder extends MoviesViewHolder{

        public MoviesCursorViewHolder(View itemView){
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            Movie movie = MovieCursorUtils.movieFromCursor(mCursor, getAdapterPosition());
            if (movie != null && mOnMovieClickListener != null){
                mOnMovieClickListener.onClick(movie);
            }
        }
    }
}
