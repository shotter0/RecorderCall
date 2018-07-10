package com.ys.recorder.util;

public class Biantai {
    private static long lastClickTime;

    public static boolean isThreeClick() {
        long l1 = System.currentTimeMillis();
        long l2 = l1 - lastClickTime;
        if ((0L < l2) && (l2 < 3000L)) {
            return true;
        }
        lastClickTime = l1;
        return false;
    }

    public static boolean isTwoClick() {
        long l1 = System.currentTimeMillis();
        long l2 = l1 - lastClickTime;
        if ((0L < l2) && (l2 < 2000L)) {
            return true;
        }
        lastClickTime = l1;
        return false;
    }

    public static boolean isOneClick() {
        long l1 = System.currentTimeMillis();
        long l2 = l1 - lastClickTime;
        if ((0L < l2) && (l2 < 1000L)) {
            return true;
        }
        lastClickTime = l1;
        return false;
    }
}
