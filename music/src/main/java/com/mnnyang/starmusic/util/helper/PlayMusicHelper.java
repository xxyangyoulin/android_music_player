package com.mnnyang.starmusic.util.helper;

import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.bean.PlaySongInfo;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.http.HttpCallback;
import com.mnnyang.starmusic.util.http.HttpUtils;

/**
 * Created by mnnyang on 17-4-19.
 */

public class PlayMusicHelper {
    public void playMusic(String id, String lrclink) {

        HttpUtils.getPlaySong(id, new HttpCallback<PlaySongInfo>() {
            @Override
            public void onSuccess(PlaySongInfo info) {
                if (info == null || info.getBitrate() == null) {
                    LogUtils.e(this, "The data returned was null");
                    return;
                }

                String songUrl = info.getBitrate().getFile_link();
                String title = info.getSonginfo().getTitle();
                String artilst = info.getSonginfo().getAuthor();
                String album = info.getSonginfo().getPic_big();
                String albumBigPic = info.getSonginfo().getPic_premium();
                long duration = info.getBitrate().getFile_duration() * 1000;
                String lrcLink = info.getSonginfo().getLrclink();

                LogUtils.i(this, "title" + title + "album" + album + "songUrl:" + songUrl);

                Music music = new Music().setId(-1)
                        .setAlbumPath(album)
                        .setAlbumBigPic(albumBigPic)
                        .setTitle(title)
                        .setArtist(artilst)
                        .setPath(songUrl)
                        .setLrcLink(lrcLink)
                        .setDuration(duration)
                        .setType(Music.Type.ONLINE);

                Cache.getPlayService().play(music);
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });

    }
}
