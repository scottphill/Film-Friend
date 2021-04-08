package com.usf_mobile_dev.filmfriend.ui.saved_preferences;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MatchPreferences;

import org.jetbrains.annotations.NotNull;

public class DeletePreferencesDialogFragment extends DialogFragment {

    MatchPreferences preferences;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface DeletePreferencesDialogListener {
        public void onDialogPositiveClick(DeletePreferencesDialogFragment dialog);
        public void onDialogNegativeClick(DeletePreferencesDialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    DeletePreferencesDialogListener listener;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder deletePreferencesDialogBuilder
                = new AlertDialog.Builder(getActivity());
        deletePreferencesDialogBuilder
                .setTitle(R.string.delete_mp_title)
                .setMessage(String.format(
                        "Are you sure you want to delete \"%s\"",
                        preferences.getPreference_title()
                ))
                .setPositiveButton(
                        R.string.confirm_delete_mp,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                listener.onDialogPositiveClick(
                                        DeletePreferencesDialogFragment.this
                                );
                            }
                        }
                )
                .setNegativeButton(
                        R.string.deny_delete_mp,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                listener.onDialogNegativeClick(
                                        DeletePreferencesDialogFragment.this
                                );
                            }
                        }
                );

        return deletePreferencesDialogBuilder.create();
    }


    // Override the Fragment.onAttach() method to instantiate the DeletePreferencesDialogListener
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the DeletePreferencesDialogListener so we can send events to the host
            listener = (DeletePreferencesDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(
                    "The activity must implement DeletePreferencesDialogListener"
            );
        }
    }

    public MatchPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(MatchPreferences preferences) {
        this.preferences = preferences;
    }

}
