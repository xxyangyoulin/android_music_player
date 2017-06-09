package com.mnnyang.starmusic.view.widght;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.bean.LyricLine;
import com.mnnyang.starmusic.util.LrcUtils;
import com.mnnyang.starmusic.util.general.LogUtils;

import java.util.ArrayList;

/**
 * 自己定义的 不忍直视.!!...<br>
 * Created by mnnyang on 17-4-6.
 */

public class LV extends View {

    private int highLightColor = Color.GREEN;
    private int defaultColor = Color.WHITE;

    private float highLightSize;
    private float defaultSize;

    private Paint paint;
    private ArrayList<LyricLine> lyrics;

    private int heightIndex;
    private float highLightY;

    private float rowHeight;
    private int currentPosition;
    private float mScrollY;
    private boolean userTouch;
    private boolean mSliding;


    public LV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        highLightSize = getResources().getDimension(R.dimen.high_light_size);
        defaultSize = getResources().getDimension(R.dimen.default_size);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(defaultColor);
        paint.setTextSize(defaultSize);

        rowHeight = getTextHeight("哈哈") + 15;

        //模拟数据
        /*lyrics = new ArrayList<Lyric>();
        for (int i = 0; i < 20; i++) {
            lyrics.add(new Lyric(i * 2000, "我要发财了" + i));
        }*/
    }

    private void smoothScrollTo(float toY) {
        final ValueAnimator animator = ValueAnimator.ofFloat(mScrollY, toY);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (userTouch) {
                    animator.cancel();
                    return;
                }
                mScrollY = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mSliding = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSliding = false;
                invalidate();
            }
        });
        animator.setDuration(640);
        animator.setInterpolator(new OvershootInterpolator(0.5f));
//        animator.setTarget(this);


        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (lyrics == null) {
            drawHorizontalText(canvas, "没有歌词哦", getHeight() / 2, true);
            return;
        }

//        heightIndex = 5;
        LyricLine lyric = lyrics.get(heightIndex);

        if (heightIndex != lyrics.size() - 1) {

            int showedTime = currentPosition - lyric.getStartShowPosition();
            int totalTime = lyrics.get(heightIndex + 1).getStartShowPosition() - lyric.getStartShowPosition();

            float scale = ((float) showedTime) / totalTime;

            float translateY = scale * rowHeight;
            //移动画布
            canvas.translate(9, -translateY);
        }

        //画高亮歌词
        drawCenterText(canvas, lyric.getText());


        //画高亮上面的歌词
        for (int i = 0; i < heightIndex; i++) {
            float y = highLightY - (heightIndex - i) * rowHeight;
            drawHorizontalText(canvas, lyrics.get(i).getText(), y, false);
        }

        //画高亮下面的歌词
        for (int i = heightIndex + 1; i < lyrics.size(); i++) {
            float y = highLightY + (i - heightIndex) * rowHeight;
            drawHorizontalText(canvas, lyrics.get(i).getText(), y, false);
        }

    }

    /**
     * 画水平和垂直都居中的文本
     *
     * @param canvas
     * @param text
     */
    private void drawCenterText(Canvas canvas, String text) {
        int textHeight = getTextHeight(text);
        highLightY = getHeight() / 2 + textHeight / 2;

        drawHorizontalText(canvas, text, highLightY, true);
    }

    /**
     * 画水平居中的文字
     */
    private void drawHorizontalText(Canvas canvas, String text, float y, boolean isHighlight) {
        paint.setTextSize(isHighlight ? highLightSize : defaultSize);
        paint.setColor(isHighlight ? highLightColor : defaultColor);

        int textWidth = getTextWidth(text);
        int x = getWidth() / 2 - textWidth / 2;

        canvas.drawText(text, x, y, paint);
    }

    @NonNull
    private int getTextWidth(String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    @NonNull
    private int getTextHeight(String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    /**
     * 更新播放位置
     */
    public void updatePosition(int currentPosition) {

        if (lyrics == null) {
            return;
        }

        this.currentPosition = currentPosition;

        /**重新调用onDraw*/
        for (int i = 0; i < lyrics.size(); i++) {
            int startShowPosition = lyrics.get(i).getStartShowPosition();
            if (currentPosition > startShowPosition) {
                if (i == lyrics.size() - 1) {
                    heightIndex = i;
                    break;
                } else if (currentPosition < lyrics.get(i + 1).getStartShowPosition()) {
                    heightIndex = i;
                    break;
                }
            }
        }

        invalidate();
    }

    /**
     * 设置音乐路径找到歌词
     *
     * @param data
     */
    public void setMusicPath(String data) {
        heightIndex = 0;

        lyrics = LrcUtils.loadLyric(data);
    }

    public void aysFile(String data) {
        heightIndex = 0;
        lyrics = LrcUtils.analysis(data);
        if (lyrics.size()==0){
            LogUtils.e(this, "changduwei 0");
            lyrics =null;
        }
    }
}
