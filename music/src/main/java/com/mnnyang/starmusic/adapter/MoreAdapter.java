package com.mnnyang.starmusic.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.mnnyang.starmusic.R;

import java.util.List;

/**
 * 更多点击id必须设置为 iv_item_more<br>
 * Created by mnnyang on 17-4-29.
 */

public abstract class MoreAdapter<T> extends RecyclerBaseAdapter<T> {
    public MoreAdapter(@LayoutRes int itemLayoutId, @NonNull List<T> data) {
        super(itemLayoutId, data);
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

    public MoreAdapter setMoreClickListener(MoreAdapter.ItemMoreClickListener itemMoreClickListener) {
        this.itemMoreClickListener = itemMoreClickListener;
        return this;
    }

    private MoreAdapter.ItemMoreClickListener itemMoreClickListener;

    public interface ItemMoreClickListener {
        void onMoreClick(View view, int position);
    }
}
