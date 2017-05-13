package com.mnnyang.starmusic.bean;

import java.util.List;

/**
 * Created by mnnyang on 17-4-19.
 */

public class TopList {
    private List<OnlineMusic> song_list;
    private Billboard billboard;

    public List<OnlineMusic> getSong_list() {
        return song_list;
    }

    public TopList setSong_list(List<OnlineMusic> song_list) {
        this.song_list = song_list;
        return this;
    }

    public Billboard getBillboard() {
        return billboard;
    }

    public TopList setBillboard(Billboard billboard) {
        this.billboard = billboard;
        return this;
    }

    public static class Billboard {
        private String update_date;
        private String name;
        private String comment;
        private String pic_s640;
        private String pic_s444;
        private String pic_s260;
        private String pic_s210;

        public String getUpdate_date() {
            return update_date;
        }

        public Billboard setUpdate_date(String update_date) {
            this.update_date = update_date;
            return this;
        }

        public String getName() {
            return name;
        }

        public Billboard setName(String name) {
            this.name = name;
            return this;
        }

        public String getComment() {
            return comment;
        }

        public Billboard setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public String getPic_s640() {
            return pic_s640;
        }

        public Billboard setPic_s640(String pic_s640) {
            this.pic_s640 = pic_s640;
            return this;
        }

        public String getPic_s444() {
            return pic_s444;
        }

        public Billboard setPic_s444(String pic_s444) {
            this.pic_s444 = pic_s444;
            return this;
        }

        public String getPic_s260() {
            return pic_s260;
        }

        public Billboard setPic_s260(String pic_s260) {
            this.pic_s260 = pic_s260;
            return this;
        }

        public String getPic_s210() {
            return pic_s210;
        }

        public Billboard setPic_s210(String pic_s210) {
            this.pic_s210 = pic_s210;
            return this;
        }
    }
}
