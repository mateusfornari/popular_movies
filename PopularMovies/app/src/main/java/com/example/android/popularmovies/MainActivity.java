package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mMoviesList;
    private ProgressBar mLoadingIndicator;
    private Button mRefreshButton;

    private String mSort = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindComponents();

        loadMovies();
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

    private void showErrorMessage(){
        Toast.makeText(this, getString(R.string.msg_internet_required), Toast.LENGTH_LONG).show();
        mRefreshButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        loadMovies();
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
                List<Movie> movies = JsonUtils.fetchMovieList(s);

                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);

                MoviesAdapter adapter = new MoviesAdapter(movies);

                mMoviesList.setLayoutManager(manager);

                mMoviesList.setAdapter(adapter);
            }else{
                showErrorMessage();
            }

        }
    }


}
