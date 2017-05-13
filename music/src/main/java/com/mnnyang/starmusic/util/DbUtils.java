package com.mnnyang.starmusic.util;

import android.content.Context;

import com.mnnyang.starmusic.util.general.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 数据库通用操作工具<br>
 * 需要init<br>
 * Created by mnnyang on 17-4-8.
 */

public class DbUtils {

    public static Context context;

    public static void init(Context context) {
        DbUtils.context = context.getApplicationContext();
    }

    /**
     * 把assets下的数据库拷贝到/data/data/<包名>/files/数据库名.db
     *
     * @param assetsDbName 要复制到的路径
     */
    private void copyDB(String assetsDbName) {
        File file = new File(context.getFilesDir(), assetsDbName);
        if (file.exists() && file.length() > 0) {
            LogUtils.i(DbUtils.class, assetsDbName + "<The database is already exists !");
        } else {
            try {
                InputStream is =
                        context.getAssets().open(assetsDbName);

                FileOutputStream fos = new FileOutputStream(file);
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
