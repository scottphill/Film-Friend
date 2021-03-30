package com.usf_mobile_dev.filmfriend.ui.settings;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.usf_mobile_dev.filmfriend.R;

public class Settings {

    public static void openPopUp(Activity main, String type) {

        Dialog dialog = new Dialog(main);
        dialog.setContentView(R.layout.dialog_settings_pop_up);
        dialog.setCancelable(true);

        double popup_width = main.getResources().getDisplayMetrics().widthPixels * .8;
        double popup_height = main.getResources().getDisplayMetrics().heightPixels * .6;
        dialog.getWindow().setLayout((int) popup_width, (int) popup_height);

        TextView textView = (TextView) dialog.findViewById(R.id.popup_text);
        StringBuilder output_sb = new StringBuilder();
        switch (type) {
            case "About":
                output_sb.append(
                        "FilmFriend is an application that recommends you movies based " +
                        "on your preferences. This app chooses an ideal movie for you and your " +
                        "friends. That way, you won't have to spend hours searching for a movie " +
                        "that everyone can enjoy! Additionally, this app encourages community " +
                        "engagement, and allows you to view popular movies in your area from " +
                        "the comfort of your smart phone!"
                );
                break;
            case "Credits":
                output_sb.append("App Creators:\nBrandon Poirier\nScott Hill\nAl Allums\n\n");
                output_sb.append("Credits To:\n");
                output_sb.append("TMDB\n");
                break;
            default:
                output_sb.append("ERROR: incorrect selection??");
        }
        textView.setText(output_sb.toString());

        ImageButton cancel_button = (ImageButton) dialog.findViewById(R.id.popup_close_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
