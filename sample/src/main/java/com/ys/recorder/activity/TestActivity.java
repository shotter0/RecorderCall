package com.ys.recorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mabeijianxi.smallvideo.R;
import com.ys.recorder.service.ListenerService;
import com.ys.recorder.service.ScreenViewService;
import com.ys.recorder.util.FileUtil;
import com.ys.recorder.util.SharedPerManager;
import com.ys.recorder.view.WaitDialogUtil;

public class TestActivity extends BaseActivity {

    WaitDialogUtil waitDialogUtil;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
//        SharedPerManager.setPhonrNum("15917384131"); //客户测试电话
        SharedPerManager.setPhonrNum("110");
//        如果固件打包里面包含这个jar，就注释这一行代码
//        FileUtil.wroteSdCard(TestActivity.this, "/sdcard/android.jar");

        Intent intent = new Intent(TestActivity.this, ScreenViewService.class);
        startService(intent);
        Intent intentListener = new Intent(TestActivity.this, ListenerService.class);
        startService(intentListener);

        waitDialogUtil = new WaitDialogUtil(TestActivity.this);
        waitDialogUtil.show("Loading...");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                waitDialogUtil.dismiss();
                finish();
            }
        }, 2000);
    }
}
