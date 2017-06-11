package com.mnnyang.starmusic.activity;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.bean.SearchMusic;
import com.mnnyang.starmusic.util.helper.StatusHelper;
import com.mnnyang.starmusic.util.binding.BindLayout;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.util.general.LogUtils;
import com.mnnyang.starmusic.util.http.HttpCallback;
import com.mnnyang.starmusic.util.http.HttpUtils;
import com.mnnyang.starmusic.adapter.SearchAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索页面
 * Created by mnnyang on 17-4-17.
 */

@BindLayout(R.layout.activity_search)
public class SearchActivity extends BaseActivity implements View.OnClickListener, SearchView.OnQueryTextListener {
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.ll_load_fail)
    LinearLayout llLoadFail;

    @BindView(R.id.iv_toolbar_return)
    ImageView ivToolbarReturn;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<SearchMusic.Song> songList = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private TextView tvLoadStatusInfo;

    private Handler handler = new Handler();

    @Override
    public void initView() {
        super.initView();
        tvLoadStatusInfo = (TextView) llLoadFail.findViewById(R.id.tv_load_fail_info);

        initSearchView();
        initRecyclerView();
        updateStatusInfo(R.string.text_load_default_search_status_info);

        inputMethod();
    }

    private void updateStatusInfo(@StringRes int strRes) {
        tvLoadStatusInfo.setText(getString(strRes));
        StatusHelper.status(recyclerView, llLoading, llLoadFail, StatusHelper.Status.FAIL);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchAdapter(R.layout.adapter_search_list, songList);
        recyclerView.setAdapter(searchAdapter);
    }

    private void initSearchView() {
        searchView.setQueryHint(getString(R.string.text_query_hint));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);

        SearchView.SearchAutoComplete searchText = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        ImageView iv = (ImageView) searchView.findViewById(R.id.search_button);
        iv.setVisibility(View.GONE);
        searchText.setTextColor(ContextCompat.getColor(this, R.color.white_ee));
        searchText.setHintTextColor(ContextCompat.getColor(this, R.color.white_aa));

        try {
            Field field = searchView.getClass().getDeclaredField("mGoButton");
            field.setAccessible(true);
            ImageView goButton = (ImageView) field.get(searchView);
            goButton.setImageResource(R.drawable.ic_search_white_24dp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        ivToolbarReturn.setOnClickListener(this);
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("query" + query);
        searchMusic(query);
        return false;
    }

    private void searchMusic(String query) {
        songList.clear();
        searchAdapter.notifyDataSetChanged();

        //TODO 检查网络状态 决定是否进行搜索

        StatusHelper.status(recyclerView, llLoading, llLoadFail, StatusHelper.Status.LOADING);
        HttpUtils.getSong(query, new HttpCallback<SearchMusic>() {
            @Override
            public void onSuccess(SearchMusic music) {
                if (music == null || music.getSong() == null) {
                    LogUtils.w(this, "The data returned was empty");

                    updateStatusInfo(R.string.text_load_not_find_content);
                    return;
                }
                StatusHelper.status(recyclerView, llLoading, llLoadFail, StatusHelper.Status.SUCCEED);
                addAndNotify(music);
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
                updateStatusInfo(R.string.text_load_fail_and_check_network);
            }
        });
    }

    private void addAndNotify(@NonNull SearchMusic music) {
        List<SearchMusic.Song> songs = music.getSong();
        songList.clear();
        songList.addAll(songs);
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toolbar_return:
                delayedExit();
                break;
            default:

                break;
        }
    }

    private void delayedExit() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken()
                , InputMethodManager.HIDE_NOT_ALWAYS);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 200);
    }


    private void inputMethod() {
        handler.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 200);
    }
}
