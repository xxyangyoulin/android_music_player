package com.mnnyang.starmusic.activity;

import android.support.design.widget.FloatingActionButton;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.interfaces.BaseActivity;
import com.mnnyang.starmusic.util.binding.BindLayout;
import com.mnnyang.starmusic.util.binding.BindView;

/**
 * Created by mnnyang on 17-4-16.
 */

@BindLayout(R.layout.activity_about)
public class AboutActivity extends BaseActivity {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {

    }
}
