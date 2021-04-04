package com.usf_mobile_dev.filmfriend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.usf_mobile_dev.filmfriend.ui.pop_up_window.PopUp;

public class Tutorial {
    
    public void launchPageTutorial(Context context, String title, MenuItem item)
    {
        PopUp popUp = new PopUp(context);
        Log.d("PageTutorial", String.format("%s", title));

        switch (title) {
            case "Match":
                popUp.setHeading(title);
                popUp.setText("Match!");
                break;
            case "History":
                popUp.setHeading(title);
                popUp.setText("History!");
                break;
            case "Discover":
                popUp.setHeading(title);
                popUp.setText("Discover!");
                break;
            default:
                popUp.setHeading("ERROR");
                popUp.setText("Invalid selection.");
                break;
        }
        popUp.openPopUp();
    }

    @SuppressLint("NonConstantResourceId")
    public void launchMatchFilterTutorial(View view) {

        PopUp popUp = new PopUp(view.getContext());

        switch (view.getId()) {
            case R.id.tut_button_release_date:
                popUp.setHeading("Release Date Range");
                popUp.setText(
                        "Enter in the year range you want your movie to be in.\n" +
                        "Leave blank if you do not wish to have this affect your results.");
                break;
            case R.id.tut_button_included_genres:
                popUp.setHeading("Genres To Include");
                popUp.setText(
                        "Check the genres you'd like to include in your recommendations.\n" +
                        "Leave blank if you do not wish to have this affect your results.\n" +
                        "Works best in tandem with \"excluded genres\" filter.");
                break;
            case R.id.tut_button_excluded_genres:
                popUp.setHeading("Genres To Exclude");
                popUp.setText(
                        "Check the genres you do NOT want to be included in your recommendations.\n" +
                        "Leave blank if you do not wish to have this affect your results.\n" +
                        "Works best in tandem with \"included genres\" filter.");
                break;
            case R.id.tut_button_rating:
                popUp.setHeading("Rating Range");
                popUp.setText(
                        "Using the two sliders, set the range for the rating of your movie.\n" +
                        "Min sets the minimum rating of the movie.\n" +
                        "Max sets the maximum rating of the movie.");
                break;
            case R.id.tut_button_watch_providers:
                popUp.setHeading("Watch Providers");
                popUp.setText(
                        "Check the watch providers you'd like to have included in your search.\n" +
                        "Leave all boxes unchecked if you do not wish to have this affect your results.");
                break;
            case R.id.tut_button_runtime:
                popUp.setHeading("Runtime");
                popUp.setText(
                        "Enter in the length of time range, in minutes, you want your movie to be in.\n" +
                        "Leave blank if you do not wish to have this affect your results.\n" +
                        "Works best in tandem with \"vote count\" filter.");
                break;
            case R.id.tut_button_vote_count:
                popUp.setHeading("Vote Count");
                popUp.setText(
                        "Enter in the length of time range, in minutes, you want your movie to be in.\n" +
                        "Leave blank if you do not wish to have this affect your results.\n" +
                        "Works best in tandem with \"runtime\" filter.");
                break;
            case R.id.tut_button_language:
                popUp.setHeading("Language");
                popUp.setText(
                        "Select what language you would like your movie in.\n" +
                        "Leave option unselected if you do not wish to have this affect your results.");
                break;
            default:
                popUp.setHeading("Error");
                popUp.setText("Text not set yet.");
                break;
        }
        popUp.openPopUp();
    }
}
