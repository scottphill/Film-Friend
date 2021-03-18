package com.usf_mobile_dev.filmfriend.ui.qr;

import androidx.appcompat.app.AppCompatActivity;

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
        startActivity(intent);
    }

    public void launchQRGenerator(View view) {
        Toast.makeText(this, "Generating Code...", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, QRGenerateActivity.class);

        intent.putExtra("CurrentMatchPreference", getIntent().getSerializableExtra("CurrentMatchPreference"));

        startActivity(intent);
    }
}