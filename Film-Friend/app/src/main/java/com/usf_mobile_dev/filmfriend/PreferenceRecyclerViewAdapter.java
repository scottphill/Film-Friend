package com.usf_mobile_dev.filmfriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import java.util.List;

public class PreferenceRecyclerViewAdapter extends RecyclerView.Adapter
        <PreferenceRecyclerViewAdapter.PreferenceViewHolder>{

    Context mContext;
    List<MatchPreferences> mMatchPreferences;

    public PreferenceRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    // Changes the list of MatchPreferences objects and updates the recyclerview
    public void setmMatchPreferences(List<MatchPreferences> matchPreferences) {
        this.mMatchPreferences = matchPreferences;
        notifyDataSetChanged();
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
        if (mMatchPreferences != null) {
            MatchPreferences current = mMatchPreferences.get(position);
            holder.preferenceItemView.setText(current.getPreference_title());
        } else {
            // Covers the case of data not being ready yet.
            holder.preferenceItemView.setText("No Preferences atm");
        }
    }

    @Override
    public int getItemCount() {
        if (mMatchPreferences != null)
            return mMatchPreferences.size();
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
