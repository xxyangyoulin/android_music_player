package com.mnnyang.starmusic.interfaces;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mnnyang.starmusic.util.binding.Binder;
import com.mnnyang.starmusic.util.general.LogUtils;

/**
 * Fragment 基类
 * Created by mnnyang on 17-4-11.
 */

public abstract class BaseFragment extends Fragment implements Operation {
    View rootView;
    protected Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LogUtils.i(this, "onCreateView");
        activity = getActivity();
        rootView = inflater.inflate(getLayout(), container, false);
        Binder.bind(this, rootView);
        initView();
        initListener();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        LogUtils.i(this, "onViewCreated");
    }

    //第一次还是要在resume里面判断
    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtils.d(this, "onHiddenChanged" + hidden);
        super.onHiddenChanged(hidden);
    }

    protected abstract int getLayout();

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


}
