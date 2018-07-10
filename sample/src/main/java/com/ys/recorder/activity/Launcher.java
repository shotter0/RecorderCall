package com.ys.recorder.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mabeijianxi.smallvideo.R;
import com.ys.recorder.service.ScreenViewService;
import com.ys.recorder.util.DisPlayUtil;
import com.ys.recorder.util.ViewSizeChange;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Launcher extends BaseActivity implements View.OnClickListener {
    private TextView tv_time;
    private Button btn_program, btn_setting, btn_file, btn_explorer;
    private LinearLayout linera;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.TIME_TICK")) {
                updateTimeShow();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initView();
        initReceiver();
    }

    private void initReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.TIME_TICK");
        registerReceiver(receiver, localIntentFilter);
    }

    private void initView() {
        linera = (LinearLayout) findViewById(R.id.linera);
        ViewSizeChange.changeLauncherView(linera);
        btn_program = (Button) findViewById(R.id.btn_program);
        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_file = (Button) findViewById(R.id.btn_file);
        btn_explorer = (Button) findViewById(R.id.btn_explorer);
        tv_time = (TextView) findViewById(R.id.tv_time);
        btn_explorer.setOnClickListener(this);
        btn_program.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        btn_file.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_program:
//                Intent intent = new Intent(Launcher.this, AppAlreadActivity.class);
//                startActivity(intent);

//                Intent intent = new Intent(Launcher.this, MediaRecorderActivity.class);
//                startActivity(intent);


                Intent intent = new Intent(Launcher.this, ScreenViewService.class);
                startService(intent);

                break;
            case R.id.btn_setting:
                DisPlayUtil.startApp(Launcher.this, "com.android.settings");
                break;
            case R.id.btn_file:
                DisPlayUtil.startApp(Launcher.this, "com.android.rockchip");
                break;
            case R.id.btn_explorer:
                DisPlayUtil.startApp(Launcher.this, "com.android.browser");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTimeShow();
    }

    private void updateTimeShow() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        tv_time.setText(simpleDateFormat.format(date));
    }

}
