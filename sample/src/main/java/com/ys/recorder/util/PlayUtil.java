package com.ys.recorder.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.mabeijianxi.smallvideo.R;

public class PlayUtil implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    Context context;
    MediaPlayer mp;

    public PlayUtil(Context context) {
        this.context = context;
        mp = new MediaPlayer();
    }

    public void playMusic() {
        try {
            mp.reset();
            mp = MediaPlayer.create(context, R.raw.notify_noice);//重新设置要播放的音频
            mp.start();//开始播放
            mp.setOnCompletionListener(this);
            mp.setOnErrorListener(this);
        } catch (Exception e) {
            e.printStackTrace();//输出异常信息
        }
    }

    public void stopMusic() {
        if (mp == null) {
            return;
        }
        if (mp.isPlaying()) {
            mp.stop();
        }
    }


    PlayMusicListener listener;

    public void setOnPlayMusicListener(PlayMusicListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e("music", "=====播放完毕====");
        if (listener != null) {
            listener.playCompletOver();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.e("music", "=====播放异常====" + i + " / " + i1);
        return false;
    }

    public interface PlayMusicListener {
        void playCompletOver();
    }


}
