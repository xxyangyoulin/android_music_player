package com.mnnyang.starmusic.util.image;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.interfaces.BlurBitmapTransformation;

/**
 * Created by mnnyang on 17-4-19.
 */

public class GlideUtils {
    public static void loadBlur(ImageView iv, String url) {
        Glide.with(Cache.getContext())
                .load(url)
                .bitmapTransform(new BlurBitmapTransformation(Cache.getContext()))
                .crossFade()
                .into(iv);
    }
}
