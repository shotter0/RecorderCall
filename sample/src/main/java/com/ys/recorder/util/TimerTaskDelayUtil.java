//package com.ys.recorder.util;
//
//import android.os.Handler;
//import android.os.Message;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class TimerTaskDelayUtil {
//
//    public static final int CHECK_PHONE_STATE = 20 * 1000;
//    private static final String TAG = "ScreenViewService";
//    TimerTaskDelayListener listener;
//    long timeLength;
//
//    Timer timer;
//    MyTask task;
//
//    public void startTimer(long timeLength, TimerTaskDelayListener listener) {
//        this.listener = listener;
//        this.timeLength = timeLength;
//        cacelTimer();
//        timer = new Timer(true);
//        task = new MyTask();
//        timer.schedule(task, 1000, timeLength);
//    }
//
//    public void cacelTimer() {
//        if (timer != null) {
//            timer.cancel();
//        }
//        if (task != null) {
//            task.cancel();
//        }
//    }
//
//    class MyTask extends TimerTask {
//        @Override
//        public void run() {
//            handler.sendEmptyMessage(START_TIMER);
//        }
//    }
//
//    private static final int START_TIMER = 234;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == START_TIMER) {
//                listener.timeIsOn();
//            }
//        }
//    };
//
//    public interface TimerTaskDelayListener {
//        /***
//         * 监听的时间到了
//         */
//        void timeIsOn();
//    }
//
//
//}
