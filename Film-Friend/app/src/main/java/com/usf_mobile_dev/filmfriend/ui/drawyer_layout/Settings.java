package com.usf_mobile_dev.filmfriend.ui.drawyer_layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MenuItem;

import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.tutorial_popups.PopUp;

public class Settings {

    @SuppressLint("NonConstantResourceId")
    public static void openPopUp(Context context, MenuItem item) {

        PopUp popup = new PopUp(context);

        switch (item.getTitle().toString()) {
            case "About":
                popup.setLayout( .4, .8);
                popup.setHeading("About");
                popup.setText(context.getString(R.string.settings_about_body));
                break;
            case "Credits":
                popup.setLayout( .4, .8);
                popup.setHeading("Credits");
                popup.setText(context.getString(R.string.settings_credits_body));
                break;
            default:
                popup.setLayout( .4, .8);
                popup.setHeading("ERROR:");
                popup.setText("Invalid selection");
                break;
        }
        popup.openPopUp();
    }
}
