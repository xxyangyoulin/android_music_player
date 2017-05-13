package com.mnnyang.starmusic.util.general;

/**
 * 颜色处理工具<br>
 * Created by mnnyang on 17-4-8.
 */

public class ColorUtils {
    /**
     * 根据百分比 计算两个颜色之间的过度颜色
     *
     * @param fraction   百分比
     * @param startValue 起始值
     * @param endValue   结束值
     * @return
     */
    public static Object evaluateColor(float fraction, Object startValue,
                                       Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }
}
