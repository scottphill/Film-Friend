package com.usf_mobile_dev.filmfriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.ui.savedPreferences.SavedPreference;

import java.util.List;

public class PreferenceRecyclerViewAdapter extends RecyclerView.Adapter
        <PreferenceRecyclerViewAdapter.PreferenceViewHolder>{

    Context mContext;
    List<SavedPreference> mSavedPreferences;

    public PreferenceRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PreferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_saved_preference, parent, false);
        PreferenceViewHolder vHolder = new PreferenceViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder, int position) {
        if (mSavedPreferences != null) {
            SavedPreference current = mSavedPreferences.get(position);
            holder.preferenceItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.preferenceItemView.setText("No Word");
        }
    }

    public void setMovieNames(List<SavedPreference> preferences){
        mSavedPreferences = preferences;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mSavedPreferences != null)
            return mSavedPreferences.size();
        else return 0;
    }

    public static class PreferenceViewHolder extends RecyclerView.ViewHolder {

        private TextView preferenceItemView;

        public PreferenceViewHolder(View itemView) {
            super(itemView);

            preferenceItemView = (TextView) itemView.findViewById((R.id.preference_name));
        }
    }
}
