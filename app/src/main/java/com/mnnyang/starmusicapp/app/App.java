package com.mnnyang.starmusicapp.app;

import android.app.Application;

import com.mnnyang.starmusicapp.util.Preferences;
import com.mnnyang.starmusicapp.util.ToastUtil;

/**
 * Created by mnnyang on 18-1-15.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initUtils();
    }

    private void initUtils() {
        Preferences.init(this);
        ToastUtil.init(this);
    }
}
