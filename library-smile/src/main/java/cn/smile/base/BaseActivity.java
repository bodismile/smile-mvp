package cn.smile.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import cn.smile.R;
import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;
import cn.smile.util.Utils;
import cn.smile.widget.SwipeBackLayout;

/**
 * Activity基类：右滑退出页面及P、M的绑定操作
 * @param <T> 和该Activity绑定的P层
 * @param <E> 和该Activity绑定的M层
 * @author  smile
 */
public abstract class BaseActivity<T extends BasePresenter, E extends BaseModel> extends ParentActivity {

    public T mPresenter;
    public E mModel;
    private SwipeBackLayout swipeBackLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置contentView
        setContentView(getLayoutId());
        //设置视图注解
        if(Utils.hasClass("butterknife.ButterKnife")){
            ButterKnife.bind(this);
        }
        //通过反射获取presenter和Model
        mPresenter = Utils.getT(this, 0);
        mModel = Utils.getT(this, 1);
        //初始化
        this.initView();
        this.initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.onDestroy();
        if(Utils.hasClass("butterknife.ButterKnife")){
            ButterKnife.unbind(this);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (!canSwipeBack()) {
            super.setContentView(layoutResID);
        } else {//允许滑动退出,则添加到SwipeBackLayout中
            super.setContentView(getContainer());
            View view = LayoutInflater.from(this).inflate(layoutResID, null);
            view.setBackgroundColor(getResources().getColor(R.color.color_window_background));
            swipeBackLayout.addView(view);
        }
    }

    private View getContainer() {
        RelativeLayout container = new RelativeLayout(this);
        swipeBackLayout = new SwipeBackLayout(this);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        ImageView iv_shadow = new ImageView(this);
        iv_shadow.setBackgroundColor(getResources().getColor(R.color.color_shadow));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        container.addView(iv_shadow, params);
        container.addView(swipeBackLayout);
        return container;
    }

    public <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 是否可以滑动返回
     * @return
     */
    public abstract boolean canSwipeBack();

    /**
     * 设置当前布局layout
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 初始化Presenter
     */
    public abstract void initPresenter();

}
