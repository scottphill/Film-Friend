package com.usf_mobile_dev.filmfriend.ui.qr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.usf_mobile_dev.filmfriend.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRGenerateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generate);

        ImageView image = findViewById(R.id.qr_image);
        String str = "www.geeksforgeeks.org";

        try {
            image.setImageBitmap(createQRBitMap(
                    str, 300, 300));
        } catch (WriterException e) {
            Log.e("QR_Generate", "WriterException error");
        } catch (IOException e) {
            Log.e("QR_Generate", "IOException error");
        } catch (NotFoundException e) {
            Log.e("QR_Generate","NotFoundException error");
        } catch (Exception e) {
            Log.e("QR_Generate", e.getLocalizedMessage());
        }

        TextView tv = findViewById(R.id.textView);
        tv.setText(str);
        Toast.makeText(this, "QR Code Generated!", Toast.LENGTH_SHORT).show();
    }

    //*
    public Bitmap createQRBitMap(String str, int height, int width)
            throws WriterException, IOException, NotFoundException
    {
        // Encoding charset
        String charset = "UTF-8";

        // Create the QR code and save
        // in the specified folder
        // as a jpg file
        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                new String(str.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, width, height);

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
    //*/
}
