package com.usf_mobile_dev.filmfriend.ui.savedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.PreferenceRecyclerViewAdapter;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import java.util.List;

public class ViewPreferencesActivity extends AppCompatActivity
{

    private RecyclerView mRecyclerView;
    private PreferenceRecyclerViewAdapter mAdapter;
    private ViewPreferencesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // Gets the viewmodel for this activity
        viewModel = new ViewModelProvider(this).get(ViewPreferencesViewModel.class);

        // Create recycler view.
        mRecyclerView = findViewById(R.id.preferences_recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PreferenceRecyclerViewAdapter(this);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sets the preferences list to change according to the match_preferences db table
        viewModel.getAllMatchPreferences().observe(
                this,
                new Observer<List<MatchPreferences>>() {
                    @Override
                    public void onChanged(List<MatchPreferences> matchPreferences) {
                        mAdapter.setmMatchPreferences(matchPreferences);
                    }
                }
        );
    }
}
