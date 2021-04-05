package com.usf_mobile_dev.filmfriend.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;

import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.pop_up_window.PopUp;

public class Settings {

    @SuppressLint("NonConstantResourceId")
    public static void openPopUp(Context context, MenuItem item) {

        PopUp popup = new PopUp(view.getContext(), .8, .4);

        switch (item.getTitle().toString()) {
            case "About":
                popup.setHeading("About");
                popup.setText(context.getString(R.string.settings_about_body));
                break;
            case "Credits":
                popup.setHeading("Credits");
                popup.setText(context.getString(R.string.settings_credits_body));
                break;
            default:
                popup.setHeading("ERROR:");
                popup.setText("Invalid selection");
                break;
        }
        popup.openPopUp();
    }
}
