package com.mnnyang.starmusic.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.OnlineMusic;

import java.util.List;

/**
 * 在线音乐
 * Created by mnnyang on 17-4-19.
 */

public class OnlineMusicAdapter extends RecyclerBaseAdapter<OnlineMusic> {
    public OnlineMusicAdapter(int itemLayoutId, List<OnlineMusic> data) {
        super(itemLayoutId, data);
    }

    @Override
    protected void convert(ViewHolder holder, int position) {
        TextView tvPosition = holder.getView(R.id.tv_position);
        switch (position) {
            case 0:
            case 1:
            case 2:
                tvPosition.setTextColor(Color.YELLOW);
                break;
            default:

                tvPosition.setTextColor(Cache.getContext().getResources().getColor(R.color.white_ee));

                break;
        }
        if (position != 0 && position != 1 && position != 2 && (position + 1) % 10 != 0) {
            holder.getView(R.id.tv_position).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.tv_position).setVisibility(View.VISIBLE);
            String posStr = (position + 1) < 4 ? " " + (position + 1) + " " : (position + 1) + "";
            holder.setText(R.id.tv_position, posStr);
        }


        holder.setText(R.id.tv_title, data.get(position).getTitle());
        holder.setText(R.id.tv_artist, data.get(position).getArtist_name() + " - " + data.get(position).getAlbum_title());

        Glide.with(Cache.getContext())
                .load(data.get(position).getPic_small())
                .placeholder(R.drawable.def_music_icon)
                .into((ImageView) holder.getView(R.id.iv_album));
    }

    @Override
    protected void setItemEvent(final ViewHolder holder) {
        super.setItemEvent(holder);
        holder.getView(R.id.iv_item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemMoreClickListener != null) {
                    itemMoreClickListener.onMoreClick(v, holder);
                }
            }
        });
    }

    /**
     * more 监听<br>
     */
    public OnlineMusicAdapter setMoreClickListener(OnlineMusicAdapter.ItemMoreClickListener itemMoreClickListener) {
        this.itemMoreClickListener = itemMoreClickListener;
        return this;
    }

    private OnlineMusicAdapter.ItemMoreClickListener itemMoreClickListener;

    public interface ItemMoreClickListener {
        void onMoreClick(View view, ViewHolder holder);
    }
}
