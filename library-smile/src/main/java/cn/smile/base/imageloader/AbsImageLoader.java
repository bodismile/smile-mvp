package cn.smile.base.imageloader;

import android.view.View;

/**兼容UIL、Glide等图片框架的抽象类
 * @author smile
 */
public abstract class AbsImageLoader implements ILoader{

    protected String getPath(String path) {
        if (path == null) {
            path = "";
        }
        if (!path.startsWith("http") && !path.startsWith("file")) {
            path = "file://" + path;
        }
        return path;
    }

    public interface ImageLoadingListener {
        void onSuccess(String imageUri, View view);
    }
}
