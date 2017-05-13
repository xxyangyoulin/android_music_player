package com.mnnyang.starmusic.model.interfaces;

import android.graphics.Bitmap;

/**
 * Created by mnnyang on 17-4-12.
 */

public interface ISplashModel {
    Bitmap getSplash();
    void checkSplash();
    void updateSplash(String url);

}
