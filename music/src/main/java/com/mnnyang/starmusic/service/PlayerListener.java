package com.mnnyang.starmusic.service;

import com.mnnyang.starmusic.bean.Music;

/**
 * 播放监听
 * Created by mnnyang on 17-4-12.
 */

public interface PlayerListener {
    void onStartPlay(Music currentMusic, int currentPlayingPosition);

    void onPlayPause();

    void onNext(Music music, int currentPlayingPosition);

    void onPrev(Music music, int currentPlayingPosition);

    void onResume(Music currentMusic);

    void progressTo(int duration);
}
