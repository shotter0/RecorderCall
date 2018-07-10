//package mabeijianxi.camera;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.yixia.videoeditor.adapter.UtilityAdapter;
//
//import java.io.File;
//
//import mabeijianxi.camera.model.MediaObject;
//import mabeijianxi.camera.model.MediaRecorderConfig;
//import mabeijianxi.camera.util.DeviceUtils;
//import mabeijianxi.camera.util.FileUtils;
//import mabeijianxi.camera.util.StringUtils;
//
//import static mabeijianxi.camera.MediaRecorderBase.SMALL_VIDEO_WIDTH;
//import static mabeijianxi.camera.MediaRecorderBase.compressConfig;
//
///**
// * 视频录制
// */
//public class MediaRecorderActivityBack extends Activity implements
//        MediaRecorderBase.OnErrorListener, OnClickListener, MediaRecorderBase.OnPreparedListener,
//        MediaRecorderBase.OnEncodeListener {
//
//    /**
//     * 录制最长时间
//     */
//    private static int RECORD_TIME_MAX = 6 * 1000;
//    /**
//     * 录制最小时间
//     */
//    private static int RECORD_TIME_MIN = (int) (1.5f * 1000);
//    /**
//     * 刷新进度条
//     */
//    private static final int HANDLE_INVALIDATE_PROGRESS = 0;
//    /**
//     * 延迟拍摄停止
//     */
//    private static final int HANDLE_STOP_RECORD = 1;
//
//    /**
//     * 下一步
//     */
//    private ImageView mTitleNext;
//    /**
//     * 摄像头数据显示画布
//     */
//    private SurfaceView mSurfaceView;
//    /**
//     * SDK视频录制对象
//     */
//    private MediaRecorderBase mMediaRecorder;
//    /**
//     * 视频信息
//     */
//    private MediaObject mMediaObject;
//
//    /**
//     * 是否是点击状态
//     */
//    private volatile boolean mPressedStatus;
//    /**
//     * 是否已经释放
//     */
//    private volatile boolean mReleased;
//    /**
//     * 视屏地址
//     */
//    public final static String VIDEO_URI = "video_uri";
//    /**
//     * 本次视频保存的文件夹地址
//     */
//    public final static String OUTPUT_DIRECTORY = "output_directory";
//    /**
//     * 视屏截图地址
//     */
//    public final static String VIDEO_SCREENSHOT = "video_screenshot";
//    /**
//     * 录制完成后需要跳转的activity
//     */
//    public final static String OVER_ACTIVITY_NAME = "over_activity_name";
//    /**
//     * 录制配置key
//     */
//    public final static String MEDIA_RECORDER_CONFIG_KEY = "media_recorder_config_key";
//
//    private boolean GO_HOME;
//
//    /**
//     * @param context
//     * @param overGOActivityName 录制结束后需要跳转的Activity全类名
//     */
//    public static void goSmallVideoRecorder(Activity context, String overGOActivityName, MediaRecorderConfig mediaRecorderConfig) {
//        context.startActivity(new Intent(context, MediaRecorderActivityBack.class).putExtra(OVER_ACTIVITY_NAME, overGOActivityName).putExtra(MEDIA_RECORDER_CONFIG_KEY, mediaRecorderConfig));
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
//        initData();
//        loadViews();
//    }
//
//    private void initData() {
//        Intent intent = getIntent();
//        MediaRecorderConfig mediaRecorderConfig = intent.getParcelableExtra(MEDIA_RECORDER_CONFIG_KEY);
//        if (mediaRecorderConfig == null) {
//            return;
//        }
//        RECORD_TIME_MAX = mediaRecorderConfig.getRecordTimeMax();
//        RECORD_TIME_MIN = mediaRecorderConfig.getRecordTimeMin();
//        MediaRecorderBase.MAX_FRAME_RATE = mediaRecorderConfig.getMaxFrameRate();
//        MediaRecorderBase.MIN_FRAME_RATE = mediaRecorderConfig.getMinFrameRate();
//        MediaRecorderBase.SMALL_VIDEO_HEIGHT = mediaRecorderConfig.getSmallVideoHeight();
//        SMALL_VIDEO_WIDTH = mediaRecorderConfig.getSmallVideoWidth();
//        MediaRecorderBase.mVideoBitrate = mediaRecorderConfig.getVideoBitrate();
//        MediaRecorderBase.mediaRecorderConfig = mediaRecorderConfig.getMediaBitrateConfig();
//        MediaRecorderBase.compressConfig = mediaRecorderConfig.getCompressConfig();
//        MediaRecorderBase.CAPTURE_THUMBNAILS_TIME = mediaRecorderConfig.getCaptureThumbnailsTime();
//        MediaRecorderBase.doH264Compress = mediaRecorderConfig.isDoH264Compress();
//        GO_HOME = mediaRecorderConfig.isGO_HOME();
//    }
//
//    Button btn_start_camera;
//
//    /**
//     * 加载视图
//     */
//    private void loadViews() {
//        setContentView(R.layout.activity_media_recorder);
//        mSurfaceView = (SurfaceView) findViewById(R.id.record_preview);
//        mTitleNext = (ImageView) findViewById(R.id.title_next);
//        mTitleNext.setOnClickListener(this);
//        btn_start_camera = (Button) findViewById(R.id.btn_start_camera);
//        btn_start_camera.setOnClickListener(this);
//    }
//
//    /**
//     * 初始化画布
//     */
//    private void initSurfaceView() {
//        final int w = DeviceUtils.getScreenWidth(this);
//        int width = w;
//        int height = (int) (w * ((MediaRecorderBase.mSupportedPreviewWidth * 1.0f) / MediaRecorderBase.SMALL_VIDEO_WIDTH));
//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceView
//                .getLayoutParams();
//        lp.width = width;
//        lp.height = height;
//        mSurfaceView.setLayoutParams(lp);
//    }
//
//    /**
//     * 初始化拍摄SDK
//     */
//    private void initMediaRecorder() {
//        mMediaRecorder = new MediaRecorderNative();
//
//        mMediaRecorder.setOnErrorListener(this);
//        mMediaRecorder.setOnEncodeListener(this);
//        mMediaRecorder.setOnPreparedListener(this);
//
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
//
////    /**
////     * 点击屏幕录制
////     */
////    private View.OnTouchListener mOnVideoControllerTouchListener = new View.OnTouchListener() {
////
////        @Override
////        public boolean onTouch(View v, MotionEvent event) {
////            if (mMediaRecorder == null) {
////                return false;
////            }
////
////            switch (event.getAction()) {
////                case MotionEvent.ACTION_DOWN:
////                    // 检测是否手动对焦
////                    // 判断是否已经超时
////                    if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
////                        return true;
////                    }
////
////                    // 取消回删
////                    if (cancelDelete())
////                        return true;
////
////                    startRecord();
////
////                    break;
////
////                case MotionEvent.ACTION_UP:
////                    // 暂停
////                    if (mPressedStatus) {
////                        stopRecord();
////
////                        // 检测是否已经完成
////                        if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
////                            mTitleNext.performClick();
////                        }
////                    }
////                    break;
////            }
////            return true;
////        }
////
////    };
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        UtilityAdapter.freeFilterParser();
//        UtilityAdapter.initFilterParser();
//
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
//        if (!mReleased) {
//            if (mMediaRecorder != null)
//                mMediaRecorder.release();
//        }
//        mReleased = false;
//    }
//
//
//    /**
//     * 开始录制
//     */
//    private void startRecord() {
//        if (mMediaRecorder != null) {
//
//            MediaObject.MediaPart part = mMediaRecorder.startRecord();
//            if (part == null) {
//                return;
//            }
//        }
//        mPressedStatus = true;
//        if (mHandler != null) {
//            mHandler.removeMessages(HANDLE_INVALIDATE_PROGRESS);
//            mHandler.sendEmptyMessage(HANDLE_INVALIDATE_PROGRESS);
//
//            mHandler.removeMessages(HANDLE_STOP_RECORD);
//            mHandler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD,
//                    RECORD_TIME_MAX - mMediaObject.getDuration());
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (mMediaObject != null && mMediaObject.getDuration() > 1) {
//            // 未转码
//            new AlertDialog.Builder(this)
//                    .setTitle(R.string.hint)
//                    .setMessage(R.string.record_camera_exit_dialog_message)
//                    .setNegativeButton(
//                            R.string.record_camera_cancel_dialog_yes,
//                            new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    mMediaObject.delete();
//                                    finish();
//                                }
//
//                            })
//                    .setPositiveButton(R.string.record_camera_cancel_dialog_no,
//                            null).setCancelable(false).show();
//            return;
//        }
//
//        if (mMediaObject != null)
//            mMediaObject.delete();
//        finish();
//    }
//
//    /**
//     * 停止录制
//     */
//    private void stopRecord() {
//        mPressedStatus = false;
//        if (mMediaRecorder != null) {
//            mMediaRecorder.stopRecord();
//        }
//        mHandler.removeMessages(HANDLE_STOP_RECORD);
//        checkStatus();
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (mHandler.hasMessages(HANDLE_STOP_RECORD)) {
//            mHandler.removeMessages(HANDLE_STOP_RECORD);
//        }
//        if (v.getId() == R.id.btn_start_camera) {
//            if (mMediaRecorder == null) {
//                return;
//            }
//            if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
//                return;
//            }
//            startRecord();
//        } else if (v.getId() == R.id.title_next) {
//            mMediaRecorder.startEncoding();
//        }
//    }
//
//    /**
//     * 取消回删
//     */
//    private boolean cancelDelete() {
//        if (mMediaObject != null) {
//            MediaObject.MediaPart part = mMediaObject.getCurrentPart();
//            if (part != null && part.remove) {
//                part.remove = false;
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void pauseRecorder() {
//        // 暂停
//        if (mPressedStatus) {
//            stopRecord();
//            if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
//                mTitleNext.performClick();
//            }
//        }
//    }
//
//
//    /**
//     * 检查录制时间，显示/隐藏下一步按钮
//     */
//
//    private int checkStatus() {
//        int duration = 0;
//        if (!isFinishing() && mMediaObject != null) {
//            duration = mMediaObject.getDuration();
//            if (duration < RECORD_TIME_MIN) {
//                // 视频必须大于3秒
//                if (mTitleNext.getVisibility() != View.INVISIBLE)
//                    mTitleNext.setVisibility(View.INVISIBLE);
//            } else {
//                // 下一步
//                if (mTitleNext.getVisibility() != View.VISIBLE) {
//                    mTitleNext.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//        return duration;
//    }
//
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case HANDLE_INVALIDATE_PROGRESS:
//                    if (mMediaRecorder != null && !isFinishing()) {
//                        if (mMediaObject != null && mMediaObject.getMedaParts() != null && mMediaObject.getDuration() >= RECORD_TIME_MAX) {
//                            stopRecord();
//                            mTitleNext.performClick();
//                            return;
//                        }
//                        if (mPressedStatus)
//                            sendEmptyMessageDelayed(0, 30);
//                    }
//                    break;
//            }
//        }
//    };
//
//    @Override
//    public void onEncodeStart() {
//        showProgress("", getString(R.string.record_camera_progress_message));
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
//        hideProgress();
//        Intent intent = null;
//        try {
//            intent = new Intent(this, Class.forName(getIntent().getStringExtra(OVER_ACTIVITY_NAME)));
//            intent.putExtra(MediaRecorderActivityBack.OUTPUT_DIRECTORY, mMediaObject.getOutputDirectory());
//            if (compressConfig != null) {
//                intent.putExtra(MediaRecorderActivityBack.VIDEO_URI, mMediaObject.getOutputTempTranscodingVideoPath());
//            } else {
//                intent.putExtra(MediaRecorderActivityBack.VIDEO_URI, mMediaObject.getOutputTempVideoPath());
//            }
//            intent.putExtra(MediaRecorderActivityBack.VIDEO_SCREENSHOT, mMediaObject.getOutputVideoThumbPath());
//            intent.putExtra("go_home", GO_HOME);
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            throw new IllegalArgumentException("需要传入录制完成后跳转的Activity的全类名");
//        }
//
//        finish();
//    }
//
//    /**
//     * 转码失败 检查sdcard是否可用，检查分块是否存在
//     */
//    @Override
//    public void onEncodeError() {
//        hideProgress();
//        Toast.makeText(this, R.string.record_video_transcoding_faild,
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
//        initSurfaceView();
//    }
//
//    public void onFinished() {
//        finish();
//    }
//
//    protected ProgressDialog mProgressDialog;
//
//    public ProgressDialog showProgress(String title, String message) {
//        return showProgress(title, message, -1);
//    }
//
//    public ProgressDialog showProgress(String title, String message, int theme) {
//        if (mProgressDialog == null) {
//            if (theme > 0)
//                mProgressDialog = new ProgressDialog(this, theme);
//            else
//                mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
//        }
//
//        if (!StringUtils.isEmpty(title))
//            mProgressDialog.setTitle(title);
//        mProgressDialog.setMessage(message);
//        mProgressDialog.show();
//        return mProgressDialog;
//    }
//
//    public void hideProgress() {
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        hideProgress();
//        mProgressDialog = null;
//    }
//}
