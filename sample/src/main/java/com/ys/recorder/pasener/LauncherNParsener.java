//package com.ys.recorder.pasener;
//
//import android.util.Log;
//import android.view.SurfaceView;
//
//import com.yixia.videoeditor.adapter.UtilityAdapter;
//import com.ys.recorder.util.ViewSizeChange;
//import com.ys.recorder.view.LauncherView;
//
//import java.io.File;
//
//import mabeijianxi.camera.MediaRecorderBase;
//import mabeijianxi.camera.MediaRecorderNative;
//import mabeijianxi.camera.VCamera;
//import mabeijianxi.camera.model.MediaObject;
//import mabeijianxi.camera.util.FileUtils;
//
//public class LauncherNParsener implements MediaRecorderBase.OnErrorListener, MediaRecorderBase.OnEncodeListener {
//
//    LauncherView launcherView;
//    SurfaceView surfaceView;
//    private MediaRecorderBase mMediaRecorder;
//    private MediaObject mMediaObject;
//    boolean isRecorder = false;
//    private static final String TAG = "LauncherNParsener";
//
//    public LauncherNParsener(LauncherView launcherView) {
//        this.launcherView = launcherView;
//        getView();
//    }
//
//    private void getView() {
//        surfaceView = launcherView.getSurfaceView();
//        ViewSizeChange.changeSurfaceSmall(surfaceView);  //初始化小的
//    }
//
//    public void changeSurfaceBigView() {
//        ViewSizeChange.changeSurfaceBig(surfaceView);
//    }
//
//    public void changeSurfaceSmalliew() {
//        ViewSizeChange.changeSurfaceSmall(surfaceView);
//    }
//
//
//    /**
//     * 初始化拍摄SDK
//     */
//    private void initMediaRecorder() {
//        mMediaRecorder = new MediaRecorderNative();
//        mMediaRecorder.setOnErrorListener(this);
//        mMediaRecorder.setOnEncodeListener(this);
//        File f = new File(VCamera.getVideoCachePath());
//        if (!FileUtils.checkFile(f)) {
//            f.mkdirs();
//        }
//        String key = String.valueOf(System.currentTimeMillis());
//        mMediaObject = mMediaRecorder.setOutputDirectory(key,
//                VCamera.getVideoCachePath() + key);
//        mMediaRecorder.setSurfaceHolder(surfaceView.getHolder());
//        mMediaRecorder.prepare();
//    }
//
//    public void onResume() {
//        UtilityAdapter.freeFilterParser();
//        UtilityAdapter.initFilterParser();
//        if (mMediaRecorder == null) {
//            initMediaRecorder();
//        } else {
//            mMediaRecorder.prepare();
//        }
//    }
//
//    public void onPause() {
//        stopRecord();
//        UtilityAdapter.freeFilterParser();
//        if (mMediaRecorder != null)
//            mMediaRecorder.release();
//    }
//
//    public void startOrStopRecorder() {
//        if (mMediaRecorder == null) {
//            launcherView.showToast("初始化录像控件失败");
//            return;
//        }
//        if (isRecorder) {   //停止录制
//            stopRecord();
//        } else {   //开始录制
//            startRecord();
//        }
//    }
//
//    //        else if (v.getId() == mabeijianxi.camera.R.id.title_next) {
////        mMediaRecorder.startEncoding();
////        }
//
//    /**
//     * 开始录制
//     */
//    public void startRecord() {
//        launcherView.showToast("开始录制");
//        if (mMediaRecorder != null) {
//            MediaObject.MediaPart part = mMediaRecorder.startRecord();
//            if (part == null) {
//                return;
//            }
//        }
//        isRecorder = true;
//    }
//
//    /**
//     * 停止录制
//     */
//    private void stopRecord() {
//        launcherView.showToast("停止录制");
//        if (mMediaRecorder != null) {
//            mMediaRecorder.stopRecord();
//        }
//        isRecorder = false;
//    }
//
//
//    @Override
//    public void onVideoError(int what, int extra) {
//        launcherView.showToast("录像异常:" + what + " /" + extra);
//    }
//
//    @Override
//    public void onAudioError(int what, String message) {
//        launcherView.showToast("录音异常:" + what + " /" + message);
//    }
//
//    @Override
//    public void onEncodeStart() {
//        Log.i(TAG, "=====开始转码=====");
//    }
//
//    @Override
//    public void onEncodeProgress(int progress) {
//        Log.i(TAG, "=====转码进度=====" + progress);
//    }
//
//    @Override
//    public void onEncodeComplete() {
//        Log.i(TAG, "=====转码结束=====");
//    }
//
//    @Override
//    public void onEncodeError() {
//        Log.i(TAG, "=====转码结束=====");
//        launcherView.showToast("视频压缩异常:");
//        launcherView.onEncodeError();
//    }
//
//
//}
