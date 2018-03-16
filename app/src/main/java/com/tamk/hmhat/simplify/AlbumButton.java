package com.tamk.hmhat.simplify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageButton;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hmhat on 16.3.2018.
 */

public class AlbumButton extends android.support.v7.widget.AppCompatImageButton {
    public AlbumButton(Context context) {
        super(context);
    }

    public AlbumButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImage(MainActivity host, String url) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    URL urlConnection = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) urlConnection
                            .openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    return myBitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                BitmapDrawable drawable = new BitmapDrawable(getResources(), result);
                AlbumButton.this.setBackgroundDrawable(drawable);
            }
        };
        task.execute();
    }

}
