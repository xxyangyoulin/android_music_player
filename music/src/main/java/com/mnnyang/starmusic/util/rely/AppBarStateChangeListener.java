package com.mnnyang.starmusic.util.rely;

import android.support.design.widget.AppBarLayout;

/**
 * appBarLayout 折叠状态监听封装
 */
public abstract class AppBarStateChangeListener
        implements AppBarLayout.OnOffsetChangedListener {

    /**
     * 展开
     */
    public static final int EXPANDED = 1;
    /**
     * 折叠
     */
    public static final int COLLAPSED = 2;

    public static final int IDLE = 3;

    private int mCurrentState = IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != EXPANDED) {
                onStateChanged(appBarLayout, EXPANDED);
            }
            mCurrentState = EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != COLLAPSED) {
                onStateChanged(appBarLayout, COLLAPSED);
            }
            mCurrentState = COLLAPSED;
        } else {
            if (mCurrentState != IDLE) {
                onStateChanged(appBarLayout,IDLE);
            }
            mCurrentState = IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, int state);
}