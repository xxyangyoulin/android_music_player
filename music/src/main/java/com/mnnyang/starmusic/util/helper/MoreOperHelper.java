package com.mnnyang.starmusic.util.helper;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.bean.PlaySongInfo;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.ToastUtils;
import com.mnnyang.starmusic.util.http.HttpCallback;
import com.mnnyang.starmusic.util.http.HttpUtils;

import java.io.File;

import static com.mnnyang.starmusic.app.Cache.getContext;
import static com.mnnyang.starmusic.app.Cache.getPlayService;

/**
 * 歌曲操作助手<br>
 * Created by mnnyang on 17-4-22.
 */

public class MoreOperHelper implements View.OnClickListener {
    private static final int REQUEST_WRITE_SETTINGS = 1;
    private final Activity activity;
    private TextView tvDel;
    private TextView tvBell;
    private TextView tvInfo;
    private TextView tvAlbum;
    private TextView tvArtist;
    private TextView tvDownload;
    private TextView tvShare;
    private TextView tvNextPlay;
    private TextView tvTitle;
    private View view;

    private Music music;
    private DialogHelper dialogHelper;
    private String shareContent;

    public MoreOperHelper(Fragment fragment, Music music) {
        this.music = music;
        this.activity = fragment.getActivity();
    }

    public MoreOperHelper(Activity activity, Music music) {
        this.music = music;
        this.activity = activity;
    }

    public void show() {
        initView();
        initListener();
        initData();

        initItemEnabled();

        dialogHelper = new DialogHelper();
        dialogHelper.showBottomDialog(activity, view);
    }

    private void initItemEnabled() {
        if (music.getType() == Music.Type.LOCAL) {
            LogUtils.d(this, "music type is local");
            tvDownload.setEnabled(false);
            tvDownload.setTextColor(Color.GRAY);
        } else {
            tvDel.setEnabled(false);
            tvDel.setTextColor(Color.GRAY);
            tvBell.setEnabled(false);
            tvBell.setTextColor(Color.GRAY);
            tvInfo.setEnabled(false);
            tvInfo.setTextColor(Color.GRAY);
        }
    }

    private void initListener() {
        tvNextPlay.setOnClickListener(this);
        tvDownload.setOnClickListener(this);
        tvArtist.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvAlbum.setOnClickListener(this);
        tvBell.setOnClickListener(this);
        tvInfo.setOnClickListener(this);
        tvDel.setOnClickListener(this);
    }

    private void initData() {
        tvArtist.setText(getContext().getString(R.string.sheet_artist, music.getArtist()));
        tvAlbum.setText(getContext().getString(R.string.sheet_album, music.getTitle()));
        tvTitle.setText(getContext().getString(R.string.sheet_title, music.getTitle()));
    }

    private void initView() {
        if (view != null) {
            return;
        }
        view = LayoutInflater.from(activity).inflate(R.layout.layout_bottom_dialog_content, null);
        tvNextPlay = (TextView) view.findViewById(R.id.tv_sheet_next_play);
        tvDownload = (TextView) view.findViewById(R.id.tv_sheet_download);
        tvArtist = (TextView) view.findViewById(R.id.tv_sheet_artist);
        tvAlbum = (TextView) view.findViewById(R.id.tv_sheet_album);
        tvShare = (TextView) view.findViewById(R.id.tv_sheet_share);
        tvTitle = (TextView) view.findViewById(R.id.tv_sheet_title);
        tvInfo = (TextView) view.findViewById(R.id.tv_sheet_info);
        tvBell = (TextView) view.findViewById(R.id.tv_sheet_bell);
        tvDel = (TextView) view.findViewById(R.id.tv_sheet_del);


    }

    @Override
    public void onClick(View v) {
        dialogHelper.hideBottomDialog();

        switch (v.getId()) {
            case R.id.tv_sheet_next_play:
                nextPlay();
                break;
            case R.id.tv_sheet_share:
                share();
                break;
            case R.id.tv_sheet_download:
                download();
                break;
            case R.id.tv_sheet_info:
                info();
                break;
            case R.id.tv_sheet_bell:
                bell();
                break;
            case R.id.tv_sheet_del:
                delete();
                break;
        }

    }

    private void delete() {
        final DialogHelper waitDialog = new DialogHelper();

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        String title = music.getTitle();
        dialog.setMessage("确定删除：" + title + " ?");

        dialog.setPositiveButton(R.string.text_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                waitDialog.showProgressDialog(activity, "正在删除", "请稍等...", false);

                Cache.getMusicList().remove(music);
                File file = new File(music.getPath());
                if (file.delete()) {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + music.getPath()));
                    getContext().sendBroadcast(intent);

//                    getPlayService().updateCurrentPlayingPosition();
                    if (delListener != null) {
                        waitDialog.hideProgressDialog();
                        delListener.onDelComplete();
                    }
                }
            }
        });
        dialog.setNegativeButton(R.string.text_cancel, null);
        dialog.show();
    }

    private void bell() {
        requestSetRingtone(music);
    }

    private void info() {
        new InfoHelper(activity, music).show();
    }

    private void download() {
        //TODO 开始下载
    }

    private void share() {
        shareContent = "我分享了一首歌: " + music.getTitle() + " - " + music.getArtist();

        if (music.getType() == Music.Type.ONLINE) {
            final DialogHelper dialogHelper = new DialogHelper();
            dialogHelper.showProgressDialog(activity, "提示", "请稍等...", true);
            HttpUtils.getPlaySong(String.valueOf(music.getId()), new HttpCallback<PlaySongInfo>() {
                @Override
                public void onSuccess(PlaySongInfo info) {
                    dialogHelper.hideProgressDialog();
                    if (info == null || info.getBitrate() == null) {
                        ToastUtils.show("网络故障 分享失败");
                        return;
                    }
                    music.setPath(info.getBitrate().getFile_link());
                    shareContent += "： " + music.getPath();
                    shareLocalMusic();
                }

                @Override
                public void onFail(Exception e) {
                    e.printStackTrace();
                    dialogHelper.hideProgressDialog();
                    ToastUtils.show("网络故障 分享失败");
                }
            });
        } else {
            shareLocalMusic();
        }
    }


    private void shareLocalMusic() {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "歌曲分享");//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, shareContent);//添加分享内容
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "歌曲分享");
        activity.startActivity(share_intent);
    }

    private void nextPlay() {
        if (music.getType() == Music.Type.LOCAL) {
            getPlayService().nextPlay(music);
            ToastUtils.show("下一首播放：" + music.getTitle());
        } else {

        }
    }

    private void requestSetRingtone(final Music music) {
        setRingtone(music);
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(getContext())) {
            ToastUtils.show("没有权限，无法设置铃声");
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getContext().getPackageName()));
            activity.startActivityForResult(intent, REQUEST_WRITE_SETTINGS);
        } else {
        }
        */
    }

    /**
     * 设置铃声
     */
    private void setRingtone(Music music) {
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(music.getPath());
        Cursor cursor = getContext().getContentResolver().query(uri, null,
                MediaStore.MediaColumns.DATA + "=?", new String[]{music.getPath()}, null);
        if (cursor == null) {
            return;
        }
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            String _id = cursor.getString(0);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Audio.Media.IS_MUSIC, true);
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_ALARM, false);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            values.put(MediaStore.Audio.Media.IS_PODCAST, false);

            getContext().getContentResolver().update(uri, values, MediaStore.MediaColumns.DATA + "=?",
                    new String[]{music.getPath()});
            Uri newUri = ContentUris.withAppendedId(uri, Long.valueOf(_id));
            RingtoneManager.setActualDefaultRingtoneUri(getContext(), RingtoneManager.TYPE_RINGTONE, newUri);
            ToastUtils.show("设置成功");
        }
        cursor.close();
    }

    public MoreOperHelper setDelListener(DelListener delListener) {
        this.delListener = delListener;
        return this;
    }

    DelListener delListener;

    public interface DelListener {
        void onDelComplete();
    }
}
