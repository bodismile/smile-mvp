package cn.smile.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.smile.R;
import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;

/**内部封装Fragment切换,无导航条
 * @author  smile
 * @date 2015-08-07-11:08
 * 一般用于主页(底部四个按钮，每个按钮对应一个Fragment)
 */
public abstract  class BaseActivityWithFragment<T extends BasePresenter, E extends BaseModel> extends BaseActivity<T,E> {

	protected List<Fragment> fragments;

	@Override
	public int rootLayoutId() {
		return R.layout.base;
	}

	@Override
	public void initView() {
		FrameLayout container = getView(R.id.container);
		container.addView(LayoutInflater.from(this).inflate(layoutId(), null), -1, -1);
		init();
	}

	@Override
	public boolean canSwipeBack() {
		return false;
	}

	/**
	 * 布局id
	 * @return
     */
	protected abstract int layoutId();

	/**
	 * 用来替换Fragment的容器id
	 * @return
     */
	protected abstract int containerId();

	/**
	 * 初始化
	 */
	protected abstract void init();

	/**
	 * 添加Fragment
	 * @param fragments
	 */
	public void addFragment(Fragment... fragments) {
		if (this.fragments == null)
			this.fragments = new ArrayList<>();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		for (Fragment fragment : fragments) {
			if (!this.fragments.contains(fragment)) {
				this.fragments.add(fragment);
				transaction.add(containerId(), fragment, fragment.getClass().getName());
			}
		}
		transaction.commit();
	}

	/**
	 * 显示指定Fragment
	 *
	 * @param fragment
	 */
	public void showFragment(Fragment fragment) {
		if (!fragment.isAdded()) {
			addFragment(fragment);
		}
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		for (Fragment f : this.fragments) {
			if (f != fragment) {
				transaction.hide(f);
			}
		}
		transaction.show(fragment).commitAllowingStateLoss();
	}

	/**
	 * 移除Fragment
	 *
	 * @param fragments
	 */
	public void removeFragment(Fragment... fragments) {
		if (this.fragments == null)
			this.fragments = new ArrayList<>();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		for (Fragment fragment : fragments)
			if (!this.fragments.contains(fragment)) {
				transaction.remove(fragment);
				this.fragments.remove(fragment);
			}
		transaction.commit();
	}

	protected void beforePause() {}
	protected void afterResume() {}
	public boolean handleBackPressed() {
		return false;
	}

	@Override
	public final void onPause() {
		beforePause();
		super.onPause();
	}

	@Override
	public final void onResume() {
		super.onResume();
		afterResume();
	}

	@Override
	public void onBackPressed() {
		if (!handleBackPressed()) {
			super.onBackPressed();
		}
	}
}