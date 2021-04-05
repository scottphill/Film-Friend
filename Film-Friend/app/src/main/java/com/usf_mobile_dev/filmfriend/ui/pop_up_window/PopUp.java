package com.usf_mobile_dev.filmfriend.ui.pop_up_window;

import android.app.Dialog;
import android.content.Context;
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

    private TextView body_TextView;
    private TextView heading_TextView;
    private ImageButton cancel_button;

    public PopUp(Context context, double h, double w ) {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pop_up);
        dialog.setCancelable(true);
        HEIGHT = h;
        WIDTH = w;

        double width = context.getResources().getDisplayMetrics().widthPixels * WIDTH;
        double height = context.getResources().getDisplayMetrics().heightPixels * HEIGHT;
        dialog.getWindow().setLayout((int) width, (int) height);

        text = new String();
        heading = new String();

        body_TextView = (TextView) dialog.findViewById(R.id.popup_text);
        heading_TextView = (TextView) dialog.findViewById(R.id.popup_heading);

        cancel_button = (ImageButton) dialog.findViewById(R.id.popup_close_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }});
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
