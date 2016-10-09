package cn.smile.base.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 使用UIL加载图片
 * @author smile
 */
public class UILImageLoader implements ILoader {

    /**
     * 获取配置器
     * @param isCircle
     * @param defaultRes
     * @return
     */
    public DisplayImageOptions getDefaultOptions(boolean isCircle, int defaultRes){
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型:设置为RGB565比起默认的ARGB_8888要节省大量的内存
//                .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                .resetViewBeforeLoading(true);//设置图片在下载前是否重置，复位
                if(isCircle){
//                    builder.displayer(new RoundedBitmapDisplayer(12));//是否设置为圆角，弧度为多少
                    builder.displayer(new CircleBitmapDisplayer(10));
                }
                if(defaultRes!=0){
                    builder.showImageForEmptyUri(defaultRes)//设置图片Uri为空或是错误的时候显示的图片
//                            .showImageOnLoading(defaultRes) //设置图片在下载期间显示的图片-->应该去掉-会造成ListView中图片闪烁
                            .showImageOnFail(defaultRes);  //设置图片加载/解码过程中错误时候显示的图片
                }
        return builder.build();//构建完成
    }

    @Override
    public void loadAvator(String path, int defaultRes, ImageView iv) {
        if(!TextUtils.isEmpty(path)) {
            //解决ListView滚动时重复加载图片
            if (!path.equals(iv.getTag())) {
                iv.setTag(path);
                ImageAware imageAware = new ImageViewAware(iv, false);
                ImageLoader.getInstance().displayImage(path, imageAware, getDefaultOptions(true, defaultRes));
            }
        }else{
            iv.setImageResource(defaultRes);
        }
    }

    @Override
    public void load(String path, int defaultRes, ImageView iv, final AbsImageLoader.ImageLoadingListener listener) {
        if(!TextUtils.isEmpty(path)) {
            if (!path.equals(iv.getTag())) {
                iv.setTag(path);
                ImageAware imageAware = new ImageViewAware(iv, false);
                ImageLoader.getInstance().displayImage(path, imageAware,
                        getDefaultOptions(false, defaultRes), new SimpleImageLoadingListener() {

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                if(listener!=null){
                                    listener.onSuccess(imageUri, view);
                                }
                            }
                        });
            }
        }else{
            iv.setImageResource(defaultRes);
        }
    }

    @Override
    public void loadBg(String url, int defaultRes, View v) {

    }

    @Override
    public void pause(Context context) {
        ImageLoader.getInstance().pause();
    }

    @Override
    public void resume(Context context) {
        ImageLoader.getInstance().resume();
    }

    @Override
    public void clearCache(Context context) {
        //清除SD卡上的图片
        ImageLoader.getInstance().clearDiskCache();
        //清空缓存图片
        ImageLoader.getInstance().clearMemoryCache();
    }
}
