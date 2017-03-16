package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    private Movie movie;

    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mRatingTextView;
    private TextView mSynopsisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(MainActivity.EXTRA_MOVIE)){
            movie = intent.getParcelableExtra(MainActivity.EXTRA_MOVIE);
            Log.d(LOG_TAG, movie.toString());
            bindComponents();
        }
    }

    private void bindComponents(){

        mPosterImageView = (ImageView) findViewById(R.id.iv_poster_details);
        mTitleTextView = (TextView) findViewById(R.id.tv_display_title);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_display_release_date);
        mRatingTextView = (TextView) findViewById(R.id.tv_display_rating);
        mSynopsisTextView = (TextView) findViewById(R.id.tv_display_synopsis);

        displayMovieData();
    }

    private void displayMovieData(){
        String url = NetworkUtils.buildImageUrl(movie);
        Picasso.with(this).load(url).into(mPosterImageView);
        mTitleTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mRatingTextView.setText(String.valueOf(movie.getRating()));
        mSynopsisTextView.setText(movie.getSynopsis());
    }
}
