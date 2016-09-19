package cn.smile.util;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * 给Drawable着色，不用在写一堆selector
 * @author  smile
 */
public final class DrawableUtil {

    /**
     * 给原始Drawable着色
     * @param originDrawable 原Drawable资源
     * @param color 要变化的颜色
     * @return
     */
    public static Drawable tint(Drawable originDrawable, int color) {
        return tint(originDrawable, ColorStateList.valueOf(color));
    }

    /**
     * 给原Drawable着色
     * @param originDrawable 原Drawable资源
     * @param color 要变化的颜色
     * @param tintMode 着色mode
     * @return
     */
    public static Drawable tint(Drawable originDrawable, int color, PorterDuff.Mode tintMode) {
        return tint(originDrawable, ColorStateList.valueOf(color), tintMode);
    }

    /**
     * 给原Drawable着色
     * @param originDrawable 原Drawable资源
     * @param colorStateList 状态color集合
     * @return
     */
    public static Drawable tint(Drawable originDrawable, ColorStateList colorStateList) {
        return tint(originDrawable, colorStateList, null);
    }

    /**
     * 根据View的状态着色
     * @param originDrawable
     * @param colorStateList
     * @param tintMode
     * @return
     */
    public static Drawable tint(Drawable originDrawable, ColorStateList colorStateList, PorterDuff.Mode tintMode) {
        Drawable tintDrawable = DrawableCompat.wrap(originDrawable.mutate());
        if (tintMode != null) {
            DrawableCompat.setTintMode(tintDrawable, tintMode);
        }
        DrawableCompat.setTintList(tintDrawable, colorStateList);
        return tintDrawable;
    }


}