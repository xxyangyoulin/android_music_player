package com.mnnyang.starmusic.fragment;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.activity.AlbumActivity;
import com.mnnyang.starmusic.adapter.AlbumAdapter;
import com.mnnyang.starmusic.adapter.RecyclerBaseAdapter;
import com.mnnyang.starmusic.api.Constants;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.util.helper.StatusHelper;
import com.mnnyang.starmusic.util.rely.SpacesItemDecoration;

/**
 * Created by mnnyang on 17-4-12.
 */

public class AlbumFragment extends PagerFragment implements RecyclerBaseAdapter.ItemClickListener, AlbumAdapter.ItemMoreClickListener {

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.ll_load_fail)
    LinearLayout llLoadFail;
    private AlbumAdapter albumAdapter;


    @Override
    protected int getLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void initData() {
        initRecyclerView();
        initStatus();
    }

    @Override
    public void initListener() {
        super.initListener();
        albumAdapter.setItemClickListener(this);
        albumAdapter.setMoreClickListener(this);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
        recyclerView.addItemDecoration(decoration);
        albumAdapter = new AlbumAdapter(R.layout.adapter_album, Cache.getAlbums());
        recyclerView.setAdapter(albumAdapter);
    }

    private void initStatus() {
        if (Cache.getMusicList().isEmpty()) {
            StatusHelper.status(recyclerView, llLoading, llLoadFail, StatusHelper.Status.FAIL);
            TextView tvFailInfo = (TextView) llLoadFail.findViewById(R.id.tv_load_fail_info);
            tvFailInfo.setText("未找到任何专辑");
        }
    }

    @Override
    public void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        ImageView iv = (ImageView) holder.getView(R.id.iv_album);
        gotoAlbumActivity(iv, Cache.getAlbums().get(holder.getAdapterPosition()));
    }

    @Override
    public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        onMoreClick(view, holder.getAdapterPosition());
    }

    @Override
    public void onMoreClick(View view, int position) {

    }

    /**
     * 进入专辑详情页
     */
    private void gotoAlbumActivity(ImageView iv, String album) {
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra(Constants.INTENT_ALBUM, album);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        iv,
                        getString(R.string.translatin_name_album));
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }
}
