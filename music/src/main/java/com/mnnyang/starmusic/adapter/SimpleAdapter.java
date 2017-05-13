package com.mnnyang.starmusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mnnyang.starmusic.bean.Lrc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Lrc 替换曾自己的Bean
 * Created by mnnyang on 2016/11/12.
 */

public abstract class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHeader> {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_HEADER = 2;
    public static final int TYPE_FOOTER = 3;
    public static final int TYPE_STATE_MORE = 4;

    public static final int STATE_MORE_NO_MORE = 5;
    public static final int STATE_MORE_MORE = 6;
    public static final int STATE_MORE_ERROR = 7;


    /**
     * 左滑动
     */
    public static final int LEFT_SWIPE = 8;
    /**
     * 右滑动
     */
    public static final int RIGHT_SWIPE = 9;
    /**
     *
     */
    public static final int LEFT_AND_RIGHT_SWIPE = 10;

    //当前滑动方向
    private int currentSwipeDirection = RIGHT_SWIPE;

    public void setDatas(List<Lrc> datas) {
        mDatas = datas;
    }

    /**
     * 数据
     */
    private List<Lrc> mDatas;
    /**
     * 头布局
     */
    protected View mHeaderView;
    /**
     * 尾布局
     */
    protected View mFooterView;
    /**
     * 加载更多布局
     */
    protected View mLoadingMoreStateView;
    /**
     * 空数据布局
     */
    protected View mEmptyView;
    /**
     * 满屏头布局
     */
    private boolean mIsSpanHeader = true;
    /**
     * 满屏尾布局
     */
    private boolean mIsSpanFooter = false;
    /**
     * 满屏加载更多布局
     */
    private boolean isSpanMoreView = true;
    /**
     * 可拖拽 移动
     */
    private boolean canMove;
    /**
     * 是否有加载更多功能
     */
    private boolean hadLoadingMore = true;//TODO 先设置为true

    /**
     * 当前RecyclerView
     */
    private RecyclerView mRecyclerView;

    private int mItemLayout;
    private Context mContext;
    private final LayoutInflater mInflater;

    //点击事件
    private OnItemClickListener mItemClickListener;

    public boolean isItemViewSwipeEnabled() {
        return itemViewSwipeEnabled;
    }

    /**
     * 设置是否可以左右滑动操作
     *
     * @param itemViewSwipeEnabled 是否可以滑动
     * @param direction            可滑动的方向
     */
    public SimpleAdapter setItemViewSwipeEnabled(boolean itemViewSwipeEnabled, int direction) {
        this.itemViewSwipeEnabled = itemViewSwipeEnabled;
        currentSwipeDirection = direction;
        return this;
    }

    /**
     * 是否可以滑动删除 以及滑动出现菜单
     */
    private boolean itemViewSwipeEnabled = true;


    /**
     * 获取当前recyclerView 可能为空
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 设置当前RecyclerView
     *
     * @param recyclerView
     */
    public SimpleAdapter setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        return this;
    }


    /**
     * 设置加载更多布局
     *
     * @param loadingMoreStateView
     */
    public SimpleAdapter setLoadingMoreStateView(View loadingMoreStateView) {
        mLoadingMoreStateView = loadingMoreStateView;
        return this;
    }


    /**
     * 是否有加载更多布局
     *
     * @return
     */
    public boolean isHadLoadingMore() {
        return hadLoadingMore;
    }

    /**
     * 是否有加载更多功能
     *
     * @param hadLoadingMore
     */
    public SimpleAdapter setLoadingMore(boolean hadLoadingMore) {
        this.hadLoadingMore = hadLoadingMore;
        return this;
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<Lrc> getDatas() {
        return mDatas;
    }

    /**
     * 追加更多数据
     *
     * @param moreDatas
     */
    public SimpleAdapter addMoreDatas(List<Lrc> moreDatas) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.addAll(moreDatas);
        return this;
    }

    /**
     * 设置空数据时候的View
     * 注意:  这个View必须和RecyclerView同级覆盖显示
     *
     * @param emptyView
     * @return
     */
    public SimpleAdapter setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        return this;
    }


//    public void setLoadingMoreState(final int state) {
//
//        post(new Runnable() {
//            @Override
//            public void run() {
//                switch (state) {
//                    case STATE_MORE_NO_MORE:
//                        System.out.println("没有更多了");
//                        ((TextView) mLoadingMoreStateView.findViewById(R.id.tv_state))
//                                .setText("--end--");
//                        ((ProgressBar) mLoadingMoreStateView.findViewById(R.id.progress_bar))
//                                .setVisibility(View.INVISIBLE);
//                        break;
//                    case STATE_MORE_MORE:
//                        System.out.println("加载更多中");
//                        ((TextView) mLoadingMoreStateView.findViewById(R.id.tv_state))
//                                .setText("加载中...");
//                        ((ProgressBar) mLoadingMoreStateView.findViewById(R.id.progress_bar))
//                                .setVisibility(View.VISIBLE);
//                        break;
//                    case STATE_MORE_ERROR:
//                        System.out.println("错误没网络");
//                        ((TextView) mLoadingMoreStateView.findViewById(R.id.tv_state))
//                                .setText("网络发生错误");
//                        ((ProgressBar) mLoadingMoreStateView.findViewById(R.id.progress_bar))
//                                .setVisibility(View.INVISIBLE);
//                        break;
//                }
//            }
//        });
//    }

    //点击事件接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    //设置点击事件
    public SimpleAdapter setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
        return this;
    }

    //构造
    public SimpleAdapter(Context context, List<Lrc> datas, int mItemLayout, RecyclerView recyclerView) {
        this.mDatas = datas;
        this.mItemLayout = mItemLayout;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mRecyclerView = recyclerView;
    }

    //绑定
    @Override
    public void onBindViewHolder(ViewHeader holder, int position) {
        //如果是头布局
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }
        if (getItemViewType(position) == TYPE_FOOTER) {
            return;
        }
        if (getItemViewType(position) == TYPE_STATE_MORE) {
            System.out.println("bind 加载更多状态=====");
            return;
        }
        position = getRealPosition(holder);
        convert(holder, mDatas.get(position), position);
        setItemEvent(holder);
    }

    /**
     * 获取删了头布局的下角标
     *
     * @param holder
     * @return
     */
    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;//尾布局不影响数据的获取
    }

    //建立
    @Override
    public ViewHeader onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ViewHeader(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ViewHeader(mFooterView);
        }

        if (hadLoadingMore && mLoadingMoreStateView != null && viewType == TYPE_STATE_MORE) {
            return new ViewHeader(mLoadingMoreStateView);
        }

        View view = mInflater.inflate(mItemLayout, parent, false);
        return new ViewHeader(view);
    }

    /**
     * @param holder
     * @param item     当前bean
     * @param position
     */
    public abstract void convert(ViewHeader holder, Lrc item, int position);

    //设置item点击事件
    private void setItemEvent(final ViewHeader holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //这个获取位置的方法，防止添加删除导致位置不变
                int layoutPosition = holder.getAdapterPosition();
                //为了习惯 即使有头布局时候 我们也让第一个一般的布局角标为1

                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, mHeaderView == null ? layoutPosition : layoutPosition - 1);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int layoutPosition = holder.getAdapterPosition();
                //时候可以拖动当前View
                if (canMove) {
                    mItemTouchHelper.startDrag(mRecyclerView.getChildViewHolder(v));
                }

                if (mItemClickListener != null) {
                    mItemClickListener.onItemLongClick(v, layoutPosition);
                }
                return false;
            }
        });
    }

    //总数
    @Override
    public int getItemCount() {
        int count = mDatas.size();
        if (mHeaderView != null) {
            count++;
        }
        if (mFooterView != null) {
            count++;
        }
        if (hadLoadingMore && mLoadingMoreStateView != null) {
            count++;
        }

        //TODO 根据需求修改
        if (mEmptyView != null && count == 0 && mEmptyView.getVisibility() != VISIBLE) {
            mEmptyView.setVisibility(VISIBLE);
        } else if (mEmptyView != null && count != 0 && mEmptyView.getVisibility() != GONE) {
            mEmptyView.setVisibility(GONE);
        }

        return count;
    }

    //类型
    @Override
    public int getItemViewType(int position) {
        boolean hadLoadView = (hadLoadingMore && mLoadingMoreStateView != null);

        if (mHeaderView == null && mFooterView == null && !hadLoadView) {
            return TYPE_NORMAL;
        }
        if (mHeaderView != null && position == 0) {
            return TYPE_HEADER;
        }

        int hadLoadingPos = hadLoadView ? 2 : 1;
        if (mFooterView != null && position == getItemCount() - hadLoadingPos) {
            return TYPE_FOOTER;
        }
        if (hadLoadView && position == getItemCount() - 1) {
            return TYPE_STATE_MORE;
        }

        return TYPE_NORMAL;
    }

    //grid时候头布局和尾布局 合理布局
    //TODO 可以设置不同类型跨度不同的空间
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mIsSpanHeader && getItemViewType(position) == TYPE_HEADER) {

                        return gridManager.getSpanCount();
                    }
                    if (mIsSpanFooter && getItemViewType(position) == TYPE_FOOTER) {

                        return gridManager.getSpanCount();
                    }
                    if (isSpanMoreView && getItemViewType(position) == TYPE_STATE_MORE) {
                        return gridManager.getSpanCount();
                    }

                    return 1;
                }
            });
        }
    }

    //合理布局 对于StaggeredGridLayoutManager情况的处理
    //TODO 需要再添加其他情况
    @Override
    public void onViewAttachedToWindow(ViewHeader holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            switch (holder.getItemViewType()) {
                case TYPE_HEADER:
                    if (mHeaderView != null && mIsSpanHeader)
                        p.setFullSpan(true);
                    break;

                case TYPE_FOOTER:
                    if (mFooterView != null && mIsSpanFooter)
                        p.setFullSpan(true);
                    break;

                case TYPE_STATE_MORE:
                    if (mLoadingMoreStateView != null && isSpanMoreView)
                        p.setFullSpan(true);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 头布局span
     */
    public SimpleAdapter spanHeaderView(boolean isSpan) {
        mIsSpanHeader = isSpan;
        return this;
    }

    /**
     * 尾布局span
     */
    public SimpleAdapter spanFooterView(boolean isSpan) {
        mIsSpanFooter = isSpan;
        return this;
    }

    /**
     * 更多布局span
     */
    public SimpleAdapter setSpanMoreView(boolean spanMoreView) {
        isSpanMoreView = spanMoreView;
        return this;
    }

    /**
     * 获取头布局
     *
     * @return
     */
    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * 移除头布局
     */
    public SimpleAdapter removeHeaderView() {
        if (mHeaderView != null) {
            notifyItemRemoved(0);
            mHeaderView = null;
        }
        return this;
    }

    /**
     * 移除尾布局
     */
    public SimpleAdapter removeFooterView() {
        if (mFooterView != null) {
            mFooterView = null;
            notifyItemRemoved(getItemCount() - 1);
        }
        return this;
    }

    /**
     * 设置头布局
     *
     * @param headerView
     */
    public SimpleAdapter setHeaderView(View headerView) {
        mHeaderView = headerView;
        return this;
    }

    /**
     * 获取尾布局
     *
     * @return
     */
    public View getFooterView() {
        return mFooterView;
    }

    /**
     * 设置尾布局
     *
     * @param footerView
     */
    public SimpleAdapter setFooterView(View footerView) {
        mFooterView = footerView;
        return this;
    }

    /**
     * 插入数据到
     *
     * @param pos
     */
    public SimpleAdapter addData(int pos, Lrc datas) {
        if (pos <= mDatas.size()) {
            mDatas.add(pos, datas);
            notifyItemInserted(pos);
        }
        return this;
    }

    /**
     * 删除数据到
     *
     * @param pos
     */
    public SimpleAdapter deleteDataRefresh(int pos) {

        System.out.println("pos====" + pos);

        int index;

        if (pos >= 0) {
            index = mHeaderView == null ? pos : pos - 1;
            if (index < 0 && index >= mDatas.size()) {
                return this;
            }
            mDatas.remove(index);
            notifyItemRemoved(pos);
        }

        return this;
    }


    //ViewHolder
    class ViewHeader extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;

        ViewHeader(View itemView) {
            super(itemView);
            //如果是头布局
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            if (itemView == mLoadingMoreStateView) {
                return;
            }

            if (mViews == null)//当心
                mViews = new SparseArray<View>();
        }

        //通过viewId获取控件
        <T extends View> T getView(int viewId) {

            //存起来就不用再findViewById了
            View view = mViews.get(viewId);
            if (view == null) {
                //没有就从item获取一下
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        /**
         * 设置TextView的值
         */
        public ViewHeader setText(int viewId, String text) {
            TextView tv = getView(viewId);
            tv.setText(text);
            return this;
        }

        public ViewHeader setImageResource(int viewId, int resId) {
            ImageView view = getView(viewId);
            view.setImageResource(resId);
            return this;
        }

        public ViewHeader setImageBitamp(int viewId, Bitmap bitmap) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bitmap);
            return this;
        }

        public ViewHeader setImageURI(int viewId, String uri) {
            ImageView view = getView(viewId);
            //TODO 设置自己加载图片的方法
//        Imageloader.getInstance().loadImg(view,uri);
            return this;
        }
    }

    /**
     * 设置是否可以拖拽
     * 需要给出绑定的RecyclerView
     */
    public SimpleAdapter setCanMove(boolean canMove) {
        this.canMove = canMove;
        if (canMove) {
            if (mItemTouchHelper == null) {
                mItemTouchHelper = new ItemTouchHelper(new Callback());
            }
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        } else {
            mItemTouchHelper = null;
        }
        return this;
    }


    /* ************ 拖动回调 start***************/
    private ItemTouchHelper mItemTouchHelper;

    class Callback extends ItemTouchHelper.Callback {
        int to;
        int from;

        //用于设置拖拽和滑动的方向
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //头布局尾布局更多布局不可滑动删除
            int type = viewHolder.getItemViewType();
            if (type == TYPE_HEADER || type == TYPE_FOOTER || type == TYPE_STATE_MORE) {
                return makeMovementFlags(0, 0);
            }

            //网格式布局有4个方向
            //线性式布局有2个方向
            int dragFlags = 0, swipeFlags = 0;
            if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager
                    || recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }

            //是否允许侧滑删除 以及侧滑方向
            if (itemViewSwipeEnabled) {
                if (currentSwipeDirection == LEFT_AND_RIGHT_SWIPE) {
                    swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END; //设置侧滑方向为从两个方向都可以

                } else if (currentSwipeDirection == LEFT_SWIPE) {
                    swipeFlags = ItemTouchHelper.START; //设置侧滑方向

                } else {
                    swipeFlags = ItemTouchHelper.END;
                }
            }
            return makeMovementFlags(dragFlags, swipeFlags);//swipeFlags 为0的话item不滑动
        }

        //长摁item拖拽时会回调这个方法
        //有头布局不要交换
        //有尾布局不要交换
        //有加载更多布局不要交换
        //更新适配器中item的位置
        @Override
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            to = target.getAdapterPosition();
            from = viewHolder.getAdapterPosition();

            if (to == 0 && mHeaderView != null) {
                return true;
            }
            if (to == getItemCount() - 2 && mFooterView != null && mLoadingMoreStateView != null) {
                return true;
            }
            if (to == getItemCount() - 1 && mLoadingMoreStateView == null) {
                return true;
            }
            if (to == getItemCount() - 1 && mLoadingMoreStateView != null) {
                return true;
            }

            if (mHeaderView != null) {
                Collections.swap(mDatas, from - 1, to - 1);//交换数据链表中数据的位置 TODO 注意指针越界问题
            } else {
                Collections.swap(mDatas, from, to);//交换数据链表中数据的位置 TODO 注意指针越界问题
            }
            notifyItemMoved(from, to);
            return true;
        }

        //这里处理滑动删除
        //头布局时候数据要让数据角标准确
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            viewHolder.getAdapterPosition();

            int pos = viewHolder.getAdapterPosition();

            switch (viewHolder.getItemViewType()) {
                case TYPE_NORMAL:
                    if (mHeaderView != null) {
                        mDatas.remove(pos - 1);
                    } else {
                        mDatas.remove(pos);
                    }
                    notifyItemRemoved(pos);
                    break;

                default:
                    break;
            }

        }


        @Override
        public boolean isItemViewSwipeEnabled() {
            return super.isItemViewSwipeEnabled();
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;//返回true则为所有item都设置可以拖拽
        }

        //当item拖拽开始时调用
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);

            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);//拖拽时设置背景色为灰色 TODO 有颜色错乱问题
            }
        }

        //当item拖拽完成时调用
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(Color.WHITE);//拖拽停止时设置背景色为白色 TODO 有颜色错乱问题
            System.out.println("拖动完成-------");

        }

        //当item视图变化时调用
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            //根据item滑动偏移的值修改item透明度。screenwidth是我提前获得的屏幕宽度
            viewHolder.itemView.setAlpha(1 - Math.abs(dX) / getScreenWidth());
        }
    }

    /* ************ 拖动回调 end***************/

    public int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;//像素点
    }

    private LoadingMore mLoadingMore;

    interface LoadingMore {
        List<Lrc> downMore(Integer page);
    }

    boolean isLoading = false;

    /**
     * 设置之后  setOnScrollListener必须由Adapter设置
     *
     * @param loadingMore
     * @return
     */
    public SimpleAdapter setLoadMore(LoadingMore loadingMore, final int remain) {
        mLoadingMore = loadingMore;


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //TODO mRecyclerView 的布局方向会影响到
                //是为了防止数据没有铺面页面的时候

                System.out.println("dy  " + dy + "---dx  " + dx);
                if (dy >= 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }
                if (recyclerView.getLayoutManager().getLayoutDirection() == RecyclerView.HORIZONTAL) {

                } else {
                    if (dx >= 0) {
                        isSlidingToLast = true;
                    } else {
                        isSlidingToLast = false;
                    }
                }


                //转让接口
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrolled(recyclerView, dx, dy);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                int lastVisibleItemPos = 0;
                int totalItemCount = manager.getItemCount();

                // 当不滚动时
                //找到最后可见的item的位置
                //gri继承自Linear 所以不用判断
                System.out.println("进入");
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (manager instanceof LinearLayoutManager) {
                        System.out.println("进入2");
                        lastVisibleItemPos = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                    } else {

                        StaggeredGridLayoutManager staManager = ((StaggeredGridLayoutManager) manager);
                        int[] positions = staManager.findLastVisibleItemPositions(null);
                        int spanCount = staManager.getSpanCount();

                        int max = positions[0];
                        for (int i = 0; i < spanCount - 1; i++) {
                            if (positions[i + 1] > max) {
                                max = positions[i + 1];
                            }
                        }
                        lastVisibleItemPos = max;
                    }

                    // 判断是否滚动到底部，并且是向最后滚动 且没有进入加载更多

                    System.out.println("l" + lastVisibleItemPos + "t" + totalItemCount + "s" + isSlidingToLast + "ing" + isLoading);

                    if (lastVisibleItemPos >= (totalItemCount - remain - 1) && isSlidingToLast && !isLoading) {
                        System.out.println("进入3");
                        //加载更多功能的代码
                        isLoading = true;
                        new MoreDownloadTask().execute(++page);
                    }
                }

                //转让接口
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }
        });
        return this;
    }

    private int page = 1;

    private OnScrollListener mOnScrollListener;

    /**
     * 设置滑动监听 需要再调用了 setLoadMore之后
     *
     * @param scrollListener
     */
    public void setOnScrollListener(OnScrollListener scrollListener) {
        mOnScrollListener = scrollListener;
    }

    interface OnScrollListener {
        void onScrolled(RecyclerView recyclerView, int dx, int dy);

        void onScrollStateChanged(RecyclerView recyclerView, int newState);
    }

    private class MoreDownloadTask extends AsyncTask<Integer, Integer, List<Lrc>> {


        @Override
        protected List<Lrc> doInBackground(Integer... pages) {
            System.out.println("后台处理下载");
            if (mLoadingMore != null) {
                return mLoadingMore.downMore(pages[0]);
            } else {
                Log.i("simpleAdapter", "没有继承下载更多接口");
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Lrc> tvs) {
            super.onPostExecute(tvs);
            isLoading = false;

            if (tvs == null) {
                return;
            }

            addMoreDatas(tvs);
            SimpleAdapter.this.notifyDataSetChanged();
            Log.d("simpleAdapter", "下载结束");
        }
    }
}