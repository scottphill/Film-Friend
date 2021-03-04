package com.usf_mobile_dev.filmfriend.ui.match;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.usf_mobile_dev.filmfriend.R;

public class MatchFragment extends Fragment {

    private MatchViewModel matchViewModel;
    final private String[] genres =
        {
            "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama",
            "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance",
            "Science Fiction", "TV Movie", "Thriller", "War", "Western"
        };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        matchViewModel =
                new ViewModelProvider(this).get(MatchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_match, container, false);
        final TextView textView = root.findViewById(R.id.text_match_header);
        matchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

    public void onCheckboxClicked(View view)
    {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId())
        {
            case R.id.checkBox_genre_0:
                if (checked)
                {
                    // if checkBox_genre_0
                }
                else
                    break;
            case R.id.checkBox_genre_1:
                if (checked)
                {
                    // if checkBox_genre_1
                }
                else
                    break;
            case R.id.checkBox_genre_2:
                if (checked)
                {
                    // if checkBox_genre_2
                }
                else
                    break;
            case R.id.checkBox_genre_3:
                if (checked)
                {
                    // if checkBox_genre_3
                }
                else
                    break;
            case R.id.checkBox_genre_4:
                if (checked)
                {
                    // if checkBox_genre_4
                }
                else
                    break;
            case R.id.checkBox_genre_5:
                if (checked)
                {
                    // if checkBox_genre_5
                }
                else
                    break;
            case R.id.checkBox_genre_6:
                if (checked)
                {
                    // if checkBox_genre_6
                }
                else
                    break;
            case R.id.checkBox_genre_7:
                if (checked)
                {
                    // if checkBox_genre_7
                }
                else
                    break;
            case R.id.checkBox_genre_8:
                if (checked)
                {
                    // if checkBox_genre_8
                }
                else
                    break;
            case R.id.checkBox_genre_9:
                if (checked)
                {
                    // if checkBox_genre_9
                }
                else
                    break;
            case R.id.checkBox_genre_10:
                if (checked)
                {
                    // if checkBox_genre_10
                }
                else
                    break;
            case R.id.checkBox_genre_11:
                if (checked)
                {
                    // if checkBox_genre_11
                }
                else
                    break;
            case R.id.checkBox_genre_12:
                if (checked)
                {
                    // if checkBox_genre_12
                }
                else
                    break;
            case R.id.checkBox_genre_13:
                if (checked)
                {
                    // if checkBox_genre_13
                }
                else
                    break;
            case R.id.checkBox_genre_14:
                if (checked)
                {
                    // if checkBox_genre_14
                }
                else
                    break;
            case R.id.checkBox_genre_15:
                if (checked)
                {
                    // if checkBox_genre_15
                }
                else
                    break;
            case R.id.checkBox_genre_16:
                if (checked)
                {
                    // if checkBox_genre_16
                }
                else
                    break;
            default:
                // Do nothing.
                break;
        }
    }

}