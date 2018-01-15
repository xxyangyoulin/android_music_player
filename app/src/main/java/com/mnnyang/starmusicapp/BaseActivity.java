package com.mnnyang.starmusicapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mnnyang.starmusicapp.helper.Cache;

import butterknife.ButterKnife;

/**
 * Created by mnnyang on 18-1-15.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public static String TAG = "BaseActivity";

    protected void beforeInit(Bundle state) {
        //do nothing
    }

    protected abstract int getContentLayout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        Cache.addActivity(this);

        beforeInit(savedInstanceState);

        setContentView(getContentLayout());

        ButterKnife.bind(this);
        initView(savedInstanceState);
    }

    protected abstract void initView(Bundle state);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cache.rmActivity(this);
    }
}
