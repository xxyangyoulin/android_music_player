package com.mnnyang.starmusic.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.api.Constants;
import com.mnnyang.starmusic.api.PlayMode;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.util.SystemUtils;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.Preferences;
import com.mnnyang.starmusic.util.general.ToastUtils;

import java.util.List;
import java.util.Random;

import static com.mnnyang.starmusic.api.Constants.CODE_EXIT;
import static com.mnnyang.starmusic.api.Constants.INTENT_NOTIFINATION;
import static com.mnnyang.starmusic.api.Constants.KEY_PLAY_MDOE;

/**
 * 后台播放服务
 * Created by mnnyang on 17-4-11.
 */

public class PlayService extends Service {

    private static final int PROGRESS_UPDATE_DELAY_MILLIS = 100;
    private Music nextPlayMusic;
    private List<Music> musicList;
    private PlayerListener playerListener;
    private int currentPlayingPosition;
    private Music currentMusic;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler handler = new Handler(Looper.getMainLooper());

    private boolean isPreparing;
    private boolean isPausing;

    @Override
    public void onCreate() {
        super.onCreate();
        Cache.setPlayService(this);
        musicList = Cache.getMusicList();
        setMediaPlayerListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        parseIntent(intent);
        LogUtils.i(this, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 解析意图
     */
    private void parseIntent(Intent intent) {
        if (intent == null) return;

        LogUtils.i(this, "解析-----");
        int notifyCode = intent.getIntExtra(INTENT_NOTIFINATION, -1);
        switch (notifyCode) {
            case Constants.CODE_PLAY_OR_PAUSE:
                resumeOrPause();
                break;
            case Constants.CODE_NEXT:
                next();
                break;
            case CODE_EXIT:
                SystemUtils.exit();
                break;
        }
    }

    /**
     * 设置监听
     */
    private void setMediaPlayerListener() {
        mediaPlayer.setOnPreparedListener(onPreparedListener);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
    }

    /**
     * 根据id更新当前播放的position currentMusic<br>
     *
     * @param id
     */
    public int updateCurrentPlayingPosition(long id) {
        if (id == -1) {
            return 0;
        }
        for (int i = 0; i < musicList.size(); i++) {
            if (id == musicList.get(i).getId()) {
                currentPlayingPosition = i;
                currentMusic = musicList.get(i);
                LogUtils.d(this, "title:" + currentMusic.getTitle() + "pos" + i);
                return i;
            }
        }

        return 0;
    }

    /**
     * 保存当前播放歌曲的id
     */
    private void saveCurrentPlayingLocalMusicId(long id) {
        Preferences.putLong(Constants.KEY_PLAYING_ID, id);
    }

    public void play(Music music) {
        if (music == null) {
            LogUtils.w(this, "play() music is null");
            return;
        }
//        pause();
        reset();

        try {
            mediaPlayer.setDataSource(music.getPath());
            isPreparing = true;
            mediaPlayer.prepareAsync();

            if (music.getType() == Music.Type.ONLINE) {
                currentMusic = music;
            } else {
                updateCurrentPlayingPosition(music.getId());
                saveCurrentPlayingLocalMusicId(currentMusic.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放当前列表的指定条目
     */
    private void play(int position) {
        if (position > musicList.size() - 1) {
            position = 0;
        } else if (position < 0) {
            position = musicList.size() - 1;
        }

        Music music = musicList.get(position);
        play(music);
    }

    private void start() {
        LogUtils.d(this, "start");
        mediaPlayer.start();
        isPausing = false;
        if (playerListener != null) {
            playerListener.onStartPlay(currentMusic, currentPlayingPosition);
            handler.post(progressRunnable);
        }

        notification(true);
    }


    public void pause() {
        if (mediaPlayer.isPlaying()) {
            isPausing = true;
            LogUtils.d(this, "pause");
            mediaPlayer.pause();
            if (playerListener != null) {
                playerListener.onPlayPause();
            }
        }
        removeProgressUpdate();

        notification(false);
    }

    private void removeProgressUpdate() {
        handler.removeCallbacks(progressRunnable);
    }

    public void seekTo(int duration) {
        if (mediaPlayer.isPlaying() || isPausing) {
            if (!isPreparing && currentMusic != null) {
                mediaPlayer.seekTo(duration);
            }
        }
    }

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (playerListener != null && mediaPlayer.isPlaying()) {
                playerListener.progressTo(mediaPlayer.getCurrentPosition());
            }
            handler.postDelayed(this, PROGRESS_UPDATE_DELAY_MILLIS);
        }
    };

    /**
     * 释放 mMediaPlayer
     */
    private void reset() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
    }

    MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            isPreparing = false;
            start();
        }
    };
    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            isPausing = true;
            pause();

            if (playerListener != null) {
                playerListener.progressTo(0);
            }
            PlayService.this.next();
        }
    };

    /**
     * 下一曲操作
     */
    public void next() {
        //预定下一首
        if (nextPlayMusic != null) {
            play(nextPlayMusic);
            nextPlayMusic = null;
            return;
        }

        if (musicList.isEmpty()) {
            ToastUtils.show(getString(R.string.current_list_is_empty));
            return;
        }

        PlayMode mode = PlayMode.valueOf(Preferences.getInt(KEY_PLAY_MDOE, 0));
        switch (mode) {
            case LOOP:
                currentPlayingPosition++;
                break;
            case SINGLE:
                break;
            case SHUFFLE:
                currentPlayingPosition = new Random().nextInt(musicList.size());
                break;
        }
        play(currentPlayingPosition);

        if (playerListener != null) {
            playerListener.onNext(currentMusic, currentPlayingPosition);
        }
    }

    public void prev() {
        LogUtils.d(this, "prev");

        if (musicList.isEmpty()) {
            ToastUtils.show("播放列表没有歌曲");
            return;
        }

        PlayMode mode = PlayMode.valueOf(Preferences.getInt(KEY_PLAY_MDOE, 0));
        switch (mode) {
            case LOOP:
                currentPlayingPosition--;
                break;
            case SINGLE:
                break;
            case SHUFFLE:
                currentPlayingPosition = new Random().nextInt(musicList.size());
                break;
        }

        play(currentPlayingPosition);

        if (playerListener != null) {
            playerListener.onPrev(currentMusic, currentPlayingPosition);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayerBinder();
    }

    /**
     * 播放暂停切换
     */
    public void resumeOrPause() {
        LogUtils.i(this, mediaPlayer.getCurrentPosition() + "");
        if (mediaPlayer.isPlaying()) {
            pause();
            return;
        }
        if (isPausing && currentMusic != null) {
            start();
            return;
        }
        if (currentMusic != null) {
            play(currentMusic);

        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public Music getPlayingMusic() {
        return currentMusic;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Cache.setPlayService(null);
        LogUtils.i(this, "onDestroy");
    }

    /**
     * 关闭服务
     */
    public void exit() {
        pause();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        stopSelf();
        Cache.setPlayService(null);
    }

    public void nextPlay(Music music) {
        nextPlayMusic = music;
    }

    private class PlayerBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    /**
     * 发送通知
     */
    private void notification(boolean playing) {
        if (currentMusic == null) {
            LogUtils.d(this, ":音乐为空, 不发通知");
            return;
        }
        Notification notification = SystemUtils.buildPlayBarNotification(this, currentMusic, Constants.NOTIFICATION_FLOG, playing);
        startForeground(Constants.NOTIFICATION_ID, notification);
    }

    public PlayService setPlayerListener(PlayerListener playerListener) {
        this.playerListener = playerListener;
        return this;
    }



}
