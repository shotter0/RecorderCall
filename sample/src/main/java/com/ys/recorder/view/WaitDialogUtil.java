package com.ys.recorder.view;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.mabeijianxi.smallvideo.R;

import java.util.Timer;
import java.util.TimerTask;


public class WaitDialogUtil {

    Dialog waitDialog;
    Context context;
    public static final String TAG = WaitDialogUtil.class.getName();
    private final TextView mTv;

    public WaitDialogUtil(Context context) {
        this.context = context;
        waitDialog = new Dialog(context, R.style.MyDialog);
        View recdialog = View.inflate(context, R.layout.dialog_wait, null);
        mTv = (TextView) recdialog.findViewById(R.id.tv_dialog_wait);
        waitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        waitDialog.setContentView(recdialog);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.setCancelable(true);
        waitDialog.getWindow().setGravity(Gravity.CENTER);
    }

    public void show(String text_dialog) {
        dismiss();
        mTv.setText(text_dialog);
        waitDialog.show();
    }

    public void dismiss() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }
    }

    public boolean isShowing() {
        if (waitDialog != null && waitDialog.isShowing()) {
            return true;
        }
        return false;
    }

    public void show(String text_dialog, int time) {
        mTv.setText(text_dialog);
        waitDialog.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                waitDialog.dismiss();
                t.cancel();
            }
        }, time);
    }

}
