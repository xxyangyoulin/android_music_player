package com.mnnyang.starmusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * viewpager <br>
 * Created by mnnyang on 17-4-12.
 */

public class HomeAdapter extends FragmentPagerAdapter {

    List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    String[] title = Cache.getContext().getResources().getStringArray(R.array.home_title);

    public HomeAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(BaseFragment fragment) {
        fragments.add(fragment);
    }
}
