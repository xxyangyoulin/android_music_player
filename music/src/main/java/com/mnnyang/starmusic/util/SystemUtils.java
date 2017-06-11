package com.mnnyang.starmusic.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.api.Constants;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.util.helper.DialogHelper;
import com.mnnyang.starmusic.service.PlayService;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.Preferences;
import com.mnnyang.starmusic.util.image.BitmapLoader;
import com.mnnyang.starmusic.activity.MainActivity;
import com.mnnyang.starmusic.activity.BaseActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 系统操作工具
 * Created by mnnyang on 17-4-10.
 */

public class SystemUtils {
    /**
     * 获取App版本号
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 判断wifi状态
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 夜间模式切换
     */
    public static void switchNight(final BaseActivity activity) {

        final DialogHelper dialogHelper = new DialogHelper();
        dialogHelper.showProgressDialog(activity, "", "请稍等..", false);

        Cache.newInstance().handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentNightMode = activity.getResources().getConfiguration().uiMode
                        & Configuration.UI_MODE_NIGHT_MASK;

                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        Preferences.putBoolean(Constants.KEY_MODE_NIGHT_YES, true);
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        Preferences.putBoolean(Constants.KEY_MODE_NIGHT_YES, false);
                        break;
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                        // We don't know what mode we're in, assume notnight
                        LogUtils.e(SystemUtils.class, "UI_MODE_NIGHT_UNDEFINED");
                        break;
                    default:
                        break;
                }
                dialogHelper.hideProgressDialog();
                activity.recreate();//TODO Android 6.0.1不调用也可以完成切换
            }
        }, 500);
    }

    /**
     * 退出应用
     */
    public static void exit() {
        Cache.clearAllActivity();
        Cache.getPlayService().exit();
    }


    public static Notification buildPlayBarNotification(Service service, @NonNull Music music, int reqCode, boolean isPlaying) {
        LogUtils.i("buildPlayBarNotification", "buldNotification");
        RemoteViews remoteViews = getRemoteViews(service, music, isPlaying);
        //获取通知栏服务
        NotificationManager manager = (NotificationManager) Cache.getContext().getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service);
        Notification notification = builder
                .setContentTitle(music.getTitle())
                .setContentText(music.getArtist() + " - " + music.getAlbum())
                .setTicker(music.getTitle())
                .setWhen(System.currentTimeMillis())//时间
                .setSmallIcon(R.drawable.ic_stat_music)//设置小图标
                .setAutoCancel(false)
                .setOngoing(true)//无法删除通知
                .setLargeIcon(BitmapLoader.newInstance().loadAlbumBitmap(music.getAlbumPath()))
                .setContentIntent(getContentIntent(service, reqCode))

                .setContent(remoteViews)
                .build();

        return notification;
    }

    private static RemoteViews getRemoteViews(Service service, @NonNull Music music, boolean isPlaying) {
        RemoteViews remoteViews = new RemoteViews(Cache.getContext().getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.tv_notify_title, music.getTitle());
        remoteViews.setTextViewText(R.id.tv_notify_artist, music.getArtist() + " - " + music.getAlbum());
        remoteViews.setImageViewBitmap(R.id.iv_notify_album, BitmapLoader.newInstance().loadAlbumBitmap(music.getAlbumPath()));
        remoteViews.setImageViewResource(R.id.iv_play_or_pause, isPlaying ? R.drawable.ic_pause_black_48dp : R.drawable.ic_play_arrow_black_48dp);

        remoteViews.setOnClickPendingIntent(R.id.iv_play_or_pause, getServicePendingIntent(service, Constants.CODE_PLAY_OR_PAUSE));
        remoteViews.setOnClickPendingIntent(R.id.iv_next, getServicePendingIntent(service, Constants.CODE_NEXT));
        remoteViews.setOnClickPendingIntent(R.id.iv_exit, getServicePendingIntent(service, Constants.CODE_EXIT));
        remoteViews.setOnClickPendingIntent(R.id.ll_notify_root, getContentIntent(service, Constants.CODE_OPEN_MAIN_PAGE));

        return remoteViews;
    }

    private static PendingIntent getServicePendingIntent(Service service, int code) {
        Intent resultIntent = new Intent(service, PlayService.class);
        resultIntent.putExtra(Constants.INTENT_NOTIFINATION, code);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getService(service, code,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return contentIntent;
    }

    public static PendingIntent getContentIntent(Service service, int requestCode) {
        Intent resultIntent = new Intent(service, MainActivity.class);
        resultIntent.putExtra(Constants.INTENT_NOTIFINATION, requestCode);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(service, requestCode,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return contentIntent;
    }
}
