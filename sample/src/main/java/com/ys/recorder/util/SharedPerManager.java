package com.ys.recorder.util;

import android.media.CamcorderProfile;
import android.util.Log;

import com.ys.recorder.RecorderApplication;


public class SharedPerManager {

//    public static boolean isSimExict() {
//        return ((boolean) RecorderApplication.getInstance().getData("isSimExict", false));
//    }
//
//    public static void setSimExict(boolean isSimExict) {
//        RecorderApplication.getInstance().saveData("isSimExict", isSimExict);
//    }

    public static String getPhonrNum() {
        return ((String) RecorderApplication.getInstance().getData("phonrNum", "18925274342"));
    }

    public static void setPhonrNum(String phonrNum) {
        RecorderApplication.getInstance().saveData("phonrNum", phonrNum);
    }


    public static void setScreenHeight(int screenHeight) {
        RecorderApplication.getInstance().saveData("screenHeight", screenHeight);
    }

    public static void setScreenWidth(int screenWidth) {
        Log.e("width", "====setScreenWidth====" + screenWidth);
        RecorderApplication.getInstance().saveData("screenWidth", screenWidth);
    }


    public static int getScreenHeight() {
        return ((Integer) RecorderApplication.getInstance().getData("screenHeight", 768));
    }

    public static int getScreenWidth() {
        return ((Integer) RecorderApplication.getInstance().getData("screenWidth", 1366));
    }

}
