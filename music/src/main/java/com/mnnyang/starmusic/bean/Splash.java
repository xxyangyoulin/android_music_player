package com.mnnyang.starmusic.bean;

import java.util.List;

/**
 * splash 每日一图 Javabean
 * Created by mnnyang on 17-4-12.
 */

public class Splash {

    /**
     * images : [{"startdate":"20170411","fullstartdate":"201704111600","enddate":"20170412","url":"/az/hprichbg/rb/SpacewalkSelfie_ZH-CN10118363891_1920x1080.jpg","urlbase":"/az/hprichbg/rb/SpacewalkSelfie_ZH-CN10118363891","copyright":"宇航员Terry Virts的太空自拍照 (© NASA)","copyrightlink":"http://www.bing.com/search?q=Astronaut+Terry+Virts&form=hpcapt&mkt=zh-cn","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20170411_SpacewalkSelfie%22&FORM=HPQUIZ","wp":false,"hsh":"71a466880914f4ac38ddbb827d4228e9","drk":1,"top":1,"bot":1,"hs":[]}]
     * tooltips : {"LOADING":"正在加载...","previous":"上一个图像","next":"下一个图像","walle":"此图片不能下载用作壁纸。","walls":"下载今日美图。仅限用作桌面壁纸。"}
     */

    private TooltipsBean tooltips;
    private List<ImagesBean> images;

    public TooltipsBean getTooltips() {
        return tooltips;
    }

    public void setTooltips(TooltipsBean tooltips) {
        this.tooltips = tooltips;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class TooltipsBean {
        /**
         * LOADING : 正在加载...
         * previous : 上一个图像
         * next : 下一个图像
         * walle : 此图片不能下载用作壁纸。
         * walls : 下载今日美图。仅限用作桌面壁纸。
         */

        private String loading;
        private String previous;
        private String next;
        private String walle;
        private String walls;

        public String getLoading() {
            return loading;
        }

        public void setLoading(String loading) {
            this.loading = loading;
        }

        public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getWalle() {
            return walle;
        }

        public void setWalle(String walle) {
            this.walle = walle;
        }

        public String getWalls() {
            return walls;
        }

        public void setWalls(String walls) {
            this.walls = walls;
        }
    }

    public String getUrl() {
        if (images != null && images.size() > 0) {
            return images.get(0).getUrl();
        }
        return "";
    }

    public static class ImagesBean {
        /**
         * startdate : 20170411
         * fullstartdate : 201704111600
         * enddate : 20170412
         * url : /az/hprichbg/rb/SpacewalkSelfie_ZH-CN10118363891_1920x1080.jpg
         * urlbase : /az/hprichbg/rb/SpacewalkSelfie_ZH-CN10118363891
         * copyright : 宇航员Terry Virts的太空自拍照 (© NASA)
         * copyrightlink : http://www.bing.com/search?q=Astronaut+Terry+Virts&form=hpcapt&mkt=zh-cn
         * quiz : /search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20170411_SpacewalkSelfie%22&FORM=HPQUIZ
         * wp : false
         * hsh : 71a466880914f4ac38ddbb827d4228e9
         * drk : 1
         * top : 1
         * bot : 1
         * hs : []
         */

        private String startdate;
        private String fullstartdate;
        private String enddate;
        private String url;
        private String urlbase;
        private String copyright;
        private String copyrightlink;
        private String quiz;
        private boolean wp;
        private String hsh;
        private int drk;
        private int top;
        private int bot;
        private List<?> hs;

        public String getStartdate() {
            return startdate;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
        }

        public String getFullstartdate() {
            return fullstartdate;
        }

        public void setFullstartdate(String fullstartdate) {
            this.fullstartdate = fullstartdate;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlbase() {
            return urlbase;
        }

        public void setUrlbase(String urlbase) {
            this.urlbase = urlbase;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public String getCopyrightlink() {
            return copyrightlink;
        }

        public void setCopyrightlink(String copyrightlink) {
            this.copyrightlink = copyrightlink;
        }

        public String getQuiz() {
            return quiz;
        }

        public void setQuiz(String quiz) {
            this.quiz = quiz;
        }

        public boolean isWp() {
            return wp;
        }

        public void setWp(boolean wp) {
            this.wp = wp;
        }

        public String getHsh() {
            return hsh;
        }

        public void setHsh(String hsh) {
            this.hsh = hsh;
        }

        public int getDrk() {
            return drk;
        }

        public void setDrk(int drk) {
            this.drk = drk;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getBot() {
            return bot;
        }

        public void setBot(int bot) {
            this.bot = bot;
        }

        public List<?> getHs() {
            return hs;
        }

        public void setHs(List<?> hs) {
            this.hs = hs;
        }
    }
}
