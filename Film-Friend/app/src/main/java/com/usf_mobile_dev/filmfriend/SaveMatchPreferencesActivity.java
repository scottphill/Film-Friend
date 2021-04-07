package com.usf_mobile_dev.filmfriend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import java.util.ArrayList;
import java.util.Arrays;

public class SaveMatchPreferencesActivity extends AppCompatActivity
{
    public static final int REQUEST_CODE_SAVE_MP = 3;

    private SaveMatchPreferencesViewModel viewModel;
    private RecyclerView includedGenresRecyclerView;
    private RecyclerView excludedGenresRecyclerView;
    private RecyclerView watchProvidersRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_match_preferences);

        // Gets the viewmodel for this activity
        viewModel = new ViewModelProvider(this).get(SaveMatchPreferencesViewModel.class);

        // Sets the MatchPreferences and the UI if the intent is not null
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            // Gets the MatchPreferences from the intent and passes it to the viewmodel
            viewModel.setMatchPreferences(
                    (MatchPreferences) intent.getSerializableExtra(
                            SaveMatchPreferencesViewModel.INTENT_EXTRAS_MOVIE_PREFERENCES
                    )
            );

            // Adjusts the UI to display the data from the MatchPreferences
            MatchPreferences mp = viewModel.getMatchPreferences();
            ((EditText)findViewById(R.id.preferences_name_edit_text))
                    .setText(mp.getPreference_title());
            ((TextView)findViewById(R.id.txtViewReleaseYearStart))
                    .setText(String.valueOf(mp.getRelease_year_start()));
            ((TextView)findViewById(R.id.txtViewReleaseYearEnd))
                    .setText(String.valueOf(mp.getRelease_year_end()));
            includedGenresRecyclerView = ((RecyclerView)
                    findViewById(R.id.chosen_genres_recyclerview));
            excludedGenresRecyclerView = ((RecyclerView)
                    findViewById(R.id.rejected_genres_recyclerview));
            ((TextView)findViewById(R.id.txtViewRatingLowerBound))
                    .setText(String.valueOf(mp.getRating_min()));
            ((TextView)findViewById(R.id.txtViewRatingUpperBound))
                    .setText(String.valueOf(mp.getRating_max()));
            watchProvidersRecyclerView = ((RecyclerView)
                    findViewById(R.id.watch_providers_recyclerview));
            ((TextView)findViewById(R.id.txtViewRuntimeLowerBound))
                    .setText(String.valueOf(mp.getRuntime_min()));
            ((TextView)findViewById(R.id.txtViewRuntimeUpperBound))
                    .setText(String.valueOf(mp.getRuntime_max()));
            ((TextView)findViewById(R.id.txtViewVoteCountLowerBound))
                    .setText(String.valueOf(mp.getVote_count_min()));
            ((TextView)findViewById(R.id.txtViewVoteCountUpperBound))
                    .setText(String.valueOf(mp.getVote_count_max()));
            ((TextView)findViewById(R.id.txtViewChosenLanguage))
                    .setText(mp.getSelected_language_name());

            // Sets up the included genres recyclerview
            StringRecyclerViewAdapter includedGenresAdapter;
            if(mp.getIncluded_genres_list().isEmpty())
                includedGenresAdapter = new StringRecyclerViewAdapter(
                        this,
                        new ArrayList<>(Arrays.asList("Any"))
                );
            else
                includedGenresAdapter = new StringRecyclerViewAdapter(
                        this,
                        mp.getIncluded_genres_list()
                );
            includedGenresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            includedGenresRecyclerView.setAdapter(includedGenresAdapter);

            // Sets up the excluded genres recyclerview
            StringRecyclerViewAdapter excludedGenresAdapter;
            if(mp.getExcluded_genres_list().isEmpty())
                excludedGenresAdapter = new StringRecyclerViewAdapter(
                        this,
                        new ArrayList<>(Arrays.asList("None"))
                );
            else
                excludedGenresAdapter = new StringRecyclerViewAdapter(
                        this,
                        mp.getExcluded_genres_list()
                );
            excludedGenresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            excludedGenresRecyclerView.setAdapter(excludedGenresAdapter);

            // Sets up the watch providers recyclerview
            StringRecyclerViewAdapter watchProvidersAdapter;
            if(mp.getWatch_providers_list().isEmpty())
                watchProvidersAdapter = new StringRecyclerViewAdapter(
                        this,
                        new ArrayList<>(Arrays.asList("Any"))
                );
            else
                watchProvidersAdapter = new StringRecyclerViewAdapter(
                        this,
                        mp.getWatch_providers_list()
                );
            watchProvidersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            watchProvidersRecyclerView.setAdapter(watchProvidersAdapter);

            // Adds functionality to the button to save the match preferences
            ((Button)findViewById(R.id.btnSaveMatchPreferences)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { saveMatchPreference(); }
                });
        }
        /*
        Exits the Activity if the intent passed in is null and the Match Preferences
            can't be received. This shouldn't ever really happen, but I put this
            code in just in case.
         */
        else {
            Toast.makeText(
                    this,
                    "Error in receiving Match Preferences. Exiting Activity!",
                    Toast.LENGTH_LONG
            ).show();
            this.finish();  // Exits the activity
        }
    }

    public void saveMatchPreference() {
        // Saves the MatchPreferences to the room database
        viewModel.saveMatchPreferences(
                // Gets the preferences title entered by the user
                ((EditText)findViewById(R.id.preferences_name_edit_text))
                        .getText()
                        .toString()
        );

        // Tells the user that the preference was saved
        Toast.makeText(
                this,
                "Saved the Match Preferences to the database!",
                Toast.LENGTH_SHORT
        ).show();

        // Closes the Activity
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.tutorial_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.tutorial) {
            Tutorial t = new Tutorial();
            t.launchPageTutorial(this, "Save Preference");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
