package cn.smile.base;

import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import cn.smile.R;
import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;
import cn.smile.listener.OnPullRefreshListener;
import cn.smile.util.SLog;
import cn.smile.widget.MultiStateView;
import io.nlopez.smartadapters.adapters.MultiAdapter;

import static cn.smile.widget.MultiStateView.VIEW_STATE_CONTENT;
import static cn.smile.widget.MultiStateView.VIEW_STATE_ERROR;

/**封装上下拉刷新的Fragment
 * @author smile
 * @date 2015-08-18-15:33
布局文件如下：
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
public abstract class BaseFragmentWithRefresh<T extends BasePresenter, E extends BaseModel,D>
        extends LazyFragment<T,E> implements PullToRefreshBase.OnRefreshListener2,OnPullRefreshListener {

    /**
     * 上下拉刷新view
     */
    private PullToRefreshListView refreshView;
    /**
     * 状态View:loading、error、empty等
     */
    private MultiStateView multiStateView;

    LayoutInflater mInflater;

    @Override
    public void initView(LayoutInflater inflater) {
        mInflater = inflater;
        //初始化状态View
        multiStateView = getView(R.id.multiStateView);
        //初始化listview
        refreshView = getView(R.id.refreshView);
        //初始只允许下拉刷新
        refreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //设置加载监听器
        refreshView.setOnRefreshListener(this);
        //初始化Adapter
        isFirstLoading = true;
        mAdapter = initAdapter();
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
     * @return
     */
    public ListView getListView(){
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
     * 空布局
     */
    private View emptyView;
    /**
     * 适配器
     */
    private MultiAdapter mAdapter;
    /**
     * 是否是初始加载
     */
    private boolean isFirstLoading=true;
    /**
     * 总数
     */
    private int total = -1;

    /**
     * 设置空视图布局
     * @return
     */
    public int emptyViewId(){
        return 0;
    }

    /**
     * EmptyView初始化完成
     */
    public void initEmptyViewFinish(){}

    /**
     * 获取空视图
     * @return
     */
    public View getEmptyView(){
        return emptyView;
    }

    /**
     * 显示空视图
     */
    private void showEmptyView() {
        if(emptyView==null){
            initEmptyView(mInflater);
        }
        if(emptyView.getVisibility()!=View.VISIBLE){
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏空视图
     */
    public void hideEmptyView() {
        if(emptyView!=null && emptyView.getVisibility()!=View.GONE)
            emptyView.setVisibility(View.GONE);
    }

    /**
     * 初始化EmptyView
     */
    private void initEmptyView(LayoutInflater inflater){
        int id = emptyViewId();
        if(id>0){
            emptyView = inflater.inflate(id,null);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            multiStateView.addView(emptyView,lp);
            initEmptyViewFinish();
        }
    }

    /**
     * 初始化适配器
     * @return
     */
    public abstract MultiAdapter initAdapter();

    /**
     * @return
     */
    public MultiAdapter getAdapter(){
        return mAdapter;
    }

    /**
     * 设置List总数
     * @param total
     */
    public void setTotal(int total){
        this.total = total;
    }

    /**
     * @return
     */
    public int getTotal(){
        return total;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
        hideEmptyView();
        onPullDownTo(pullToRefreshBase);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
        if(total<0){
            throw new IllegalStateException("you must call setTotal(int total) method before pull up to refresh");
        }else{
            int curCount = mAdapter.getCount();
            if(total>curCount){
                onPullUpTo(pullToRefreshBase);
            }else{
                toast("数据已全部加载");
                resetRefresh();
            }
        }
    }

    /**
     * 成功回调
     * @param page
     * @param list
     */
    public void onSuccess(int page, List<D> list){
        if(page==1){//首页
            getMultiStateView().setViewState(VIEW_STATE_CONTENT);
            mAdapter.clearItems();
            if(list!=null && list.size()>0){//有数据
                mAdapter.setItems(list);
                if(list.size()< 10){//如果一页就加载完了数据
                    setStartMode();
                }else{
                    setBothMode();
                }
            }else{//无数据
                showEmptyView();
                setStartMode();
            }
        }else{//加载更多
            mAdapter.addItems(list);
            setBothMode();
        }
        if(isFirstLoading){
            isFirstLoading=false;
        }else{
            resetRefresh();
        }
    }

    /**
     * 错误回调
     */
    public void onFail(int page,Throwable e){
        if (page == 1) {
            getMultiStateView().setViewState(VIEW_STATE_ERROR);
        }else{
            if(e!=null){
                SLog.e(e.getMessage());
                toast(e.getMessage());
            }
        }
        resetRefresh();
    }
}
