package com.mnnyang.starmusic.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.util.binding.BindLayout;
import com.mnnyang.starmusic.fragment.SettingFragment;
import com.mnnyang.starmusic.interfaces.BaseActivity;

/**
 * Created by mnnyang on 17-4-19.
 */

@BindLayout(R.layout.activity_set)
public class SetActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            SettingFragment settingFragment = new SettingFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fl_content, settingFragment)
                    .commit();
        }
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.text_menu_set));
    }
}
