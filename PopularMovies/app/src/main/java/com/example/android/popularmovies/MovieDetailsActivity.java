package com.example.android.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.ReviewsAdapter;
import com.example.android.popularmovies.adapters.TrailersAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.domain.Review;
import com.example.android.popularmovies.domain.Video;
import com.example.android.popularmovies.loaders.FavoriteMoviesLoader;
import com.example.android.popularmovies.loaders.MoviesLoader;
import com.example.android.popularmovies.utilities.JsonUtils;
import com.example.android.popularmovies.utilities.MovieCursorUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements
        FavoriteMoviesLoader.FavoriteMovieByIdListener,
        MoviesLoader.LoadReviewsListener,
        MoviesLoader.LoadTrailersListener,
        TrailersAdapter.OnVideoClickListener{

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    private static final String BUNDLE_VIDEOS_LIST = "videos_list";
    private static final String BUNDLE_REVIEWS_LIST = "reviews_list";

    private Movie movie;

    private ActivityMovieDetailsBinding mBinding;

    private FavoriteMoviesLoader favoriteMoviesLoader;

    private MoviesLoader moviesLoader;

    private List<Video> videos;
    private List<Review> reviews;

    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;

    private boolean hideLoadingIndicator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(MainActivity.EXTRA_MOVIE)){
            movie = intent.getParcelableExtra(MainActivity.EXTRA_MOVIE);
            Log.d(LOG_TAG, movie.toString());
        }


        displayMovieData();

        favoriteMoviesLoader = new FavoriteMoviesLoader(this, getSupportLoaderManager());
        favoriteMoviesLoader.loadFavoriteMovieById(movie.getId(), this);

        moviesLoader = new MoviesLoader(this, getSupportLoaderManager());

        mTrailersAdapter = new TrailersAdapter(videos);
        mTrailersAdapter.setOnVideoClickListener(this);

        mReviewsAdapter = new ReviewsAdapter(reviews);


        RecyclerView.LayoutManager videosManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.includeTrailersList.rvVideosList.setLayoutManager(videosManager);
        mBinding.includeTrailersList.rvVideosList.setAdapter(mTrailersAdapter);

        RecyclerView.LayoutManager reviewsManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.includeReviewsList.rvReviewsList.setLayoutManager(reviewsManager);
        mBinding.includeReviewsList.rvReviewsList.setAdapter(mReviewsAdapter);

        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey(BUNDLE_VIDEOS_LIST)) {
                this.videos = savedInstanceState.getParcelableArrayList(BUNDLE_VIDEOS_LIST);
                mTrailersAdapter.swapVideos(this.videos);
            } else {
                if(NetworkUtils.isOnline(this)) {
                    moviesLoader.loadTrailers(movie.getId(), this);
                }else{
                    showErrorMessage();
                }
            }

            if (savedInstanceState.containsKey(BUNDLE_REVIEWS_LIST)) {
                this.reviews = savedInstanceState.getParcelableArrayList(BUNDLE_REVIEWS_LIST);
                mReviewsAdapter.swapReviews(this.reviews);
            } else {
                if(NetworkUtils.isOnline(this)) {
                    moviesLoader.loadReviews(movie.getId(), this);
                }else{
                    showErrorMessage();
                }
            }
        }else{
            if(NetworkUtils.isOnline(this)){
                moviesLoader.loadTrailers(movie.getId(), this);
                moviesLoader.loadReviews(movie.getId(), this);
            }else{
                showErrorMessage();
            }
        }
    }


    public void insertFavorite(View view){
        ContentValues values = MovieCursorUtils.getContentValues(movie);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        getContentResolver().insert(uri, values);
    }

    public void deleteFavorite(View view){
        Uri uri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, movie.getId());
        getContentResolver().delete(uri, null, null);
    }

    private void displayMovieData(){
        String url = NetworkUtils.buildImageUrl(movie);
        Picasso.with(this).load(url).into(mBinding.includeMovieDetails.ivPosterDetails);
        mBinding.tvDisplayTitle.setText(movie.getTitle());
        mBinding.includeMovieDetails.tvDisplayReleaseDate.setText(movie.getReleaseDate());
        mBinding.includeMovieDetails.tvDisplayRating.setText(String.valueOf(movie.getRating()));
        mBinding.includeMovieDetails.tvDisplaySynopsis.setText(movie.getSynopsis());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_VIDEOS_LIST, (ArrayList<Video>) videos);
        outState.putParcelableArrayList(BUNDLE_REVIEWS_LIST, (ArrayList<Review>) reviews);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFavoriteMovieByIdLoaded(Cursor data) {
        if(data != null && data.getCount() > 0){
            movie = MovieCursorUtils.movieFromCursor(data, 0);
            mBinding.includeMovieDetails.btUnmarkFavorite.setVisibility(View.VISIBLE);
            mBinding.includeMovieDetails.btMarkFavorite.setVisibility(View.INVISIBLE);
        }else{
            mBinding.includeMovieDetails.btUnmarkFavorite.setVisibility(View.INVISIBLE);
            mBinding.includeMovieDetails.btMarkFavorite.setVisibility(View.VISIBLE);
        }

    }

    private void showLoadingIndicator(){
        mBinding.includeMovieDetails.pgLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private synchronized void hideLoadingIndicator(){
        if(hideLoadingIndicator) {
            mBinding.includeMovieDetails.pgLoadingIndicator.setVisibility(View.INVISIBLE);
        }
        hideLoadingIndicator = true;
    }

    @Override
    public void onStartLoadingTrailers() {
        Log.d(LOG_TAG, "Loading trailers");
        showLoadingIndicator();
    }

    @Override
    public void onTrailersLoaded(String data) {
        Log.d(LOG_TAG, data);
        this.videos = JsonUtils.fetchVideoList(data);
        hideLoadingIndicator();
        mTrailersAdapter.swapVideos(this.videos);
        mBinding.includeTrailersList.rvVideosList.setAdapter(mTrailersAdapter);
    }

    @Override
    public void onStartLoadingReviews() {
        Log.d(LOG_TAG, "Loading reviews");
        showLoadingIndicator();
    }

    @Override
    public void onReviewsLoaded(String data) {
        Log.d(LOG_TAG, data);
        this.reviews = JsonUtils.fetchReviewList(data);
        hideLoadingIndicator();
        mReviewsAdapter.swapReviews(this.reviews);
        mBinding.includeReviewsList.rvReviewsList.setAdapter(mReviewsAdapter);
    }

    @Override
    public void onVideoClick(Video video) {
        Uri uri = NetworkUtils.buildYutubeUri(video);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void showErrorMessage() {
        Toast.makeText(this, getString(R.string.msg_internet_required), Toast.LENGTH_LONG).show();
    }

}
