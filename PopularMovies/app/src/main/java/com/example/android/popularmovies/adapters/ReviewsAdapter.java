package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.domain.Review;

import java.util.List;

/**
 * Created by mateus on 30/04/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> reviews;

    private Context mContext;

    public ReviewsAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    public void swapReviews(List<Review> reviews){
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(reviews == null)
            return 0;
        return reviews.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView reviewAuthorTextView;
        private TextView reviewContentTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            reviewAuthorTextView = (TextView) itemView.findViewById(R.id.tv_review_author);
            reviewContentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
        }

        public void bind(Review review){
            reviewAuthorTextView.setText(review.getAuthor());
            reviewContentTextView.setText(review.getContent());
        }


    }

}
