package com.usf_mobile_dev.filmfriend;

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
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;


public class ViewMatchPreferencesActivity extends AppCompatActivity
{

    private ViewMatchPreferencesViewModel viewModel;

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
                            ViewMatchPreferencesViewModel.INTENT_EXTRAS_MOVIE_PREFERENCES
                    )
            );

            // Adjusts the UI to display the data from the MatchPreferences
            MatchPreferences mp = viewModel.getMatchPreferences();
            ((TextView)findViewById(R.id.txtViewReleaseYearStart))
                    .setText(String.valueOf(mp.getRelease_year_start()));
            ((TextView)findViewById(R.id.txtViewReleaseYearEnd))
                    .setText(String.valueOf(mp.getRelease_year_end()));
            // TODO: SETUP Chosen Genres RecyclerView
            // TODO: SETUP Rejected Genres RecyclerView
            ((TextView)findViewById(R.id.txtViewRatingLowerBound))
                    .setText(String.valueOf(mp.getRating_min()));
            ((TextView)findViewById(R.id.txtViewRatingUpperBound))
                    .setText(String.valueOf(mp.getRating_max()));
            // TODO: SETUP Chosen Watch Providers RecyclerView
            ((TextView)findViewById(R.id.txtViewRuntimeLowerBound))
                    .setText(String.valueOf(mp.getRuntime_min()));
            ((TextView)findViewById(R.id.txtViewRuntimeUpperBound))
                    .setText(String.valueOf(mp.getRuntime_max()));
            ((TextView)findViewById(R.id.txtViewVoteCountLowerBound))
                    .setText(String.valueOf(mp.getVote_count_min()));
            ((TextView)findViewById(R.id.txtViewVoteCountUpperBound))
                    .setText(String.valueOf(mp.getVote_count_max()));
            ((TextView)findViewById(R.id.txtViewChosenLanguage))
                    .setText(mp.getSelected_language());

            // Adds functionality to the button to send the match preferences back
            //   to be displayed in the match fragment
            ((Button)findViewById(R.id.btnSendBack)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: Add code to send the current match preferences
                            //   back to be displayed in the match fragment
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
}
