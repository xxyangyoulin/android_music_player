package com.mnnyang.starmusic.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.mnnyang.starmusic.util.general.BitmapUtils;

/**
 * Created by mnnyang on 17-4-19.
 */

public class CirCleBitmapTransformation extends BitmapTransformation {

    public CirCleBitmapTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap bitmap, int i, int i1) {
        return BitmapUtils.createCircleImage(bitmap);
    }

    @Override
    public String getId() {
        return "ooCircle";
    }
}