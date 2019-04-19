package com.asterisk.nam.demoservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String MY_KEY = "key1";
    private final static String MY_DATA = "a cup is on the table";
    private Button mButtonUnboundService, mButtonBoundService;
    private Intent mIntentS, mIntentB;
    private ServiceConnection mServiceConnection;
    private Boolean mFeelService;
    private ServiceMp3 mServiceMp3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        registerListener();
    }

    public void initViews() {
        mButtonUnboundService = findViewById(R.id.button_start_unbound_service);
        mButtonBoundService = findViewById(R.id.button_start_mp3);
    }

    public void registerListener() {
        mButtonUnboundService.setOnClickListener(this);
        mButtonBoundService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start_unbound_service:
                startForegroundServiceNow();
                break;
            case R.id.button_start_mp3:
                startForegroundServiceNow();
                break;
        }
    }

    public void startForegroundServiceNow() {
        mIntentS = new Intent(MainActivity.this, ServiceMp3.class);
        mIntentS.putExtra(MY_KEY, MY_DATA);
        mIntentS.setAction(ServiceMp3.ACTION_START);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mIntentS);
        } else {
            startService(mIntentS);
        }
        mFeelService = true;
    }

    public void bindServiceNow() {
        createServiceConnection();
        mIntentB = new Intent(MainActivity.this, ServiceMp3.class);
        bindService(mIntentB, mServiceConnection, BIND_AUTO_CREATE);
        mFeelService = true;
    }

    public void createServiceConnection() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ServiceMp3.BinderData binderData = (ServiceMp3.BinderData) service;
                mServiceMp3 = binderData.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
    }

    public void stopService(){
        stopService(mIntentS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
