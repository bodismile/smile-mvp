package cn.smile.base.imageloader;

import cn.smile.util.Utils;

/**
 * @author smile
 */
public class ImageLoaderFactory {

    private static volatile ILoader sInstance;

    private ImageLoaderFactory() {}

    public static ILoader getLoader() {
        if (sInstance == null) {
            synchronized (ImageLoaderFactory.class) {
                if (sInstance == null) {
                    if (Utils.hasClass("com.bumptech.glide.Glide")) {
                        sInstance = new GlideImageLoader();
                    }
                }
            }
        }
        return sInstance;
    }
}
