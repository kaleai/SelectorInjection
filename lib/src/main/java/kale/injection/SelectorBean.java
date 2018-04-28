package kale.injection;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;

/**
 * @author Kale
 * @date 2018/4/27
 */
public class SelectorBean {

    @Nullable
    public Drawable drawable;

    public int color;

    public int strokeColor, strokeWidth;

    public static SelectorBean create(TintTypedArray a, int drawableRes, int colorRes, int strokeColorRes, int strokeWidthRes) {
        SelectorBean bean = new SelectorBean();
        bean.drawable = getDrawable(a, drawableRes);
        bean.color = a.getColor(colorRes, SelectorInjection.DEFAULT_COLOR);
        bean.strokeColor = a.getColor(strokeColorRes, SelectorInjection.DEFAULT_COLOR);
        bean.strokeWidth = a.getDimensionPixelSize(strokeWidthRes, 2);
        return bean;
    }

    @Nullable
    private static Drawable getDrawable(TintTypedArray a, int resId) {
        Drawable drawable = a.getDrawable(resId);
        if (drawable != null) {
            return drawable.mutate(); // 防止shape复用产生的问题
        } else {
            return null;
        }
    }
}
