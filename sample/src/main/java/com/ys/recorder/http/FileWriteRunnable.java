package com.ys.recorder.http;

import android.content.Context;
import android.os.Handler;
import android.util.Log;


import com.ys.recorder.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileWriteRunnable implements Runnable {

    int rawId;
    String savePath;
    long fileLength;
    WriteSdListener listener;
    Context context;
    Handler handler = new Handler();

    public FileWriteRunnable(Context context, int rawId, String savePath, long fileLength, WriteSdListener listener) {
        this.context = context;
        this.rawId = rawId;
        this.savePath = savePath;
        this.fileLength = fileLength;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            FileUtil.creatDirPathNoExists();
            File fileSave = new File(savePath);
            if (fileSave.exists()) {
                long fileSaveLength = fileSave.length();
                if (Math.abs(fileLength - fileSaveLength) < 10240) {  //如果文件内存满足调教的话
                    Log.i("cdl", "=========文件合法 ，直接去播放就好了===================");
                    backSuccess(savePath);
                    return;
                }
                Log.i("cdl", "=========文件不合法 删除文件===================");
                fileSave.delete();
            }
            fileSave.createNewFile();
            Log.i("cdl", "=========开始写入===================");
            InputStream inStream = context.getResources().openRawResource(rawId);
            FileOutputStream fileOutputStream = new FileOutputStream(fileSave);
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len = 0;
            long sum = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
                sum += len;
                int progress = (int) (sum * 100 / fileLength);
                backProgress(progress);
            }
            byte[] bs = outStream.toByteArray();
            fileOutputStream.write(bs);
            outStream.close();
            inStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            backSuccess(savePath);
            Log.i("cdl", "=========写入完毕===================");
        } catch (Exception e) {
            backFailed(e.toString());
            Log.i("cdl", "=========写入异常===================" + e.toString());
        }
    }

    public void backFailed(final String rrorDesc) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.writrFailed(rrorDesc);
            }
        });
    }


    public void backSuccess(final String filePath) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.writeSuccess(filePath);
            }
        });
    }

    public void backProgress(final int prgress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("cdl", "=========写入中===================" + prgress);
                listener.writeProgress(prgress);
            }
        });
    }


    /***
     * 读写文件监听
     */
    public interface WriteSdListener {

        void writeProgress(int progress);

        void writeSuccess(String savePath);

        void writrFailed(String errorDesc);
    }

}
