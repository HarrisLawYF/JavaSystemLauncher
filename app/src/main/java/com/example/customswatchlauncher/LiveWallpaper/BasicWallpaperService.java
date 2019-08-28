package com.example.customswatchlauncher.LiveWallpaper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.customswatchlauncher.R;

public class BasicWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine {
        private List<PointObject> circles;
        private int width;
        private int height;
        private final Handler m_handler = new Handler();
        private boolean visible = true;
        private final Runnable initialDrawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }

        };

        public MyWallpaperEngine() {
            circles = new ArrayList<PointObject>();
            m_handler.post(initialDrawRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
        }
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        private void draw() {
            float x = new Random().nextInt(300) + 50;
            float y = new Random().nextInt(300) + 50;

            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    circles.clear();
                    circles.add(new PointObject(x, y));
                    drawCircles(canvas, circles);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    circles.clear();
                    circles.add(new PointObject(x, y));
                    drawCircles(canvas, circles);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            super.onTouchEvent(event);
        }

        // Surface view requires that all elements are drawn completely
        private void drawCircles(Canvas canvas, List<PointObject> circles) {
            //To cover the drawn circles
            canvas.drawColor(Color.BLACK);
            for (PointObject point : circles) {
                //canvas.drawCircle(point.x, point.y, 20.0f);
                Bitmap m_scaled = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_rocket), 80, 80, true);
                canvas.drawBitmap(m_scaled,point.x,point.y,new Paint());
            }
        }
    }
}