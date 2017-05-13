package com.mnnyang.starmusic.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.mnnyang.starmusic.api.Constants;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.helper.StatusHelper;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.activity.AlbumActivity;
import com.mnnyang.starmusic.adapter.AlbumAdater;
import com.mnnyang.starmusic.adapter.RecyclerBaseAdapter;
import com.mnnyang.starmusic.interfaces.BaseFragment;

/**
 * Created by mnnyang on 17-4-12.
 */

public class AlbumFragment extends BaseFragment implements RecyclerBaseAdapter.ItemClickListener, AlbumAdater.ItemMoreClickListener {

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.ll_load_fail)
    LinearLayout llLoadFail;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private AlbumAdater albumAdater;


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
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
        recyclerView.addItemDecoration(decoration);
        albumAdater = new AlbumAdater(R.layout.adapter_album, Cache.getAlbums());
        recyclerView.setAdapter(albumAdater);
        albumAdater.setItemClickListener(this);
        albumAdater.setMoreClickListener(this);

    }

    private void initStatus() {
        if (Cache.getMusicList().isEmpty()) {
            StatusHelper.status(recyclerView, llLoading, llLoadFail, StatusHelper.Status.FAIL);
            TextView tvFailInfo = (TextView) llLoadFail.findViewById(R.id.tv_load_fail_info);
            tvFailInfo.setText("没有找到任何专辑");
        }
    }

    @Override
    public void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        ImageView iv = (ImageView) holder.getView(R.id.iv_album);
        gotoAlbumActivity(iv, Cache.getAlbums().get(holder.getAdapterPosition()));
    }

    private void gotoAlbumActivity(ImageView iv, String album) {
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra(Constants.INTENT_ALBUM, album);


        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        iv,
                        getString(R.string.translatin_name_album));
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

    }

    @Override
    public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        onMoreClick(view, holder.getAdapterPosition());
    }

    @Override
    public void onMoreClick(View view, int position) {

    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;
            //注释这两行是为了上下间距相同
            int position = parent.getChildAdapterPosition(view);
            if (position == 0 || position == 1) {
                outRect.top = space * 2;
            }

            if (position % 2 == 0) {
                outRect.right = space / 2;
            } else {
                outRect.left = space / 2;
            }
        }
    }
}
