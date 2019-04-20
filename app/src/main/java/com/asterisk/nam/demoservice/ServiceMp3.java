package com.asterisk.nam.demoservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

public class ServiceMp3 extends Service {

    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_START = "action_start";
    public static final String ACTION_RESTART = "action_restart";
    private final static String TAG_MEDIA = "tag";
    private final static String CHANNEL_ID = "android8";
    private final static int NOTIFICATION_ID = 69;
    private final static String BUTTON_BACK_TITLE = "back";
    private final static String BUTTON_NEXT_TITLE = "next";
    private final static String BUTTON_PAUSE_TITLE = "pause";
    private final static String BUTTON_RESTART_TITLE = "pause";
    private final static int REQUEST_CODE = 2;
    private final static int POSITION_LIST = 0;
    private final static int ACTION_COMPACT_VIEW_1 = 1;
    private final static int ACTION_COMPACT_VIEW_2 = 2;
    private final static int ACTION_COMPACT_VIEW_3 = 3;
    private static MediaPlayer mMediaPlayer;
    private static NotificationManager mNotificationManager;
    private static Notification mBuilder;
    private static NotificationCompat.Builder mBuilderC;
    private static MediaSession mMediaSession;
    private IBinder mIBinder = new BinderData();
    private ArrayList<Song> mSongList;

    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            addSong();
            switch (intent.getAction()) {
                case ACTION_PAUSE:
                    mMediaPlayer.pause();
                    changeNotification(getApplicationContext());
                    break;
                case ACTION_START:
                    mMediaPlayer.start();
                    showNotifiMp3(getApplicationContext());
                    break;
                case ACTION_RESTART:
                    showNotifiMp3(getApplicationContext());
                    mMediaPlayer.pause();
                    mMediaPlayer.stop();
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), mSongList.get(POSITION_LIST).getFile());
                    mMediaPlayer.start();
                    break;
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void addSong() {
        mSongList = new ArrayList<>();
        mSongList.add(new Song(getString(R.string.mp3_lmsl), R.raw.lmds));
        mSongList.add(new Song(getString(R.string.mp3_gaxn), R.raw.gaxn));
        mSongList.add(new Song(getString(R.string.mp3_dab), R.raw.dab));
        createMediaPlayer();
    }

    public void createMediaPlayer() {
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), mSongList.get(POSITION_LIST).getFile());
    }

    public void showNotifiMp3(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaSession = new MediaSession(context, TAG_MEDIA);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_adjust_black_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.winter))
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(context.getString(R.string.em_se_la_co_dau))
                    .setContentText(context.getString(R.string.auth_eslcd))
                    .setAutoCancel(true)
                    .setColor(Color.GRAY)
                    .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(ACTION_COMPACT_VIEW_1, ACTION_COMPACT_VIEW_2, ACTION_COMPACT_VIEW_3).setMediaSession(mMediaSession.getSessionToken()))
                    .setContentIntent(Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_skip_previous_black_24dp, BUTTON_BACK_TITLE, Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_pause_black_24dp, BUTTON_PAUSE_TITLE, createPendingIntentStartPause())
                    .addAction(R.drawable.ic_skip_next_black_24dp, BUTTON_NEXT_TITLE, Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_refresh_black_24dp, BUTTON_RESTART_TITLE, createPendingIntentRestart())
                    .build()
            ;
        }
        {
            mBuilderC = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_adjust_black_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.winter))
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(context.getString(R.string.em_se_la_co_dau))
                    .setContentText(context.getString(R.string.auth_eslcd))
                    .setAutoCancel(true)
                    .setColor(Color.GRAY)
                    .setContentIntent(Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_skip_previous_black_24dp, BUTTON_BACK_TITLE, Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_pause_black_24dp, BUTTON_PAUSE_TITLE, createPendingIntentStartPause())
                    .addAction(R.drawable.ic_skip_next_black_24dp, BUTTON_NEXT_TITLE, Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_refresh_black_24dp, BUTTON_RESTART_TITLE, createPendingIntentRestart())
            ;
        }
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.my_data);
            String description = context.getString(R.string.my_des);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mNotificationManager.createNotificationChannel(channel);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, mBuilder);
        } else {
            mNotificationManager.notify(NOTIFICATION_ID, mBuilderC.build());
        }
    }

    public void changeNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaSession = new MediaSession(context, TAG_MEDIA);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_adjust_black_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.winter))
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(context.getString(R.string.em_se_la_co_dau))
                    .setContentText(context.getString(R.string.auth_eslcd))
                    .setAutoCancel(true)
                    .setColor(Color.GRAY)
                    .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2).setMediaSession(mMediaSession.getSessionToken()))
                    .setContentIntent(Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_skip_previous_black_24dp, BUTTON_BACK_TITLE, Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_play_arrow_black_24dp, BUTTON_PAUSE_TITLE, createPendingIntentStartPause())
                    .addAction(R.drawable.ic_skip_next_black_24dp, BUTTON_NEXT_TITLE, Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_refresh_black_24dp, BUTTON_RESTART_TITLE, createPendingIntentRestart())
                    .build()
            ;
        }
        {
            mBuilderC = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_adjust_black_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.winter))
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(context.getString(R.string.em_se_la_co_dau))
                    .setContentText(context.getString(R.string.auth_eslcd))
                    .setAutoCancel(true)
                    .setColor(Color.GRAY)
                    .setContentIntent(Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_skip_previous_black_24dp, BUTTON_BACK_TITLE, Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_play_arrow_black_24dp, BUTTON_PAUSE_TITLE, createPendingIntentStartPause())
                    .addAction(R.drawable.ic_skip_next_black_24dp, BUTTON_NEXT_TITLE, Mp3Activity.getIntentMp3(context))
                    .addAction(R.drawable.ic_refresh_black_24dp, BUTTON_RESTART_TITLE, createPendingIntentRestart())
            ;
        }
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.my_data);
            String description = context.getString(R.string.my_des);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mNotificationManager.createNotificationChannel(channel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.cancel(NOTIFICATION_ID);
            startForeground(1, mBuilder);
        } else {
            mNotificationManager.notify(NOTIFICATION_ID, mBuilderC.build());
        }
    }

    public PendingIntent createPendingIntentStartPause() {
        if (mMediaPlayer.isPlaying()) {
            Intent servicePause = new Intent(getApplicationContext(), ServiceMp3.class).setAction(ACTION_PAUSE);
            PendingIntent servicePendingPause = PendingIntent.getService(getApplicationContext(), REQUEST_CODE, servicePause, PendingIntent.FLAG_ONE_SHOT);
            return servicePendingPause;
        } else {
            Intent serviceStart = new Intent(getApplicationContext(), ServiceMp3.class).setAction(ACTION_START);
            PendingIntent servicePendingStart = PendingIntent.getService(getApplicationContext(), REQUEST_CODE, serviceStart, PendingIntent.FLAG_ONE_SHOT);
            return servicePendingStart;
        }
    }

    public PendingIntent createPendingIntentRestart() {
        Intent serviceStart = new Intent(getApplicationContext(), ServiceMp3.class).setAction(ACTION_RESTART);
        PendingIntent servicePendingStart = PendingIntent.getService(getApplicationContext(), REQUEST_CODE, serviceStart, PendingIntent.FLAG_ONE_SHOT);
        return servicePendingStart;
    }

    class BinderData extends Binder {
        public ServiceMp3 getService() {
            return ServiceMp3.this;
        }
    }
}
