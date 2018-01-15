package com.mnnyang.starmusicapp.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mnnyang.starmusicapp.R;

/**
 * Created by mnnyang on 18-1-15.
 */

public class NavItemView extends FrameLayout {

    private boolean isSelected = false;
    private ImageView ivIcon;
    private TextView tvName;

    public NavItemView(@NonNull Context context) {
        super(context);
        init();
    }

    public NavItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NavItemView);
        Drawable drawable = array.getDrawable(R.styleable.NavItemView_icon);
        String name = array.getString(R.styleable.NavItemView_name);
        array.recycle();

        setName(name);
        setIcon(drawable);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_menu_item, this);
        ivIcon = findViewById(R.id.iv_icon);
        tvName = findViewById(R.id.tv_name);
        setClickable(true);
    }

    public void setName(String name) {
        if (tvName != null && name != null) {
            tvName.setText(name);
        }
    }

    public void setIcon(@DrawableRes int resId) {
        if (ivIcon != null) {
            ivIcon.setImageResource(resId);
        }
    }

    private void setIcon(Drawable drawable) {
        if (ivIcon != null && drawable != null) {
            ivIcon.setImageDrawable(drawable);
        }
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {
            setBackgroundColor(0x15000000);
        } else {
            setBackgroundColor(0x00000000);
        }
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }
}
