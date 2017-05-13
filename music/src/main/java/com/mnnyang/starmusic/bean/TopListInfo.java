package com.mnnyang.starmusic.bean;

import java.io.Serializable;

/**
 * 排行榜条目信息javabean
 * Created by mnnyang on 17-4-19.
 */

public class TopListInfo implements Serializable {
    private String title;
    private String type;
    private String url;
    private String music1;
    private String music2;
    private String music3;

    public boolean isInit() {
        return isInit;
    }

    public TopListInfo setInit(boolean init) {
        isInit = init;
        return this;
    }

    private boolean isInit;



    public String getTitle() {
        return title;
    }

    public TopListInfo setTitle(String title) {
        this.title = title;
        return this;
    }
    public String getType() {
        return type;
    }

    public TopListInfo setType(String type) {
        this.type = type;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public TopListInfo setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMusic1() {
        return music1;
    }

    public TopListInfo setMusic1(String music1) {
        this.music1 = music1;
        return this;
    }

    public String getMusic2() {
        return music2;
    }

    public TopListInfo setMusic2(String music2) {
        this.music2 = music2;
        return this;
    }

    public String getMusic3() {
        return music3;
    }

    public TopListInfo setMusic3(String music3) {
        this.music3 = music3;
        return this;
    }
}
