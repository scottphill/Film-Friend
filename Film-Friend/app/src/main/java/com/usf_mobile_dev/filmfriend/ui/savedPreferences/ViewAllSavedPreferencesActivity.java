package com.usf_mobile_dev.filmfriend.ui.savedPreferences;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.PreferenceRecyclerViewAdapter;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.Tutorial;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;
import com.usf_mobile_dev.filmfriend.ui.settings.Settings;

import java.util.List;

public class ViewAllSavedPreferencesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PreferenceRecyclerViewAdapter mAdapter;
    private ViewAllSavedPreferencesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // Gets the viewmodel for this activity
        viewModel = new ViewModelProvider(this).get(ViewAllSavedPreferencesViewModel.class);

        // Create recycler view.
        mRecyclerView = findViewById(R.id.preferences_recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PreferenceRecyclerViewAdapter(
                this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }}, new View.OnClickListener() {
                    // Gets the match preferences from the view and deletes the
                    //   match preferences from the room database.
                    @Override
                    public void onClick(View v) {
                        viewModel.deleteMatchPreferences(
                                (MatchPreferences) (v.getTag())
                        );

                        Toast.makeText(
                                v.getContext(),
                                "Match Preferences Deleted!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }});

        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sets the preferences list to change according to the match_preferences db table
        viewModel.getAllMatchPreferences().observe(
                this,new Observer<List<MatchPreferences>>() {
                    @Override
                    public void onChanged(List<MatchPreferences> matchPreferences) {
                        mAdapter.setmMatchPreferences(matchPreferences);
                    }});
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.tutorial_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Do nothing
        if (item.getItemId() == R.id.tutorial) {
            Tutorial t = new Tutorial();
            t.launchPageTutorial(this, "Preferences List");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
