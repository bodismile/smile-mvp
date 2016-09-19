package cn.smile.helper;

import cn.smile.listener.OnPageChangeListener;

/**
 * 管理Viewpager嵌套Fragment这种情况下，各Fragment之间相互跳转的问题
 * 使用方法：
 * 1、在包含Viewpager的activity/fragment的onCreate方法中调用：
 * PageChangeManager.getInstance().setPageListener(this);
 * 2、在需要进行跳转的子Fragment中调用：
 * PageChangeManager.getInstance().onSwitch(1);
 * Created by smile on 2016/7/19.
 */
public class PageChangeManager {

    private static volatile PageChangeManager sInstance;
    public OnPageChangeListener listener;
    private PageChangeManager() {}

    public static PageChangeManager getInstance() {
        if (sInstance == null) {
            synchronized (PageChangeManager.class) {
                if (sInstance == null) {
                    sInstance = new PageChangeManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 设置页面监听器
     * @param listener
     */
    public void setPageListener(OnPageChangeListener listener){
        this.listener =listener;
    }

    /**
     * 执行滑动操作
     * @param page
     */
    public void onSwitch(int page){ listener.onChange(page); }

}
