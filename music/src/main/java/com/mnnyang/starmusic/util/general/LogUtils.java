package com.mnnyang.starmusic.util.general;

import android.util.Log;

/**
 * 日志工具<br>
 * Created by mnnyang on 17-4-8.
 */

public class LogUtils {

    public static boolean debug = true; //TODO 修改调试模式
    public static final String 前缀 = "mnnyang----->";
    public static final String 后缀 = ":::::";

    //信息级别
    public static void i(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.i(newTag, msg);
    }

    //debug级别
    public static void d(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.d(newTag, msg);
    }

    //警告级别
    public static void w(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.w(newTag, msg);
    }

    //详细
    public static void v(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.v(newTag, msg);
    }

    //错误级别
    public static void e(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.e(newTag, msg);
    }

    private static String getNewTag(Object tag) {
        String newTag = "";

        if (tag instanceof String) {
            newTag = (String) tag;
        } else if (tag instanceof Class) {
            newTag = ((Class) tag).getSimpleName();
        } else {
            newTag = tag.getClass().getSimpleName();
        }
        return 前缀+newTag+后缀;
    }
}
