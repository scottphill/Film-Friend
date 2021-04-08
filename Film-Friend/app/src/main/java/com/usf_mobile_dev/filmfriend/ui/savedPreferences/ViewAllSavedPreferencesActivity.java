package com.usf_mobile_dev.filmfriend.ui.savedPreferences;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.usf_mobile_dev.filmfriend.PreferenceRecyclerViewAdapter;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.Tutorial;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;
import com.usf_mobile_dev.filmfriend.ui.settings.Settings;
import com.usf_mobile_dev.filmfriend.ViewMatchPreferencesActivity;
import com.usf_mobile_dev.filmfriend.ViewMatchPreferencesViewModel;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;
import com.usf_mobile_dev.filmfriend.ui.qr.MPJSONHandling;
import com.usf_mobile_dev.filmfriend.ui.qr.QRGenerateActivity;
import java.util.List;


public class ViewAllSavedPreferencesActivity extends AppCompatActivity
        implements DeletePreferencesDialogFragment.DeletePreferencesDialogListener {

    public static final int VIEW_MP_REQUEST = 45;
    public static final String INTENT_EXTRAS_MP = "com.usf_mobile_dev.filmfriend.ui.savedPreferences.ViewAllSavedPreferencesActivity.intent_extras_mp";

    private RecyclerView mRecyclerView;
    private PreferenceRecyclerViewAdapter mAdapter;
    private ViewAllSavedPreferencesViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Gets the viewmodel for this activity
        viewModel = new ViewModelProvider(this).get(ViewAllSavedPreferencesViewModel.class);

        // Create recycler view.
        mRecyclerView = findViewById(R.id.preferences_recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PreferenceRecyclerViewAdapter(
                this, 
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startViewMPActivity((MatchPreferences) v.getTag());
                    }
                },
                new View.OnClickListener() {
                    // Gets the match preferences from the view and deletes the
                    //   match preferences from the room database.
                    @Override
                    public void onClick(View v) {
                        launchDeleteDialog((MatchPreferences) v.getTag());
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

        if (item.getItemId() == android.R.id.home) {
                this.finish();
                return true;
        }

        if (item.getItemId() == R.id.tutorial) {
            Tutorial t = new Tutorial();
            t.launchPageTutorial(this, "Preferences List");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startViewMPActivity(MatchPreferences matchPreferences) {
        Intent intent_view_mp = new Intent(
                this,
                ViewMatchPreferencesActivity.class
        );
        // Pass MoviePreferences object to intent
        intent_view_mp.putExtra(
                INTENT_EXTRAS_MP,
                matchPreferences
        );
        ((AppCompatActivity)this).startActivityForResult(intent_view_mp,
                VIEW_MP_REQUEST);
    }

    public void launchDeleteDialog(MatchPreferences preferences) {
        DeletePreferencesDialogFragment deleteDialog = new DeletePreferencesDialogFragment();
        deleteDialog.setPreferences(preferences);
        deleteDialog.show(
                getSupportFragmentManager(),
                "DeletePreferencesDialogFragment"
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Test for the right intent reply.
        if (requestCode == VIEW_MP_REQUEST && resultCode == Activity.RESULT_OK) {

            Toast.makeText(this, "Preference Imported!", Toast.LENGTH_SHORT).show();
            MatchPreferences mp = (MatchPreferences) data.getSerializableExtra(
                    INTENT_EXTRAS_MP);

            Intent intent = new Intent();
            intent.putExtra(INTENT_EXTRAS_MP, mp);

            setResult(Activity.RESULT_OK, intent);
            this.finish();
        }
    }

    @Override
    public void onDialogPositiveClick(DeletePreferencesDialogFragment dialog) {
        viewModel.deleteMatchPreferences(dialog.getPreferences());

        Toast.makeText(
                getApplication(),
                "Match Preferences Deleted",
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onDialogNegativeClick(DeletePreferencesDialogFragment dialog) {

    }
}
