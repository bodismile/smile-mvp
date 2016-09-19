package cn.smile.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;

/**
 * 工具类:包含dp-px互转等方法
 * @author  smile
 */
public final class Utils {

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**px转dp
     * @param context
     * @param dipValue
     * @return
     */
    public static int px2dp(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue / scale + 0.5f);
    }

    /**
     * 通过反射获取指定位置的泛型的实际类型参数
     * @param o 当前类对象
     * @param i 指定位置
     * @param <T> 返回实际的类型参数
     * @return
     */
    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否包含指定类
     * @param className
     * @return true-包含 false-不包含
     */
    public static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取本地资源图片
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap getBitmap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    /**
     * 以节省内存的方式读取Res资源
     * @param context
     * @param resId
     * @return
     */
    public static Drawable getDrawable(Context context, int resId){
        return new BitmapDrawable(getBitmap(context,resId));
    }

    /**
     * 保留到小数点后几位
     */
    private static final int DEF_DIV_SCALE = 3;

    /**
     * 两个Double数相除，四舍五入并保留小数点后3位数
     * @param v1
     * @param v2
     * @return Double
     */
    public static double div(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        //四舍五入
        return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return
     */
    public static double div(double v1,double v2,int scale) {
        if(scale<0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static Integer round(double v,int scale){
        if(scale<0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).intValue();
    }

}
