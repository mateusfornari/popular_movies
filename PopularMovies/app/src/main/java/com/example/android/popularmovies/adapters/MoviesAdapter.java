package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mateus on 14/03/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> movies;

    private Context mContext;

    private OnMovieClickListener mOnMovieClickListener;

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

    public void setOnMovieClickListener(OnMovieClickListener mOnMovieClickListener) {
        this.mOnMovieClickListener = mOnMovieClickListener;
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mPosterImageView;


        public MoviesViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
            mPosterImageView.setOnClickListener(this);
        }

        public void bind(Movie movie){
            String imageUrl = NetworkUtils.buildImageUrl(movie);
            Picasso.with(mContext).load(imageUrl).into(mPosterImageView);
        }



        @Override
        public void onClick(View v) {
            if(mOnMovieClickListener != null) {
                mOnMovieClickListener.onClick(movies.get(getAdapterPosition()));
            }
        }
    }

    public interface OnMovieClickListener{
        void onClick(Movie movie);
    }
}
