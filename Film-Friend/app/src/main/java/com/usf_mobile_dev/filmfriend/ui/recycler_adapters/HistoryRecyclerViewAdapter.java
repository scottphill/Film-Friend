package com.usf_mobile_dev.filmfriend.ui.recycler_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MovieListing;
import com.usf_mobile_dev.filmfriend.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter
        <HistoryRecyclerViewAdapter.MyViewHolder>{

    Context mContext;
    List<MovieListing> mMovies;
    private static ClickListener clickListener;

    public HistoryRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_movie_listing, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (mMovies != null) {
            MovieListing current = mMovies.get(position);
            holder.movieItemName.setText(current.getMovie().getTitle());
            holder.movieItemRelease.setText((String.valueOf(current.getMovie().getReleaseYear())));
            holder.listingDate.setText(String.valueOf(current.getDateViewed()));

            String posterUrl = "https://image.tmdb.org/t/p/w342"
                    + current.getMovie().getPosterPath();
            Glide.with(mContext)
                    .load(posterUrl)
                    .into(holder.movieItemPoster);
        } else {
            // Covers the case of data not being ready yet.
            holder.movieItemName.setText("No Word");
        }
    }

    public void setMovies(List<MovieListing> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();
        else return 0;
    }

    public MovieListing getMovieAtPosition(int position) {
        return mMovies.get(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView movieItemName;
        private TextView movieItemRelease;
        private TextView listingDate;
        private ImageView movieItemPoster;

        public MyViewHolder(View itemView) {
            super(itemView);

            movieItemName = itemView.findViewById(R.id.movieListingName);
            movieItemRelease = itemView.findViewById(R.id.movieListingRelease);
            movieItemPoster = itemView.findViewById(R.id.movieListingPoster);
            listingDate = itemView.findViewById(R.id.listingDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        HistoryRecyclerViewAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
