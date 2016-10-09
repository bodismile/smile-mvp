package cn.smile.base;

import android.os.Message;
import android.support.v7.widget.RecyclerView;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.smile.R;
import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;
import cn.smile.widget.MultiStateView;

/**
 * 封装BGARefreshLayout的Activity，
 * @author smile
 * @date 2015-08-18-15:33
 * 布局文件如下：
<?xml version="1.0" encoding="utf-8"?>
<com.kennyc.view.MultiStateView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/multiStateView"
    android:background="#fafafa"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:msv_emptyView="@layout/common_empty"
    app:msv_errorView="@layout/common_error"
    app:msv_loadingView="@layout/common_loading">

    <cn.bingoogolapple.refreshlayout.BGARefreshLayout
        android:id="@+id/refreshView"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
        android:id="@+id/recycleView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:overScrollMode="never"
        android:padding="5dp"
        android:scrollbars="none" />
    </cn.bingoogolapple.refreshlayout.BGARefreshLayout>

</com.kennyc.view.MultiStateView>
 */
public abstract class BaseActivityWithBGARefresh <T extends BasePresenter, E extends BaseModel>
        extends BaseActivity<T,E> implements BGARefreshLayout.BGARefreshLayoutDelegate,BGAOnRVItemClickListener {
    /**
     * 状态View
     */
    MultiStateView multiStateView;
    /**
     * 下拉刷新父控件
     */
    BGARefreshLayout mRefreshLayout;
    /**
     * recyclerView
     */
    RecyclerView mRecyclerView;
    /**
     * 适配器
     */
    BGARecyclerViewAdapter mAdapter;
    /**
     * 布局管理器
     */
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void initView() {
        multiStateView = getView(R.id.multiStateView);
        mRefreshLayout = getView(R.id.refreshView);
        mRecyclerView = getView(R.id.recycleView);
        //设置下拉刷新样式
        mRefreshLayout.setRefreshViewHolder(initViewHolder());
        //设置上下拉刷新事件
        mRefreshLayout.setDelegate(this);
        //-----RecycleView相关-------
        //设置布局管理器
        mLayoutManager = initLayoutManager();
        mRecyclerView.setLayoutManager(mLayoutManager);
        //设置Adapter
        mAdapter = initAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        //设置点击事件
        mAdapter.setOnRVItemClickListener(this);
    }

    @Override
    public void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case 0:
                mRefreshLayout.endRefreshing();
                break;
            case 1:
                mRefreshLayout.endLoadingMore();
                break;
            default:
                break;
        }
    }

    /**
     * 重置下拉刷新
     */
    public void resetRefresh() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 500);
    }

    /**
     * 重置加载更多
     */
    public void resetLoadMore() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        }, 500);
    }

    /**
     * 获取状态切换的父View
     * @return
     */
    public MultiStateView getMultiStateView(){
        return multiStateView;
    }

    /**
     * 获取当前适配器
     *
     * @return
     */
    public BGARecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 获取当前布局管理器
     *
     * @return
     */
    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    /**
     * 平滑滚动到指定位置
     *
     * @param pos
     */
    public void smoothScrollToPosition(int pos) {
        mRecyclerView.smoothScrollToPosition(pos);
    }

    /**
     * 设置适配器
     *
     * @return
     */
    protected abstract BGARecyclerViewAdapter initAdapter(RecyclerView recyclerView);

    /**
     * 设置下拉刷新样式
     *
     * @return
     */
    protected abstract BGARefreshViewHolder initViewHolder();

    /**
     * 设置LayoutManager
     *
     * @return
     */
    protected abstract RecyclerView.LayoutManager initLayoutManager();

}