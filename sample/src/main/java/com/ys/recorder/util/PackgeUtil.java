package com.ys.recorder.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.ys.recorder.entity.AppInfomation;

import java.util.ArrayList;
import java.util.List;

public class PackgeUtil {

    /***
     * 获取手机中所有已安装的应用，并判断是否系统应用
     *
     * @param context
     * @return 非系统应用
     */
    public static void getPackage(Context context, PackageListener listener) {
        try {
            ArrayList<AppInfomation> appList = new ArrayList<AppInfomation>();
            List<PackageInfo> packages = context.getPackageManager()
                    .getInstalledPackages(0);
            for (int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = packages.get(i);
                AppInfomation tmpInfo = new AppInfomation();
                String name = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString(); //app名字
                String packageName = packageInfo.packageName;                                                //包名
                tmpInfo.appName = name;
                tmpInfo.packageName = packageName;
                tmpInfo.drawable = packageInfo.applicationInfo.loadIcon(context
                        .getPackageManager());
                MyLog.i("PackgeUtil", "====" + name + " /" + packageName + "  /");
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    MyLog.i("PackgeUtil", "====" + name + " /" + packageName + "  /");
                    appList.add(tmpInfo);
                }
                if (packageName.equals("com.android.camera2") || packageName.equals("com.android.server.telecom") ||
                        packageName.equals("com.adtv") || packageName.equals("com.android.mms") ||
                        packageName.equals("com.android.browser") || packageName.equals("com.android.settings") ||
                        packageName.equals("android.rk.RockVideoPlayer") || packageName.equals("com.android.contacts") ||
                        packageName.equals("com.android.gallery3d") || packageName.equals("com.android.providers.downloads.ui") ||
                        packageName.equals("com.android.music") || packageName.equals("com.android.rockchip")) {
                    MyLog.i("PackgeUtil", name + " /" + "======packageName=" + packageName);
                    appList.add(tmpInfo);
                }
            }
            listener.getSuccess(appList);
        } catch (Exception e) {
            String desc = e.toString();
            listener.getFail(desc);
        }
    }

    public interface PackageListener {
        void getSuccess(ArrayList<AppInfomation> appList);

        void getFail(String error);
    }

}
