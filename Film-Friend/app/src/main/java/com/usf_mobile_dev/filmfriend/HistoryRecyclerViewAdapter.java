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
    List<Movie_temp> mData;

    public HistoryRecyclerViewAdapter(Context mContext, List<Movie_temp> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_movie_temp, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_movie_name.setText(mData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_movie_name;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_movie_name = (TextView) itemView.findViewById((R.id.movie_text));
        }
    }
}
