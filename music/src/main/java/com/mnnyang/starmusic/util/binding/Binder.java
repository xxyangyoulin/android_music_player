package com.mnnyang.starmusic.util.binding;

import android.app.Activity;
import android.view.View;

import com.mnnyang.starmusic.util.general.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * findViewById 注解<br>
 * {@link Activity}中 onCreate方法设置Binder.bind(this);<br>
 * Created by mnnyang on 17-4-8.
 */

public class Binder {
    public static void bind(Activity activity) {
        bindContentView(activity);
        bind(activity, activity.getWindow().getDecorView());
    }

    public static void bind(Object target, View view) {
        Class clazz = target.getClass();
        //获得Activity的所有成员变量
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            //获取每个成员变量上面的注解
            BindView bindView = field.getAnnotation(BindView.class);
            LogUtils.i("注解", field.getName());
            if (bindView != null) {
                LogUtils.i("注解", field.getName() + "进入");
                try {
                    field.setAccessible(true);
                    field.set(target, view.findViewById(bindView.value()));
                    LogUtils.i("注解", field.getName() + "---" + bindView.value());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                LogUtils.i("注解", field.getName() + "==null?" + (field == null));
            }
        }
    }

    private static void bindContentView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        //获取Activity中的注解
        BindLayout contentView = clazz.getAnnotation(BindLayout.class);
        if (contentView != null) {
            //如果这个activity上面存在这个注解的话，
            // 就取出这个注解对应的value值，其实就是前面说的布局文件。
            try {
                Method setViewMethod = clazz.getMethod("setContentView", int.class);
                setViewMethod.invoke(activity, contentView.value());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
