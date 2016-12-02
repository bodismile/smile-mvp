package cn.smile.base.imageloader;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * 抽象的图片加载接口
 * @author smile
 */
public interface ILoader {

    /**
     * 加载圆形头像
     * @param path
     * @param iv
     * @param defaultRes
     */
    void loadAvator(String path, int defaultRes, ImageView iv);
    /**
     * 加载ImageView图片
     * @param iv
     * @param path
     * @param defaultRes
     */
    void load(String path, int defaultRes, ImageView iv, AbsImageLoader.ImageLoadingListener listener);
    /**
     * 加载View背景
     * @param v
     * @param url
     * @param defaultRes
     */
    void loadBg(String url, int defaultRes, View v);
    /**
     * 暂停
     * @param activity
     */
    void pause(Context activity);
    /**
     * 恢复加载
     * @param activity
     */
    void resume(Context activity);
    /**
     * 清除缓存
     */
    void clearCache(Context activity);
}
