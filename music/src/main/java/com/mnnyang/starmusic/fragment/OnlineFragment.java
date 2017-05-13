package com.mnnyang.starmusic.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.mnnyang.starmusic.R;
import com.mnnyang.starmusic.api.Constants;
import com.mnnyang.starmusic.app.Cache;
import com.mnnyang.starmusic.bean.TopListInfo;
import com.mnnyang.starmusic.util.binding.BindView;
import com.mnnyang.starmusic.activity.OnlineActivity;
import com.mnnyang.starmusic.adapter.OnlineListAdapter;
import com.mnnyang.starmusic.adapter.RecyclerBaseAdapter;
import com.mnnyang.starmusic.interfaces.BaseFragment;

import java.util.List;

/**
 *
 * Created by mnnyang on 17-4-12.
 */

public class OnlineFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_loading)
    LinearLayout llLoadingStatus;
    @BindView(R.id.ll_load_fail)
    LinearLayout llLoadFailStatus;

    private List<TopListInfo> topListInfos;

    @Override
    protected int getLayout() {
        return R.layout.fragment_online;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        topListInfos = Cache.getMusicTopList();

        if (topListInfos.isEmpty()) {
            String[] titles = getResources().getStringArray(R.array.top_list_title);
            String[] types = getResources().getStringArray(R.array.top_list_type);

            for (int i = 0; i < titles.length; i++) {
                TopListInfo info = new TopListInfo();
                info.setTitle(titles[i]).setType(types[i]);
                topListInfos.add(info);
                System.out.println(titles[i]);
            }
        }

        OnlineListAdapter listAdapter = new OnlineListAdapter(R.layout.adapter_top_list, topListInfos);
        recyclerView.setAdapter(listAdapter);

        listAdapter.setItemClickListener(new RecyclerBaseAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
                if (holder.getView(R.id.ll_top_title).getVisibility() == View.VISIBLE) {
                    return;
                }
                TopListInfo info = topListInfos.get(holder.getAdapterPosition());
                Intent intent = new Intent(getActivity(), OnlineActivity.class);
                intent.putExtra(Constants.INTENT_TOP_LIST_INFO, info);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {

            }
        });
    }
}
