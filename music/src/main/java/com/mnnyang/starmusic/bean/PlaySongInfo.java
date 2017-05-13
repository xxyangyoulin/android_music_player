package com.mnnyang.starmusic.bean;

/**
 * Created by mnnyang on 17-4-19.
 */

public class PlaySongInfo {
    private Bitrate bitrate;
    private Songinfo songinfo;


    public Songinfo getSonginfo() {
        return songinfo;
    }

    public PlaySongInfo setSonginfo(Songinfo songinfo) {
        this.songinfo = songinfo;
        return this;
    }

    public Bitrate getBitrate() {
        return bitrate;
    }

    public PlaySongInfo setBitrate(Bitrate bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    public class Songinfo {
        String title;
        String author;
        String album_title;
        String lrclink;
        String album_id;

        String pic_small;
        String pic_radio;
        String pic_big;
        String pic_premium;

        public String getTitle() {
            return title;
        }

        public Songinfo setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getAuthor() {
            return author;
        }

        public Songinfo setAuthor(String author) {
            this.author = author;
            return this;
        }

        public String getAlbum_title() {
            return album_title;
        }

        public Songinfo setAlbum_title(String album_title) {
            this.album_title = album_title;
            return this;
        }

        public String getLrclink() {
            return lrclink;
        }

        public Songinfo setLrclink(String lrclink) {
            this.lrclink = lrclink;
            return this;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public Songinfo setAlbum_id(String album_id) {
            this.album_id = album_id;
            return this;
        }

        public String getPic_small() {
            return pic_small;
        }

        public Songinfo setPic_small(String pic_small) {
            this.pic_small = pic_small;
            return this;
        }

        public String getPic_radio() {
            return pic_radio;
        }

        public Songinfo setPic_radio(String pic_radio) {
            this.pic_radio = pic_radio;
            return this;
        }

        public String getPic_big() {
            return pic_big;
        }

        public Songinfo setPic_big(String pic_big) {
            this.pic_big = pic_big;
            return this;
        }

        public String getPic_premium() {
            return pic_premium;
        }

        public Songinfo setPic_premium(String pic_premium) {
            this.pic_premium = pic_premium;
            return this;
        }
    }

    public class Bitrate {
        String file_link;
        String file_extension;
        String file_size;
        long file_duration;


        public String getFile_extension() {
            return file_extension;
        }

        public Bitrate setFile_extension(String file_extension) {
            this.file_extension = file_extension;
            return this;
        }

        public String getFile_size() {
            return file_size;
        }

        public Bitrate setFile_size(String file_size) {
            this.file_size = file_size;
            return this;
        }

        public long getFile_duration() {
            return file_duration;
        }

        public Bitrate setFile_duration(long file_duration) {
            this.file_duration = file_duration;
            return this;
        }

        public String getFile_link() {
            return file_link;
        }

        public Bitrate setFile_link(String file_link) {
            this.file_link = file_link;
            return this;
        }
    }
}
