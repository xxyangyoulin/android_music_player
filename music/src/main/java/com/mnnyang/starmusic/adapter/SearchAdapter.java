package com.mnnyang.starmusic.adapter;

import android.view.View;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.bean.SearchMusic;
import com.mnnyang.starmusic.helper.PlayMusicHelper;
import com.mnnyang.starmusic.util.general.ToastUtils;

import java.util.List;

/**
 * Created by mnnyang on 17-4-21.
 */

public class SearchAdapter extends RecyclerBaseAdapter<SearchMusic.Song> {
    public SearchAdapter(int itemLayoutId, List<SearchMusic.Song> data) {
        super(itemLayoutId, data);
    }

    @Override
    protected void convert(ViewHolder holder, int position) {
        holder.setText(R.id.tv_title, data.get(position).getSongname());
        holder.setText(R.id.tv_artist, data.get(position).getArtistname());
    }

    @Override
    protected void setItemEvent(final ViewHolder holder) {
        holder.getView(R.id.iv_item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("more");
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusicHelper playOnlineMusic = new PlayMusicHelper();
                playOnlineMusic.playMusic(data.get(holder.getAdapterPosition()).getSongid(), null);
            }
        });
    }

}
