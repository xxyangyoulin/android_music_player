package com.mnnyang.starmusic.adapter;

import android.view.View;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.util.general.BitmapUtils;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.image.BitmapLoader;

import java.util.List;

/**
 * 本地音乐 <br>
 * Created by mnnyang on 17-4-12.
 */

public class LocalAdapter extends RecyclerBaseAdapter<Music> {

    public LocalAdapter(int itemLayoutId, List<Music> data) {
        super(itemLayoutId, data);
    }

    public LocalAdapter setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        return this;
    }

    private int currentPosition = -1;

    @Override
    protected void convert(ViewHolder holder, int position) {
        Music music = Cache.getMusicList().get(position);

        holder.setText(R.id.tv_title, music.getTitle());
        holder.setText(R.id.tv_artist, music.getArtist());

        holder.setImageBitmap(R.id.iv_album, BitmapUtils.createCircleImage(
                BitmapLoader.newInstance().loadAlbumBitmap(music.getAlbumPath())));
        holder.getView(R.id.iv_playing).setVisibility(position == currentPosition ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void setItemEvent(final ViewHolder holder) {
        super.setItemEvent(holder);
        holder.getView(R.id.iv_item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemMoreClickListener != null) {
                    itemMoreClickListener.onMoreClick(v, holder.getAdapterPosition());
                }
            }
        });
    }

    /**
     * 更新指示器
     */
    public void updateIndicator(int currentPosition) {
        LogUtils.w(this, "updateIndicator->" + currentPosition);
        if (currentPosition == -1) {
            return;
        }
        if (currentPosition > getItemCount() - 1) {
            return;
        }
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    /**
     * more 监听<br>
     */
    public LocalAdapter setMoreClickListener(ItemMoreClickListener itemMoreClickListener) {
        this.itemMoreClickListener = itemMoreClickListener;
        return this;
    }

    private ItemMoreClickListener itemMoreClickListener;

    public interface ItemMoreClickListener {
        void onMoreClick(View view, int position);
    }
}
