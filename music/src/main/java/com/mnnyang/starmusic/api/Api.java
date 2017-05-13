package com.mnnyang.starmusic.api;

/**
 * Created by mnnyang on 17-4-17.
 */

public class Api {
    public static final String SPLASH_BING_COM_URL = "http://cn.bing.com";
    public static final String SPLASH_BASE_URL = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";


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
}
