package cn.smile.base.mvp;

import java.util.HashMap;
import java.util.Map;

import cn.smile.util.SLog;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 用于管理RxBus的事件和Rxjava的生命周期处理
 * @author smile
 */
public class RxFactory {

    private static Observable.Transformer schedulersTransformer;

    static {
        schedulersTransformer = createSchedulers();
    }

    private static <T> Observable.Transformer<T, T> createSchedulers() {
        return new  Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable)observable)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return schedulersTransformer;
    }

    public RxBus mRxBus = RxBus.getInstance();
    private Map<String, Observable<?>> mObservables = new HashMap<>();
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    /**
     * 发送一个指定Tag的事件
     * @param tag
     * @param content
     */
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }

    /**
     * 接收并处理指定TAG的事件
     * @param tag
     * @param action1
     */
    public void on(String tag, Action1<Object> action1) {
        Observable<?> mObservable = mRxBus.register(tag);
        mObservables.put(tag, mObservable);
        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        SLog.e(throwable.getMessage());
                    }
                }));
    }

    public void add(Subscription m) {
        mCompositeSubscription.add(m);
    }

    public void clear() {
        mCompositeSubscription.unsubscribe();
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet()) {
            mRxBus.unregister(entry.getKey(), entry.getValue());
        }
    }
}
