package com.usf_mobile_dev.filmfriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.ui.savedPreferences.PreferencesActivity;
import com.usf_mobile_dev.filmfriend.ui.savedPreferences.SavedPreference;

import java.util.List;

public class PreferenceRecyclerViewAdapter extends RecyclerView.Adapter
        <PreferenceRecyclerViewAdapter.PreferenceViewHolder>{

    Context mContext;
    List<SavedPreference> mSavedPreferences;

    public PreferenceRecyclerViewAdapter(Context mContext, List<SavedPreference> preferences) {
        this.mContext = mContext;
        this.mSavedPreferences = preferences;
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

    @Override
    public int getItemCount() {
        if (mSavedPreferences != null)
            return mSavedPreferences.size();
        else return 0;
    }

    public class PreferenceViewHolder extends RecyclerView.ViewHolder {

        private TextView preferenceItemView;

        public PreferenceViewHolder(View itemView) {
            super(itemView);

            preferenceItemView = (TextView) itemView.findViewById((R.id.preference_name));

            itemView.findViewById(R.id.preference_delete_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Delete", Toast.LENGTH_LONG).show();
                }
            });
            itemView.findViewById(R.id.preference_edit_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Edit", Toast.LENGTH_LONG).show();
                }
            });
            itemView.findViewById(R.id.preference_load_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Load", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
