package com.mnnyang.starmusic.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mnnyang on 17-4-29.
 */

public class AlbumAdater extends MoreAdater<String> {
    HashMap<String, ArrayList<Music>> albumHashMap = Cache.getAlbumHashMap();

    public AlbumAdater(@LayoutRes int itemLayoutId, @NonNull List<String> data) {
        super(itemLayoutId, data);
    }

    @Override
    protected void convert(ViewHolder holder, int position) {
        String album = data.get(position);
        holder.setText(R.id.tv_title, album);
        holder.setText(R.id.tv_second_title, albumHashMap.get(album).get(0).getArtist());
        ImageView iv = holder.getView(R.id.iv_album);

        Glide.with(Cache.getContext())
                //TODO 设置默认图片
                .load(albumHashMap.get(album).get(0).getAlbumPath())
                .into(iv);
    }
}
