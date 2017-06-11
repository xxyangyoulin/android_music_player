package com.mnnyang.starmusic.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mnnyang on 17-4-29.
 */

public class FolderAdapter extends MoreAdapter<String> {
    private HashMap<String, ArrayList<Music>> arrayListHashMap = Cache.getFolderHashMap();

    public FolderAdapter(@LayoutRes int itemLayoutId, @NonNull List<String> data) {
        super(itemLayoutId, data);
    }

    @Override
    protected void convert(ViewHolder holder, int position) {
        String folderName = data.get(position);
        int musicCount = arrayListHashMap.get(folderName).size();

        holder.setText(R.id.tv_title, folderName);
        holder.setText(R.id.tv_second_title, musicCount + " 首歌曲");
    }
}
