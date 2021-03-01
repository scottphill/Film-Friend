package com.usf_mobile_dev.filmfriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter
        <HistoryRecyclerViewAdapter.MyViewHolder>{

    Context mContext;
    List<MovieListing> mMovies;

    public HistoryRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        //this.mMovies = mMovies;
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
            holder.movieItemView.setText(current.getMovieName());
        } else {
            // Covers the case of data not being ready yet.
            holder.movieItemView.setText("No Word");
        }
    }

    public void setMovieNames(List<MovieListing> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();
        else return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView movieItemView;

        public MyViewHolder(View itemView) {
            super(itemView);

            movieItemView = (TextView) itemView.findViewById((R.id.movieName));
        }
    }
}
