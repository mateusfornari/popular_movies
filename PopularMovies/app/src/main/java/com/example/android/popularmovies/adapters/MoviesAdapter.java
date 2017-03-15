package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mateus on 14/03/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> movies;

    private Context mContext;

    public MoviesAdapter(List<Movie> movies){
        this.movies = movies;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder{

        private ImageView mPosterImageView;

        private static final String BASE_URL = "http://image.tmdb.org/t/p/";

        private static final String IMAGE_SIZE = "w185";


        public MoviesViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
        }

        public void bind(Movie movie){
            String imageUrl = buildImageUrl(movie);
            Picasso.with(mContext).load(imageUrl).into(mPosterImageView);
        }

        private String buildImageUrl(Movie movie){
            return BASE_URL + IMAGE_SIZE + movie.getPosterPath();
        }
    }
}
