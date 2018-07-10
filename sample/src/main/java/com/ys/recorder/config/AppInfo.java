package com.ys.recorder.config;

import android.os.Environment;

public class AppInfo {

    /***
     * 上线模式 false
     * 测试模式 true
     */
    public static final boolean isDebug = false;

    public static final String BASE_LOCAL_URL = Environment.getExternalStorageDirectory().getPath() + "/call";
    public static final String BASE_VIDEO_PATH = BASE_LOCAL_URL + "/ysRecorder";

    public static final String CALL_PHONT_NUM = "CALL_PHONT_NUM";
    /***
     * 点击Menu按钮
     */
    public static final String MENU_CODE_BROADCAST = "MENU_CODE_BROADCAST";

}
