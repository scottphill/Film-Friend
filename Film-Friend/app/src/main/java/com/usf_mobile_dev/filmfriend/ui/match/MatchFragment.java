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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.usf_mobile_dev.filmfriend.R;

public class MatchFragment extends Fragment {

    private MatchViewModel matchViewModel;
    final private String[] genres =
        {
            "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama",
            "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance",
            "Sci-Fi", "TV Movie", "Thriller", "War", "Western"
        };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        matchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_match, container, false);

        Toolbar toolbar = root.findViewById(R.id.match_toolbar);

        FloatingActionButton fab = root.findViewById(R.id.match_FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
                startActivity(intent);
                //*/
            }
        });

        checkbox_stub(root);

        return root;
    }
    
    private void checkbox_stub(View root)
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