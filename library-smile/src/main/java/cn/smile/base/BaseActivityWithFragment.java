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

/**内部封装Fragment切换
 * @author  smile
 * @date 2015-08-07-11:08
 * 有时候，导航条不适合封装在较复杂的界面
 */
public abstract  class BaseActivityWithFragment<T extends BasePresenter, E extends BaseModel> extends BaseActivity<T,E> {

	protected List<Fragment> fragments;

	@Override
	public int getLayoutId() {
		return R.layout.base;
	}

	@Override
	public void initView() {
		FrameLayout container = getView(R.id.container);
		container.addView(LayoutInflater.from(this).inflate(layout(), null), -1, -1);
		init();
	}

	@Override
	public boolean canSwipeBack() {
		return false;
	}
	protected abstract int layout();
	protected abstract int fragmentLayout();
	protected abstract void init();

	/**
	 * 添加Fragment
	 *
	 * @param fragments
	 */
	public void addFragment(Fragment... fragments) {
		if (this.fragments == null)
			this.fragments = new ArrayList<>();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		for (Fragment fragment : fragments) {
			if (!this.fragments.contains(fragment)) {
				this.fragments.add(fragment);
				transaction.add(fragmentLayout(), fragment, fragment.getClass().getName());
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