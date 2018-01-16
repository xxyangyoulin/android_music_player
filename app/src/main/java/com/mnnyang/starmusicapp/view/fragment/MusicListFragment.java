package com.mnnyang.starmusicapp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mnnyang.starmusicapp.BaseLazyFragment;
import com.mnnyang.starmusicapp.R;

/**
 * Created by mnnyang on 18-1-16.
 */

public class MusicListFragment extends BaseLazyFragment {
    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.adapter_normal_list,null);
    }

    @Override
    protected void initData() {

    }
}
