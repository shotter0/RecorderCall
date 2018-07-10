package com.ys.recorder.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActivityCollector {
    public static final String TAG = "ActivityCollector";
    private static List<Activity> activities = new ArrayList();

    public static void addActivity(Activity paramActivity) {
        activities.add(paramActivity);
        Log.i("ActivityCollector", "添加activity = " + paramActivity.getClass());
    }

    public static void finishAll() {
        Iterator localIterator = activities.iterator();
        while (localIterator.hasNext()) {
            Activity localActivity = (Activity) localIterator.next();
            if (!localActivity.isFinishing()) {
                localActivity.finish();
            }
        }
        Log.i("ActivityCollector", "移除所有的activity");
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            Log.i("activityName---", cpn.getClassName());
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }


    public static void removeActivity(Activity paramActivity) {
        activities.remove(paramActivity);
        Log.i("ActivityCollector", "移除activity = " + paramActivity.getClass());
    }
}
