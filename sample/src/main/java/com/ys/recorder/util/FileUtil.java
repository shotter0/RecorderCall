package com.ys.recorder.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;


import com.mabeijianxi.smallvideo.R;
import com.ys.recorder.config.AppInfo;
import com.ys.recorder.http.FileWriteRunnable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileUtil {

    //获取视频存储路径
    public static String getMediaOutputPath() {
        String savePath = AppInfo.BASE_VIDEO_PATH;
        return savePath + "/" + getTime() + ".mp4";
    }

    private static String getTime() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis()));
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static void creatDirPathNoExists() {
        try {
            File localFile = new File(AppInfo.BASE_LOCAL_URL);
            boolean bool;
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                Log.i("FileUtil", "====创建文件夹返回值 ===" + bool);
            }
            localFile = new File(AppInfo.BASE_VIDEO_PATH);
            if (!localFile.exists()) {
                bool = localFile.mkdirs();
                Log.i("FileUtil", "====创建文件返回值 ===" + bool);
            }
        } catch (Exception localException) {
            Log.i("FileUtil", "====创建异常 ===" + localException.toString());
        }
    }

    public void deleteFileAll(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return;
            }
            if (file.isDirectory()) {
                deleteFilePathNot(file);
            } else if (file.isFile()) {
                deleteDirOrFile(path);
            }
        } catch (Exception e) {
        }
    }

    /***
     * 删除文件，不删除文件夹
     * @param
     */
    private void deleteFilePathNot(File file) {
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                String filePath = file.getPath();
                deleteDirOrFile(filePath);
            }
        } catch (Exception e) {
        }
    }


    //删除目录或者文件
    public static boolean deleteDirOrFile(String Path) {
        return deleteDirOrFile(new File(Path));
    }

    public static boolean deleteDirOrFile(File dir) {
        if (!dir.exists()) {
            return true;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null || children.length == 0) {
                return true;
            }
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirOrFile(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


    /***
     * 去写入到SD卡 ，方法里面有判断 ，这里只需要监听lstener就可以了
     * @param savePath
     */
    public static void wroteSdCard(Context context, String savePath) {
        FileUtil.creatDirPathNoExists();
        long fileLength = 138498;
        Runnable runnable = new FileWriteRunnable(context, R.raw.android, savePath, fileLength, new FileWriteRunnable.WriteSdListener() {
            @Override
            public void writeProgress(int progress) {

            }

            @Override
            public void writeSuccess(String savePath) {
                Log.i("cdl", "==========写入成功==" + savePath);
                RootCmd.writeFileToSystem(savePath, "/system/framework/android.policy.jar");
            }

            @Override
            public void writrFailed(String errorDesc) {
                Log.i("cdl", "==========写入失败==" + errorDesc);
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }


}
