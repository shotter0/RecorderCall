package com.ys.recorder.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mabeijianxi.smallvideo.R;
import com.ys.recorder.config.AppInfo;
import com.ys.recorder.pasener.LaunchParsener;
import com.ys.recorder.util.Biantai;
import com.ys.recorder.util.PhoneUtils;
import com.ys.recorder.util.PlayUtil;
import com.ys.recorder.util.SharedPerManager;
import com.ys.recorder.util.SimCheckUtil;
import com.ys.recorder.util.TimerTaskUtil;
import com.ys.recorder.util.ViewSizeChange;
import com.ys.recorder.view.LauncherView;
import com.ys.recorder.view.MyToastView;

/***
 * 录像悬浮窗
 * 1：屏幕变小，开始录像
 * 2：开始打电话，停止录像，压缩时视频
 */

public class ScreenViewService extends Service implements LauncherView {
    private static final String TAG = "ScreenViewService";
    private View view;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWmParams;
    PlayUtil playUtil;
    TimerTaskUtil timerTaskUtil;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(AppInfo.MENU_CODE_BROADCAST)) {
                Log.i(TAG, "=======接收到广播========MENU_CODE_BROADCAST=");
                toDoPlayMusicCallPhone();
            } else if (action.equals("android.intent.action.PHONE_STATE")) { //电话状态发生改变
                Log.i(TAG, "=======接收到广播========PHONE_STATE=");
            }
        }
    };

    private static final int STATE_PLAY_DEFAULT = -1;  //默认状态
    private static final int STATE_PLAY_CALL_MUSIC = 0;  //播放音乐
    private static final int STATE_PLAY_CALL_POLICE = 1; //拨打电话
    private static final int STATE_PLAY_DIS_CALL_POLICE = 2; //挂断电话
    int receiverBroadNum = STATE_PLAY_DEFAULT;               //当前外接控件的状态

    private void toDoPlayMusicCallPhone() {
        if (Biantai.isOneClick()) {
            Log.i(TAG, "=======点击太快了=========");
            return;
        }
        if (launcherNParsener == null) {
            Log.i(TAG, "=======launcherNParsener==null=========");
            return;
        }
        if (!launcherNParsener.viewState()) { //如果显示的是小介面放大
            changeViewState(true);
        }
        receiverBroadNum++;
        if (receiverBroadNum == STATE_PLAY_CALL_MUSIC) { //去播放音乐
            Log.i(TAG, "=======状态去播放音乐=========");
            playUtil.playMusic();
            btn_call_police.setText("确认报警，请再按一次");
        } else if (receiverBroadNum == STATE_PLAY_CALL_POLICE) { //去拨打电话
            Log.i(TAG, "=======状态去拨打电话=========" + SharedPerManager.getPhonrNum());
            btn_call_police.setText("正在拨打 : 110 ");
            callPhone();
        } else if (receiverBroadNum == STATE_PLAY_DIS_CALL_POLICE) {
            Log.i(TAG, "=======状态挂断电话=========");
            cacelPhone();
            receiverBroadNum = STATE_PLAY_DEFAULT;
        }
    }

    /***
     * 拨打电话
     */
    private void callPhone() {
        playUtil.stopMusic();
        boolean isSimExict = SimCheckUtil.checkSimExict(getBaseContext());
        if (!isSimExict) {
            btn_call_police.setText("当前未检测到SIM卡,请检查");
            MyToastView.getInstance().Toast(getBaseContext(), "当前未检测到SIM卡,请检查");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + SharedPerManager.getPhonrNum()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /***
     * 挂断电话
     */
    public void cacelPhone() {
        try {
            Log.i(TAG, "====挂断电话，缩放屏幕");
            boolean isPnoneState = isTelephonyCalling();
            if (isPnoneState) {
                PhoneUtils.endCall();
            }
            changeViewState(false);
        } catch (Exception e) {
        }
    }


    public void onCreate() {
        super.onCreate();
        Log.e("llll", "==============service起来了=======");
        initOther();
        createFloatView();
        initReceiver();
    }

    private void initOther() {
        timerTaskUtil = new TimerTaskUtil();
        playUtil = new PlayUtil(getBaseContext());
    }

    private void createFloatView() {
        mWmParams = new WindowManager.LayoutParams();
        mWindowManager = ((WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE));
        mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mWmParams.format = WindowManager.LayoutParams.LAYOUT_CHANGED;
        mWmParams.flags = WindowManager.LayoutParams.FORMAT_CHANGED;
        mWmParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mWmParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        view = LayoutInflater.from(getApplication()).inflate(R.layout.view_camera_pop, null);
        initView(view);
        mWindowManager.addView(view, mWmParams);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    SurfaceView record_preview;
    LaunchParsener launcherNParsener;
    RelativeLayout rela_surface;
    RelativeLayout rela_surface_bgg;
    Button btn_call_police;

    private void initView(View view) {
        btn_call_police = (Button) view.findViewById(R.id.btn_call_police);
        ViewSizeChange.setBtnViewSize(btn_call_police);
        rela_surface_bgg = (RelativeLayout) view.findViewById(R.id.rela_surface_bgg);
        rela_surface = (RelativeLayout) view.findViewById(R.id.rela_surface);
        record_preview = (SurfaceView) view.findViewById(R.id.record_preview);
        launcherNParsener = new LaunchParsener(this);
        launcherNParsener.onResume();

        record_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppInfo.MENU_CODE_BROADCAST);
                sendBroadcast(intent);
            }
        });


    }

    @Nullable
    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    @Override
    public void showWaitDialog(boolean isShow) {

    }

    @Override
    public void showToast(String toast) {

    }

    @Override
    public SurfaceView getSurfaceView() {
        return record_preview;
    }

    @Override
    public RelativeLayout getRelaSurfaceView() {
        return rela_surface;
    }

    @Override
    public void onEncodeError() {

    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppInfo.MENU_CODE_BROADCAST);
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (playUtil != null) {
            playUtil.stopMusic();
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (timerTaskUtil != null) {
            timerTaskUtil.cacelTimer();
        }
    }

    /**
     * 改变录像框大小
     * true 大
     * false 小
     */
    private void changeViewState(boolean isChangeState) {
        if (launcherNParsener == null) {
            launcherNParsener = new LaunchParsener(this);
        }
        boolean isViewState = launcherNParsener.viewState();
        if (isChangeState) {
            if (!isViewState) {
                launcherNParsener.changeSurfaceBigView();
            }
            rela_surface_bgg.setVisibility(View.VISIBLE);
            startChangeViewStateTimer();
        } else {  //改小
            rela_surface_bgg.setVisibility(View.GONE);
            if (isViewState) {
                launcherNParsener.changeSurfaceSmalliew();
            }
            receiverBroadNum = STATE_PLAY_DEFAULT;
            if (playUtil != null) {
                playUtil.stopMusic();
            }
        }
    }

    /***
     * 屏幕放大，20秒监听，如果当前不在通话中，就缩放屏幕
     */
    private void startChangeViewStateTimer() {
        timerTaskUtil.startTimer(TimerTaskUtil.CHECK_PHONE_STATE, new TimerTaskUtil.TimerTaskListener() {
            @Override
            public void timeIsOn() {
                Log.i(TAG, "============时间到了，缩小屏幕");
                boolean isPhoneOn = isTelephonyCalling();
                if (isPhoneOn) { //如果正在打电话 ，就重新记时
                    Log.i(TAG, "============当前在通话中，重新及时间===");
                    startChangeViewStateTimer();
                    return;
                }
                if (launcherNParsener.viewState()) { //如果是大屏幕的话，就缩小
                    changeViewState(false);
                }
            }
        });
    }

    public boolean isTelephonyCalling() {
        boolean calling = false;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (TelephonyManager.CALL_STATE_OFFHOOK == telephonyManager.getCallState() || TelephonyManager.CALL_STATE_RINGING == telephonyManager.getCallState()) {
            calling = true;
        }
        return calling;
    }
}