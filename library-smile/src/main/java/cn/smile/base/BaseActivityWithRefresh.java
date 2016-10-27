package cn.smile.base;

import android.os.Message;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.smile.R;
import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;
import cn.smile.widget.MultiStateView;

/**
 * 封装上下拉刷新ListView的Activity，
 * @author smile
 * @date 2015-08-18-15:33
 * 布局文件中必须包含以下内容：
<?xml version="1.0" encoding="utf-8"?>
<cn.smile.widget.MultiStateView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:ptr="http://schemas.android.com/tools"
    android:id="@id/multiStateView"
    android:background="#fafafa"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:msv_emptyView="@layout/common_empty"
    app:msv_errorView="@layout/common_error"
    app:msv_loadingView="@layout/common_loading">

    <com.handmark.pulltorefresh.library.PullToRefreshListView
        android:id="@id/refreshView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:paddingTop="@dimen/dimen_10"
        android:cacheColorHint="@color/color_transparent"
        android:divider="@null"
        android:fadingEdge="none"
        android:fadingEdgeLength="0dp"
        android:fastScrollEnabled="false"
        android:footerDividersEnabled="false"
        android:headerDividersEnabled="false"
        android:smoothScrollbar="true"
        ptr:ptrHeaderBackground="#fafafa"
        ptr:ptrHeaderTextColor="#ff8c8c8c"
        ptr:ptrListViewExtrasEnabled="true"
        android:listSelector="@color/color_transparent"
        android:scrollbars="none"/>

</cn.smile.widget.MultiStateView>
 */
public abstract class BaseActivityWithRefresh<T extends BasePresenter, E extends BaseModel>
        extends BaseActivity<T,E> implements PullToRefreshBase.OnRefreshListener2{
    /**
     * 上下拉刷新view
     */
    PullToRefreshListView refreshView;
    /**
     * 状态View:loading、error、empty等
     */
    MultiStateView multiStateView;

    @Override
    public void initView() {
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
    public PullToRefreshListView getRefreshView(){
        return refreshView;
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
        }, 1000);
    }

    @Override
    public void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what){
            case 0:
                refreshView.onRefreshComplete();
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
