package com.mnnyang.starmusic.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.adapter.MoreAdater;
import com.mnnyang.starmusic.adapter.RecyclerBaseAdapter;
import com.mnnyang.starmusic.api.Constants;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.helper.StatusHelper;
import com.mnnyang.starmusic.util.binding.BindLayout;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.util.general.ScreenUtils;
import com.mnnyang.starmusic.util.general.ToastUtils;
import com.mnnyang.starmusic.util.image.BitmapLoader;
import com.mnnyang.starmusic.interfaces.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mnnyang on 17-4-29.
 */

@BindLayout(R.layout.activity_album)
public class AlbumActivity extends BaseActivity implements RecyclerBaseAdapter.ItemClickListener, MoreAdater.ItemMoreClickListener {
    @BindView(R.id.iv_album)
    ImageView ivAlbum;
    @BindView(R.id.iv_album_bg)
    ImageView ivAlbumBg;

    @BindView(R.id.fab_play)
    FloatingActionButton fabPlay;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.ll_load_fail)
    LinearLayout llLoadFail;
    @BindView(R.id.tv_load_fail_info)
    TextView tvLoadFailInfo;

    private ArrayList<Music> musics;
    private ArrayList<Music> musicArrayList = new ArrayList<>();
    private MusicAdapter musicAdapter;

    @Override
    protected void initWindow() {
        super.initWindow();
        ScreenUtils.setSystemBarTransparent(this);
    }

    @Override
    public void initView() {
        super.initView();

        Intent intent = getIntent();
        String album = intent.getStringExtra(Constants.INTENT_ALBUM);

        initRecyclerView(album);
        initTitle(album);
        musics = Cache.getAlbumHashMap().get(album);

        Glide.with(this).load(musics.get(0).getAlbumPath()).into(ivAlbum);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupEnterAnimation();
        }
    }

    private void initRecyclerView(String album) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        musicArrayList.addAll(Cache.getAlbumHashMap().get(album));
        initStatus();

        musicAdapter = new MusicAdapter(R.layout.adapter_local_item, musicArrayList);
        recyclerView.setAdapter(musicAdapter);
    }

    private void initStatus() {
        if (musicArrayList.isEmpty()) {
            StatusHelper.status(recyclerView, null, llLoadFail, StatusHelper.Status.FAIL);
            tvLoadFailInfo.setText(getString(R.string.text_no_song));
        } else {
            StatusHelper.status(recyclerView, null, llLoadFail, StatusHelper.Status.SUCCEED);
        }
    }


    private void initTitle(String album) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(album);
        }
    }

    @Override
    public void initListener() {
        musicAdapter.setItemClickListener(this);
        musicAdapter.setMoreClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.arc_motion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    private void animateRevealShow() {
        Bitmap bitmap = BitmapLoader.newInstance().loadCacheBitmap(musics.get(0).getAlbumPath(), 550, 550);
        ivAlbumBg.setImageBitmap(bitmap);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ViewAnimationUtils.createCircularReveal(
                    ivAlbumBg,
                    (int) ivAlbum.getX(),
                    (int) ivAlbum.getY(),
                    ivAlbum.getWidth() * 2,
                    (float) Math.hypot(ivAlbumBg.getWidth(), ivAlbumBg.getHeight()))
                    .setDuration(300)
                    .start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
        return true;
    }

    @Override
    public void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        Cache.getPlayService().play(musicArrayList.get(holder.getAdapterPosition()));
    }

    @Override
    public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
        onMoreClick(view, holder.getAdapterPosition());
    }

    @Override
    public void onMoreClick(View view, int position) {
        ToastUtils.show("更多");
    }

    private class MusicAdapter extends MoreAdater<Music> {

        public MusicAdapter(@LayoutRes int itemLayoutId, @NonNull List<Music> data) {
            super(itemLayoutId, data);
        }

        @Override
        protected void convert(ViewHolder holder, int position) {
            holder.getView(R.id.iv_album).setVisibility(View.GONE);
            holder.setText(R.id.tv_title, getData().get(position).getTitle());
            holder.setText(R.id.tv_artist, getData().get(position).getArtist());

            if (Cache.getPlayService().isPlaying()) {
                holder.getView(R.id.iv_playing).setVisibility(
                        musicArrayList.get(position) == Cache.getPlayService().getPlayingMusic() ?
                                View.VISIBLE : View.INVISIBLE);
            }
        }
    }
}
