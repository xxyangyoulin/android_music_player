package com.mnnyang.starmusic.view.view;

/**
 * Created by mnnyang on 17-4-30.
 */

public interface PlayBarState {
    enum State {
        CLOSE,OPEN
    }

    void closeBar();

    void openBar();

}
