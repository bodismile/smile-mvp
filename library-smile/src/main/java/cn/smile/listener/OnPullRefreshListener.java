package cn.smile.listener;

import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by Administrator on 2016/11/14.
 */

public interface OnPullRefreshListener<V extends View> {
    /**
     * 上拉刷新
     * @param base
     */
    void onPullUpTo(PullToRefreshBase<V> base);

    /**
     * 下拉刷新
     * @param base
     */
    void onPullDownTo(PullToRefreshBase<V> base);
}
