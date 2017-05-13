package com.mnnyang.starmusic.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.OnlineMusic;
import com.mnnyang.starmusic.bean.TopList;
import com.mnnyang.starmusic.bean.TopListInfo;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.ToastUtils;
import com.mnnyang.starmusic.util.http.HttpCallback;
import com.mnnyang.starmusic.util.http.HttpUtils;

import java.util.List;

/**
 * 排行榜 adapter
 * Created by mnnyang on 17-4-19.
 */

public class OnlineListAdapter extends RecyclerBaseAdapter<TopListInfo> {


    public OnlineListAdapter(int itemLayoutId, List<TopListInfo> data) {
        super(itemLayoutId, data);
    }

    @Override
    protected void convert(final ViewHolder holder, int position) {
        final TopListInfo info = data.get(position);
        String type = info.getType();

        if (type.equals("#")) {
            holder.getView(R.id.ll_top_title).setVisibility(View.VISIBLE);
            holder.getView(R.id.ll_top_content).setVisibility(View.GONE);
            initTopTitle(holder, info);
            return;
        }

        holder.getView(R.id.ll_top_title).setVisibility(View.GONE);
        holder.getView(R.id.ll_top_content).setVisibility(View.VISIBLE);

        if (info.isInit()) {
            updateInfo(holder, info);
            return;
        }

        setLoadingStatusInfo(holder);

        HttpUtils.getTopListInfo(info.getType(), "3", "0", new HttpCallback<TopList>() {
            @Override
            public void onSuccess(TopList list) {
                if (list == null || list.getBillboard() == null || list.getSong_list() == null) {
                    LogUtils.e(this, "getTopListInfo(): list == null || list.getBillboard() == null " +
                            "|| list.getSong_list() == null");
                    return;
                }

                List<OnlineMusic> songList = list.getSong_list();

                for (int i = 0; i < songList.size(); i++) {
                    LogUtils.i(this, songList.get(i).getTitle() + " " + songList.get(i).getArtist_name());
                }

                String picUrl = list.getBillboard().getPic_s260();

                if (songList.size() > 0) {
                    info.setMusic1("1. " + songList.get(0).getTitle() + " - " + songList.get(0).getArtist_name());
                }
                if (songList.size() > 1) {
                    info.setMusic2("2. " + songList.get(1).getTitle() + " - " + songList.get(1).getArtist_name());
                }
                if (songList.size() > 2) {
                    info.setMusic3("3. " + songList.get(2).getTitle() + " - " + songList.get(2).getArtist_name());
                }

                info.setUrl(picUrl);
                info.setInit(true);

                updateInfo(holder, info);
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
                setFailStatusInfo(holder);
                ToastUtils.show(Cache.getContext().getString(R.string.text_toast_load_fail));
            }
        });
    }

    private void setFailStatusInfo(ViewHolder holder) {
        //holder.setText(R.id.tv_title, info.getTitle());
        holder.setText(R.id.tv_music1, Cache.getContext().getString(R.string.text_load_fail));
        holder.setText(R.id.tv_music2, Cache.getContext().getString(R.string.text_load_fail));
        holder.setText(R.id.tv_music3, Cache.getContext().getString(R.string.text_load_fail));
    }

    private void initTopTitle(ViewHolder holder, TopListInfo info) {
        holder.setText(R.id.tv_header_title, info.getTitle());
    }

    private void setLoadingStatusInfo(ViewHolder holder) {
        //holder.setText(R.id.tv_title, info.getTitle());
        holder.setText(R.id.tv_music1, Cache.getContext().getString(R.string.text_loading));
        holder.setText(R.id.tv_music2, Cache.getContext().getString(R.string.text_loading));
        holder.setText(R.id.tv_music3, Cache.getContext().getString(R.string.text_loading));
    }

    private void updateInfo(ViewHolder holder, TopListInfo info) {
        //holder.setText(R.id.tv_title, info.getTitle());
        Glide.with(Cache.getContext())
                .load(info.getUrl())
                .placeholder(R.drawable.def_music_icon)
                .into((ImageView) holder.getView(R.id.iv_header_list_pic));

        String music1 = info.getMusic1();
        holder.setText(R.id.tv_music1, TextUtils.isEmpty(music1) ? "" : music1);
        String music2 = info.getMusic2();
        holder.setText(R.id.tv_music2, TextUtils.isEmpty(music2) ? "" : music2);
        String music3 = info.getMusic3();
        holder.setText(R.id.tv_music3, TextUtils.isEmpty(music3) ? "" : music3);
    }
}
