package com.mnnyang.starmusic.view.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 跑马灯<br>
 * Created by mnnyang on 17-4-19.
 */

public class MaTextView extends TextView {
    public MaTextView(Context context) {
        super(context);
    }

    public MaTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
