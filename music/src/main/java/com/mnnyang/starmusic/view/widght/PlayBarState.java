package com.mnnyang.starmusic.view.widght;

/**
 * Created by mnnyang on 17-4-30.
 */

public interface PlayBarState {
    enum State {
        CLOSE,OPEN
    }

    void hideBar();

    void openBar();

}
