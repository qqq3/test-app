package org.asdtm.testapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class BActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.asdtm.testapp.R.layout.activity_b);

        ImageView receivedImage = (ImageView) findViewById(R.id.receivedImage);

        byte[] bmpByte = getIntent().getByteArrayExtra("bmp");
        Bitmap bmpImage = BitmapFactory.decodeByteArray(bmpByte, 0, bmpByte.length);
        receivedImage.setImageBitmap(bmpImage);
    }
}