package com.asterisk.nam.demoservice;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Mp3Activity extends AppCompatActivity {

    public static final int REQUEST_CODE = 69;
    public static final int FLAGS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3_main);
    }

    public static PendingIntent getIntentMp3(Context context){
        Intent intent = new Intent(context,Mp3Activity.class);
        PendingIntent pendingIntent=  PendingIntent.getActivity(context,REQUEST_CODE,intent,FLAGS);
        return  pendingIntent;
    }
}
