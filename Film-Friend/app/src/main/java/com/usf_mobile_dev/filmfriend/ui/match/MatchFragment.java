package com.usf_mobile_dev.filmfriend.ui.match;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.usf_mobile_dev.filmfriend.GenresGridAdapter;
import com.usf_mobile_dev.filmfriend.LanguagesGridAdapter;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.SaveMatchPreferencesActivity;
import com.usf_mobile_dev.filmfriend.SaveMatchPreferencesViewModel;
import com.usf_mobile_dev.filmfriend.Tutorial;
import com.usf_mobile_dev.filmfriend.api.GenreResponse;
import com.usf_mobile_dev.filmfriend.api.LanguageResponse;
import com.usf_mobile_dev.filmfriend.ui.qr.MPJSONHandling;
import com.usf_mobile_dev.filmfriend.ui.qr.QRCameraActivity;
import com.usf_mobile_dev.filmfriend.ui.qr.QRGenerateActivity;
import com.usf_mobile_dev.filmfriend.ui.savedPreferences.ViewAllSavedPreferencesActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MatchFragment extends Fragment {

    private MatchViewModel matchViewModel;
    private RecyclerView includedGenresGrid;
    private RecyclerView excludedGenresGrid;
    private RecyclerView languagesGrid;

    final private String[] genres = {
        "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama",
        "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance",
        "Sci-Fi", "TV Movie", "Thriller", "War", "Western"
    };
    final private String[] watch_providers = {
        "Netflix", "Hulu", "Disney+", "Amazon Prime", "Google Play"
    };
    final private int DEF_RELEASE_YEAR_MIN = 1850;
    final private int DEF_RELEASE_YEAR_MAX = 2021;
    final private int DEF_LOWEST_RATING = 0;
    final private int DEF_HIGHEST_RATING = 10;
    final private int DEF_RUNTIME_MIN = 0;
    final private int DEF_RUNTIME_MAX = 500;
    final private int DEF_VOTE_COUNT_MIN = 0;
    final private int DEF_VOTE_COUNT_MAX = 10000000;

    final private String Intent_QR_CurrentMatchPreference =
            "com.usf_mobile_dev.filmfriend.ui.qr.CurrentMatchPreference";
    final private String Intent_QR_NewMatchPreferencesFromQR =
            "com.usf_mobile_dev.filmfriend.ui.qr.NewMatchPreferencesFromQR";
  
    // Unique tag for the intent reply
    public static final int QR_REQUEST = 1;
    public static final int MP_REQUEST = 2;
  
    // Made these private variables so they can be put into an intent for QR
    private EditText release_year_start;
    private EditText release_year_end;
    private SeekBar rating_min;
    private SeekBar rating_max;
    private CheckBox wp_cb_0;
    private CheckBox wp_cb_1;
    private CheckBox wp_cb_2;
    private CheckBox wp_cb_3;
    private CheckBox wp_cb_4;
    private EditText runtime_min;
    private EditText runtime_max;
    private EditText vote_count_min;
    private EditText vote_count_max;
    private TextView label_rating_min;
    private TextView label_rating_max;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        matchViewModel = new ViewModelProvider(getActivity()).get(MatchViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.match_nav_menu, menu);
        inflater.inflate(R.menu.tutorial_menu, menu);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_match, container, false);

        release_year_start = root.findViewById(R.id.release_date_start);
        release_year_end = root.findViewById(R.id.release_date_end);

        rating_min = root.findViewById(R.id.seekBar_rating_min);
        rating_max = root.findViewById(R.id.seekBar_rating_max);

        label_rating_min = root.findViewById(R.id.label_rating_min);
        // Ensures that min is always less than max.
        rating_min.setOnSeekBarChangeListener((new SeekBar.OnSeekBarChangeListener() {

            @SuppressLint("DefaultLocale")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int max = seekBar.getMax();

                if (progress >= max) {
                    rating_min.setProgress(max-1);
                }
                if (rating_max.getProgress() <= progress) {
                    rating_max.setProgress(progress + 1);
                }

                // Updates the label for rating_min so the user can see where the bar is at.
                label_rating_min.setText(String.format("%.1f",
                        ((double) seekBar.getProgress() / seekBar.getMax() * DEF_HIGHEST_RATING)));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)  { }

        }));

        label_rating_max = root.findViewById(R.id.label_rating_max);
        // Ensures that max is always greater than min.
        rating_max.setOnSeekBarChangeListener((new SeekBar.OnSeekBarChangeListener() {

            @SuppressLint("DefaultLocale")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int min = seekBar.getMin();

                if (progress <= min) {
                    rating_max.setProgress(min + 1);
                }
                if (rating_min.getProgress() >= progress) {
                    rating_min.setProgress(progress - 1);
                }

                // Updates the label for rating_max so the user can see where the bar is at.
                label_rating_max.setText(String.format("%.1f",
                        ((double) seekBar.getProgress() /  seekBar.getMax()) * DEF_HIGHEST_RATING));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
          
        }));

        wp_cb_0 = (CheckBox)root.findViewById(R.id.wp_button_0);
        wp_cb_1 = (CheckBox)root.findViewById(R.id.wp_button_1);
        wp_cb_2 = (CheckBox)root.findViewById(R.id.wp_button_2);
        wp_cb_3 = (CheckBox)root.findViewById(R.id.wp_button_3);
        wp_cb_4 = (CheckBox)root.findViewById(R.id.wp_button_4);

        wp_cb_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setWPVal(watch_providers[0],
                        ((CheckBox) v).isChecked());
                String watchProvider = watch_providers[0];
                boolean isChecked = ((CheckBox) v).isChecked();
                if(isChecked)
                    matchViewModel.getMP().addWatchProviderToList(watchProvider);
                else
                    matchViewModel.getMP().removeWatchProviderFromList(watchProvider);
            }});
        wp_cb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setWPVal(watch_providers[1],
                        ((CheckBox) v).isChecked());
                String watchProvider = watch_providers[1];
                boolean isChecked = ((CheckBox) v).isChecked();
                if(isChecked)
                    matchViewModel.getMP().addWatchProviderToList(watchProvider);
                else
                    matchViewModel.getMP().removeWatchProviderFromList(watchProvider);
            }});
        wp_cb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setWPVal(watch_providers[2],
                        ((CheckBox) v).isChecked());
                String watchProvider = watch_providers[2];
                boolean isChecked = ((CheckBox) v).isChecked();
                if(isChecked)
                    matchViewModel.getMP().addWatchProviderToList(watchProvider);
                else
                    matchViewModel.getMP().removeWatchProviderFromList(watchProvider);
            }});
        wp_cb_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                matchViewModel.setWPVal(watch_providers[3],
                        ((CheckBox) v).isChecked());
                String watchProvider = watch_providers[3];
                boolean isChecked = ((CheckBox) v).isChecked();
                if(isChecked)
                    matchViewModel.getMP().addWatchProviderToList(watchProvider);
                else
                    matchViewModel.getMP().removeWatchProviderFromList(watchProvider);
            }});
        wp_cb_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setWPVal(watch_providers[4],
                        ((CheckBox) v).isChecked());
                String watchProvider = watch_providers[4];
                boolean isChecked = ((CheckBox) v).isChecked();
                if(isChecked)
                    matchViewModel.getMP().addWatchProviderToList(watchProvider);
                else
                    matchViewModel.getMP().removeWatchProviderFromList(watchProvider);
            }});

        runtime_min = root.findViewById(R.id.runtime_min);
        runtime_max = root.findViewById(R.id.runtime_max);

        vote_count_min = root.findViewById(R.id.vote_count_min);
        vote_count_max = root.findViewById(R.id.vote_count_max);

        FloatingActionButton fab = root.findViewById(R.id.match_FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Update info to MoviePreferences object
                setMoviePreferences();

                matchViewModel.getTMDBMovie(getActivity());
            }
        });

        FloatingActionButton qr_code_fab = root.findViewById(R.id.match_qr_FAB);
        qr_code_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMoviePreferences();

                Toast.makeText(
                        view.getContext(),
                        "Generating Code...",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(view.getContext(),
                        QRGenerateActivity.class);

                intent.putExtra(
                        Intent_QR_CurrentMatchPreference,
                        matchViewModel.getMP());

                startActivity(intent);
            }
        });

        ((ImageButton)root.findViewById(R.id.match_save_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setMoviePreferences();

                        Intent savePreferencesIntent = new Intent(
                                getActivity(),
                                SaveMatchPreferencesActivity.class
                        );
                        savePreferencesIntent.putExtra(
                                SaveMatchPreferencesViewModel.INTENT_EXTRAS_MOVIE_PREFERENCES,
                                matchViewModel.getMP());
                        startActivity(savePreferencesIntent);
                    }
                });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Test for the right intent reply.
        if (requestCode == QR_REQUEST && resultCode == Activity.RESULT_OK) {

            //Toast.makeText(getActivity(), "MP Has Been Passed!", Toast.LENGTH_SHORT).show();
            MatchPreferences MPfromQR = (MatchPreferences) data.getSerializableExtra(
                    QRCameraActivity.INTENT_EXTRAS_QR_MP);
            matchViewModel.setMP(MPfromQR);
            setUI(MPfromQR);

            try {
                Log.d("Setting Match UI", MPJSONHandling.mpToPrettyJSON(MPfromQR));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            setUI(MPfromQR);
        }
        else if (requestCode == MP_REQUEST && resultCode == Activity.RESULT_OK) {
            //*
            //Toast.makeText(getActivity(), "MP Has Been Passed!", Toast.LENGTH_SHORT).show();
            MatchPreferences MPfromQR = (MatchPreferences) data.getSerializableExtra(
                    ViewAllSavedPreferencesActivity.INTENT_EXTRAS_MP);
            matchViewModel.setMP(MPfromQR);
            //Need to set the map within the viewModel
            /*for(int i = 0; i < MPfromQR.getNumSelectedExcludedGenres(); i++)
            {
                matchViewModel.setExcludedGenreVal(matchViewModel.getGenreID(MPfromQR.getExcluded_genres_list().get(i)), true);
            }*/
            setUI(MPfromQR);

            try {
                Log.d("Setting Match UI", MPJSONHandling.mpToPrettyJSON(MPfromQR));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            if (MPfromQR != null) {
                setUI(MPfromQR);
            }
            //*/
        }
    }

    public void setUI(MatchPreferences qrMP) {
        // Sets the release years
        release_year_start.setText(String.valueOf(qrMP.getRelease_year_start()));
        release_year_end.setText(String.valueOf(qrMP.getRelease_year_end()));

        // Sets the Included Genres
        for (int i = 0; i < includedGenresGrid.getAdapter().getItemCount(); ++i) {
            CheckBox cb =
                    (CheckBox) includedGenresGrid.findViewHolderForAdapterPosition(i).itemView;
            int id = matchViewModel.getGenreID(String.valueOf(cb.getText()));
            Boolean cb_val = qrMP.getGenres_to_include().get(id);
            cb.setChecked(cb_val);
        }

        // Sets the Excluded Genres
        for (int i = 0; i < excludedGenresGrid.getAdapter().getItemCount(); ++i) {
            CheckBox cb =
                    (CheckBox) excludedGenresGrid.findViewHolderForAdapterPosition(i).itemView;
            int id = matchViewModel.getGenreID(String.valueOf(cb.getText()));
            Boolean cb_val = qrMP.getGenres_to_exclude().get(id);
            cb.setChecked(cb_val);
        }

        // Sets the rating bars
        rating_min.setProgress((int)
                ((qrMP.getRating_min() / DEF_HIGHEST_RATING) * rating_min.getMax())
        );
        rating_max.setProgress((int)
                ((qrMP.getRating_max() / DEF_HIGHEST_RATING) * rating_max.getMax())
        );

        // Sets the Watch Providers
        boolean[] wp_vals = { false, false, false, false, false };
        try {
            HashMap<Integer, Boolean> wp = qrMP.getWatch_providers_to_include();
            for (int i = 0; i < watch_providers.length; ++i) {
                wp_vals[i] = wp.get(matchViewModel.getWPID(watch_providers[i]));
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            Arrays.fill(wp_vals, false);
        }
        wp_cb_0.setChecked(wp_vals[0]);
        wp_cb_1.setChecked(wp_vals[1]);
        wp_cb_2.setChecked(wp_vals[2]);
        wp_cb_3.setChecked(wp_vals[3]);
        wp_cb_4.setChecked(wp_vals[4]);

        // Sets the runtime
        runtime_min.setText(String.valueOf(qrMP.getRuntime_min()));
        runtime_max.setText(String.valueOf(qrMP.getRuntime_max()));

        // Sets the Vote Count
        vote_count_min.setText(String.valueOf(qrMP.getVote_count_min()));
        vote_count_max.setText(String.valueOf(qrMP.getVote_count_max()));

        // Sets the Language
        boolean languageFound = false;
        for (int i = 0; i < languagesGrid.getAdapter().getItemCount(); ++i) {
            RadioButton rb =
                    (RadioButton) languagesGrid.findViewHolderForAdapterPosition(i).itemView;
            if (String.valueOf(rb.getText()).equals(
                    matchViewModel.getLanguageFromID(qrMP.getSelected_language_code()))
            ) {
                rb.setChecked(true);
                languageFound = true;
                break;
            }
        }
        if(!languageFound){
            matchViewModel.setSelectedLanguage("English");
        }
    }

    public void setMoviePreferences() {
        // Tries to catch variables that might be blank
        try {
            matchViewModel.setReleaseYear(
                    Integer.parseInt(release_year_start.getText().toString()), true);
        }
        catch (Exception e) {
            matchViewModel.setReleaseYear(DEF_RELEASE_YEAR_MIN, true);
        }
        try {
            matchViewModel.setReleaseYear(
                    Integer.parseInt(release_year_end.getText().toString()), false);
        }
        catch (Exception e) {
            matchViewModel.setReleaseYear(DEF_RELEASE_YEAR_MAX, false);
        }
        try {
            matchViewModel.setRating(
                    ((double) rating_min.getProgress()/ rating_min.getMax())* DEF_HIGHEST_RATING,
                    true);
        }
        catch (Exception e) {
            matchViewModel.setRating(DEF_LOWEST_RATING, true);
        }
        try {
            matchViewModel.setRating(
                    ((double) rating_max.getProgress()/ rating_max.getMax())* DEF_HIGHEST_RATING,
                    false);
        }
        catch (Exception e) {
            matchViewModel.setRating(DEF_HIGHEST_RATING, false);
        }
        try {
            matchViewModel.setRuntime(
                    Integer.parseInt(runtime_min.getText().toString()), true);
        }
        catch (Exception e) {
            matchViewModel.setRuntime(DEF_RUNTIME_MIN, true);
        }
        try {
            matchViewModel.setRuntime(
                    Integer.parseInt(runtime_max.getText().toString()), false);
        }
        catch (Exception e) {
            matchViewModel.setRuntime(DEF_RUNTIME_MAX, false);
        }
        try {
            matchViewModel.setVoteCount(
                    Integer.parseInt(vote_count_min.getText().toString()), true);
        }
        catch (Exception e) {
            matchViewModel.setVoteCount(DEF_VOTE_COUNT_MIN, true);
        }
        try {
            matchViewModel.setVoteCount(
                    Integer.parseInt(vote_count_max.getText().toString()), false);
        }
        catch (Exception e) {
            matchViewModel.setVoteCount(DEF_VOTE_COUNT_MAX, false);
        }
        /*
        try {
            matchViewModel.setWPVal(watch_providers[0], wp_cb_0.isChecked());
        }
        catch (Exception e) {
            matchViewModel.setWPVal(watch_providers[0], false);
        }
        try {
            matchViewModel.setWPVal(watch_providers[1], wp_cb_1.isChecked());
        }
        catch (Exception e) {
            matchViewModel.setWPVal(watch_providers[1], false);
        }
        try {
            matchViewModel.setWPVal(watch_providers[2], wp_cb_2.isChecked());
        }
        catch (Exception e) {
            matchViewModel.setWPVal(watch_providers[2], false);
        }
        try {
            matchViewModel.setWPVal(watch_providers[3], wp_cb_3.isChecked());
        }
        catch (Exception e) {
            matchViewModel.setWPVal(watch_providers[3], false);
        }
        try {
            matchViewModel.setWPVal(watch_providers[4], wp_cb_4.isChecked());
        }
        catch (Exception e) {
            matchViewModel.setWPVal(watch_providers[4], false);
        }
         */
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sets up the GridView for the included genres checkboxes
        includedGenresGrid = (RecyclerView)view.findViewById(R.id.included_genres_grid);
        GenresGridAdapter genresIncludedGridAdapter = new GenresGridAdapter(
                new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {
                        String genre = buttonView.getText().toString();
                        matchViewModel.setIncludedGenreVal(genre, isChecked);
                        if(isChecked)
                            matchViewModel.getMP().addIncludedGenreToList(genre);
                        else
                            matchViewModel.getMP().removeIncludedGenreFromList(genre);
                    }
                });
        includedGenresGrid.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        includedGenresGrid.setAdapter(genresIncludedGridAdapter);

        // Sets up the GridView for the excluded genres checkboxes
        excludedGenresGrid = (RecyclerView)view.findViewById(R.id.excluded_genres_grid);
        GenresGridAdapter genresExcludedGridAdapter = new GenresGridAdapter(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {
                        String genre = buttonView.getText().toString();
                        matchViewModel.setExcludedGenreVal(genre, isChecked);
                        if(isChecked)
                            matchViewModel.getMP().addExcludedGenreToList(genre);
                        else
                            matchViewModel.getMP().removeExcludedGenreFromList(genre);
                    }
                }
        );
        excludedGenresGrid.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        excludedGenresGrid.setAdapter(genresExcludedGridAdapter);

        // Observes the LiveData for when the genres change in the ViewModel
        matchViewModel.getGenres().observe(
                getViewLifecycleOwner(),
                new Observer<List<GenreResponse.Genre>>() {
                    @Override
                    public void onChanged(List<GenreResponse.Genre> genres) {
                        genresIncludedGridAdapter.setGenres(genres);
                        genresExcludedGridAdapter.setGenres(genres);
                    }
                });

        // Sets up the GridView for the languages radiobuttons grid
        languagesGrid = (RecyclerView)view.findViewById(R.id.languages_recyclerview);
        LanguagesGridAdapter languagesGridAdapter = new LanguagesGridAdapter(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String language = buttonView.getText().toString();
                        Log.d("LANGUAGE","in OnCheckedChanged " + language);
                        if(isChecked && !matchViewModel.getSelectedLanguage()
                                .getValue()
                                .equals(language)) {
                            matchViewModel.setSelectedLanguage(language);
                        }
                    }
                }
        );
        languagesGrid.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        languagesGrid.setAdapter(languagesGridAdapter);
        matchViewModel.getLanguages().observe(
                getViewLifecycleOwner(),
                new Observer<List<LanguageResponse>>() {
                    @Override
                    public void onChanged(List<LanguageResponse> languageResponses) {
                        //Log.d("LANGUAGE","in OnChanged for languages");
                        languagesGridAdapter.setLanguages(languageResponses);
                    }
                }
        );
        matchViewModel.getSelectedLanguage().observe(
                getViewLifecycleOwner(),
                new Observer<String>() {
                    @Override
                    public void onChanged(String selectedLanguage) {
                        Log.d("LANGUAGE","in OnChanged: " + selectedLanguage);
                        languagesGridAdapter.setSelectedLanguage(selectedLanguage);
                    }
                }
        );

        // Sets the UI to its default state
        matchViewModel.getMP().resetMatchPreference();
        setUI(matchViewModel.getMP());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.preferences_menu:
                Intent intent_pref = new Intent(getActivity(),
                        ViewAllSavedPreferencesActivity.class);
                startActivityForResult(intent_pref, MP_REQUEST);
                return true;
            case R.id.qr_code_menu:
                Toast.makeText(
                        this.getContext(),
                        "Opening Camera...",
                        Toast.LENGTH_SHORT
                ).show();
                Intent intent = new Intent(getActivity(), QRCameraActivity.class);
                startActivityForResult(intent, QR_REQUEST);
                return true;
            case R.id.tutorial:
                Tutorial t = new Tutorial();
                t.launchPageTutorial(this.getContext(), "Match");
                return true;
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }
}