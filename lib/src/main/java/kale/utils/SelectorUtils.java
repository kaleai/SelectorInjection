package kale.utils;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import kale.injection.SelectorInjection;
import kale.ui.view.ISelectorView;

import static kale.injection.SelectorInjection.DEFAULT_COLOR;

/**
 * @author Kale
 * @date 2018/4/25
 */
public class SelectorUtils {

    /**
     * https://yifeng.studio/2017/03/30/android-tint/
     */
    public static Drawable tintDrawable(Drawable drawable, int color) {
        drawable.mutate();
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color));
        return wrappedDrawable;
    }

    public static SelectorInjection injectionToSelectorView(View view, AttributeSet attrs, int defStyle) {
        if (view instanceof ISelectorView) {
            if (view instanceof TextView) {
                new AppCompatTextViewHelper(((TextView) view)).loadFromAttributes(attrs, defStyle);
                view.setClickable(true);
            }
            SelectorInjection injection = ((ISelectorView) view).initSelectorInjection(view.getContext(), attrs);
            injection.injection(view);
            return injection;
        } else {
            return null;
        }
    }

    @Nullable
    public static Drawable getDrawable(TypedArray a, int resId) {
        Drawable drawable = a.getDrawable(resId);
        if (drawable != null) {
            return drawable.mutate();
        } else {
            return null;
        }
    }

    public static int getColor(TypedArray a, int resId) {
        return a.getColor(resId, DEFAULT_COLOR);
    }

    public static int getDimension(TypedArray a, int resId) {
        int defaultWidth = 2;
        return a.getDimensionPixelSize(resId, defaultWidth);
    }
}
