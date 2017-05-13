package com.mnnyang.starmusic.util.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.LruCache;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.util.general.BitmapUtils;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.ScreenUtils;

import java.util.HashSet;

/**
 * 图片加载工具<br>
 * Created by mnnyang on 17-4-9.
 */

public class BitmapLoader {

    public static final String DEFAULT_BITMAP_KEY = "default_bitmap";
    /**
     * 图片内存缓存
     */
    LruCache<String, Bitmap> bitmapLruCache;
    LruCache<String, Bitmap> albumLruCache;

    public BitmapLoader() {
        //1/8的可用最大内存 转换单位为 KB
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;
        bitmapLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //转换单位为KB
                return value.getByteCount() / 1024;
            }
        };
        albumLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //转换单位为KB
                return value.getByteCount() / 1024;
            }
        };
    }

    private static class CacheLoaderHolder {
        private static BitmapLoader bitmapLoader = new BitmapLoader();
    }

    public static BitmapLoader newInstance() {
        return CacheLoaderHolder.bitmapLoader;
    }

    /**
     * 内存缓存 加载本地的图片<br>
     * 需要把默认图片替换成自己的的默认图片
     *
     * @return
     */
    public Bitmap loadCacheBitmap(String path, int reqWidth, int reqHeight) {
        Bitmap bitmap;
        //path为空的选项,使用默认图片
        if (TextUtils.isEmpty(path)) {
            bitmap = bitmapLruCache.get(DEFAULT_BITMAP_KEY);
            if (bitmap == null) {
                //TODO 替换成自己的的默认图片
                bitmap = BitmapFactory.decodeResource(Cache.getContext().getResources(), R.mipmap.ic_launcher);
                bitmapLruCache.put(DEFAULT_BITMAP_KEY, bitmap);
            }
        } else {
            bitmap = bitmapLruCache.get(path);
            if (bitmap == null) {
                bitmap = ImageResizer.decodeSampledBitmapFromFile(path, reqWidth, reqHeight);
                //没有找到图片
                if (bitmap == null) {
                    LogUtils.w(BitmapLoader.class, "not find image :" + path);
                    bitmap = loadCacheBitmap(null, 0, 0);
                }
                bitmapLruCache.put(path, bitmap);
            }
        }

        return bitmap;
    }

    public Bitmap loadAlbumBitmap(String path) {
        Bitmap bitmap;
        if (TextUtils.isEmpty(path)) {
            bitmap = albumLruCache.get(DEFAULT_BITMAP_KEY);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(Cache.getContext().getResources(), R.drawable.def_music_icon);
                albumLruCache.put(DEFAULT_BITMAP_KEY, bitmap);
            }
        } else {
            bitmap = albumLruCache.get(path);
            if (bitmap == null) {
                bitmap = ImageResizer.decodeSampledBitmapFromFile(path, ScreenUtils.dp2px(50), ScreenUtils.dp2px(50));
                if (bitmap == null) {
                    LogUtils.w(BitmapLoader.class, "not find image :" + path);
                    bitmap = loadAlbumBitmap(null);
                }
                albumLruCache.put(path, bitmap);
            }
        }

        return bitmap;
    }

    private HashSet<BlurLoadTask> blurLoadTasks = new HashSet<>();



    /**
     * 加载模糊专辑背景
     */
    public void loadBlurAlbumBitmap(String path, BlurCompleteListener listener) {
        for (BlurLoadTask task : blurLoadTasks) {
            LogUtils.e(this, "移除blur加载任务");
            task.cancel(true);
            blurLoadTasks.remove(task);
        }
        BlurLoadTask blurLoadTask = new BlurLoadTask(listener);
        blurLoadTasks.add(blurLoadTask);
        blurLoadTask.execute(path);
    }


    private  class BlurLoadTask extends AsyncTask<String, Integer, Bitmap> {

        BlurCompleteListener listener;

        BlurLoadTask(BlurCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = ImageResizer.decodeSampledBitmapFromFile(params[0],
                    100, 200);
            try {
                bitmap = BitmapUtils.blur(bitmap, 50);
                if (bitmap == null) {
                    bitmap = ImageResizer.decodeSampledBitmapFromResources(Cache.getContext().getResources(),
                            R.drawable.splash_default_bg,
                            100, 200);
                }
            } catch (Exception e) {
                LogUtils.e(this, e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (listener != null) {
                listener.onComplete(bitmap);
            }
            blurLoadTasks.remove(this);
        }
    }

    public interface BlurCompleteListener {
        void onComplete(Bitmap bitmap);
    }

    public Bitmap getBlurAlbumBitmap() {
        return blurAlbumBitmap;
    }

    public BitmapLoader setBlurAlbumBitmap(Bitmap blurAlbumBitmap) {
        this.blurAlbumBitmap = blurAlbumBitmap;
        return this;
    }

    /**
     * 临时缓存播放页模糊背景图片
     */
    private Bitmap blurAlbumBitmap;
}
