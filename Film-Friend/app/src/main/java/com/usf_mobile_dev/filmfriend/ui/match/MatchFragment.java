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
    final private int DEF_RATING_MIN = 0;
    final private int DEF_RATING_MAX = 10;
    final private int DEF_RUNTIME_MIN = 0;
    final private int DEF_RUNTIME_MAX = 500;
    final private int DEF_VOTE_COUNT_MIN = 0;
    final private int DEF_VOTE_COUNT_MAX = 10000;

    final private String Intent_QR_CurrentMatchPreference =
            "com.usf_mobile_dev.filmfriend.ui.qr.CurrentMatchPreference";
    final private String Intent_QR_NewMatchPreferencesFromQR =
            "com.usf_mobile_dev.filmfriend.ui.qr.NewMatchPreferencesFromQR";

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
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.match_nav_menu, menu);
        inflater.inflate(R.menu.tutorial_menu, menu);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        matchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
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
                        ((double) seekBar.getProgress() / seekBar.getMax() * DEF_RATING_MAX)));
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
                        ((double) seekBar.getProgress() /  seekBar.getMax()) * DEF_RATING_MAX));
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
            }});
        wp_cb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setWPVal(watch_providers[1],
                        ((CheckBox) v).isChecked());
            }});
        wp_cb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setWPVal(watch_providers[2],
                        ((CheckBox) v).isChecked());
            }});
        wp_cb_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setWPVal(watch_providers[3],
                        ((CheckBox) v).isChecked());
            }});
        wp_cb_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setWPVal(watch_providers[4],
                        ((CheckBox) v).isChecked());
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

    // Unique tag for the intent reply
    public static final int QR_REQUEST = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Test for the right intent reply.
        if (requestCode == QR_REQUEST && resultCode == Activity.RESULT_OK) {

            //Toast.makeText(getActivity(), "MP Has Been Passed!", Toast.LENGTH_SHORT).show();
            assert data != null;
            MatchPreferences MP_from_QR = (MatchPreferences) data
                    .getSerializableExtra(Intent_QR_NewMatchPreferencesFromQR);
            matchViewModel.setMP(MP_from_QR);
            setUI(MP_from_QR);

            try {
                Log.d("Setting Match UI", MPJSONHandling.mpToPrettyJSON(MP_from_QR));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            setUI(MP_from_QR);
        }
    }

    public void setUI(MatchPreferences qrMP) {

        // Release years
        release_year_start.setText(String.valueOf(qrMP.getRelease_year_start()));
        release_year_end.setText(String.valueOf(qrMP.getRelease_year_end()));

        // Genres Included
        for (int i = 0; i < includedGenresGrid.getAdapter().getItemCount(); ++i) {
            CheckBox cb =
                    (CheckBox) includedGenresGrid.findViewHolderForAdapterPosition(i).itemView;
            int id = matchViewModel.getGenreID(String.valueOf(cb.getText()));
            Boolean cb_val = qrMP.getGenres_to_include().get(id);
            cb.setChecked(cb_val);
        }

        // Genres Excluded
        for (int i = 0; i < excludedGenresGrid.getAdapter().getItemCount(); ++i) {
            CheckBox cb =
                    (CheckBox) excludedGenresGrid.findViewHolderForAdapterPosition(i).itemView;
            int id = matchViewModel.getGenreID(String.valueOf(cb.getText()));
            Boolean cb_val = qrMP.getGenres_to_include().get(id);
            cb.setChecked(cb_val);
        }

        // Ratings
        rating_min.setProgress((int)(qrMP.getRating_min() * DEF_RATING_MAX));
        rating_max.setProgress((int)(qrMP.getRating_max() * DEF_RATING_MAX));

        // Watch Providers
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

        // Runtime
        runtime_min.setText(String.valueOf(qrMP.getRuntime_min()));
        runtime_max.setText(String.valueOf(qrMP.getRuntime_max()));

        // Vote Count
        vote_count_min.setText(String.valueOf(qrMP.getVote_count_min()));
        vote_count_max.setText(String.valueOf(qrMP.getVote_count_max()));

        // Language
        for (int i = 0; i < languagesGrid.getAdapter().getItemCount(); ++i) {
            RadioButton rb =
                    (RadioButton) languagesGrid.findViewHolderForAdapterPosition(i).itemView;
            if (String.valueOf(rb.getText()) ==
                    matchViewModel.getLanguageFromID(qrMP.getSelected_language())) {
                rb.setChecked(true);
                break;
            }
        }

        // Update ViewModel
        matchViewModel.setMP(qrMP);

        release_year_start.setText(qrMP.getRelease_year_start()); // ERROR
        release_year_end.setText(qrMP.getRelease_year_end());

        rating_min.setProgress((int)(qrMP.getRating_min() * rating_min.getMax() * DEF_RATING_MAX));
        rating_max.setProgress((int)(qrMP.getRating_max() * rating_max.getMax() * DEF_RATING_MAX));

        wp_cb_0.setChecked(qrMP.getWatch_providers_to_include().get((int)0));
        wp_cb_1.setChecked(qrMP.getWatch_providers_to_include().get((int)1));
        wp_cb_2.setChecked(qrMP.getWatch_providers_to_include().get((int)2));
        wp_cb_3.setChecked(qrMP.getWatch_providers_to_include().get((int)3));
        wp_cb_4.setChecked(qrMP.getWatch_providers_to_include().get((int)4));

        runtime_min.setText(qrMP.getRuntime_min());
        runtime_max.setText(qrMP.getRuntime_max());

        vote_count_min.setText(qrMP.getVote_count_min());
        vote_count_max.setText(qrMP.getVote_count_max());

         //*/
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
                    ((double) rating_min.getProgress()/ rating_min.getMax())*DEF_RATING_MAX,
                    true);
        }
        catch (Exception e) {
            matchViewModel.setRating(DEF_RATING_MIN, true);
        }
        try {
            matchViewModel.setRating(
                    ((double) rating_max.getProgress()/ rating_max.getMax())*DEF_RATING_MAX,
                    false);
        }
        catch (Exception e) {
            matchViewModel.setRating(DEF_RATING_MAX, false);
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
                        languagesGridAdapter.setLanguages(languageResponses);
                    }
                }
        );
        matchViewModel.getSelectedLanguage().observe(
                getViewLifecycleOwner(),
                new Observer<String>() {
                    @Override
                    public void onChanged(String selectedLanguage) {
                        languagesGridAdapter.setSelectedLanguage(selectedLanguage);
                    }
                }
        );
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.preferences_menu:
                Intent intent_pref = new Intent(getActivity(),
                        ViewAllSavedPreferencesActivity.class);
                startActivity(intent_pref);
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

    /*
    private void genre_checkbox_stub(View root)
    {
        CheckBox cb_0 = (CheckBox)root.findViewById(R.id.checkBox_genre_0);
        CheckBox cb_1 = (CheckBox)root.findViewById(R.id.checkBox_genre_1);
        CheckBox cb_2 = (CheckBox)root.findViewById(R.id.checkBox_genre_2);
        CheckBox cb_3 = (CheckBox)root.findViewById(R.id.checkBox_genre_3);
        CheckBox cb_4 = (CheckBox)root.findViewById(R.id.checkBox_genre_4);
        CheckBox cb_5 = (CheckBox)root.findViewById(R.id.checkBox_genre_5);
        CheckBox cb_6 = (CheckBox)root.findViewById(R.id.checkBox_genre_6);
        CheckBox cb_7 = (CheckBox)root.findViewById(R.id.checkBox_genre_7);
        CheckBox cb_8 = (CheckBox)root.findViewById(R.id.checkBox_genre_8);
        CheckBox cb_9 = (CheckBox)root.findViewById(R.id.checkBox_genre_9);
        CheckBox cb_10 = (CheckBox)root.findViewById(R.id.checkBox_genre_10);
        CheckBox cb_11 = (CheckBox)root.findViewById(R.id.checkBox_genre_11);
        CheckBox cb_12 = (CheckBox)root.findViewById(R.id.checkBox_genre_12);
        CheckBox cb_13 = (CheckBox)root.findViewById(R.id.checkBox_genre_13);
        CheckBox cb_14 = (CheckBox)root.findViewById(R.id.checkBox_genre_14);
        CheckBox cb_15 = (CheckBox)root.findViewById(R.id.checkBox_genre_15);
        CheckBox cb_16 = (CheckBox)root.findViewById(R.id.checkBox_genre_16);

        cb_0.setText(genres[0]);
        cb_1.setText(genres[1]);
        cb_2.setText(genres[2]);
        cb_3.setText(genres[3]);
        cb_4.setText(genres[4]);
        cb_5.setText(genres[5]);
        cb_6.setText(genres[6]);
        cb_7.setText(genres[7]);
        cb_8.setText(genres[8]);
        cb_9.setText(genres[9]);
        cb_10.setText(genres[10]);
        cb_11.setText(genres[11]);
        cb_12.setText(genres[12]);
        cb_13.setText(genres[13]);
        cb_14.setText(genres[14]);
        cb_15.setText(genres[15]);
        cb_16.setText(genres[16]);

        cb_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[0], ((CheckBox) v).isChecked());
            }});
        cb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[1], ((CheckBox) v).isChecked());
            }});
        cb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[2], ((CheckBox) v).isChecked());
            }});
        cb_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[3], ((CheckBox) v).isChecked());
            }});
        cb_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[4], ((CheckBox) v).isChecked());
            }});
        cb_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[5], ((CheckBox) v).isChecked());
            }});
        cb_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[6], ((CheckBox) v).isChecked());
            }});
        cb_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[7], ((CheckBox) v).isChecked());
            }});
        cb_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[8], ((CheckBox) v).isChecked());
            }});
        cb_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[9], ((CheckBox) v).isChecked());
            }});
        cb_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[10], ((CheckBox) v).isChecked());
            }});
        cb_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[11], ((CheckBox) v).isChecked());
            }});
        cb_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[12], ((CheckBox) v).isChecked());
            }});
        cb_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[13], ((CheckBox) v).isChecked());
            }});
        cb_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[14], ((CheckBox) v).isChecked());
            }});
        cb_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[15], ((CheckBox) v).isChecked());
            }});
        cb_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(genres[16], ((CheckBox) v).isChecked());
            }});
    }
     */

}