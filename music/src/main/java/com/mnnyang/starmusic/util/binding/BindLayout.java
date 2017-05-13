package com.mnnyang.starmusic.util.binding;

/**
 * Activity 布局注解<br>
 * 位于类之上<br>
 * Created by mnnyang on 17-4-8.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindLayout {
    int value();
}
