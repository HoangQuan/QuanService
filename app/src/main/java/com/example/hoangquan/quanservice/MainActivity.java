package com.example.hoangquan.quanservice;

import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.os.IBinder;
import android.content.ServiceConnection;
public class MainActivity extends Activity {
    IntentFilter intentFilter;
    private QuanService serviceBinder;
    Intent i;
    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            //---called when the connection is made---
            serviceBinder = ((QuanService.MyBinder)service).getService();
            try {
                URL[]urls = new URL[] {
                        new URL("http://www.amazon.com/somefiles.pdf"),
                        new URL("http://www.wrox.com/somefiles.pdf"),
                        new URL("http://www.google.com/somefiles.pdf"),
                        new URL("http://www.learn2develop.net/somefiles.pdf")};
//---assign the URLs to the service through the serviceBinder object---
                serviceBinder.urls = urls;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                startService(i);
            }
        public void onServiceDisconnected(ComponentName className) {
            //---called when the service disconnects---
            serviceBinder = null;
        }
    };
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //---intent to filter for file downloaded intent---
        intentFilter = new IntentFilter();
        intentFilter.addAction("FILE_DOWNLOADED_ACTION");
        //---register the receiver---
        registerReceiver(intentReceiver, intentFilter);
        Button btnStart = (Button) findViewById(R.id.btnStartService);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                i = new Intent(MainActivity.this, QuanService.class);
                bindService(i, connection, Context.BIND_AUTO_CREATE);
            }
        });
        Button btnStop = (Button) findViewById(R.id.btnStoptService);
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(getBaseContext(), QuanService.class));
            }
        });
    }
}
