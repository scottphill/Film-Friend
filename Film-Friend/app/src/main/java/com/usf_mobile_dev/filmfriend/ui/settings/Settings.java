package com.usf_mobile_dev.filmfriend.ui.settings;

import android.app.Activity;

import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.pop_up_window.PopUp;

public class Settings {

    private static PopUp popup;

    public static void openPopUp(Activity main, String type) {

        popup = new PopUp(main);

        switch (type) {
            case "About":
                popup.setHeading(type);
                popup.setText(main.getString(R.string.settings_about_body));
                break;
            case "Credits":
                popup.setHeading(type);
                popup.setText(main.getString(R.string.settings_credits_body));
                break;
            default:
                popup.setHeading("ERROR:");
                popup.setText("Incorrect selection");
                break;
        }

        popup.openPopUp();
    }
}
