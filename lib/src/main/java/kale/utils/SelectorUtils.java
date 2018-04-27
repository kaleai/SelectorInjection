package kale.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;

import kale.injection.SelectorInjection;
import kale.injection.SvgInjection;
import kale.ui.view.ISelectorView;

import static kale.injection.SelectorInjection.DEFAULT_COLOR;

/**
 * @author Kale
 * @date 2018/4/25
 */
public class SelectorUtils {

    public static SelectorInjection injectionToSelectorView(View view, AttributeSet attrs, int defStyle) {
        if (view instanceof ISelectorView) {

            new SvgInjection(view).loadFromAttributes(attrs, defStyle).injection();

            SelectorInjection injection = ((ISelectorView) view).initSelectorInjection(view.getContext(), attrs);
            injection.loadFromAttributes(attrs, defStyle);
            injection.injection();
            return injection;
        } else {
            return null;
        }
    }

    /**
     * https://yifeng.studio/2017/03/30/android-tint/
     */
    public static Drawable tintDrawable(Drawable drawable, int color) {
        drawable.mutate();
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color));
        return wrappedDrawable;
    }

    @Nullable
    public static Drawable getDrawable(TintTypedArray a, int resId) {
        Drawable drawable = a.getDrawable(resId);
        if (drawable != null) {
            return drawable.mutate(); // 放置shape复用产生的问题
        } else {
            return null;
        } 
    }

    public static int getColor(TintTypedArray a, int resId) {
        return a.getColor(resId, DEFAULT_COLOR);
    }

    public static int getDimension(TintTypedArray a, int resId) {
        int defaultWidth = 2;
        return a.getDimensionPixelSize(resId, defaultWidth);
    }

    public static int darken(final int color) {
        return blendColors(Color.BLACK, color, 0.3f);
    }

    public static int lighten(final int color, float fraction) {
        return blendColors(Color.WHITE, color, fraction);
    }

    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio of which to blend. 1.0 will return {@code color1}, 0.5 will give an even blend,
     *              0.0 will return {@code color2}.
     */
    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRatio = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRatio);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRatio);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRatio);
        return Color.rgb((int) r, (int) g, (int) b);
    }

}
