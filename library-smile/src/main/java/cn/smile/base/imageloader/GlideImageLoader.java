package cn.smile.base.imageloader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import cn.smile.helper.CropCircleTransformation;

/**
 * 使用Glide加载图片
 * @author smile
 */
public class GlideImageLoader extends AbsImageLoader {

    public GlideImageLoader(){}

    @Override
    public void loadAvator(String path, int defaultRes,ImageView iv) {
        final String finalPath = getPath(path);
        Glide.with(iv.getContext())
                .load(finalPath)
                .placeholder(defaultRes)
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(iv.getContext()))
                .into(iv);
    }

    @Override
    public void loadBg(String path, int defaultRes, final View v) {
        final String finalPath = getPath(path);
        Glide.with(v.getContext())
                .load(finalPath)
                .placeholder(defaultRes)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        v.setBackgroundDrawable(resource);
                    }
                });
    }

    @Override
    public void load(String path, int defaultRes, final ImageView iv, final ImageLoadingListener listener) {
        final String finalPath = getPath(path);
        Glide.with(iv.getContext())
                .load(finalPath)
                .asBitmap()
                .placeholder(defaultRes)
                .error(defaultRes)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<Bitmap> target,
                                               boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource,String model,
                                                   Target<Bitmap> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        if(listener!=null){
                            listener.onSuccess(finalPath,iv);
                        }
                        return false;
                    }
                })
                .into(iv);
    }

    @Override
    public void pause(Activity activity) {
        Glide.with(activity).pauseRequests();
    }

    @Override
    public void resume(Activity activity) {
        Glide.with(activity).resumeRequestsRecursive();
    }
}
