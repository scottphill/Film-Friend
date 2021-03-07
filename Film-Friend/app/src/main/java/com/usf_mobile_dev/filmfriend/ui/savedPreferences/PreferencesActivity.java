package com.usf_mobile_dev.filmfriend.ui.savedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.PreferenceRecyclerViewAdapter;
import com.usf_mobile_dev.filmfriend.R;

import java.util.LinkedList;

public class PreferencesActivity extends AppCompatActivity {

    private final LinkedList<SavedPreference> mPreferenceList = new LinkedList<>();

    private RecyclerView mRecyclerView;
    private PreferenceRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);


        for (int i = 0; i < 20; i++) {
            mPreferenceList.addLast(new SavedPreference("Preference" + i));
        }

        // Create recycler view.
        mRecyclerView = findViewById(R.id.preferences_recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PreferenceRecyclerViewAdapter(this, mPreferenceList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
