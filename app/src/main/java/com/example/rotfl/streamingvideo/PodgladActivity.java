package com.example.rotfl.streamingvideo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by rotfl on 12.05.2016.
 * Podgląd obrazu podczas edytowania  barwy, nasycenia jasnosci itp.
 */
public class PodgladActivity extends Activity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_dialog);

        Intent i = getIntent();
        Bundle pobierzDane = i.getExtras();
//        String url = pobierzDane.getString("url");

        image = (ImageView) findViewById(R.id.imageView);

        new DownloadImageTask((ImageView)  image).execute("http://192.168.0.112:80/jpg/image.jpg");
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
    protected void onPostExecute(Bitmap result) {
        //pDlg.dismiss();
        bmImage.setImageBitmap(result);
        bmImage.setRotationX(90);

    }};
}
