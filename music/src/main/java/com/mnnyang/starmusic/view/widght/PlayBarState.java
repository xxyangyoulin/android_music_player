package com.mnnyang.starmusic.view.widght;

/**
 * Created by mnnyang on 17-4-30.
 */

public interface PlayBarState {
    enum State {
        CLOSE,OPEN
    }

    /**
     * 隐藏播放条
     */
    void hideBar();

    /**
     * 显示播放条
     */
    void showBar();

}
