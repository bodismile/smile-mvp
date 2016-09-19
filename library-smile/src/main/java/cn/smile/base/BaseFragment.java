package cn.smile.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;
import cn.smile.util.SLog;
import cn.smile.util.Utils;
import icepick.Icepick;

/**
 * Fragment基类
 * @param <T>
 * @param <E>
 * @author smile
 */
public abstract class BaseFragment<T extends BasePresenter, E extends BaseModel> extends Fragment {

    public T mPresenter;
    public E mModel;
    public View view;
    protected Handler mHandler;

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if(Utils.hasClass("icepick")){
            Icepick.saveInstanceState(this, bundle);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过反射获取presenter和Model
        mPresenter = Utils.getT(this, 0);
        mModel = Utils.getT(this, 1);
        mHandler=new WeakReferenceHandler(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(Utils.hasClass("icepick")){
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        ButterKnife.bind(this, view);
    }

    //-----------友盟统计-----start
    public void onResume() {
        super.onResume();
        if(Utils.hasClass("com.umeng.analytics")){
            MobclickAgent.onPageStart(getClass().getName()); //统计页面
        }
    }

    public void onPause() {
        super.onPause();
        if(Utils.hasClass("com.umeng.analytics")){
            MobclickAgent.onPageEnd(getClass().getName());
        }
    }
    //-----------友盟统计-----end

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null)
            mPresenter.onDestroy();
        ButterKnife.unbind(this);
        //取消handler订阅，防止内存泄露
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler=null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(),container,false);
        this.initView(inflater);
        this.initPresenter();
        return view;
    }

    /**
     * 获取指定布局
     * @param id
     * @param <T>
     * @return
     */
    protected  <T extends View> T getView(int id) {
        return (T) view.findViewById(id);
    }

    /**
     * 设置当前布局layout
     * @return
     */
    public abstract int getLayoutId();
    /**
     * 初始化View
     */
    public abstract void initView(LayoutInflater inflater);

    /**
     * 绑定view和mode
     */
    public abstract void initPresenter();

    //-----------------------------------------------------------------------------------------

    /**启动指定Activity
     * @param target
     * @param bundle
     */
    public void startActivity(Class<? extends Activity> target, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), target);
        if (bundle != null)
            intent.putExtra(getActivity().getPackageName(), bundle);
        getActivity().startActivity(intent);
    }

    /**获取Bundle
     * @return
     */
    public Bundle getBundle(){
        return getActivity().getIntent().getBundleExtra(getActivity().getPackageName());
    }

    protected void runOnMain(Runnable runnable) {
        getActivity().runOnUiThread(runnable);
    }

    private Toast toast;
    protected final static String NULL = "";

    public void toast(int resId){
        toast(getResources().getString(resId));
    }

    public void toast(final Object obj) {
        try {
            runOnMain(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(getActivity(), NULL,Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * log
     * @param msg
     */
    public static void log(String msg){
        if(!TextUtils.isEmpty(msg)){
            SLog.i(msg);
        }else{
            SLog.i("msg is null,stop log");
        }
    }

    /**
     * 发送待执行的消息
     * @param message
     */
    protected void postMessage(Message message) {
        if (this.mHandler != null) {
            this.mHandler.sendMessage(message);
        }else {
            this.processMessage(message);
        }
    }

    /**
     * @param message
     */
    public void processMessage(Message message) {

    }

    static class WeakReferenceHandler extends Handler {

        private final WeakReference<BaseFragment> instance;

        public WeakReferenceHandler(final BaseFragment fragment) {
            this.instance = new WeakReference<BaseFragment>(fragment);
        }

        @Override
        public void handleMessage(final Message message) {
            final BaseFragment fragment = this.instance.get();
            if (fragment != null) {
                fragment.processMessage(message);
            }
        }
    }
}