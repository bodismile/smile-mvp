package cn.smile.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.BaseAdapter;

import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;
import cn.smile.widget.MultiStateView;

/**
 * 懒加载Fragment
 * @author smile
 */
public abstract class LazyFragment<T extends BasePresenter, E extends BaseModel> extends BaseFragment<T,E> {

    protected boolean isPrepared = false;
    protected boolean isFirstResume = true;
    protected boolean isFirstVisible = true;
    protected boolean isFirstInvisible = true;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onPrepare();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                onPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    public synchronized void onPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    /**
     * 第一次对用户可见时会调用该方法
     */
    protected abstract void onFirstUserVisible();
    /**
     * 对用户可见时会调用该方法，除了第一次
     */
    public void onUserVisible() {}
    /**
     * 第一次对用户不可见时会调用该方法
     */
    public void onFirstUserInvisible() {}
    /**
     * 对用户不可见时会调用该方法，除了第一次
     */
    public void onUserInvisible() {}

    protected boolean canLoadData(@NonNull MultiStateView multiStateView, BaseAdapter adapter) {
        @MultiStateView.ViewState
        int viewState = multiStateView.getViewState();
        if (viewState == MultiStateView.VIEW_STATE_LOADING ||
            (viewState == MultiStateView.VIEW_STATE_CONTENT && adapter.getCount() > 0) ||
            viewState == MultiStateView.VIEW_STATE_EMPTY ||
            viewState == MultiStateView.VIEW_STATE_ERROR) {
            return false;
        }
        return true;
    }

}
