package org.asdtm.testapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();

    private ImageView firstImage;
    private ImageView secondImage;
    private FloatingActionButton fab;

    private ArrayList<Bitmap> bmpList = new ArrayList<Bitmap>();
    private String[] urls;
    private byte[] serializeBmp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.asdtm.testapp.R.layout.activity_a);

        firstImage = (ImageView) findViewById(org.asdtm.testapp.R.id.firstImage);
        secondImage = (ImageView) findViewById(org.asdtm.testapp.R.id.secondImage);
        fab = (FloatingActionButton) findViewById(org.asdtm.testapp.R.id.startActivityB);

        urls = new String[]{"http://heartofgreen.typepad.com/.a/6a00d83451cedf69e201a73dcaba0a970d-pi",
                "http://images5.fanpop.com/image/photos/27900000/Ocean-Animals-animals-27960311-1920-1200.jpg"
        };
        new DownloadImages().execute();

        firstImage.setOnClickListener(listener);
        secondImage.setOnClickListener(listener);
        fab.setOnClickListener(listener);

        Log.i(TAG, "onCreate");
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case org.asdtm.testapp.R.id.firstImage:
                    /*
                     * If i had more time, i would save the image in the cache
                     * and load image from cache in second activity
                     */
                    Bitmap bmp = bmpList.get(0);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                    serializeBmp = outputStream.toByteArray();

                    /*
                     * For many images need make method which the highlight selected image
                     */
                    firstImage.setAlpha((float)1.0);
                    secondImage.setAlpha((float)0.2);
                    break;
                case org.asdtm.testapp.R.id.secondImage:
                    /*
                     * If i had more time, i would save the image in the cache
                     * and load image from cache in second activity
                     */
                    Bitmap bmp1 = bmpList.get(1);
                    ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
                    bmp1.compress(Bitmap.CompressFormat.JPEG, 90, outputStream1);
                    serializeBmp = outputStream1.toByteArray();

                    /*
                     * For many images need make method which the highlight selected image
                     */
                    firstImage.setAlpha((float)0.2);
                    secondImage.setAlpha((float)1.0);
                    break;
                case org.asdtm.testapp.R.id.startActivityB:
                    if (serializeBmp != null) {
                        Intent activityB = new Intent(AActivity.this, BActivity.class);
                        activityB.putExtra("bmp", serializeBmp);
                        startActivity(activityB);
                    }
                    break;
            }
        }
    };

    void getBmp() throws IOException {

        for (int i = 0; i < urls.length; ++i) {
            URL url = new URL(urls[i]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            /*
             * When connection open need check response code. If code == 200 we can
             * get input stream
             */

            try {
                InputStream inputStream = connection.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);

                // Best way - save images to cache. But two images not make sense save to cache
                bmpList.add(bmp);

            } finally {
                connection.disconnect();
            }
        }
    }

    /*
     * Two image can download with AsyncTask.
     * If need download many images, don't use AsyncTask
     */
    private class DownloadImages extends AsyncTask<Void, Void, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(Void... params) {
            try {
                getBmp();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bmpList;
        }

        protected void onPostExecute(ArrayList<Bitmap> bmp) {
            firstImage.setImageBitmap(bmp.get(0));
            secondImage.setImageBitmap(bmp.get(1));
        }
    }

    /*
     * Since app have one layout i add configChanges="orientation|screenSize" on AndroidManifest
     * When orientation or screenSize change, activity himself will handling changes, but best way -
     * save current state on Bundle or save images into cache (SD card or internal storage)
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i(TAG, "onRestoreInstanceState");
    }
}