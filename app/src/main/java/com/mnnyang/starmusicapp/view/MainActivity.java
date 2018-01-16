package com.mnnyang.starmusicapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mnnyang.starmusicapp.BaseActivity;
import com.mnnyang.starmusicapp.R;
import com.mnnyang.starmusicapp.util.LogUtil;
import com.mnnyang.starmusicapp.util.ScreenUtils;
import com.mnnyang.starmusicapp.view.adapter.HomeAdapter;
import com.mnnyang.starmusicapp.view.fragment.MusicListFragment;
import com.mnnyang.starmusicapp.view.widget.NavItemView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindViews({R.id.niv_setting, R.id.niv_theme})
    List<NavItemView> navList;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle state) {
        navList.get(0).setSelected(true);

        initStatusBarHeight();
        initToolbarDrawerLayout();
        initViewPager();
    }

    private void initViewPager() {
        HomeAdapter homeAdapter = new HomeAdapter(getSupportFragmentManager());
        homeAdapter.addFragment(new MusicListFragment());
        homeAdapter.addTitles("歌曲");
        viewPager.setAdapter(homeAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initStatusBarHeight() {
        ViewGroup.LayoutParams params = viewStatusBar.getLayoutParams();
        params.height = ScreenUtils.getStatusBarHeight(this);
        if (params.height != 0) {
            LogUtil.e(this, params.height + "");
            viewStatusBar.setLayoutParams(params);
        }
    }

    private void initToolbarDrawerLayout() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name,
                R.string.app_name);
        toggle.syncState();
        drawerLayout.setDrawerListener(toggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick({R.id.niv_setting, R.id.niv_theme})
    public void navClick(View view) {

        if (((NavItemView) view).isSelectAble()) {
            for (NavItemView itemView : navList) {
                itemView.setSelected(view == itemView);
            }
        }
        navOnClick(view.getId());
    }

    private void navOnClick(int id) {
        switch (id) {
            case R.id.niv_setting:
                Toast.makeText(this, "set", Toast.LENGTH_SHORT).show();
                break;
            case R.id.niv_theme:
                Toast.makeText(this, "theme", Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
