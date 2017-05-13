package com.mnnyang.starmusic.util.http;

import com.mnnyang.starmusic.api.BaiduApi;
import com.mnnyang.starmusic.bean.Lrc;
import com.mnnyang.starmusic.bean.PlaySongInfo;
import com.mnnyang.starmusic.bean.SearchMusic;
import com.mnnyang.starmusic.bean.TopList;
import com.mnnyang.starmusic.util.general.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

import static com.mnnyang.starmusic.api.BaiduApi.BAIDUAPI_BASE;
import static com.mnnyang.starmusic.api.BaiduApi.METHOD;
import static com.mnnyang.starmusic.api.BaiduApi.OFFSET;
import static com.mnnyang.starmusic.api.BaiduApi.QUERY;
import static com.mnnyang.starmusic.api.BaiduApi.SIZE;
import static com.mnnyang.starmusic.api.BaiduApi.SONGID;
import static com.mnnyang.starmusic.api.BaiduApi.TYPE;

/**
 * Created by mnnyang on 17-4-17.
 */

public class HttpUtils {

    /**
     * 获取歌词
     */
    public static void getLrc(String songID, final HttpCallback<Lrc> httpCallBack) {
        OkHttpUtils.get()
                .url(BAIDUAPI_BASE)
                .addParams(METHOD, BaiduApi.METHOD_LRC)
                .addParams(SONGID, songID)
                .build().execute(new JsonCallback<Lrc>(Lrc.class) {
            @Override
            public void onError(Call call, Exception e, int id) {
                httpCallBack.onFail(e);
            }

            @Override
            public void onResponse(Lrc lrc, int id) {
                httpCallBack.onSuccess(lrc);
            }
        });
    }

    /**
     * 搜索歌曲
     * method=baidu.ting.search.catalogSug&query=海阔天空
     */
    public static void getSong(String songName, final HttpCallback<SearchMusic> httpCallBack) {
        OkHttpUtils.get()
                .url(BAIDUAPI_BASE)
                .addParams(METHOD, BaiduApi.METHOD_SEARCH)
                .addParams(QUERY, songName)
                .build().execute(new JsonCallback<SearchMusic>(SearchMusic.class) {
            @Override
            public void onError(Call call, Exception e, int id) {
                httpCallBack.onFail(e);
            }

            @Override
            public void onResponse(SearchMusic response, int id) {
                httpCallBack.onSuccess(response);
            }
        });
    }

    /**
     * 获取排行榜
     */
    public static void getTopListInfo(String type, String size, String offset, final HttpCallback<TopList> httpCallBack) {
        OkHttpUtils.get()
                .url(BAIDUAPI_BASE)
                .addParams(METHOD, BaiduApi.METHOD_BILLLIST)
                .addParams(TYPE, type)
                .addParams(SIZE, size)
                .addParams(OFFSET, offset)
                .build()
                .execute(new JsonCallback<TopList>(TopList.class) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        httpCallBack.onFail(e);
                    }

                    @Override
                    public void onResponse(TopList response, int id) {
                        httpCallBack.onSuccess(response);
                    }
                });
    }

    /**
     * 根据歌曲id获取歌曲详细信息
     */
    public static void getPlaySong(String songId, final HttpCallback<PlaySongInfo> httpCallBack) {
        OkHttpUtils.get()
                .url(BAIDUAPI_BASE)
                .addParams(METHOD, BaiduApi.METHOD_PLAY)
                .addParams(SONGID, songId)
                .build().execute(new JsonCallback<PlaySongInfo>(PlaySongInfo.class) {
            @Override
            public void onError(Call call, Exception e, int id) {
                httpCallBack.onFail(e);
            }

            @Override
            public void onResponse(PlaySongInfo response, int id) {
                httpCallBack.onSuccess(response);
            }
        });
    }

    public static void downLrc(String link, String lrcName, final HttpCallback<File> httpCallBack) {
        OkHttpUtils.get()
                .url(link)
                .build().execute(new FileCallBack(FileUtils.LRC_PATH, lrcName+".lrc") {
            @Override
            public void onError(Call call, Exception e, int id) {
                httpCallBack.onFail(e);
            }

            @Override
            public void onResponse(File response, int id) {
                httpCallBack.onSuccess(response);
            }
        });
    }
}
