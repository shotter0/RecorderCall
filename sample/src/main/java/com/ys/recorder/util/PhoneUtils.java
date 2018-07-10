package com.ys.recorder.util;

import android.content.Context;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

/**
 * Created by jsjm on 2018/6/23.
 */

public class PhoneUtils {

    //挂断电话
    public static void endCall() {
        try {
            Class<?> clazz = Class.forName("android.os.ServiceManager");
            Method method = clazz.getMethod("getService", String.class);
            IBinder ibinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
