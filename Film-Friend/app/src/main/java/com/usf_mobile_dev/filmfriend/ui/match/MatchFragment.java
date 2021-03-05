package com.usf_mobile_dev.filmfriend.ui.match;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.usf_mobile_dev.filmfriend.MainActivity;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.savedPreferences.PreferencesActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MatchFragment extends Fragment {

    private MatchViewModel matchViewModel;
    final private String[] genres = {
        "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama",
        "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance",
        "Sci-Fi", "TV Movie", "Thriller", "War", "Western"
    };
    final private String[] watch_providers = {
        "Netflix", "Hulu", "Disney+", "Prime Video", "Google Play"
    };

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

        FloatingActionButton fab = root.findViewById(R.id.match_FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
                startActivity(intent);
                //*/

                Toast.makeText(getActivity(), "FAB pressed!", Toast.LENGTH_SHORT).show();
            }
        });

        EditText release_year_start = root.findViewById(R.id.release_date_start);
        EditText release_year_end = root.findViewById(R.id.release_date_end);

        release_year_start.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) {
                matchViewModel.setReleaseYear(
                        Integer.parseInt(release_year_start.getText().toString()), true);
            }
        });
        release_year_end.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) {
                matchViewModel.setReleaseYear(
                        Integer.parseInt(release_year_end.getText().toString()), false);
            }
        });

        SeekBar seekbar_min = root.findViewById(R.id.seekBar_rating_min);
        SeekBar seekbar_max = root.findViewById(R.id.seekBar_rating_max);

        seekbar_min.setOnSeekBarChangeListener((new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Refreshes too slow
                //Toast.makeText(getActivity(), progress + "", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                matchViewModel.setRating(seekBar.getProgress(), true);
                Toast.makeText(getActivity(), seekBar.getProgress() + "",
                        Toast.LENGTH_SHORT).show();
            }
        }));
        seekbar_max.setOnSeekBarChangeListener((new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Refreshes too slow
                //Toast.makeText(getActivity(), progress + "", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                matchViewModel.setRating(seekBar.getProgress(), false);
                Toast.makeText(getActivity(), seekBar.getProgress() + "",
                        Toast.LENGTH_SHORT).show();
            }
        }));

        CheckBox wp_cb_0 = (CheckBox)root.findViewById(R.id.wp_button_0);
        CheckBox wp_cb_1 = (CheckBox)root.findViewById(R.id.wp_button_1);
        CheckBox wp_cb_2 = (CheckBox)root.findViewById(R.id.wp_button_2);
        CheckBox wp_cb_3 = (CheckBox)root.findViewById(R.id.wp_button_3);
        CheckBox wp_cb_4 = (CheckBox)root.findViewById(R.id.wp_button_4);

        wp_cb_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setWPVal(wp_cb_0.getText().toString(), ((CheckBox) v).isChecked());
            }});
        wp_cb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(wp_cb_1.getText().toString(), ((CheckBox) v).isChecked());
            }});
        wp_cb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(wp_cb_2.getText().toString(), ((CheckBox) v).isChecked());
            }});
        wp_cb_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(wp_cb_3.getText().toString(), ((CheckBox) v).isChecked());
            }});
        wp_cb_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchViewModel.setGenreVal(wp_cb_4.getText().toString(), ((CheckBox) v).isChecked());
            }});

        genre_checkbox_stub(root);

        EditText runtime_min = root.findViewById(R.id.runtime_min);
        EditText runtime_max = root.findViewById(R.id.runtime_max);

        runtime_min.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) {
                matchViewModel.setRuntime(
                        Integer.parseInt(runtime_min.getText().toString()), true);
            }
        });
        runtime_max.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) {
                matchViewModel.setRuntime(
                        Integer.parseInt(runtime_max.getText().toString()), false);
            }
        });

        EditText vote_count_min = root.findViewById(R.id.vote_count_min);
        EditText vote_count_max = root.findViewById(R.id.vote_count_max);

        vote_count_min.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) {
                matchViewModel.setVoteCount(
                        Integer.parseInt(vote_count_min.getText().toString()), true);
            }
        });
        vote_count_max.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) {
                matchViewModel.setVoteCount(
                        Integer.parseInt(vote_count_max.getText().toString()), false);
            }
        });

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences_menu:
                Intent intent = new Intent(getActivity(),
                        PreferencesActivity.class);
                startActivity(intent);
                return true;
            case R.id.qr_code_menu:
                return true;
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }
    
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

}