package com.mnnyang.starmusic.api;

/**
 * Created by mnnyang on 17-4-17.
 */

public class BaiduApi {
    //http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type=2&size=10&offset=0
    public static final String BAIDUAPI_BASE = "http://tingapi.ting.baidu.com/v1/restserver/ting";

    public static final String METHOD = "method";
    public static final String SIZE = "size";
    public static final String TYPE = "type";
    public static final String SONGID = "songid";
    public static final String QUERY = "query";
    public static final String OFFSET = "offset";

    public static final String METHOD_BILLLIST = "baidu.ting.billboard.billList";
    public static final String METHOD_SEARCH = "baidu.ting.search.catalogSug";
    public static final String METHOD_PLAY = "baidu.ting.song.play";
    public static final String METHOD_LRC = "baidu.ting.song.lry";
    /**
     * 百度搜索
     */
    public static final String BAIDU_SONG_SEARCH = BAIDUAPI_BASE + "?method=" + METHOD_SEARCH + "&query=";

    /**
     * 榜单
     */
    public static class List {
        public static String get(int type, int size) {
            return BAIDUAPI_BASE + "?method=" + METHOD_BILLLIST + "&type=" + type + "&size=" + size + "&offset=0";
        }
    }

    /**
     * 搜索
     */
    public static class Search {
        public static String get(String songName) {
            return BAIDU_SONG_SEARCH + songName;
        }
    }


    /**
     * <p>播放</p>
     * http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.song.play&songid=877578
     */
    public static class Play {
        public static String get(long songId) {
            return BAIDUAPI_BASE + "?method="+METHOD_PLAY+"&songid="+songId;
        }
    }

    /**
     * <p></p>
     * method=baidu.ting.song.lry&songid=877578
     */
    public static class Lrc {
        public static String get(long songId) {
            return BAIDUAPI_BASE + "?method="+METHOD_LRC+"&songid="+songId;
        }
    }
}
