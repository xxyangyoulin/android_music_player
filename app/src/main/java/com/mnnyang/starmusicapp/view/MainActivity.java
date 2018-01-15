package com.mnnyang.starmusicapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.mnnyang.starmusicapp.BaseActivity;
import com.mnnyang.starmusicapp.R;
import com.mnnyang.starmusicapp.util.ScreenUtils;
import com.mnnyang.starmusicapp.view.widget.NavItemView;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.niv_setting)
    NavItemView nivSetting;

    @Override
    protected void beforeInit(Bundle state) {
        ScreenUtils.setSystemBarTransparent(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle state) {
        nivSetting.setSelected(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.niv_setting)
    public void setting() {
        Toast.makeText(this, "fff", Toast.LENGTH_SHORT).show();
    }
}
