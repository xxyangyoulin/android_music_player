package com.mnnyang.starmusic.bean;

import java.util.List;

/**
 * Created by mnnyang on 17-4-17.
 */

public class SearchMusic {

    /*
     * bitrate_fee: "{"0":"129|-1","1":"-1|-1"}",
     weight: "5320",
     songname: "海阔天空",
     songid: "268425156",
     has_mv: "0",
     yyr_artist: "0",
     artistname: "韩红",
     resource_type_ext: "0",
     resource_provider: "1",
     control: "0000000000",
     encrypted_songid: "2107fffd7c409584153f0L"
     },
     */

    public List<Song> getSong() {
        return song;
    }

    public SearchMusic setSong(List<Song> song) {
        this.song = song;
        return this;
    }

    private List<Song> song;

    public static class Song {
        private String songname;
        private String artistname;
        private String songid;

        public String getSongname() {
            return songname;
        }

        public Song setSongname(String songname) {
            this.songname = songname;
            return this;
        }

        public String getArtistname() {
            return artistname;
        }

        public Song setArtistname(String artistname) {
            this.artistname = artistname;
            return this;
        }

        public String getSongid() {
            return songid;
        }

        public Song setSongid(String songid) {
            this.songid = songid;
            return this;
        }

        @Override
        public String toString() {
            return "Song{" +
                    "songname='" + songname + '\'' +
                    ", artistname='" + artistname + '\'' +
                    ", songid='" + songid + '\'' +
                    '}';
        }
    }
}
