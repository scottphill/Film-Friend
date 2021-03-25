package com.usf_mobile_dev.filmfriend.ui.qr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.usf_mobile_dev.filmfriend.MainActivity;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

public class QrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
    }

    public void launchQRCamera(View view) {
        Toast.makeText(this, "Opening Camera...", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, QRCameraActivity.class);
        startActivityForResult(intent, QR_REQUEST);
    }

    public void launchQRGenerator(View view) {
        Toast.makeText(this, "Generating Code...", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, QRGenerateActivity.class);

        intent.putExtra("com.usf_mobile_dev.filmfriend.ui.qr.CurrentMatchPreference", getIntent()
                .getSerializableExtra("com.usf_mobile_dev.filmfriend.ui.match.CurrentMatchPreference"));

        startActivity(intent);
    }

    // Unique tag for the intent reply
    public static final int QR_REQUEST = 2;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Test for the right intent reply.
        if (requestCode == QR_REQUEST && resultCode == Activity.RESULT_OK) {

            // Return to match page
            //Intent intent = getParentActivityIntent();
            Intent intent = new Intent();
            intent.putExtra("com.usf_mobile_dev.filmfriend.ui.qr.NewMatchPreferencesFromQR",
                    data.getSerializableExtra(
                            "com.usf_mobile_dev.filmfriend.ui.qr.NewMatchPreferencesFromQR"));

            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}