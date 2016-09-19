package cn.smile.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import cn.smile.R;
import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;

/**内部封装导航条
 * @author smile
 * @date 2015-08-07-11:08
 */
public abstract  class BaseActivityWithNavi<T extends BasePresenter, E extends BaseModel> extends BaseNaviActivity<T,E> {

	protected View view;

	@Override
	public int getLayoutId() {
		return R.layout.base_with_navi;
	}

	@Override
	public void initView() {
		//初始化导航栏
		initNaviView();
		//将指定的view添加到container容器中
		FrameLayout container = getView(R.id.container);
		view = layout(getLayoutInflater());
		container.addView(view, -1, -1);
	}

	/**
	 * 获取指定当前view下面的id
	 * @param id
     * @return
     */
	protected <T extends View> T findView(int id) {
		return (T) view.findViewById(id);
	}

	/**
	 * 获取当前View
	 * @return
     */
	protected abstract View layout(LayoutInflater inflater);

}