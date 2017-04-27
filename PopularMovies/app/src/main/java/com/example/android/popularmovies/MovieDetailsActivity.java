package com.example.android.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.loaders.FavoriteMoviesLoader;
import com.example.android.popularmovies.loaders.MoviesLoader;
import com.example.android.popularmovies.utilities.MovieCursorUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity implements
        FavoriteMoviesLoader.FavoriteMovieByIdListener,
        MoviesLoader.LoadReviewsListener,
        MoviesLoader.LoadTrailersListener{

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    private Movie movie;

    private ActivityMovieDetailsBinding mBinding;

    private FavoriteMoviesLoader favoriteMoviesLoader;

    private MoviesLoader moviesLoader;

    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        mBinding.cbFavorite.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(MainActivity.EXTRA_MOVIE)){
            movie = intent.getParcelableExtra(MainActivity.EXTRA_MOVIE);
            Log.d(LOG_TAG, movie.toString());
            bindComponents();
        }

        favoriteMoviesLoader = new FavoriteMoviesLoader(this, getSupportLoaderManager());
        favoriteMoviesLoader.loadFavoriteMovieById(movie.getId(), this);

        moviesLoader = new MoviesLoader(this, getSupportLoaderManager());

        moviesLoader.loadReviews(movie.getId(), this);
        moviesLoader.loadTrailers(movie.getId(), this);

    }

    private void bindComponents(){

        mBinding.cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!isFavorite)
                        insertFavorite();
                }else{
                    if(isFavorite)
                        deleteFavorite();
                }
            }
        });

        displayMovieData();
    }


    private void insertFavorite(){
        ContentValues values = MovieCursorUtils.getContentValues(movie);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        getContentResolver().insert(uri, values);
    }

    private void deleteFavorite(){
        Uri uri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, movie.getId());
        getContentResolver().delete(uri, null, null);
    }



    private void displayMovieData(){
        String url = NetworkUtils.buildImageUrl(movie);
        Picasso.with(this).load(url).into(mBinding.ivPosterDetails);
        mBinding.tvDisplayTitle.setText(movie.getTitle());
        mBinding.tvDisplayReleaseDate.setText(movie.getReleaseDate());
        mBinding.tvDisplayRating.setText(String.valueOf(movie.getRating()));
        mBinding.tvDisplaySynopsis.setText(movie.getSynopsis());
    }

    @Override
    public void onFavoriteMovieByIdLoaded(Cursor data) {
        mBinding.cbFavorite.setVisibility(View.VISIBLE);
        if(data != null && data.getCount() > 0){
            movie = MovieCursorUtils.movieFromCursor(data, 0);
            isFavorite = true;
        }else{
            isFavorite = false;
        }
        mBinding.cbFavorite.setChecked(isFavorite);
    }

    @Override
    public void onStartLoadingTrailers() {
        Log.d(LOG_TAG, "Loading trailers");
    }

    @Override
    public void onTrailersLoaded(String data) {
        Log.d(LOG_TAG, data);
    }

    @Override
    public void onStartLoadingReviews() {
        Log.d(LOG_TAG, "Loading reviews");
    }

    @Override
    public void onReviewsLoaded(String data) {
        Log.d(LOG_TAG, data);
    }
}
