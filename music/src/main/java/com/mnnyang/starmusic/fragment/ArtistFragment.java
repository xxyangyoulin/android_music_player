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
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.util.helper.StatusHelper;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.adapter.ArtistAdater;
import com.mnnyang.starmusic.adapter.RecyclerBaseAdapter;
import com.mnnyang.starmusic.interfaces.BaseFragment;

/**
 * Created by mnnyang on 17-4-12.
 */

public class ArtistFragment extends BaseFragment
        implements RecyclerBaseAdapter.ItemClickListener, ArtistAdater.ItemMoreClickListener {

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.ll_load_fail)
    LinearLayout llLoadFail;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ArtistAdater artistAdater;


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
        artistAdater = new ArtistAdater(R.layout.adapter_artist, Cache.getArtists());
        recyclerView.setAdapter(artistAdater);
        artistAdater.setItemClickListener(this);
        artistAdater.setMoreClickListener(this);
    }

    private void initStatus() {
        if (Cache.getMusicList().isEmpty()) {
            StatusHelper.status(recyclerView, llLoading, llLoadFail, StatusHelper.Status.FAIL);
            TextView tvFailInfo = (TextView) llLoadFail.findViewById(R.id.tv_load_fail_info);
            tvFailInfo.setText("没有找到任何艺术家");
        }
    }

    @Override
    public void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder) {

    }

    @Override
    public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {

    }

    @Override
    public void onMoreClick(View view, int position) {

    }
}
