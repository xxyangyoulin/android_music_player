package com.mnnyang.starmusic.util;

import android.text.format.DateUtils;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.app.Cache;

import java.util.Locale;

/**
 * Created by mnnyang on 17-4-22.
 */

public class TimeFormat {
    public static String format(long duration) {
        long minute = (duration / DateUtils.MINUTE_IN_MILLIS);
        long second = (duration / DateUtils.SECOND_IN_MILLIS % 60);
        String mm = String.format(Locale.getDefault(), "%02d", minute);
        String ss = String.format(Locale.getDefault(), "%02d", second);
        return String.format(Cache.getContext().getString(R.string.play_show_time), mm, ss);
    }
}
