package com.usf_mobile_dev.filmfriend.ui.match;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.usf_mobile_dev.filmfriend.GenresGridAdapter;
import com.usf_mobile_dev.filmfriend.LanguagesGridAdapter;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.api.GenreResponse;
import com.usf_mobile_dev.filmfriend.api.LanguageResponse;
import com.usf_mobile_dev.filmfriend.ui.qr.QrActivity;
import com.usf_mobile_dev.filmfriend.ui.savedPreferences.PreferencesActivity;

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

    // Made these private variables so they can be put into an intent for QR
    private EditText release_year_start;
    private EditText release_year_end;
    private SeekBar seekbar_min;
    private SeekBar seekbar_max;
    private CheckBox wp_cb_0;
    private CheckBox wp_cb_1;
    private CheckBox wp_cb_2;
    private CheckBox wp_cb_3;
    private CheckBox wp_cb_4;
    private EditText runtime_min;
    private EditText runtime_max;
    private EditText vote_count_min;
    private EditText vote_count_max;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.match_nav_menu, menu);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        matchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_match, container, false);

        release_year_start = root.findViewById(R.id.release_date_start);
        release_year_end = root.findViewById(R.id.release_date_end);

        seekbar_min = root.findViewById(R.id.seekBar_rating_min);
        seekbar_max = root.findViewById(R.id.seekBar_rating_max);

        // Ensures that min is always less than max.
        seekbar_min.setOnSeekBarChangeListener((new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int max = seekBar.getMax();

                if (progress >= max) {
                    seekbar_min.setProgress(max-1);
                }
                if (seekbar_max.getProgress() <= progress) {
                    seekbar_max.setProgress(progress + 1);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(
                        getActivity(),
                        String.format("%.1f",
                                ((double) seekBar.getProgress() / seekBar.getMax() * DEF_RATING_MAX)),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }));

        // Ensures that max is always greater than min.
        seekbar_max.setOnSeekBarChangeListener((new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int min = seekBar.getMin();

                if (progress <= min) {
                    seekbar_max.setProgress(min + 1);
                }
                if (seekbar_min.getProgress() >= progress) {
                    seekbar_min.setProgress(progress - 1);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(
                        getActivity(),
                        String.format("%.1f",
                                ((double) seekBar.getProgress() /  seekBar.getMax()) * DEF_RATING_MAX),
                        Toast.LENGTH_SHORT
                ).show();
            }
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

        MatchPreferences qrMP = (MatchPreferences) getActivity().getIntent().getSerializableExtra(
                "NewMatchPreferencesFromQR");
        if (qrMP != null) {
            matchViewModel.setMP(qrMP);
            Toast.makeText(getActivity(), "MP Has Been Passed!", Toast.LENGTH_SHORT).show();
            setUI();
        }

        return root;
    }

    public void setUI() {

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
                    ((double)seekbar_min.getProgress()/seekbar_min.getMax())*DEF_RATING_MAX,
                    true);
        }
        catch (Exception e) {
            matchViewModel.setRating(DEF_RATING_MIN, true);
        }
        try {
            matchViewModel.setRating(
                    ((double)seekbar_max.getProgress()/seekbar_max.getMax())*DEF_RATING_MAX,
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences_menu:
                Intent intent_pref = new Intent(getActivity(),
                        PreferencesActivity.class);
                startActivity(intent_pref);
                return true;
            case R.id.qr_code_menu:
                Intent intent_qr = new Intent(getActivity(), QrActivity.class);
                // Pass MoviePreferences object to intent
                setMoviePreferences();
                intent_qr.putExtra("CurrentMatchPreference", matchViewModel.getMP());
                startActivity(intent_qr);
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