//package com.ys.recorder.activity;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.mabeijianxi.smallvideo.R;
//import com.yixia.videoeditor.adapter.UtilityAdapter;
//import com.ys.recorder.util.ViewSizeChange;
//import com.ys.recorder.view.MyToastView;
//
//import java.io.File;
//
//import mabeijianxi.camera.MediaRecorderBase;
//import mabeijianxi.camera.MediaRecorderNative;
//import mabeijianxi.camera.VCamera;
//import mabeijianxi.camera.model.MediaObject;
//import mabeijianxi.camera.util.FileUtils;
//
///**
// * 视频录制
// */
//public class MediaRecorderActivityBack0 extends Activity implements
//        MediaRecorderBase.OnErrorListener,
//        OnClickListener,
//        MediaRecorderBase.OnPreparedListener,
//        MediaRecorderBase.OnEncodeListener {
//
//    private SurfaceView mSurfaceView;
//    private MediaRecorderBase mMediaRecorder;
//    private MediaObject mMediaObject;
//    Button btn_start_camera;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
//        setContentView(mabeijianxi.camera.R.layout.activity_media_recorder);
//        loadViews();
//    }
//
//    /**
//     * 加载视图
//     */
//    private void loadViews() {
//        mSurfaceView = (SurfaceView) findViewById(R.id.record_preview);
//        ViewSizeChange.changeSurfaceSize(mSurfaceView);
//        btn_start_camera = (Button) findViewById(R.id.btn_start_camera);
//        btn_start_camera.setOnClickListener(this);
//    }
//
//    /**
//     * 初始化拍摄SDK
//     */
//    private void initMediaRecorder() {
//        mMediaRecorder = new MediaRecorderNative();
//        mMediaRecorder.setOnErrorListener(this);
//        mMediaRecorder.setOnEncodeListener(this);
//        mMediaRecorder.setOnPreparedListener(this);
//        File f = new File(VCamera.getVideoCachePath());
//        if (!FileUtils.checkFile(f)) {
//            f.mkdirs();
//        }
//        String key = String.valueOf(System.currentTimeMillis());
//        mMediaObject = mMediaRecorder.setOutputDirectory(key,
//                VCamera.getVideoCachePath() + key);
//        mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
//        mMediaRecorder.prepare();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        UtilityAdapter.freeFilterParser();
//        UtilityAdapter.initFilterParser();
//        if (mMediaRecorder == null) {
//            initMediaRecorder();
//        } else {
//            mMediaRecorder.prepare();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        stopRecord();
//        UtilityAdapter.freeFilterParser();
//        if (mMediaRecorder != null)
//            mMediaRecorder.release();
//    }
//
//    /**
//     * 开始录制
//     */
//    private void startRecord() {
//        if (mMediaRecorder != null) {
//            MediaObject.MediaPart part = mMediaRecorder.startRecord();
//            if (part == null) {
//                return;
//            }
//        }
//        isRecorder = true;
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (mMediaObject != null)
//            mMediaObject.delete();
//        finish();
//    }
//
//    /**
//     * 停止录制
//     */
//    private void stopRecord() {
//        if (mMediaRecorder != null) {
//            mMediaRecorder.stopRecord();
//        }
//        isRecorder = false;
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == mabeijianxi.camera.R.id.btn_start_camera) {
//            if (mMediaRecorder == null) {
//                return;
//            }
//            startOrStopRecorder();
//        }
////        else if (v.getId() == mabeijianxi.camera.R.id.title_next) {
////        mMediaRecorder.startEncoding();
////        }
//    }
//
//    boolean isRecorder = false;
//
//    private void startOrStopRecorder() {
//        if (isRecorder) {   //停止录制
//            stopRecord();
//        } else {   //开始录制
//            startRecord();
//        }
//    }
//
//    @Override
//    public void onEncodeStart() {
//    }
//
//    @Override
//    public void onEncodeProgress(int progress) {
//    }
//
//    /**
//     * 转码完成
//     */
//    @Override
//    public void onEncodeComplete() {
//        MyToastView.getInstance().Toast(MediaRecorderActivityBack0.this, "压缩视屏成功拉");
//    }
//
//    /**
//     * 转码失败 检查sdcard是否可用，检查分块是否存在
//     */
//    @Override
//    public void onEncodeError() {
//        Toast.makeText(this, mabeijianxi.camera.R.string.record_video_transcoding_faild,
//                Toast.LENGTH_SHORT).show();
//        finish();
//    }
//
//    @Override
//    public void onVideoError(int what, int extra) {
//
//    }
//
//    @Override
//    public void onAudioError(int what, String message) {
//
//    }
//
//    @Override
//    public void onPrepared() {
//
//    }
//}
