package com.mnnyang.starmusic.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.mnnyang.starmusic.util.general.BitmapUtils;

public class BlurBitmapTransformation extends BitmapTransformation {

    public BlurBitmapTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap bitmap, int i, int i1) {
        return BitmapUtils.blur(bitmap, 50);
    }

    @Override
    public String getId() {
        return "ooBlur";
    }
}