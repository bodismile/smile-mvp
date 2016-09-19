package cn.smile.base.mvp;

import android.content.Context;

import cn.smile.util.Utils;

/**
 * Presenter基类：资源清除
 * @author smile
 */
public abstract class BasePresenter<E, T> {

    public Context context;
    public E mModel;
    public T mView;
    public RxFactory mRxFactory =null;

    public void bindVM(T v, E m) {
        this.mView = v;
        this.mModel = m;
        //是否有导入rxjava/rxandroid
        if(Utils.hasClass("rx.subscriptions.CompositeSubscription") &&
           Utils.hasClass("rx.android.schedulers.AndroidSchedulers")){
            this.mRxFactory = new RxFactory();
        }
        this.subscribe();
    }

    public abstract void subscribe();

    public void onDestroy() {
        if(mRxFactory!=null){
            mRxFactory.clear();
        }
    }
}
