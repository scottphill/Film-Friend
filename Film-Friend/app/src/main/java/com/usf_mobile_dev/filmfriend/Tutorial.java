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
    
    public void launchPageTutorial(Context context, String title)
    {
        PopUp popUp = new PopUp(context);
        Log.d("PageTutorial", String.format("%s", title));

        switch (title) {
            case "Match":
                popUp.setLayout( .7, .8);
                popUp.setHeading(title);
                popUp.setText("This page offers will match \n" +
                        "There are a number major buttons:\n\n" +
                        "Camera Icon (Top Right): This opens your phones camera for scanning QR codes\n\n" +
                        "List Icon (Next to Camera): This takes you to your saved preferences.\n" +
                        "More info on preferences page.\n\n" +
                        "Save Icon (Bottom Right): This button allows you to save your current filters\n" +
                        "as a Preference and is added to your Preference List.\n\n" +
                        "QR button (Below Save Icon): This will generate a QR code based on your current " +
                        "filters so that anyone can scan it. More info on the QR page.\n\n" +
                        "Search Button (Below QR): This will find you the perfect movie based on your\n" +
                        "filters!\n");
                break;
            case "History":
                popUp.setLayout( .7, .8);
                popUp.setHeading(title);
                popUp.setText("Each time you view a movie, you can see that movie here.\n" +
                        "By changing the filter, you can show all of the movies, or just" +
                        "the movies in your Watch List.\n");
                break;
            case "Discover":
                popUp.setLayout( .7, .8);
                popUp.setHeading(title);
                popUp.setText("Movies that other users have added to their Watch List appear here.\n" +
                        "You can increase the search range by changing the drop down at the top of the page.\n");
                break;
            case "Preferences List":
                popUp.setLayout( .4, .8);
                popUp.setHeading(title);
                popUp.setText("Here is your list of saved preferences! You can import them to your Match page, " +
                        "generate a code to share with your friends, or delete a preference.");
                break;
            case "Save Preference":
                popUp.setLayout( .4, .8);
                popUp.setHeading(title);
                popUp.setText("Our app allows you to save and reload filters you've entered! " +
                        "Give it a name and save it to your device. You can import preferences to the Match " +
                        "page from the Preference List page.");
                break;
            case "QR Code Generation":
                popUp.setLayout( .4, .8);
                popUp.setHeading(title);
                popUp.setText("Let your friends scan your code to import your preferences to their page!");
                break;
            case "QR Camera":
                popUp.setLayout( .4, .8);
                popUp.setHeading(title);
                popUp.setText("Use this camera to scan your friend's codes to import their preferences!");
                break;
            default:
                popUp.setLayout( .4, .8);
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
                popUp.setLayout( .4, .8);
                popUp.setHeading("Release Date Range");
                popUp.setText(
                        "Enter in the year range you want your movie to be in.\n" +
                        "Leave blank if you do not wish to have this affect your results.");
                break;
            case R.id.tut_button_included_genres:
                popUp.setLayout( .4, .8);
                popUp.setHeading("Genres To Include");
                popUp.setText(
                        "Check the genres you'd like to include in your recommendations.\n" +
                        "Leave blank if you do not wish to have this affect your results.\n" +
                        "Works best in tandem with \"excluded genres\" filter.");
                break;
            case R.id.tut_button_excluded_genres:
                popUp.setLayout( .4, .8);
                popUp.setHeading("Genres To Exclude");
                popUp.setText(
                        "Check the genres you do NOT want to be included in your recommendations.\n" +
                        "Leave blank if you do not wish to have this affect your results.\n" +
                        "Works best in tandem with \"included genres\" filter.");
                break;
            case R.id.tut_button_rating:
                popUp.setLayout( .4, .8);
                popUp.setHeading("Rating Range");
                popUp.setText(
                        "Using the two sliders, set the range for the rating of your movie.\n" +
                        "Min sets the minimum rating of the movie.\n" +
                        "Max sets the maximum rating of the movie.");
                break;
            case R.id.tut_button_watch_providers:
                popUp.setLayout( .4, .8);
                popUp.setHeading("Watch Providers");
                popUp.setText(
                        "Check the watch providers you'd like to have included in your search.\n" +
                        "Leave all boxes unchecked if you do not wish to have this affect your results.");
                break;
            case R.id.tut_button_runtime:
                popUp.setLayout( .4, .8);
                popUp.setHeading("Runtime");
                popUp.setText(
                        "Enter in the length of time range, in minutes, you want your movie to be in.\n" +
                        "Leave blank if you do not wish to have this affect your results.\n" +
                        "Works best in tandem with \"vote count\" filter.");
                break;
            case R.id.tut_button_vote_count:
                popUp.setLayout( .4, .8);
                popUp.setHeading("Vote Count");
                popUp.setText(
                        "Enter in the length of time range, in minutes, you want your movie to be in.\n" +
                        "Leave blank if you do not wish to have this affect your results.\n" +
                        "Works best in tandem with \"runtime\" filter.");
                break;
            case R.id.tut_button_language:
                popUp.setLayout( .4, .8);
                popUp.setHeading("Language");
                popUp.setText(
                        "Select what language you would like your movie in.\n" +
                        "Leave option unselected if you do not wish to have this affect your results.");
                break;
            default:
                popUp.setLayout( .4, .8);
                popUp.setHeading("Error");
                popUp.setText("Text not set yet.");
                break;
        }
        popUp.openPopUp();
    }
}
