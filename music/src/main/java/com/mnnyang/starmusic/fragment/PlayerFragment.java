package com.mnnyang.starmusic.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.api.PlayMode;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.Lrc;
import com.mnnyang.starmusic.bean.Music;
import com.mnnyang.starmusic.bean.SearchMusic;
import com.mnnyang.starmusic.service.PlayerListener;
import com.mnnyang.starmusic.util.LrcUtils;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.util.general.FileUtils;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.general.Preferences;
import com.mnnyang.starmusic.util.general.ScreenUtils;
import com.mnnyang.starmusic.util.general.ToastUtils;
import com.mnnyang.starmusic.util.http.HttpCallback;
import com.mnnyang.starmusic.util.http.HttpUtils;
import com.mnnyang.starmusic.util.image.BitmapLoader;
import com.mnnyang.starmusic.util.image.GlideUtils;
import com.mnnyang.starmusic.view.widght.LyricView;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mnnyang.starmusic.api.Constants.KEY_PLAY_MDOE;

/**
 * Created by mnnyang on 17-4-13.
 */

public class PlayerFragment extends BaseFragment implements PlayerListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.iv_player_bg2)
    ImageView ivBlurBg2;
    @BindView(R.id.iv_player_bg1)
    ImageView ivBlurBg1;

    @BindView(R.id.iv_toolbar_return)
    ImageView ivToolbarReturn;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_artist)
    TextView getTvToolbarArtist;

    @BindView(R.id.sb_player_seek_bar)
    SeekBar seekBar;

    @BindView(R.id.iv_player_play_mode)
    ImageView ivPlayMode;
    @BindView(R.id.fab_play)
    FloatingActionButton fabPlayPause;
    @BindView(R.id.iv_player_prev)
    ImageView ivPrev;
    @BindView(R.id.iv_player_next)
    ImageView ivNext;
    @BindView(R.id.iv_like)
    ImageView ivList;
    private Music oldMusic;

    @BindView(R.id.lyric_view)
    LyricView lyricView;

    private AlphaAnimation showAlphaAnimation;
    private AlphaAnimation hideAlphaAnimation;
    private Bitmap blurBitmap;

    @Override
    protected int getLayout() {
        return R.layout.fragment_player;
    }


    @Override
    public void initView() {
        initStatusBarPadding();
        lyricView.setHighLightTextColor(Color.WHITE);
        lyricView.setLineSpace(26);
        lyricView.setPlayable(true);
        initModeShow();
    }

    @Override
    public void initListener() {
        seekBar.setOnSeekBarChangeListener(this);
        ivToolbarReturn.setOnClickListener(this);
        fabPlayPause.setOnClickListener(this);
        ivPlayMode.setOnClickListener(this);
        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivList.setOnClickListener(this);


        lyricView.setOnPlayerClickListener(new LyricView.OnPlayerClickListener() {
            @Override
            public void onPlayerClicked(long progress, String content) {
                seekTo((int) progress);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toolbar_return:
                getActivity().onBackPressed();
                break;
            case R.id.iv_player_play_mode:
                changePlayMode();
                break;
            case R.id.fab_play:
                play();
                break;
            case R.id.iv_player_prev:
                prev();
                break;
            case R.id.iv_player_next:
                next();
                break;
            case R.id.iv_like:
                list();
                break;
        }
    }

    private void changePlayMode() {
        PlayMode mode = PlayMode.valueOf(Preferences.getInt(KEY_PLAY_MDOE, 0));
        switch (mode) {
            case LOOP:
                mode = PlayMode.SINGLE;
                ToastUtils.show(getString(R.string.mode_single));
                break;
            case SINGLE:
                mode = PlayMode.SHUFFLE;
                ToastUtils.show(getString(R.string.mode_shuffle));
                break;
            case SHUFFLE:
                mode = PlayMode.LOOP;
                ToastUtils.show(getString(R.string.mode_loop));
                break;
        }
        Preferences.putInt(KEY_PLAY_MDOE, mode.value());
        initModeShow();

    }

    private void initModeShow() {
        int mode = Preferences.getInt(KEY_PLAY_MDOE, 0);
        ivPlayMode.setImageLevel(mode);
    }

    private void share() {
        Toast.makeText(getActivity(), "share", Toast.LENGTH_SHORT).show();
    }

    private void list() {

    }

    private void next() {
        Cache.getPlayService().next();
    }

    private void prev() {
        Cache.getPlayService().prev();
    }

    private void play() {
        Cache.getPlayService().resumeOrPause();
    }

    /**
     * 透明状态栏导致布局上移解决
     */
    private void initStatusBarPadding() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = ScreenUtils.getStatusBarHeight(getContext());
            if (statusBarHeight > 0) {
                llContent.setPadding(0, statusBarHeight, 0, 0);
            }

        }
    }

    @Override
    public void initData() {
        oldMusic = Cache.getPlayService().getPlayingMusic();
        initMusicInfoShow(oldMusic);

        Bitmap blurAlbumBitmap = BitmapLoader.newInstance().getBlurAlbumBitmap();
        if (blurAlbumBitmap != null) {
            if (isBlur1Top) {
                ivBlurBg1.setImageBitmap(blurAlbumBitmap);
            } else {
                ivBlurBg2.setImageBitmap(blurAlbumBitmap);
            }
        } else {
            setBlurBackground(oldMusic);
        }
    }

    private void initMusicInfoShow(Music music) {
        initProgress(music);
        initDuration(music);

        updateToolbarInfo(music);
        updatePlayButtonState(Cache.getPlayService().isPlaying());
    }

    private void initProgress(Music music) {
        seekBar.setMax(music != null ? (int) music.getDuration() : 0);
        seekBar.setProgress(0);

//        uiSeekbar.setMax(music != null ? (int) music.getDuration() : 0);
//        uiSeekbar.setProgress(0);
    }


    private void initDuration(Music oldMusic) {
        /*if (oldMusic != null) {
            long duration = oldMusic.getDuration();
            tvTotalTime.setText(TimeFormat.format(duration));
            tvProgressTime.setText(getString(R.string.play_show_time, "00", "00"));
        } else {
            tvTotalTime.setText(getString(R.string.play_show_time, "--", "--"));
            tvProgressTime.setText(getString(R.string.play_show_time, "--", "--"));
        }*/
    }

    private void updateDuration(int duration) {
//        tvProgressTime.setText(TimeFormat.format(duration));
    }

    private void updateToolbarInfo(Music music) {
        tvToolbarTitle.setText(music != null ? music.getTitle() : getString(R.string.app_name));
        getTvToolbarArtist.setText(music != null ? music.getArtist() : getString(R.string.default_artist));
    }


    private void updatePlayButtonState(boolean isPlaying) {
        fabPlayPause.setSelected(!isPlaying);
    }


    /**
     * 切换标记
     */
    private boolean isBlur1Top = true;

    /**
     * 设置模糊背景
     */
    private void setBlurBackground(Music music) {
        if (music == null) {
            return;
        }

        if (music.getType() == Music.Type.ONLINE) {
            GlideUtils.loadBlur(ivBlurBg1, music.getAlbumBigPic());
            GlideUtils.loadBlur(ivBlurBg2, music.getAlbumBigPic());
            return;
        }

        BitmapLoader.newInstance().loadBlurAlbumBitmap(music.getAlbumPath(), new BitmapLoader.BlurCompleteListener() {
            @Override
            public void onComplete(Bitmap bitmap) {
                blurBitmap = bitmap;
                Cache.newInstance().handler.removeCallbacks(runnable);
                Cache.newInstance().handler.postDelayed(runnable, 100);
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            alphaAnim(blurBitmap);
        }
    };

    private void alphaAnim(Bitmap bitmap) {
        if (showAlphaAnimation == null) {
            showAlphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha_show);
            hideAlphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha_hide);
        }

        if (isBlur1Top) {
            ivBlurBg2.setImageBitmap(bitmap);
            ivBlurBg2.startAnimation(showAlphaAnimation);
            ivBlurBg1.startAnimation(hideAlphaAnimation);
        } else {
            ivBlurBg1.setImageBitmap(bitmap);
            ivBlurBg1.startAnimation(showAlphaAnimation);
            ivBlurBg2.startAnimation(hideAlphaAnimation);
        }
        BitmapLoader.newInstance().setBlurAlbumBitmap(bitmap);

        isBlur1Top = !isBlur1Top;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(this, "onResume");
        //获取歌词
        if (oldMusic != null) {
            UpdateLrc(oldMusic);
        }
    }


    @Override
    public void onStartPlay(Music currentMusic, int currentPlayingPosition) {
        LogUtils.i(this, "onStartPlay");
        //新播放的和上一首相同
        updatePlayButtonState(true);
        if (currentMusic != null && currentMusic != oldMusic) {
            //更换背景
            initMusicInfoShow(currentMusic);
            setBlurBackground(currentMusic);
            oldMusic = currentMusic;

            //初始化时间
            initDuration(currentMusic);

            //获取歌词
            UpdateLrc(currentMusic);
        }
    }

    private void UpdateLrc(@NonNull final Music currentMusic) {
        lyricView.reset("歌词加载中...");
        //先在本地路径查找:
        final String lrcName = getLrcName(currentMusic);
        final File lrcFile = new File(FileUtils.LRC_PATH, lrcName);
        if (lrcFile.exists()) {
            String lrcString = LrcUtils.readLyricFile(lrcFile);

            Matcher m1 = Pattern.compile("[0-9]{2}:[0-9]{2}.[0-9]{2}").matcher(lrcString);

            if (m1.find()) {
                System.out.println("正常歌词");
                lyricView.setLyricFile(lrcFile, "UTF-8");
            } else {
                System.out.println("不正常歌词");
                setEmptyLrc();
            }
            return;
        }
        if (!TextUtils.isEmpty(currentMusic.getLrcLink())) {
            LogUtils.i(this, "来自在线歌词" + currentMusic.getLrcLink());
            HttpUtils.downLrc(currentMusic.getLrcLink(), currentMusic.getTitle(), new HttpCallback<File>() {
                @Override
                public void onSuccess(File file) {
                    lyricView.setLyricFile(file, "UTF-8");
                }

                @Override
                public void onFail(Exception e) {
                    e.printStackTrace();
                    LogUtils.e(this, "歌词下载失败");
                    setEmptyLrc();
                }
            });

            return;
        }


        HttpUtils.getSong(currentMusic.getTitle(), new HttpCallback<SearchMusic>() {
            @Override
            public void onSuccess(SearchMusic music) {
                if (music != null && music.getSong() != null && music.getSong().size() > 0) {
                    HttpUtils.getLrc(music.getSong().get(0).getSongid(), new HttpCallback<Lrc>() {
                        @Override
                        public void onSuccess(Lrc lrc) {
                            //TODO 可能歌词只是纯文本
                            String content = lrc.getLrcContent();
                            System.out.println("--->" + content + "---");

                            if (!TextUtils.isEmpty(content)) {
                                FileUtils.saveFile(FileUtils.LRC_PATH, lrcName, content);
                                lyricView.setLyricFile(lrcFile, "UTF-8");
                            } else {
                                setEmptyLrc();
                            }
                        }

                        @Override
                        public void onFail(Exception e) {
                            e.printStackTrace();
                            setEmptyLrc();
                        }
                    });
                } else {
                    setEmptyLrc();
                }
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
                setEmptyLrc();
            }
        });
    }

    private void setEmptyLrc() {
        lyricView.reset("暂无歌词, 请您欣赏");
    }

    @NonNull
    private String getLrcName(@NonNull Music currentMusic) {
        return currentMusic.getTitle() + ".lrc";
    }


    @Override
    public void onPlayPause() {
        updatePlayButtonState(false);
    }

    @Override
    public void onNext(Music music, int currentPlayingPosition) {

    }

    @Override
    public void onPrev(Music music, int currentPlayingPosition) {

    }

    @Override
    public void onResume(Music currentMusic) {

    }

    @Override
    public void progressTo(int duration) {
        if (seekBar == null) {
            return;
        }
        lyricView.setCurrentTimeMillis(duration);

        seekBar.setProgress(duration);
//        uiSeekbar.setProgress(duration);
        updateDuration(duration);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            seekTo(progress);
        }
    }

    private void seekTo(int progress) {
        Cache.getPlayService().seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
