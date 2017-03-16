package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.utilities.JsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MoviesAdapter.OnMovieClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMoviesList;
    private ProgressBar mLoadingIndicator;
    private Button mRefreshButton;

    private List<Movie> movies;

    private String mSort = "popular";

    public static final String EXTRA_MOVIE = "movie";

    private static final String BUNDLE_KEY = "movies_bundle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindComponents();

        if(savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY)){
            Log.d(LOG_TAG, "Has saved bundle");
            movies = savedInstanceState.getParcelableArrayList(BUNDLE_KEY);
            displayMovieList();
        }else {
            loadMovies();
        }
    }

    private void bindComponents(){
        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies_list);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRefreshButton = (Button) findViewById(R.id.bt_refresh);
        mRefreshButton.setOnClickListener(this);
    }

    private void loadMovies(){

        if(NetworkUtils.isOnline(this)) {
            URL url = NetworkUtils.buildUrl(mSort);
            new MoviesTask().execute(url);
        }else{
            showErrorMessage();
        }
    }

    private void displayMovieList(){
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);

        MoviesAdapter adapter = new MoviesAdapter(movies);

        adapter.setOnMovieClickListener(MainActivity.this);

        mMoviesList.setLayoutManager(manager);

        mMoviesList.setAdapter(adapter);
    }

    private void showErrorMessage(){
        Toast.makeText(this, getString(R.string.msg_internet_required), Toast.LENGTH_LONG).show();
        mRefreshButton.setVisibility(View.VISIBLE);
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
        super.onSaveInstanceState(outState);
    }

    class MoviesTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mRefreshButton.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {

            try {
                return NetworkUtils.getResponseFromHttpUrl(params[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null && !s.isEmpty()) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                movies = JsonUtils.fetchMovieList(s);

                displayMovieList();
            }else{
                showErrorMessage();
            }

        }
    }


}
