package com.mnnyang.starmusic.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.api.Constants;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.bean.OnlineMusic;
import com.mnnyang.starmusic.bean.TopList;
import com.mnnyang.starmusic.bean.TopListInfo;
import com.mnnyang.starmusic.helper.MoreOperHelper;
import com.mnnyang.starmusic.helper.PlayMusicHelper;
import com.mnnyang.starmusic.helper.StatusHelper;
import com.mnnyang.starmusic.util.binding.BindLayout;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.ToastUtils;
import com.mnnyang.starmusic.util.http.HttpCallback;
import com.mnnyang.starmusic.util.http.HttpUtils;
import com.mnnyang.starmusic.adapter.OnlineMusicAdapter;
import com.mnnyang.starmusic.adapter.RecyclerBaseAdapter;
import com.mnnyang.starmusic.interfaces.BaseActivity;
import com.mnnyang.starmusic.interfaces.BlurBitmapTransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * OnlineActivity
 * Created by mnnyang on 17-4-19.
 */

@BindLayout(R.layout.activity_online)
public class OnlineActivity extends BaseActivity implements RecyclerBaseAdapter.ItemClickListener, OnlineMusicAdapter.ItemMoreClickListener {
    /**
     * 每次更新的条数
     */
    private static final String ONCE_SIZE = "10";

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.ll_load_fail)
    LinearLayout llLoadFail;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TopListInfo topListInfo;
    private boolean isLoadingMore;
    private boolean isNoMoreMusic;

    //在线歌曲数据
    private List<OnlineMusic> onlineMusics;
    private OnlineMusicAdapter musicAdapter;
    private View header;
    private LinearLayoutManager linearLayoutManager;
    private View footer;
    private String songType;
    private LinearLayout loading;
    private TextView tvNoMoreOrFail;

    @Override
    public void initData() {
        onlineMusics = new ArrayList<>();

        parseIntent();
        initRecyclerView();

        if (topListInfo != null) {
            setTitle(topListInfo.getTitle().replace("百度", ""));

            songType = topListInfo.getType();
            init();
        }
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        musicAdapter = new OnlineMusicAdapter(R.layout.adapter_music_list, onlineMusics);

        header = View.inflate(this, R.layout.layout_top_list_header, null);
        footer = View.inflate(this, R.layout.layout_top_list_footer, null);
        musicAdapter.setHeader(header);
        musicAdapter.setFooter(footer);
        musicAdapter.setItemClickListener(this);
        musicAdapter.setMoreClickListener(this);

        recyclerView.setAdapter(musicAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int count = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastVisibleItemPosition();

                LogUtils.d(this, "count" + count + "  last" + lastVisible);

                if (lastVisible == count - 1 && !isLoadingMore) {
                    loadMore(count - 2);//减去头布局和尾布局多出来的数
                } else {
                    LogUtils.d(this, "loading..");
                }
            }
        });
    }

    private void parseIntent() {
        Intent intent = getIntent();
        topListInfo = (TopListInfo) intent.getSerializableExtra(Constants.INTENT_TOP_LIST_INFO);
    }

    private void init() {
        StatusHelper.status(recyclerView, llLoading, llLoadFail, StatusHelper.Status.LOADING);
        HttpUtils.getTopListInfo(songType, "10", "0", new HttpCallback<TopList>() {
            @Override
            public void onSuccess(TopList list) {
                if (list == null || list.getSong_list() == null || list.getBillboard() == null) {
                    LogUtils.w(this, "The data returned was empty");
                    StatusHelper.status(recyclerView, llLoading, llLoadFail, StatusHelper.Status.FAIL);
                    return;
                }
                StatusHelper.status(recyclerView, llLoading, llLoadFail, StatusHelper.Status.SUCCEED);

                if (list.getSong_list().size() < 10) {
                    changeLoadMoreStatus(StatusHelper.Status.NO_MORE);
                    isNoMoreMusic = true;
                }

                initHeader(list);
                addAndNotify(list.getSong_list());
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
                StatusHelper.status(recyclerView, llLoading, llLoadFail, StatusHelper.Status.FAIL);
            }
        });
    }

    private void loadMore(int count) {
        if (isNoMoreMusic) {
            LogUtils.d(this, "loadMore() isNoMoreMusic");
            return;
        }
        if (count < 10) {
            isNoMoreMusic = true;
            LogUtils.d(this, "loadMore() isNoMoreMusic");
            changeLoadMoreStatus(StatusHelper.Status.NO_MORE);
            return;
        }

        LogUtils.i(this, "loadMore() before loading count:" + count);
        isLoadingMore = true;

        changeLoadMoreStatus(StatusHelper.Status.LOADING);
        HttpUtils.getTopListInfo(songType, ONCE_SIZE, count + "", new HttpCallback<TopList>() {
            @Override
            public void onSuccess(TopList list) {
                isLoadingMore = false;

                if (list == null || list.getSong_list() == null || list.getBillboard() == null) {
                    isNoMoreMusic = true;
                    LogUtils.w(this, "The more data returned was empty");
                    changeLoadMoreStatus(StatusHelper.Status.NO_MORE);
                    return;
                }
                changeLoadMoreStatus(StatusHelper.Status.SUCCEED);
                if (list.getSong_list().size() < 10) {
                    isNoMoreMusic = true;
                    changeLoadMoreStatus(StatusHelper.Status.NO_MORE);
                }

                initHeader(list);
                addAndNotify(list.getSong_list());
            }

            @Override
            public void onFail(Exception e) {
                isLoadingMore = false;
                changeLoadMoreStatus(StatusHelper.Status.FAIL);

                e.printStackTrace();
            }
        });
    }


    @Override
    public void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        ToastUtils.show("item" + holder.getAdapterPosition());
        int index = holder.getAdapterPosition() - 1;

        String songId = onlineMusics.get(index).getSong_id();
        String lrcLink = onlineMusics.get(index).getLrclink();

        //TODO 歌曲获取失败, 导致胡乱播放本地歌曲
        new PlayMusicHelper().playMusic(songId, lrcLink);
    }

    @Override
    public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        onMoreClick(view, holder);
    }

    @Override
    public void onMoreClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        int index = holder.getAdapterPosition() - 1;

        OnlineMusic onlineMusic = onlineMusics.get(index);
        Music music = new Music()
                .setType(Music.Type.ONLINE)
                .setId(Long.parseLong(onlineMusic.getSong_id()))
                .setTitle(onlineMusic.getTitle())
                .setAlbum(onlineMusic.getAlbum_title())
                .setArtist(onlineMusic.getArtist_name());

        MoreOperHelper helper = new MoreOperHelper(this, music);
        helper.show();
    }

    private void addAndNotify(List<OnlineMusic> list) {
        onlineMusics.addAll(list);
        musicAdapter.notifyDataSetChanged();
    }

    private void initHeader(TopList list) {
        TextView tvHeaderTitle = (TextView) header.findViewById(R.id.tv_header_title);
        TextView tvHeaderTime = (TextView) header.findViewById(R.id.tv_header_time);
        TextView tvHeaderDescribe = (TextView) header.findViewById(R.id.tv_header_describe);
        ImageView ivHeaderListPic = (ImageView) header.findViewById(R.id.iv_header_list_pic);
        ImageView ivHeaderBlurBg = (ImageView) header.findViewById(R.id.iv_header_blur_bg);

        TopList.Billboard billboard = list.getBillboard();
        tvHeaderTitle.setText(billboard.getName());
        tvHeaderDescribe.setText(billboard.getComment());
        tvHeaderTime.setText(billboard.getUpdate_date());

        Glide.with(Cache.getContext())
                .load(billboard.getPic_s444())
                .placeholder(R.drawable.def_music_icon)
                .bitmapTransform(new BlurBitmapTransformation(Cache.getContext()))
                .into(ivHeaderBlurBg);

        Glide.with(Cache.getContext())
                .load(billboard.getPic_s640())
                .placeholder(R.drawable.def_music_icon)
                .into(ivHeaderListPic);
    }

    private void changeLoadMoreStatus(StatusHelper.Status status) {
        if (loading == null || tvNoMoreOrFail == null) {
            loading = (LinearLayout) footer.findViewById(R.id.ll_loading);
            tvNoMoreOrFail = (TextView) footer.findViewById(R.id.tv_no_more_or_fail);
        }
        switch (status) {
            case SUCCEED:
                tvNoMoreOrFail.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                break;
            case LOADING:
                loading.setVisibility(View.VISIBLE);
                tvNoMoreOrFail.setVisibility(View.GONE);
                break;
            case FAIL:
                loading.setVisibility(View.GONE);
                tvNoMoreOrFail.setVisibility(View.VISIBLE);
                tvNoMoreOrFail.setText(getString(R.string.text_load_more_fail));
                break;
            case NO_MORE:
                loading.setVisibility(View.GONE);
                tvNoMoreOrFail.setVisibility(View.VISIBLE);
                tvNoMoreOrFail.setText(getString(R.string.text_no_more));
                break;
            default:
                break;
        }
    }


}
