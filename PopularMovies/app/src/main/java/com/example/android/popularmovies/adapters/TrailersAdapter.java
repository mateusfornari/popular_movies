package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.domain.Video;

import java.util.List;

/**
 * Created by mateus on 30/04/17.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private Context mContext;
    private List<Video> videos;
    private OnVideoClickListener onVideoClickListener;

    public TrailersAdapter(List<Video> videos){
        this.videos = videos;
    }

    public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
        this.onVideoClickListener = onVideoClickListener;
    }

    @Override
    public TrailersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.video_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.ViewHolder holder, int position) {
        holder.bind(videos.get(position));
    }

    @Override
    public int getItemCount() {
        if(videos == null)
            return 0;
        return videos.size();
    }

    public void swapVideos(List<Video> videos){
        this.videos = videos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView videoNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            videoNameTextView = (TextView) itemView.findViewById(R.id.tv_video_name);
            itemView.setOnClickListener(this);
        }

        public void bind(Video video){
            videoNameTextView.setText(video.getName());
        }

        @Override
        public void onClick(View v) {
            if(onVideoClickListener != null){
                onVideoClickListener.onVideoClick(videos.get(getAdapterPosition()));
            }
        }
    }

    public interface OnVideoClickListener{
        void onVideoClick(Video video);
    }
}
