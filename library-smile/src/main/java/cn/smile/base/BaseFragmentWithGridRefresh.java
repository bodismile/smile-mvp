package cn.smile.base;

import android.os.Message;
import android.view.LayoutInflater;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import cn.smile.R;
import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;
import cn.smile.widget.MultiStateView;

/**封装上下拉刷新GridView的Fragment
 * @author smile
 * @date 2015-08-18-15:33
 * 布局如下：
<?xml version="1.0" encoding="utf-8"?>
<cn.smile.widget.MultiStateView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:ptr="http://schemas.android.com/apk/res-auto"
    android:id="@id/multiStateView"
    android:background="@color/color_fafafa"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:msv_emptyView="@layout/common_empty"
    app:msv_errorView="@layout/common_error"
    app:msv_loadingView="@layout/common_loading">

    <com.handmark.pulltorefresh.library.PullToRefreshGridView
        android:scrollbarStyle="outsideOverlay"
        android:id="@id/refreshView"
        android:fadingEdge="none"
        android:clipToPadding="false"
        android:paddingTop="@dimen/dimen_10"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:listSelector="#00000000"
        android:horizontalSpacing="@dimen/dimen_5"
        android:verticalSpacing="@dimen/dimen_5"
        android:stretchMode="columnWidth"
        android:numColumns="auto_fit"
        android:fastScrollEnabled="false"
        android:scrollbars="none"
        android:headerDividersEnabled="false"
        android:footerDividersEnabled="false"
        android:smoothScrollbar="true"
        ptr:ptrHeaderBackground="@color/color_fafafa"
        ptr:ptrHeaderTextColor="#ff8c8c8c"
        ptr:ptrListViewExtrasEnabled="true" />

</cn.smile.widget.MultiStateView>
 */
public abstract class BaseFragmentWithGridRefresh<T extends BasePresenter, E extends BaseModel>
        extends LazyFragment<T,E> implements PullToRefreshBase.OnRefreshListener2{

    /**
     * 上下拉刷新view
     */
    PullToRefreshGridView refreshView;
    /**
     * 状态View:loading、error、empty等
     */
    MultiStateView multiStateView;

    @Override
    public void initView(LayoutInflater inflater) {
        //初始化状态View
        multiStateView = getView(R.id.multiStateView);
        //初始化listview
        refreshView = getView(R.id.refreshView);
        //初始只允许下拉刷新
        refreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //设置加载监听器
        refreshView.setOnRefreshListener(this);
    }

    /**
     * 获取状态切换的父View
     * @return
     */
    public MultiStateView getMultiStateView(){
        return multiStateView;
    }

    /**
     * 获取下拉刷新View
     * @return
     */
    public PullToRefreshGridView getRefreshView(){
        return refreshView;
    }

    /**
     * 获取内部的GridView
     * @return
     */
    public GridView getGridView(){
        return refreshView.getRefreshableView();
    }

    /**
     * 设置刷新模式
     */
    public void setBothMode(){
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    /**
     *设置只允许下拉刷新
     */
    public void setStartMode(){
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 500);
    }

    @Override
    public void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what){
            case 0:
                refreshView.onRefreshComplete();
                //这句话需放到onRefreshComplete后才有效
                refreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                break;
            case 1:
                refreshView.onRefreshComplete(); // 下拉刷新完成
                break;
            default:
                break;
        }
    }

    /**
     * 刷新完成动作
     */
    public void resetRefresh() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        }, 500);
    }

}
