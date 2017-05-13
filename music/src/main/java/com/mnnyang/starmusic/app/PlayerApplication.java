package com.mnnyang.starmusic.app;

import android.app.Application;

import com.mnnyang.starmusic.util.http.HttpInterceptor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by mnnyang on 17-4-11.
 */

public class PlayerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Cache.init(this.getApplicationContext());

        initOkHttpUtils();
    }

    private void initOkHttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .addInterceptor(new LoggerInterceptor("okhttp-oo", true))
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}
