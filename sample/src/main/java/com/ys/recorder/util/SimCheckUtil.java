package com.ys.recorder.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by jsjm on 2018/6/22.
 */

public class SimCheckUtil {

    /***
     * 检测手机有咩有SIM卡
     * @param context
     * @return
     */
    public static boolean checkSimExict(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }


}
