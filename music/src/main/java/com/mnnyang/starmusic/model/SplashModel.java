package com.mnnyang.starmusic.model;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.api.Api;
import com.mnnyang.starmusic.api.Constants;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Splash;
import com.mnnyang.starmusic.model.interfaces.ISplashModel;
import com.mnnyang.starmusic.util.general.FileUtils;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.Preferences;
import com.mnnyang.starmusic.util.general.ScreenUtils;
import com.mnnyang.starmusic.util.image.ImageResizer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

/**
 * SplashModel
 * Created by mnnyang on 17-4-12.
 */

public class SplashModel implements ISplashModel {
    @Override
    public Bitmap getSplash() {
        Bitmap bitmap = ImageResizer.decodeSampledBitmapFromFile(
                FileUtils.getSplashDir(Cache.getContext()).getPath() + File.separator + Constants.SPLASH_NAME,
                ScreenUtils.getSWidth(), ScreenUtils.getSHeight());

        if (bitmap == null) {
            bitmap = ImageResizer.decodeSampledBitmapFromResources(
                    Cache.getContext().getResources(),
                    R.drawable.splash_default_bg,
                    ScreenUtils.getSWidth(), ScreenUtils.getSHeight());
        }

        checkSplash();

        return bitmap;
    }

    @Override
    public void checkSplash() {
        final Gson gson = new Gson();
        OkHttpUtils.get().url(Api.SPLASH_BASE_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String json, int id) {
                if (TextUtils.isEmpty(json)) {
                    LogUtils.d(SplashModel.class, "checkSplash json ic_toolbar_return null");
                    return;
                }
                Splash splash = gson.fromJson(json, Splash.class);
                String oldSplashUrl = Preferences.getString(Constants.SPLASH_URL, "");
                String newSplashUrl = splash.getUrl();

                if (!oldSplashUrl.equals(newSplashUrl)) {
                    updateSplash(newSplashUrl);
                } else {
                    LogUtils.i(SplashModel.class, "splash image was newest");
                }
            }
        });
    }

    @Override
    public void updateSplash(final String splashUrl) {
        String fullUrl = Api.SPLASH_BING_COM_URL + splashUrl;

        OkHttpUtils.get().url(fullUrl).build().execute(
                new FileCallBack(FileUtils.getSplashDir(Cache.getContext()).getPath(), Constants.SPLASH_NAME) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        LogUtils.i(SplashModel.class, "downloadSplash completed !");
                        Preferences.putString(Constants.SPLASH_URL, splashUrl);
                    }
                });
    }
}
