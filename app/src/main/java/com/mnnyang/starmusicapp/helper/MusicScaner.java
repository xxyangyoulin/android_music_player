package com.mnnyang.starmusicapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * 音乐扫描工具
 * Created by mnnyang on 17-4-9.
 */

public class MusicScaner {

    public ArrayList<Music> scanLocalMusic(final Context context) {
        final ArrayList<Music> musicList = new ArrayList<>();

        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                        null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            return musicList;
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
            String albumPath = getAlbumPath(context, albumId);

            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

            Music music = new Music();
            music.setId(id)
                    .setDuration(duration)
                    .setTitle(title)
                    .setArtist(artist)
                    .setPath(path)
                    .setAlbum(album)
                    .setAlbumPath(albumPath)
                    .setFileName(fileName)
                    .setFileSize(fileSize);

            musicList.add(music);
        }

        cursor.close();

        return musicList;
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
