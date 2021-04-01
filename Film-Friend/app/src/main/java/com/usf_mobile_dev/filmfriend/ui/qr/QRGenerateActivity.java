package com.usf_mobile_dev.filmfriend.ui.qr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class QRGenerateActivity extends AppCompatActivity {

    public static final String INTENT_EXTRAS_MATCH_PREFERENCES = "com.usf_mobile_dev.filmfriend.ui.qr.CurrentMatchPreference";

    private MatchPreferences MP = null;
    private String json = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generate);

        ImageView image = findViewById(R.id.qr_image);

        // Convert MatchPreferences object to JSON string
        try {
            MP = (MatchPreferences) getIntent().getSerializableExtra(
                    INTENT_EXTRAS_MATCH_PREFERENCES
            );
            json = MPJSONHandling.mpToJSON(MP);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR Code", Toast.LENGTH_SHORT).show();
        }

        // Get the size of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        double h = displayMetrics.heightPixels;
        double w = displayMetrics.widthPixels;
        int qr_size = (int)(Math.min(h, w) * .7);

        Log.d("height pixels", h + "");
        Log.d("width pixels", w + "");
        Log.d("final size pixels", qr_size + "");

        // Convert JSON string to bitmap and set it to the ImageView
        try {
            Bitmap bm = createQRBitMap(json, qr_size, qr_size);

            image.setImageBitmap(bm);
            image.setContentDescription(json);

            Toast.makeText(this, "QR Code Generated!", Toast.LENGTH_SHORT).show();

        } catch (WriterException | NotFoundException | IOException | NullPointerException e) {
            Log.e("Bitmap from JSON", "ERROR in QRGenerateActivity");
            Toast.makeText(this, "Error displaying your QR Code.", Toast.LENGTH_SHORT).show();
        }

        // For debugging only, should be invisible
        TextView tv = findViewById(R.id.textView);
        tv.setText(json);
    }

    public Bitmap createQRBitMap(String str, int height, int width)
            throws WriterException, IOException, NotFoundException {

        // Encoding charset
        String charset = "UTF-8";

        // Create the QR code BitMatrix
        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                new String(str.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, width, height);

        // Create Bitmap from BitMatrix
        int bit_height = bitMatrix.getHeight();
        int bit_width = bitMatrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(bit_width, bit_height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
}
