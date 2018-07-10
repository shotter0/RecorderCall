package com.ys.recorder.util;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ViewSizeChange {


    public static void changeSurfaceBig(RelativeLayout surfaceView) {
        int width = SharedPerManager.getScreenWidth();
        int viewWidth = width - (width / 5);
        int viewHeight = viewWidth * 9 / 16;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
        params.width = viewWidth;
        params.height = viewHeight;
        int leftMargin = (width - viewWidth) / 2;
        params.topMargin = viewWidth / 4;
        params.leftMargin = leftMargin;
        surfaceView.setLayoutParams(params);
    }

    public static void changeSurfaceSmall(RelativeLayout surfaceView) {
        int height = SharedPerManager.getScreenHeight();
//        int viewSize = 150;
        int viewSize = 80;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
        params.width = viewSize;
        params.height = viewSize;
        params.topMargin = height - viewSize;
        params.leftMargin = 0;
        surfaceView.setLayoutParams(params);
    }


    public static void changeLauncherView(LinearLayout linera) {
        int width = SharedPerManager.getScreenWidth();
        int height = SharedPerManager.getScreenHeight();
        int minSize = (Math.min(width, height)) / 2;
        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) linera.getLayoutParams();
        localLayoutParams.width = minSize;
        localLayoutParams.height = minSize;
        linera.setLayoutParams(localLayoutParams);
    }


    public static void setBtnViewSize(Button btn_call_police) {
        int height = SharedPerManager.getScreenHeight();
        int topMatgin = (height / 2) + 100;
        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) btn_call_police.getLayoutParams();
        localLayoutParams.topMargin = topMatgin;
        btn_call_police.setLayoutParams(localLayoutParams);
    }
}
