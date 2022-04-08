package com.example.urbotanist.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Async Task for downloading a bitmap from an URL.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
  DownloadImageListener downloadImageListener;

  public DownloadImageTask(DownloadImageListener listener) {
    downloadImageListener = listener;


  }

  protected Bitmap doInBackground(String... urls) {
    try {

      URL url = new java.net.URL(urls[0]);
      HttpURLConnection connection = (HttpURLConnection) url
          .openConnection();
      connection.setDoInput(true);
      connection.connect();
      InputStream input = connection.getInputStream();
      Bitmap myBitmap = BitmapFactory.decodeStream(input);
      return myBitmap;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  protected void onPostExecute(Bitmap result) {
    downloadImageListener.onImageDownloadFinished(result);
  }
}