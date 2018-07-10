package com.ys.recorder.util;

import android.util.Log;

import java.io.DataOutputStream;

public class RootCmd {

    /***
     * @param command
     * @return
     */
    public static boolean exusecmd(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            Log.e("cdl", "======000==writeSuccess======");
            process.waitFor();
        } catch (Exception e) {
            Log.e("cdl", "======111=writeError======" + e.toString());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

//     RootCmd.writeFileToSystem(srcfile.getPath(), "/system/media/bootanimation.zip");
    public static void writeFileToSystem(String filePath, String sysFilePath) {
        exusecmd("mount -o rw,remount /system");
        exusecmd("rm -rf /system/media/boomAnimal.zip");
        exusecmd("chmod 777 /system/media");
        exusecmd("cp  " + filePath + " " + sysFilePath);
    }


}
