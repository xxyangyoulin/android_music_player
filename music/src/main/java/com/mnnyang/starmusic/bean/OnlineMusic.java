package com.mnnyang.starmusic.bean;

/**
 * Created by mnnyang on 17-4-19.
 */

public class OnlineMusic {
    private String pic_big;
    private String pic_small;
    private String lrclink;
    private String song_id;
    private String title;
    private String ting_uid;
    private String album_title;
    private String artist_name;

    public String getPic_big() {
        return pic_big;
    }

    public OnlineMusic setPic_big(String pic_big) {
        this.pic_big = pic_big;
        return this;
    }

    public String getPic_small() {
        return pic_small;
    }

    public OnlineMusic setPic_small(String pic_small) {
        this.pic_small = pic_small;
        return this;
    }

    public String getLrclink() {
        return lrclink;
    }

    public OnlineMusic setLrclink(String lrclink) {
        this.lrclink = lrclink;
        return this;
    }

    public String getSong_id() {
        return song_id;
    }

    public OnlineMusic setSong_id(String song_id) {
        this.song_id = song_id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public OnlineMusic setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTing_uid() {
        return ting_uid;
    }

    public OnlineMusic setTing_uid(String ting_uid) {
        this.ting_uid = ting_uid;
        return this;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public OnlineMusic setAlbum_title(String album_title) {
        this.album_title = album_title;
        return this;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public OnlineMusic setArtist_name(String artist_name) {
        this.artist_name = artist_name;
        return this;
    }
}
