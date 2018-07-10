package com.ys.recorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ys.recorder.activity.TestActivity;
import com.ys.recorder.config.AppInfo;


public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    public static final String KEYCODE_MENU_CLICK = "com.ys.keyevent.menu";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i("cdl", "开机广播接收的广播==" + action);
        if (action.equals(ACTION_BOOT)) {
            Intent intent1 = new Intent(context, TestActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        } else if (action.equals(KEYCODE_MENU_CLICK)) {
            Intent intent1 = new Intent();
            intent1.setAction(AppInfo.MENU_CODE_BROADCAST);
            context.sendBroadcast(intent1);
        }
    }
}
