package com.mnnyang.starmusicapp.helper;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mnnyang on 18-1-15.
 */

public class Cache {
    /*Activity的栈*/
    private final List<Activity> activities = new ArrayList<>();

    /********************单例模式*************************/
    private Cache() {
    }

    private static class Holder {
        private static final Cache instance = new Cache();
    }

    public static Cache instance() {
        return Holder.instance;
    }

    /*****************Activity栈 start*******************/
    public static void addActivity(Activity activity) {
        instance().activities.add(activity);
    }

    public static void rmActivity(Activity activity) {
        instance().activities.remove(activity);
    }

    public static void clearAllActivity() {
        for (int i = instance().activities.size() - 1; i >= 0; i--) {
            Activity activity = instance().activities.get(i);
            instance().activities.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
