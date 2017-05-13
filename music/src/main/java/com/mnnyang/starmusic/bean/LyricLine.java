package com.mnnyang.starmusic.bean;

/**
 * 一行歌词
 * Created by mnnyang on 17-4-6.
 */

public class LyricLine {
    private int startShowPosition;
    private String text;

    public LyricLine() {
    }

    public LyricLine(int time, String text) {
        this.startShowPosition = time;
        this.text = text;
    }

    public int getStartShowPosition() {
        return startShowPosition;
    }

    public LyricLine setStartShowPosition(int startShowPosition) {
        this.startShowPosition = startShowPosition;
        return this;
    }

    public String getText() {
        return text;
    }

    public LyricLine setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String toString() {
        return startShowPosition + "---" + text;
    }
}
