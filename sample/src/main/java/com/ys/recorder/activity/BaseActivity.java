package com.ys.recorder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.ys.recorder.util.ActivityCollector;
import com.ys.recorder.util.FileUtil;
import com.ys.recorder.util.SharedPerManager;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FileUtil.creatDirPathNoExists();

        getScreenSize();
        ActivityCollector.addActivity(BaseActivity.this);
    }

    private void getScreenSize() {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int i = localDisplayMetrics.widthPixels;
        int j = localDisplayMetrics.heightPixels;
        SharedPerManager.setScreenWidth(i);
        SharedPerManager.setScreenHeight(j);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(BaseActivity.this);
    }
}
