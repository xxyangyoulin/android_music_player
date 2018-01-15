package com.mnnyang.starmusicapp.app;

/**
 * Created by mnnyang on 17-5-1.
 */

public enum PlayMode {
    LOOP(0), SINGLE(1), SHUFFLE(2);

    private int value;

    PlayMode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static PlayMode valueOf(int value) {
        switch (value) {
            case 1:
                return SINGLE;
            case 2:
                return SHUFFLE;
            case 0:
            default:
                return LOOP;
        }
    }
}
