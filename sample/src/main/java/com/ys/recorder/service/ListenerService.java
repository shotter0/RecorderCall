package com.ys.recorder.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.ys.recorder.util.FileSizeUtil;

import java.util.Timer;
import java.util.TimerTask;


public class ListenerService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /***
     * 被杀后自启
     * @param paramIntent
     * @param paramInt1
     * @param paramInt2
     * @return
     */
    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startTimer();
    }

    Timer timer;
    MyTask task;

    public void startTimer() {
        cacelTimer();
        timer = new Timer(true);
        task = new MyTask();
        timer.schedule(task, 1000, 3 * 60 * 1000); //没三分钟检查一次内存
    }

    public void cacelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
    }

    class MyTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(START_TIMER_CLEAR);
        }
    }

    private static final int START_TIMER_CLEAR = 234;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == START_TIMER_CLEAR) {
                checkSdPath();
            }
        }
    };

    private void checkSdPath() {
        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/ysRecorder";
            double size = FileSizeUtil.getFileOrFilesSize(path, 3);
            Log.i("haha", "======================检查内存一次 :  " + size + "  MB");
        } catch (Exception e) {
        }
    }
}
