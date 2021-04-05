package com.usf_mobile_dev.filmfriend.ui.settings;

import android.annotation.SuppressLint;
import android.view.View;

import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.pop_up_window.PopUp;

public class Settings {

    @SuppressLint("NonConstantResourceId")
    public static void openPopUp(View view) {

        PopUp popup = new PopUp(view.getContext(), .8, .4);

        switch (view.getId()) {
            case R.id.settings_about:
                popup.setHeading("About");
                popup.setText(view.getContext().getString(R.string.settings_about_body));
                break;
            case R.id.settings_credit:
                popup.setHeading("Credits");
                popup.setText(view.getContext().getString(R.string.settings_credits_body));
                break;
            default:
                popup.setHeading("ERROR:");
                popup.setText("Invalid selection");
                break;
        }
        popup.openPopUp();
    }
}
