package com.mnnyang.starmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.api.Constants;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.util.helper.MoreOperHelper;
import com.mnnyang.starmusic.util.helper.StatusHelper;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.Preferences;
import com.mnnyang.starmusic.util.general.ToastUtils;
import com.mnnyang.starmusic.adapter.LocalAdapter;
import com.mnnyang.starmusic.adapter.RecyclerBaseAdapter;
import com.mnnyang.starmusic.interfaces.BaseFragment;
import com.mnnyang.starmusic.view.view.PlayBarState;

/**
 * Created by mnnyang on 17-4-12.
 */

public class LocalFragment extends BaseFragment implements
        LocalAdapter.ItemMoreClickListener, RecyclerBaseAdapter.ItemClickListener {

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.ll_load_fail)
    LinearLayout llLoadFail;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private LocalAdapter localAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        initStatus();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        localAdapter = new LocalAdapter(R.layout.adapter_local_item, Cache.getMusicList());
        recyclerView.setAdapter(localAdapter);
        localAdapter.setMoreClickListener(this);
        localAdapter.setItemClickListener(this);

        //读取记录的播放的歌曲,跳转到记录的歌曲的位置
        int recordingPosition = findRecordingPosition();
        localAdapter.setCurrentPosition(recordingPosition);
        recyclerView.scrollToPosition(recordingPosition - 1 < 0 ? 0 : recordingPosition - 1);
    }

    int dyTotal;

    @Override
    public void initListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println(dy);
                dyTotal += dy;
                if (dyTotal > 10) {
                    ((PlayBarState) activity).closeBar();
                    dyTotal = 0;
                } else if (dyTotal < -10) {
                    ((PlayBarState) activity).openBar();
                    dyTotal = 0;
                }
            }
        });

    }

    /**
     * 获取记录的播放到的曲目
     */
    private int findRecordingPosition() {
        int position = -1;
        long id = Preferences.getLong(Constants.KEY_PLAYING_ID, -1);
        if (id != -1) {
            for (int i = 0; i < Cache.getMusicList().size(); i++) {
                if (id == Cache.getMusicList().get(i).getId()) {
                    position = i;
                }
            }
        }
        return position;
    }

    public void startPlay(Music music) {
        Cache.getPlayService().play(music);
    }

    /**
     * 播放歌曲变化
     */
    public void onChange(int position) {
        LogUtils.w(this, "---onChange");
        if (localAdapter != null) {
            localAdapter.updateIndicator(position);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            layoutManager.scrollToPosition(position);
        }

    }

    @Override
    public void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        startPlay(Cache.getMusicList().get(holder.getAdapterPosition()));
    }

    @Override
    public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        onMoreClick(view, holder.getAdapterPosition());
    }

    @Override
    public void onMoreClick(View view, int position) {
        Music music = Cache.getMusicList().get(position);
        MoreOperHelper bottomDialogHelper = new MoreOperHelper(LocalFragment.this, music);
        bottomDialogHelper.setDelListener(new MoreOperHelper.DelListener() {
            @Override
            public void onDelComplete() {
                LogUtils.i(this, "onDelComplete");
                initStatus();

                localAdapter.notifyDataSetChanged();
                ToastUtils.show(getString(R.string.del_success));
            }
        }).show();
    }

    private void initStatus() {
        if (Cache.getMusicList().isEmpty()) {
            StatusHelper.status(recyclerView, llLoading, llLoadFail, StatusHelper.Status.FAIL);
            TextView tvFailInfo = (TextView) llLoadFail.findViewById(R.id.tv_load_fail_info);
            tvFailInfo.setText("没有找到歌曲");
        }
    }
}
