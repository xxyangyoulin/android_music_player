package com.mnnyang.starmusic.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.adapter.HomeAdapter;
import com.mnnyang.starmusic.api.PlayMode;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.fragment.AlbumFragment;
import com.mnnyang.starmusic.fragment.ArtistFragment;
import com.mnnyang.starmusic.fragment.FolderFragment;
import com.mnnyang.starmusic.fragment.LocalFragment;
import com.mnnyang.starmusic.fragment.OnlineFragment;
import com.mnnyang.starmusic.fragment.PlayerFragment;
import com.mnnyang.starmusic.service.PlayService;
import com.mnnyang.starmusic.service.PlayerListener;
import com.mnnyang.starmusic.util.SystemUtils;
import com.mnnyang.starmusic.util.binding.BindLayout;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.Preferences;
import com.mnnyang.starmusic.util.general.ScreenUtils;
import com.mnnyang.starmusic.util.image.BitmapLoader;
import com.mnnyang.starmusic.util.image.ImageResizer;
import com.mnnyang.starmusic.util.rely.AppBarStateChangeListener;
import com.mnnyang.starmusic.view.widght.PlayBarState;

import static com.mnnyang.starmusic.api.Constants.KEY_PLAY_MDOE;


@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener, PlayerListener,
        NavigationView.OnNavigationItemSelectedListener, PlayBarState {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.appbar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    //bar
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.fl_play_bar_root)
    FrameLayout flPlayBarRoot;
    @BindView(R.id.iv_bar_album_playing)
    ImageView ivAlbumPlaying;
    @BindView(R.id.ll_play_bar)
    LinearLayout llPlayBar;
    @BindView(R.id.iv_bar_play)
    ImageView ivBarPlay;
    @BindView(R.id.iv_bar_next)
    ImageView ivBarNext;
    @BindView(R.id.tv_bar_title)
    TextView TvBarTitle;
    @BindView(R.id.tv_bar_artist)
    TextView tvBarArtist;
    @BindView(R.id.pb_bar_progress_bar)
    ProgressBar pbBarProgress;

    @BindView(R.id.fab_shuffle)
    FloatingActionButton fabShuffle;

    private LocalFragment localFragment;
    private OnlineFragment onlineFragment;
    private PlayerFragment playerFragment;
    /**
     * 点击菜单为夜间模式切换
     */
    private boolean isClickNavActionNight;
    /**
     * 是否打开了播放界面
     */
    private boolean playerFragmentVisible;
    private int progressBarHeight;

    @Override
    protected void initWindow() {
        initNightMode();
        ScreenUtils.setSystemBarTransparent(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePlayBar(Cache.getPlayService().getPlayingMusic(), Cache.getPlayService().isPlaying());
    }

    @Override
    public void initView() {
        initToolbar();
        initNavigationView();

        ivAlbumPlaying.setImageBitmap(ImageResizer.decodeSampledBitmapFromResources(
                getResources(), R.drawable.def_music_icon, ScreenUtils.dp2px(40), ScreenUtils.dp2px(40)));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            LogUtils.i(this, "have toolbar");
            setSupportActionBar(toolbar);
            setTitle("");
        }
    }

    private void initNavigationView() {
        /**设置MenuItem的字体颜色**/
        Resources resource = getBaseContext().getResources();
        ColorStateList csl = resource.getColorStateList(R.color.selecotor_nav_text_color);
        System.out.println("xxxxxx" + (csl == null) + "-" + (navigationView == null) + (ivAlbumPlaying == null) + (ivBarPlay == null));
        navigationView.setItemTextColor(csl);
        /*设置MenuItem默认选中项**/
//        navigationView.getMenu().getItem(0).setChecked(true);
    }


    @Override
    public void initListener() {
        drawerLayout.addDrawerListener(drawerListener);
        Cache.getPlayService().setPlayerListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        fabShuffle.setOnClickListener(this);
        ivBarPlay.setOnClickListener(this);
        ivBarNext.setOnClickListener(this);
        llPlayBar.setOnClickListener(this);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 1:
                        fabShuffle.show();
                        break;
                    case 0:
                    default:
                        fabShuffle.hide();
                        break;
                }
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, int state) {
                switch (state) {
                    case EXPANDED:
                        showBar();
                        break;
                    case COLLAPSED:
                        hideBar();
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        progressBarHeight = (int) getResources().getDimensionPixelSize(R.dimen.play_bar_height);

        HomeAdapter homeAdapter = new HomeAdapter(getSupportFragmentManager());
        onlineFragment = new OnlineFragment();
        localFragment = new LocalFragment();

        ArtistFragment artistFragment = new ArtistFragment();
        AlbumFragment albumFragment = new AlbumFragment();
        FolderFragment folderFragment = new FolderFragment();

        homeAdapter.addFragment(albumFragment);
        homeAdapter.addFragment(localFragment);
        homeAdapter.addFragment(artistFragment);
        homeAdapter.addFragment(folderFragment);
        homeAdapter.addFragment(onlineFragment);

        viewPager.setAdapter(homeAdapter);

        //关联
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1, false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(navigationView);
                break;
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        drawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.action_set:
                gotoSet();
                return true;
            case R.id.action_night:
                isClickNavActionNight = true;
                return true;
            case R.id.action_exit:
                SystemUtils.exit();
                return true;
            case R.id.action_about:
                gotoAbout();
                return true;
        }
        return false;
    }

    private void gotoSet() {
        startActivity(new Intent(this, SetActivity.class));
    }

    private void gotoAbout() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerClosed(View drawerView) {
            if (isClickNavActionNight) {
                switchNight();
                isClickNavActionNight = false;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_play_bar:
                openPlayerFragment();
                break;
            case R.id.iv_bar_play:
                resumeOrPause();
                break;
            case R.id.iv_bar_next:
                next();
                break;
            case R.id.fab_shuffle:
                startShufflePlay();
        }
    }

    /**
     * 1. 修改播放模式为随机
     * 2. 播放下一首歌曲
     */
    private void startShufflePlay() {
        setShuffleMode();
        Cache.getPlayService().next();
    }

    private void setShuffleMode() {
        PlayMode mode = PlayMode.SHUFFLE;
        Preferences.putInt(KEY_PLAY_MDOE, mode.value());

    }


    @Override
    public void onBackPressed() {
        if (playerFragmentVisible) {
            removePlayerFragment();
            return;
        }
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();
            return;
        }

        moveTaskToBack(false);
//        super.onBackPressed();

    }

    /**
     * 夜间模式切换
     */
    private void switchNight() {
        SystemUtils.switchNight(this);
    }


    private void removePlayerFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(0, R.anim.anim_fragment_down);
        transaction.hide(playerFragment);
        transaction.commitAllowingStateLoss();
        playerFragmentVisible = false;
    }

    /**
     * 打开播放界面
     */
    public void openPlayerFragment() {
        LogUtils.i(this, "openPlayerFragment");
        if (playerFragmentVisible) {
            return;
        }
        addPlayerFragment();
    }

    private void addPlayerFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_fragment_up, 0);
        if (playerFragment == null) {
            playerFragment = new PlayerFragment();
            ft.replace(android.R.id.content, playerFragment);
        } else {
            ft.show(playerFragment);
        }
        ft.commitAllowingStateLoss();
        LogUtils.i(this, "addPlayerFragment-commit");
        playerFragmentVisible = true;
    }

    private void next() {
        Cache.getPlayService().next();
    }

    @Override
    public void onStartPlay(Music currentMusic, int currentPlayingPosition) {
        LogUtils.i(this, "start play");
        updatePlayBar(currentMusic, true);

        showBar();
        if (playerFragment != null) {
            playerFragment.onStartPlay(currentMusic, currentPlayingPosition);
        }
        if (localFragment != null) {
            localFragment.onChange(currentPlayingPosition);
        }
    }

    private void initProgressBar(Music currentMusic) {
        if (currentMusic != null) {
            pbBarProgress.setMax((int) currentMusic.getDuration());
        }
        pbBarProgress.setProgress(0);
    }

    /**
     * 更新播放条 标题艺术家和专辑
     */
    public void updatePlayBar(Music music, boolean isPlay) {
        if (music != null) {
            LogUtils.i(this, "更新::" + music.getTitle() + "-----" + music.getArtist());

            TvBarTitle.setText(music.getTitle());
            tvBarArtist.setText(music.getArtist());

            if (music.getType() == Music.Type.ONLINE) {
                Glide.with(Cache.getContext())
                        .load(music.getAlbumPath())
                        .placeholder(R.drawable.def_music_icon)
//                        .bitmapTransform(new CirCleBitmapTransformation(Cache.getContext()))
                        .into(ivAlbumPlaying);
            } else {
                ivAlbumPlaying.setImageBitmap(BitmapLoader.newInstance().loadAlbumBitmap(music.getAlbumPath()));
            }
            initProgressBar(music);
        }

        ivBarPlay.setSelected(!isPlay);
    }

    /**
     * 播放状态切换
     */
    private void resumeOrPause() {
        LogUtils.d(this, "切换播放状态");
        PlayService playService = Cache.getPlayService();
        if (playService != null) {
            playService.resumeOrPause();
            return;
        }

        LogUtils.e(this, "PlayService == null");
        updatePlayBar(null, false);
    }

    @Override
    public void onPlayPause() {
        LogUtils.d(this, "onPlayPause");
        updatePlayBar(null, false);
        if (playerFragment != null) {
            playerFragment.onPlayPause();
        }
    }

    @Override
    protected void onDestroy() {
        PlayService service = Cache.getPlayService();
        if (service != null) {
            service.setPlayerListener(null);
        }
        super.onDestroy();
    }

    @Override
    public void onNext(Music music, int currentPlayingPosition) {
        LogUtils.i(this, "onChange");
        localFragment.onChange(currentPlayingPosition);
    }

    @Override
    public void onPrev(Music music, int currentPlayingPosition) {
        localFragment.onChange(currentPlayingPosition);
    }

    @Override
    public void onResume(Music currentMusic) {

    }

    @Override
    public void progressTo(int duration) {
        pbBarProgress.setProgress(duration);
        if (playerFragment != null) {
            playerFragment.progressTo(duration);
        }
    }

    private State isClose = State.OPEN;
    boolean isScrolling = false;

    @Override
    public void hideBar() {
        if (isClose != State.CLOSE && !isScrolling) {
            LogUtils.d(this, "hideBar");
            isScrolling = true;
            llBottom.animate().translationY(flPlayBarRoot.getMeasuredHeight() - ScreenUtils.dp2px(4))
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            isScrolling = false;
                        }
                    }).start();
            isClose = State.CLOSE;
        }
    }

    @Override
    public void showBar() {
        if (isClose == State.CLOSE && !isScrolling) {
            LogUtils.d(this, "showBar");
            isScrolling = true;
            llBottom.animate().translationY(0)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            isScrolling = false;
                        }
                    }).start();
            isClose = State.OPEN;
        }
    }
}
