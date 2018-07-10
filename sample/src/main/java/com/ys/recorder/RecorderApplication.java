package com.ys.recorder;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.ys.recorder.config.AppInfo;
import com.ys.recorder.util.FileUtil;

import java.io.File;

import mabeijianxi.camera.VCamera;
import mabeijianxi.camera.util.DeviceUtils;


public class RecorderApplication extends Application {

    public static RecorderApplication instance;
    private static SharedPreferences mSharedPreferences;
    public static String USER_INFO = "call_camera";


    public static RecorderApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtil.creatDirPathNoExists();
        initSmallVideo(getApplicationContext());
        instance = this;
        mSharedPreferences = getSharedPreferences(USER_INFO, 0);
    }

    // 设置拍摄视频缓存路径
    public static void initSmallVideo(Context context) {
        // 设置拍摄视频缓存路径
        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                VCamera.setVideoCachePath(dcim + "/ysRecorder/");
            } else {
                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/ysRecorder/");
            }
        } else {
            VCamera.setVideoCachePath(dcim + "/ysRecorder/");
        }
        // 开启log输出,ffmpeg输出到logcat
//        VCamera.setDebugMode(true);
        // 初始化拍摄SDK，必须
        VCamera.initialize(context);
    }

    //====================================================================

    public void saveData(String key, Object data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        try {
            Log.i("SharedPreferences", "设置的tag =" + key + "   //date = " + data);
            if (data instanceof Integer) {
                editor.putInt(key, (Integer) data);
            } else if (data instanceof Boolean) {
                editor.putBoolean(key, (Boolean) data);
            } else if (data instanceof String) {
                editor.putString(key, (String) data);
            } else if (data instanceof Float) {
                editor.putFloat(key, (Float) data);
            } else if (data instanceof Long) {
                editor.putLong(key, (Long) data);
            }
        } catch (Exception e) {
            Log.i("SharedPreferences", "获取的的tag =" + key + "   //date = " + e.toString());
        }
        editor.commit();
    }

    public Object getData(String key, Object defaultObject) {
        try {
            Log.i("SharedPreferences", "获取的的tag =" + key + "   //date = " + defaultObject.toString());
            if (defaultObject instanceof String) {
                return mSharedPreferences.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return mSharedPreferences.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return mSharedPreferences.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return mSharedPreferences.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return mSharedPreferences.getLong(key, (Long) defaultObject);
            }
        } catch (Exception e) {
            Log.i("SharedPreferences", "获取的的tag =" + key + "   //date = " + e.toString());
            return null;
        }
        return null;
    }


}
