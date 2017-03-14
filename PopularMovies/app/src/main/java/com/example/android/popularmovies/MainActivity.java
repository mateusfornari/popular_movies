package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMoviesList;
    private ProgressBar mProgressIndicator;
    private Button mRefreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindComponents();

    }

    private void bindComponents(){
        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies_list);
        mProgressIndicator = (ProgressBar) findViewById(R.id.pb_progress_indicator);
        mRefreshButton = (Button) findViewById(R.id.bt_refresh);
    }


}
