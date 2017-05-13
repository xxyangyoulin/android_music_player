package com.mnnyang.starmusic.interfaces;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.api.Constants;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.util.binding.Binder;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.Preferences;

/**
 * <p>Activity 基类</p>
 * Created by mnnyang on 17-4-11.
 */

public abstract class BaseActivity extends AppCompatActivity implements Operation {

    protected String TAG = getClass().getSimpleName();
    protected Toolbar toolbar;

    /**
     * 请求base不要执行任何初始化 <br>
     * super.onCreate()之前调用有效
     */
    protected BaseActivity requestNotInit() {
        this.rmBaseInit = true;
        return this;
    }

    private boolean rmBaseInit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (rmBaseInit) {
            return;
        }
        initWindow();

        Binder.bind(this);
        initView();
        initListener();
        initData();

        Cache.addActivity(this);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        LogUtils.i(TAG, "onCreate");
    }

    public void initNightMode() {
        if (Preferences.getBoolean(Constants.KEY_MODE_NIGHT_YES, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * Binder.bind(this);之前<br>
     */
    protected void initWindow() {
    }


    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            LogUtils.i(this, "have toolbar");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //toolbar home按钮返回
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        Cache.rmActivity(this);
        super.onDestroy();
        LogUtils.i(TAG, "onDestroy");
    }

    /**
     * 启动Activity
     */
    public void gotoActivity(Class<? extends BaseActivity> activityClass, String name, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(this, activityClass);
        if (TextUtils.isEmpty(name) && bundle != null) {
            intent.putExtra(name, bundle);
        }
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }
}
