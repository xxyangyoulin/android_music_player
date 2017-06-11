package com.mnnyang.starmusic.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;

import com.mnnyang.starmusic.api.Constants;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.service.PlayService;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.Preferences;
import com.mnnyang.starmusic.util.image.BitmapLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 音乐扫描工具
 * Created by mnnyang on 17-4-9.
 */

public class MusicScanUtils {

    public interface CompleteListener {
        void onComplete();
    }

    public void scanLocalMusic(final List<Music> musicList, final CompleteListener completeListener) {
        final Context context = Cache.getContext();
        musicList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(500);
                Cursor cursor = context.getApplicationContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                if (cursor == null) {
                    return;
                }
                while (cursor.moveToNext()) {
                    if (0 == cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC))) {
                        continue;
                    }
                    long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));

                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String albumPath = getAlbumPath(context, albumId);

                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                    String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

                    Music music = new Music();
                    music.setId(id)
                            .setTitle(title)
                            .setArtist(artist)
                            .setPath(path)
                            .setAlbum(album)
                            .setAlbumPath(albumPath)
                            .setDuration(duration).
                            setFileName(fileName)
                            .setFileSize(fileSize);
                    musicList.add(music);

                    BitmapLoader.newInstance().loadAlbumBitmap(albumPath);//先加载进来 listView一开始就不会卡顿了//TODO 图片预加载
                }

                cursor.close();

                PlayService service = Cache.getPlayService();
                if (service != null) {
                    service.updateCurrentPlayingPosition(Preferences.getLong(Constants.KEY_PLAYING_ID, -1));
                } else {
                    LogUtils.e(this, "playservice no launch");
                }

                initArtist(musicList);
                initAlbum(musicList);
                initDir(musicList);

                if (completeListener != null) {
                    completeListener.onComplete();
                }

            }
        }).start();
    }

    private void initAlbum(List<Music> musics) {
        HashMap<String, ArrayList<Music>> albumHashMap = Cache.getAlbumHashMap();
        ArrayList<String> albums = Cache.getAlbums();
        for (Music music : musics) {
            String album = music.getAlbum();

            ArrayList<Music> albumMusic = albumHashMap.get(album);

            if (albumMusic == null) {
                albumMusic = new ArrayList<>();
                albumHashMap.put(album, albumMusic);

                albums.add(album);
            }
            albumMusic.add(music);
        }

        Collections.sort(albums);
    }

    private void initArtist(List<Music> musics) {
        HashMap<String, ArrayList<Music>> artistHashMap = Cache.getArtistHashMap();
        ArrayList<String> artists = Cache.getArtists();
        for (Music music : musics) {
            String artist = music.getArtist();

            ArrayList<Music> artistMusic = artistHashMap.get(artist);

            if (artistMusic == null) {
                artistMusic = new ArrayList<>();
                artistHashMap.put(artist, artistMusic);

                artists.add(artist);
            }
            artistMusic.add(music);
        }

        for (String artist : artists) {
            System.out.println("---新的");
            for (Music music : artistHashMap.get(artist)) {
                System.out.println("---" + music.getArtist() + "--" + music.getTitle());
            }
        }

        Collections.sort(artists);
    }

    private void initDir(List<Music> musics) {
        HashMap<String, ArrayList<Music>> dirHashMap = Cache.getFolderHashMap();
        ArrayList<String> dirs = Cache.getFolderKeys();

        for (Music music : musics) {
            String path = music.getPath();
            String temp = path.substring(0, path.lastIndexOf("/"));
            String dirName = temp.substring(temp.lastIndexOf("/"), temp.length());

            ArrayList<Music> dirMusics = dirHashMap.get(dirName);
            if (dirMusics == null) {
                dirMusics = new ArrayList<>();
                dirHashMap.put(dirName, dirMusics);

                dirs.add(dirName);
            }
            dirMusics.add(music);

        }
        Collections.sort(dirs);
    }

    private static String getAlbumPath(Context context, long id) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/audio/albums/" + id),
                new String[]{"album_art"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            path = cursor.getString(0);
            cursor.close();
        }
        return path;
    }
}
