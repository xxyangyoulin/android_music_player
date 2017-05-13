package com.mnnyang.starmusic.util.helper;

import android.view.View;

/**
 * Created by mnnyang on 17-4-19.
 */

public class StatusHelper {
    public enum Status {
        SUCCEED, LOADING, FAIL, NO_MORE
    }

    public static void status(View succeed, View loading, View fail, Status status) {
        switch (status) {
            case SUCCEED:
                if (loading != null)
                    loading.setVisibility(View.GONE);
                if (fail != null)
                    fail.setVisibility(View.GONE);
                if (succeed != null)
                    succeed.setVisibility(View.VISIBLE);
                break;
            case LOADING:
                if (succeed != null)
                    succeed.setVisibility(View.GONE);
                if (fail != null)
                    fail.setVisibility(View.GONE);
                if (loading != null)
                    loading.setVisibility(View.VISIBLE);
                break;
            case FAIL:
                if (succeed != null)
                    succeed.setVisibility(View.GONE);
                if (loading != null)
                    loading.setVisibility(View.GONE);
                if (fail != null)
                    fail.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
