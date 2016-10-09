package cn.smile.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.smile.R;
import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;

/**内部封装导航条且允许Fragment内部切换
 * @author smile
 * @date 2015-08-07-11:08
 * 一般用于一个Activity,内部几个Fragment且公用一个导航条，根据操作切换不同的Fragment
 */
public abstract  class BaseActivityWithNaviAndFragment<T extends BasePresenter, E extends BaseModel> extends BaseNaviActivity<T,E> {

	protected View view;

	protected List<Fragment> fragments;

	@Override
	public int rootLayoutId() {
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
	 * 当前的布局
	 * @return
     */
	protected abstract View layout(LayoutInflater inflater);

	/**
	 * 用来替换Fragment的容器id
	 * @return
     */
	protected abstract int containerId();

	public void addFragment(Fragment... fragments) {
		if (this.fragments == null)
			this.fragments = new ArrayList<>();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		for (Fragment fragment : fragments)
			if (!this.fragments.contains(fragment)) {
				transaction.add(containerId(), fragment);
				this.fragments.add(fragment);
			}
		transaction.commit();
	}

	public void show(Fragment fragment, String title) {
		if (listener == null && title != null) {
			tv_title.setText(title);
		}else{
			refreshTop();
		}
		if (!fragment.isAdded())
			addFragment(fragment);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.show(fragment);
		for (Fragment f : this.fragments)
			if (f != fragment)
				transaction.hide(f);
		transaction.commit();
	}

	public void removeFragment(Fragment... fragments) {
		if (this.fragments == null)
			this.fragments = new ArrayList<>();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		for (Fragment fragment : fragments)
			if (!this.fragments.contains(fragment)) {
				transaction.remove(fragment);
				this.fragments.remove(fragment);
			}
		transaction.commit();
	}
}