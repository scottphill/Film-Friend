package com.usf_mobile_dev.filmfriend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;
import com.usf_mobile_dev.filmfriend.ui.savedPreferences.ViewAllSavedPreferencesActivity;

import java.util.ArrayList;
import java.util.Arrays;


public class ViewMatchPreferencesActivity extends AppCompatActivity
{

    private ViewMatchPreferencesViewModel viewModel;
    private RecyclerView includedGenresRecyclerView;
    private RecyclerView excludedGenresRecyclerView;
    private RecyclerView watchProvidersRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_match_preferences);

        // Gets the viewmodel for this activity
        viewModel = new ViewModelProvider(this).get(ViewMatchPreferencesViewModel.class);

        // Sets the MatchPreferences and the UI if the intent is not null
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            // Gets the MatchPreferences from the intent and passes it to the viewmodel
            viewModel.setMatchPreferences(
                    (MatchPreferences) intent.getSerializableExtra(
                            ViewAllSavedPreferencesActivity.INTENT_EXTRAS_MP
                    )
            );

            // Adjusts the UI to display the data from the MatchPreferences
            MatchPreferences mp = viewModel.getMatchPreferences();
            ((TextView)findViewById(R.id.match_preferences_title_txt_view))
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
                    .setText(String.format("%.1f", mp.getRating_min()));
            ((TextView)findViewById(R.id.txtViewRatingUpperBound))
                    .setText(String.format("%.1f", mp.getRating_max()));
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

            // Adds functionality to the button to send the match preferences back
            //   to be displayed in the match fragment
            ((Button)findViewById(R.id.btnSendBack)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            exitActivityWithResult();
                        }
                    });

            // Opens an activity to edit the match preferences
            ((Button)findViewById(R.id.btnEdit)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: Add code to open the modify match preferences
                            //   activity, send the current MatchPreferences, and
                            //   receive a modified MatchPreferences as a result.
                        }
                    }
            );

            ((Button)findViewById(R.id.btnEdit)).setVisibility(View.GONE);
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

    public void exitActivityWithResult() {
        Intent intent = new Intent();
        intent.putExtra(
                ViewAllSavedPreferencesActivity.INTENT_EXTRAS_MP,
                viewModel.getMatchPreferences()
        );
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }
}
