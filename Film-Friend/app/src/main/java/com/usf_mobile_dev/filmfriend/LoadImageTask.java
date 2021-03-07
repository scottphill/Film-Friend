package com.usf_mobile_dev.filmfriend;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.net.URI;

public class LoadImageTask extends AsyncTaskLoader<Uri> {

    private ImageView image;
    private String uriAddress;

    public LoadImageTask(@NonNull Context context, String uriAddress) {
        super(context);
        this.uriAddress = uriAddress;
    }

    @Nullable
    @Override
    public Uri loadInBackground() {
        return null;
    }


}
