package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.adapters.MoviesCursorAdapter;
import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.loaders.FavoriteMoviesLoader;
import com.example.android.popularmovies.loaders.MoviesLoader;
import com.example.android.popularmovies.utilities.JsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,
        MoviesAdapter.OnMovieClickListener,
        MoviesLoader.LoadMoviesListener,
        FavoriteMoviesLoader.FavoriteMoviesListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String SORT_POPULAR = "popular";
    private static final String SORT_TOP_RATED = "top_rated";
    private static final String SORT_FAVORITE = "favorite";

    private ActivityMainBinding mBinding;

    private List<Movie> movies;

    private String mSort = SORT_POPULAR;

    public static final String EXTRA_MOVIE = "movie";

    private static final String BUNDLE_KEY = "movies_bundle";
    private static final String BUNDLE_SORT_KEY = "sort_bundle";

    private MoviesCursorAdapter mMoviesCursorAdapter;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.btRefresh.setOnClickListener(this);

        mMoviesCursorAdapter = new MoviesCursorAdapter(null);
        mMoviesCursorAdapter.setOnMovieClickListener(this);

        mMoviesAdapter = new MoviesAdapter(movies);
        mMoviesAdapter.setOnMovieClickListener(this);

        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "There is saved instance state");
        } else {
            Log.d(LOG_TAG, "There is no saved instance state");
            loadMovies();
        }

    }


    private void loadMovies() {
        Log.d(LOG_TAG, "loadMovies");
        LoaderManager manager = getSupportLoaderManager();
        if (mSort.equals(SORT_FAVORITE)) {
            FavoriteMoviesLoader loader = new FavoriteMoviesLoader(this, manager);
            loader.loadFavoriteMovies(this);
        } else {
            if (NetworkUtils.isOnline(this)) {
                MoviesLoader loader = new MoviesLoader(this, manager);
                loader.loadMovies(mSort, this);
            } else {
                showErrorMessage();
            }
        }
    }

    private void displayMovieList(MoviesAdapter adapter) {
        int gridColumns = getResources().getInteger(R.integer.grid_columns);
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), gridColumns);

        adapter.setOnMovieClickListener(MainActivity.this);

        mBinding.rvMoviesList.setLayoutManager(manager);
        mBinding.rvMoviesList.setAdapter(adapter);
    }

    private void showErrorMessage() {
        Toast.makeText(this, getString(R.string.msg_internet_required), Toast.LENGTH_LONG).show();
        mBinding.btRefresh.setVisibility(View.VISIBLE);
        mBinding.rvMoviesList.setVisibility(View.INVISIBLE);
    }

    private void showCursorErrorMessage() {
        Toast.makeText(this, getString(R.string.msg_no_favorite), Toast.LENGTH_LONG).show();
        mBinding.btRefresh.setVisibility(View.INVISIBLE);
        mBinding.rvMoviesList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        loadMovies();
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState");
        outState.putParcelableArrayList(BUNDLE_KEY, (ArrayList<Movie>) movies);
        outState.putString(BUNDLE_SORT_KEY, mSort);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "onRestoreInstanceState");
        mSort = savedInstanceState.getString(BUNDLE_SORT_KEY);
        movies = savedInstanceState.getParcelableArrayList(BUNDLE_KEY);
        if (mSort.equals(SORT_FAVORITE) || movies == null) {
            displayMovieList(mMoviesCursorAdapter);
            loadMovies();
        } else {
            displayMovieList(mMoviesAdapter);
            mMoviesAdapter.swapMovies(movies);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(LOG_TAG, "Option selected " + id);
        switch (id) {
            case R.id.action_popular:
                mSort = SORT_POPULAR;
                loadMovies();
                return true;
            case R.id.action_top_rated:
                mSort = SORT_TOP_RATED;
                loadMovies();
                return true;
            case R.id.action_favorite:
                mSort = SORT_FAVORITE;
                loadMovies();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStartLoadingMovies() {
        mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
        mBinding.btRefresh.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMoviesLoaded(String data) {
        if (data != null && !data.isEmpty()) {
            mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
            movies = JsonUtils.fetchMovieList(data);
            mBinding.rvMoviesList.setVisibility(View.VISIBLE);
            mMoviesAdapter.swapMovies(movies);
            displayMovieList(mMoviesAdapter);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onFavoriteMoviesLoaded(Cursor cursor) {
        Log.d(LOG_TAG, "onFavoriteMoviesLoaded");
        if(cursor != null){
            mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
            mBinding.rvMoviesList.setVisibility(View.VISIBLE);
            mMoviesCursorAdapter.swapCursor(cursor);
            displayMovieList(mMoviesCursorAdapter);
        }else{
            showCursorErrorMessage();
        }
    }
}
