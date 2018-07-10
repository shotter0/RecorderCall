package com.ys.recorder.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mabeijianxi.smallvideo.R;
import com.ys.recorder.adapter.AppInfosAdapter;
import com.ys.recorder.entity.AppInfomation;
import com.ys.recorder.util.DisPlayUtil;
import com.ys.recorder.util.PackgeUtil;
import com.ys.recorder.view.MyToastView;

import java.util.ArrayList;

public class AppAlreadActivity extends Activity {
    private AppInfosAdapter adapter;
    ArrayList<AppInfomation> appList = new ArrayList();
    private GridView gridView;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            getData();
        }
    };

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_alread_app);
        initView();
        regReceiver();
    }

    private void initView() {
        appList = new ArrayList();
        gridView = (GridView) findViewById(R.id.my_app_gridview);
        gridView.setNumColumns(4);
        adapter = new AppInfosAdapter(this, appList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(onItemClickListener);
    }

    private void getData() {
        appList.clear();
        PackgeUtil.getPackage(this, new PackgeUtil.PackageListener() {

            @Override
            public void getSuccess(ArrayList<AppInfomation> lists) {
                appList = lists;
                adapter.setAppInfos(appList);
                adapter.notifyDataSetChanged();
            }

            public void getFail(String paramAnonymousString) {
                MyToastView.getInstance().Toast(AppAlreadActivity.this, "获取已安装失败");
            }
        });
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View view, int position, long paramAnonymousLong) {
            AppInfomation appInfomation = appList.get(position);
            String packName = appInfomation.getPackageName();
            DisPlayUtil.startApp(AppAlreadActivity.this, packName);
        }
    };

    private void regReceiver() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        localIntentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        localIntentFilter.addAction("android.intent.action.PACKAGE_NEEDS_VERIFICATION");
        localIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        localIntentFilter.addDataScheme("package");
        registerReceiver(receiver, localIntentFilter);
    }

    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        getData();
    }
}
