package com.example.customswatchlauncher;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.customswatchlauncher.LiveWallpaper.BasicWallpaperService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends Activity {
    private TextView time_label, date_label;
    private LinearLayout top_region, bottom_region;
    private Handler m_handler = new Handler();
    static final int MIN_DISTANCE = 150;
    private GestureDetector m_detector;
    private WallpaperManager m_wallpaperManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        setTimeView.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        m_handler.postDelayed(setTimeView, 10000);
        super.onResume();
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return m_detector.onTouchEvent(event);
        }
    };

    class UserGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG","onDown: ");
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("TAG", "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("TAG", "onDoubleTap: ");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i("TAG", "onScroll: ");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("TAG", "onFling: ");
            if(event1.getX() - event2.getX() >= MIN_DISTANCE){
                displayApplications();
            }
            return true;
        }
    }

    public void showApplications(View v){
        displayApplications();
    }

    public void setScreenSaver(View v){
        //TODO: current photoTable and photoFrame not working
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent = new Intent(Settings.ACTION_DREAM_SETTINGS);
        } else {

            intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        }
        startActivity(intent);
    }

    public void setWallpaper(View v){
        m_wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            m_wallpaperManager.setResource(R.raw.wallpaper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLivepaper(View v){
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, BasicWallpaperService.class));
        startActivity(intent);
    }

    private void displayApplications(){
        Intent i = new Intent(this, AppsListActivity.class);
        startActivity(i);
    }

    private void initView(){
        time_label = (TextView) findViewById(R.id.time_label);
        date_label = (TextView) findViewById(R.id.date_label);

        top_region = (LinearLayout) findViewById(R.id.top_region);
        bottom_region = (LinearLayout) findViewById(R.id.bottom_region);

        m_detector = new GestureDetector(this, new UserGestureListener());
        top_region.setOnTouchListener(touchListener);
        bottom_region.setOnTouchListener(touchListener);
    }

    private Runnable setTimeView = new Runnable() {
        @Override
        public void run() {
            Date today = new Date();
            SimpleDateFormat time = new SimpleDateFormat("HH:mm");
            SimpleDateFormat date = new SimpleDateFormat("EEEE, yyyy-MM-dd");
            String currentTimeString = time.format(today);
            String currentDateString = date.format(today);
            time_label.setText(currentTimeString);
            date_label.setText(currentDateString);
            m_handler.postDelayed(this, 10000);
        }
    };
}