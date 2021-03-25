package com.usf_mobile_dev.filmfriend.ui.qr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.Result;

import com.usf_mobile_dev.filmfriend.MainActivity;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import org.jetbrains.annotations.NotNull;

//https://github.com/yuriy-budiyev/code-scanner
public class QRCameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 101;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        setUpPermissions();

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS);
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setFlashEnabled(false);

        //When camera finds a qr code to scan
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView text_view = findViewById(R.id.tv_textview);
                        //text_view.setText(result.getText());

                        //if decoded qr can be turned into a movie preferences object
                        try {
                            MatchPreferences mp = MPJSONHandling.JSONToMP(result.getText());
                            text_view.setText("QR code found!");

                            // Return to match page
                            //Intent intent = getParentActivityIntent();
                            Intent intent = new Intent();
                            intent.putExtra(
                                    "com.usf_mobile_dev.filmfriend.ui.qr.NewMatchPreferencesFromQR",
                                    mp);

                            setResult(Activity.RESULT_OK, intent);
                            finish();

                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            text_view.setText("Invalid QR code, please scan again.");
                        }
                    }});
            }});

        //Error callback
        mCodeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Exception error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("QRCamera", "Camera initialization error: "
                                + error.getMessage());
                    }});
            }});

        //Continuously looks for a new code
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }});
    }

    //Lifecycle methods
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void setUpPermissions() {
        int permission =
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makePermissionRequest();
        }
    }
    private void makePermissionRequest() {
        ActivityCompat.
                requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                                   CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                        "Enable camera permission to use this functionality.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}