package com.ys.recorder.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class DisPlayUtil {


    public static void startApp(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            Intent intent = manager.getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
