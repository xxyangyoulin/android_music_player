package com.mnnyang.starmusic.activity;

import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.widget.TextView;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.util.binding.BindLayout;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.interfaces.BaseActivity;

/**
 * Created by mnnyang on 17-4-16.
 */

@BindLayout(R.layout.activity_about)
public class AboutActivity extends BaseActivity {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        tvAbout.setText(Html.fromHtml("<h1><font >圆点音乐</font></h1>\n" +
                "<p><font size=\"5\"  color=\"black\">当前版本:</font></p>\n" +
                "<p><font size=\"4\"  color=\"red\">v1.0</font></p>"));
    }
}
