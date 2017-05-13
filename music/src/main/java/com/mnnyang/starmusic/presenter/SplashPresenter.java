package com.mnnyang.starmusic.presenter;

import android.graphics.Bitmap;

import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.model.SplashModel;
import com.mnnyang.starmusic.model.interfaces.ISplashModel;
import com.mnnyang.starmusic.presenter.interfaces.ISplashPresenter;
import com.mnnyang.starmusic.util.MusicScanUtils;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.activity.MainActivity;
import com.mnnyang.starmusic.activity.SplashActivity;
import com.mnnyang.starmusic.interfaces.ISplashView;

/**
 * Created by mnnyang on 17-4-12.
 */

public class SplashPresenter implements ISplashPresenter {

    /**
     * 延时进入主界面的时间
     */
    private static final long DELAYED_TIME = 500;
    private ISplashView view;
    private ISplashModel model;
    private long startTime;


    public SplashPresenter(ISplashView view) {
        this.view = view;
        model = new SplashModel();
    }

    @Override
    public void initSplash() {
        //TODO 判断是否直接进入首页
        if (Cache.getPlayService() != null) {
//        if (true) {
            gotoMainActivity();
            return;
        }
        startTime = System.currentTimeMillis();
        startService();
        Bitmap splashBitmap = model.getSplash();
        view.showSplash(splashBitmap);
        //进入之后扫描音乐
        new MusicScanUtils().scanLocalMusic(Cache.getMusicList(), new MusicScanUtils.CompleteListener() {
            @Override
            public void onComplete() {
                long endTime = System.currentTimeMillis();
                LogUtils.i(this, "endTime - startTime" + (endTime - startTime));
                if ((endTime - startTime) > DELAYED_TIME) {
                    LogUtils.i(SplashPresenter.class, "直接进入");
                    gotoMainActivity();
                } else {
                    LogUtils.i(SplashPresenter.class, "等待进入");
                    Cache.newInstance().handler.postDelayed(runnable, DELAYED_TIME - (endTime - startTime));
                }

            }
        });
    }

    private void startService() {
        ((SplashActivity) view).launchService();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            gotoMainActivity();
        }
    };

    private void gotoMainActivity() {
        ((SplashActivity) view).gotoActivity(MainActivity.class, null, null, true);
    }
}
