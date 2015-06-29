package com.example.hoangquan.quanservice;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import android.os.IBinder;
import android.os.Binder;
/**
 * Created by HoangQuan on 6/24/2015.
 */
public class QuanService extends Service {
    int counter = 0;
    URL[] urls;
    static final int UPDATE_INTERVAL = 1000;
    private Timer timer = new Timer();
    private final IBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        QuanService getService() {
            return QuanService.this;
        }
    }
    @Override
    public IBinder onBind(Intent arg0) {
        //return null;
        return binder;
    }
    @Override

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        new DoBackgroundTask().execute(urls);
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
    private int DownloadFile(URL url) {
        try { 
            Thread.sleep(5000); 
         } catch (InterruptedException e) { 
                e.printStackTrace(); 
         } 
         return 100;
    }
    private class DoBackgroundTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalBytesDownloaded = 0;
            for (int i = 0; i < count; i++) {
                totalBytesDownloaded += DownloadFile(urls[i]);
                publishProgress((int) (((i+1) / (float) count) * 100));
            }
            return totalBytesDownloaded;
        }
        protected void onProgressUpdate(Integer... progress) {
            Log.d(“Downloading files”,
                    String.valueOf(progress[0]) + “% downloaded”);
            Toast.makeText(getBaseContext(),
                String.valueOf(progress[0]) + “% downloaded”,
                Toast.LENGTH_LONG).show();
        }
        protected void onPostExecute(Long result) {
            Toast.makeText(getBaseContext(),
                    “Downloaded “ + result + “ bytes”,
                    Toast.LENGTH_LONG).show();
            stopSelf();
        }
    }
}
