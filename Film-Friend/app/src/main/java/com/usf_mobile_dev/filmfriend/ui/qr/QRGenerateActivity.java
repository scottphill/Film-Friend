package com.usf_mobile_dev.filmfriend.ui.qr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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

    MatchPreferences MP;
    ImageView image;
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generate);

        image = findViewById(R.id.qr_image);

        // Convert MatchPreferences object to JSON string
        try {
            json = MPJSONHandling.mpToJSON(
                    (MatchPreferences) getIntent().getSerializableExtra(
                            "CurrentMatchPreference"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "Error generating QR Code", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

        // Convert JSON string to bitmap and set it to the ImageView
        try {
            Log.d("QR_Generate", json);

            Bitmap bm = createQRBitMap(json, 300, 300);

            Log.d("QR_Bool", String.valueOf(bm == null));
            Log.d("QR_String", bm.toString());

            image.setImageBitmap(bm);

            Toast.makeText(this, "QR Code Generated!", Toast.LENGTH_SHORT).show();
        } catch (WriterException | NotFoundException | IOException e) {
            Log.e("QR_Generate", "ERROR in QRGenerateActivity");
            Toast.makeText(this, "There was an error generating your QR Code.",
                    Toast.LENGTH_SHORT).show();
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
