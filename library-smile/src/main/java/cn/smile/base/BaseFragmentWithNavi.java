package cn.smile.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.smile.R;
import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;

/**封装导航条操作的Fragment
 * @author smile
 * @date 2015-08-07-11:08
 */
public abstract class BaseFragmentWithNavi<T extends BasePresenter, E extends BaseModel> extends BaseNaviFragment<T,E> {

    protected View rootView;

    @Override
    public int getLayoutId() {
        return R.layout.base_with_navi;
    }

    @Override
    public void initView(LayoutInflater inflater) {
        //导航栏
        initNaviView();
        //获取容器
        FrameLayout container = (FrameLayout) view.findViewById(R.id.container);
        //将子布局添加到container中
        rootView =layout(inflater,container);
        container.addView(view, -1, -1);
    }

    /**
     * 获取指定当前view下面的id
     * @param id
     * @return
     */
    protected <T extends View> T findView(int id) {
        return (T) rootView.findViewById(id);
    }

    /**
     * 获取当前View
     * @return
     */
    protected abstract View layout(LayoutInflater inflater,ViewGroup container);

}