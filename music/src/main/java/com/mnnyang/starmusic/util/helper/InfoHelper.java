package com.mnnyang.starmusic.util.helper;

import android.app.Activity;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.util.TimeFormat;

/**
 * 歌曲信息展示助手<br>
 * Created by mnnyang on 17-4-22.
 */

public class InfoHelper {
    Activity activity;
    Music music;
    private DialogHelper dialogHelper;
    private View view;
    private TextView tvTitle;
    private TextView tvInfo;

    public InfoHelper(Activity activity, Music music) {
        this.activity = activity;
        this.music = music;
    }

    public void show() {
        initView();
        initData();
        dialogHelper = new DialogHelper();
        dialogHelper.showBottomDialog(activity, view);
    }

    private void initData() {
        tvTitle.setText(Cache.getContext().getString(R.string.sheet_title, music.getTitle()));
        tvInfo.setText(Cache.getContext().getString(R.string.sheet_info,
                music.getFileName(),
                TimeFormat.format(music.getDuration()),
                Formatter.formatFileSize(Cache.getContext(), music.getFileSize()),
                music.getPath()));
    }

    private void initView() {
        if (view != null) {
            return;
        }
        view = LayoutInflater.from(activity).inflate(R.layout.layout_bottom_dialog_song_info, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_sheet_title);
        tvInfo = (TextView) view.findViewById(R.id.tv_sheet_info);
    }
}
