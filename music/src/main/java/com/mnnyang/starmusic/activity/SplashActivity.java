package com.mnnyang.starmusic.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.view.base.ISplashView;
import com.mnnyang.starmusic.presenter.SplashPresenter;
import com.mnnyang.starmusic.service.PlayService;
import com.mnnyang.starmusic.util.binding.BindLayout;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.util.general.ScreenUtils;

/**
 * 开屏页
 */
@BindLayout(R.layout.activity_splash)
public class SplashActivity extends BaseActivity implements ISplashView {

    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    @Override
    protected void initWindow() {
        ScreenUtils.hideStatusBar(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        new SplashPresenter(this).initSplash();
    }

    @Override
    public void showSplash(Bitmap bitmap) {
        ivSplash.setImageBitmap(bitmap);
    }

    public void launchService() {
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);
    }

}
