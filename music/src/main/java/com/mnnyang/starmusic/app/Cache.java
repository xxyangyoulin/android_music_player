package com.mnnyang.starmusic.app;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.bean.TopListInfo;
import com.mnnyang.starmusic.service.PlayService;
import com.mnnyang.starmusic.util.general.Preferences;
import com.mnnyang.starmusic.util.general.ScreenUtils;
import com.mnnyang.starmusic.util.general.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 全局数据缓存
 * TODO 导致内存占用过大
 * Created by mnnyang on 17-5-5.
 */

public class Cache {
    private Context context;
    /*主服务缓存*/
    private PlayService mainService;
    /*本地全部歌曲缓存*/
    private final List<Music> localMusicList = new ArrayList<>();

    /*当前播放列表 默认为本地全部歌曲*/
    private List<Music> currentMusicList;
    /*排行榜*/
    private final ArrayList<TopListInfo> musicTopList = new ArrayList<>();

    /*艺术家*/
    private final ArrayList<String> artistKeys = new ArrayList<>();
    private final HashMap<String, ArrayList<Music>> artistHashMap = new HashMap<String, ArrayList<Music>>();

    /*专辑*/
    private final ArrayList<String> albumkeys = new ArrayList<>();
    private final HashMap<String, ArrayList<Music>> albumHashMap = new HashMap<String, ArrayList<Music>>();

    /*文件夹*/
    private final ArrayList<String> dirKeys = new ArrayList<>();
    private final HashMap<String, ArrayList<Music>> dirHashMap = new HashMap<String, ArrayList<Music>>();

    /*Activity的栈*/
    private final List<Activity> activities = new ArrayList<>();

    public Handler handler = new Handler(Looper.getMainLooper());

    /*单例模式*/
    private static class CacheHolder {
        private static Cache Cache = new Cache();
    }

    public static Cache newInstance() {
        return CacheHolder.Cache;
    }

    public static void init(Context context) {
        newInstance().context = context.getApplicationContext();
        newInstance().currentMusicList = newInstance().localMusicList;//TODO 设置默认的播放列表
        initUtils(context.getApplicationContext());
    }

    private static void initUtils(Context context) {
        Preferences.init(context);
        ScreenUtils.init(context);
        ToastUtils.init(context);
    }

    public static Context getContext() {
        return newInstance().context;
    }

    public static List<Music> getMusicList() {
        return newInstance().localMusicList;
    }

    public static List<Music> getCurrentMusicList() {
        return newInstance().currentMusicList;
    }

    /**
     * 切换当前歌曲列表
     *
     * @param currentMusicList
     * @return
     */
    public static void setCurrentMusicList(List<Music> currentMusicList) {
        newInstance().currentMusicList = currentMusicList;
    }

    public static HashMap<String, ArrayList<Music>> getArtistHashMap() {
        return newInstance().artistHashMap;
    }

    public static ArrayList<String> getArtists() {
        return newInstance().artistKeys;
    }

    public static HashMap<String, ArrayList<Music>> getAlbumHashMap() {
        return newInstance().albumHashMap;
    }

    public static ArrayList<String> getAlbums() {
        return newInstance().albumkeys;
    }

    public static HashMap<String, ArrayList<Music>> getFolderHashMap() {
        return newInstance().dirHashMap;
    }

    public static ArrayList<String> getFolderKeys() {
        return newInstance().dirKeys;
    }


    public static PlayService getPlayService() {
        return newInstance().mainService;
    }

    public static void setPlayService(PlayService mainService) {
        newInstance().mainService = mainService;
    }

    public static ArrayList<TopListInfo> getMusicTopList() {
        return newInstance().musicTopList;
    }

    /*****************Activity栈 start*******************/
    public static void addActivity(Activity activity) {
        newInstance().activities.add(activity);
    }

    public static void rmActivity(Activity activity) {
        newInstance().activities.remove(activity);
    }

    public static void clearAllActivity() {
        for (int i = newInstance().activities.size() - 1; i >= 0; i--) {
            Activity activity = newInstance().activities.get(i);
            newInstance().activities.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /*****************Activity栈 end*******************/

    private Cache() {
    }
}
