package com.usf_mobile_dev.filmfriend.ui.tutorial_popups;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.usf_mobile_dev.filmfriend.R;

public class PopUp {

    private double WIDTH;
    private double HEIGHT;

    private Dialog dialog;
    private String text;
    private String heading;

    private Context context;
    private TextView body_TextView;
    private TextView heading_TextView;
    private ImageButton cancel_button;

    public PopUp(Context context) {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pop_up);
        dialog.setCancelable(true);

        text = "";
        heading = "";
        HEIGHT = .5; // 50% of phone screen height
        WIDTH = .5; // 50% of phone screen width
        this.context = context;

        body_TextView = (TextView) dialog.findViewById(R.id.popup_text);
        heading_TextView = (TextView) dialog.findViewById(R.id.popup_heading);

        cancel_button = (ImageButton) dialog.findViewById(R.id.popup_close_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }});
    }

    public void setLayout(double h, double w) {

        try {
            if (h > 1 || h <= 0 || w > 1 || w <= 0) {
                throw new Exception();
            }
            else {
                HEIGHT = h;
                WIDTH = w;
                dialog.getWindow().setLayout(
                        (int) (context.getResources().getDisplayMetrics().widthPixels * WIDTH),
                        (int) (context.getResources().getDisplayMetrics().heightPixels * HEIGHT));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PopUp Set Layout", "Invalid layout size.");
        }
    }

    public String getText() { return text; }
    public void setText(String str) { text = str; }
    public String getHeading() { return heading; }
    public void setHeading(String str) { heading = str; }

    public void openPopUp() {

        body_TextView.setText(getText());
        heading_TextView.setText(getHeading());
        dialog.show();
    }
}
