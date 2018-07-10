package com.ys.recorder.view;


import android.view.SurfaceView;
import android.widget.RelativeLayout;

public interface LauncherView {

    void showWaitDialog(boolean isShow);

    void showToast(String toast);

    SurfaceView getSurfaceView();

    RelativeLayout getRelaSurfaceView();


    /***
     * 视频转码失败，这里做其他操作
     */
    void onEncodeError();


}
