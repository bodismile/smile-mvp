package cn.smile.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;

import cn.smile.util.SLog;
import cn.smile.util.Utils;
import icepick.Icepick;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 所有Activity的父类：包含状态恢复、资源清除、toast、log、Handler、startActivity及友盟统计操作等
 * @author smile
 */
public class ParentActivity extends AppCompatActivity {

    /**
     * Handler
     */
    protected Handler mHandler;
    /**
     * Observable(被观察者)在subscribe()之后会持有 Subscriber(Observer的) 的引用，这个引用如果不能及时被释放，将有内存泄露的风险
     */
    private CompositeSubscription mCompositeSubscription=null;
    /**
     * 解决Subscription内存泄露问题
     * @param s
     */
    protected void addSubscription(Subscription s) {
        if(!Utils.hasClass("rx.subscriptions.CompositeSubscription")){
            throw new IllegalArgumentException("you must add rxjava and rxandroid library to this project");
        }else{
            if (this.mCompositeSubscription == null) {
                this.mCompositeSubscription = new CompositeSubscription();
            }
            this.mCompositeSubscription.add(s);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //恢复状态
        if(Utils.hasClass("icepick.Icepick")){
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        mHandler=new WeakReferenceHandler(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存状态
        if(Utils.hasClass("icepick.Icepick")){
            Icepick.saveInstanceState(this, outState);
        }
    }

    //-----------友盟统计-----start
//    public void onResume() {
//        super.onResume();
//        if(Utils.hasClass("com.umeng.analytics.MobclickAgent")){
//            MobclickAgent.onResume(this); // 统计时长
//        }
//    }
//
//    public void onPause() {
//        super.onPause();
//        if(Utils.hasClass("com.umeng.analytics.MobclickAgent")){
//            MobclickAgent.onPause(this);
//        }
//    }
    //-----------友盟统计-----end

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅，防止rx内存泄露
        if (Utils.hasClass("rx.subscriptions.CompositeSubscription") && this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
            this.mCompositeSubscription=null;
        }
        //取消handler订阅，防止内存泄露
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler=null;
        }
    }

    protected void runOnMain(Runnable runnable) {
        runOnUiThread(runnable);
    }

    private Toast toast;
    protected final static String NULL = "";

    /**
     * toast
     * @param resId string资源id
     */
    public void toast(int resId){
        toast(getResources().getString(resId));
    }

    /**
     * toast
     * @param obj
     */
    public void toast(final Object obj) {
        try {
            runOnMain(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(ParentActivity.this, NULL,Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * log日志
     * @param msg
     */
    public static void log(String msg){
        if(!TextUtils.isEmpty(msg)){
            SLog.i(msg);
        }else{
            SLog.i("log msg is null");
        }
    }

    /**
     * 跳转页面
     * @param finish
     * @param target
     * @param bundle
     */
    public void startActivity(boolean finish, Class<? extends Activity> target, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(getPackageName(), bundle);
        startActivity(intent);
        if (finish){
            finish();
        }
    }

    /**
     * 获取Bundle
     * @return
     */
    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else{
            return null;
        }
    }

    /**
     * 发送待执行的消息
     * @param message
     */
    public void postMessage(Message message) {
        if (this.mHandler != null) {
            this.mHandler.sendMessage(message);
        }else {
            this.processMessage(message);
        }
    }

    /**Handler消息的实现方法
     * @param msg
     */
    public void processMessage(Message msg) {}

    /**
     * Handler静态内部类
     */
    static class WeakReferenceHandler extends Handler {

        private final WeakReference<ParentActivity> instance;

        public WeakReferenceHandler(ParentActivity activity) {
            this.instance = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(final Message message) {
            ParentActivity activity = this.instance.get();
            if (activity != null) {
                activity.processMessage(message);
            }
        }
    }
}