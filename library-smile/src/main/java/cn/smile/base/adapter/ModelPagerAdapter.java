package cn.smile.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Viewpager状态管理器
 * @author smile
 */
public class ModelPagerAdapter extends FragmentStatePagerAdapter {
    protected PagerModelManager pagerModelManager;

    public ModelPagerAdapter(FragmentManager fm, PagerModelManager pagerModelManager) {
        super(fm);
        this.pagerModelManager = pagerModelManager;
    }

    public Fragment getItem(int position) {
        return this.pagerModelManager.getItem(position);
    }

    public int getCount() {
        return this.pagerModelManager.getFragmentCount();
    }

    public CharSequence getPageTitle(int position) {
        return this.pagerModelManager.hasTitles()?this.pagerModelManager.getTitle(position):super.getPageTitle(position);
    }
}