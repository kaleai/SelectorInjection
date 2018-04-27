package kale.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
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


    /**
     * copy from DrawableUtils
     */
    public static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";

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

            new SvgInjection(view).loadFromAttributes(attrs, defStyle).injection();

            SelectorInjection injection = ((ISelectorView) view).initSelectorInjection(view.getContext(), attrs);
            injection.loadFromAttributes(attrs, defStyle);
            injection.injection();
            return injection;
        } else {
            return null;
        }
    }

    @Nullable
    public static Drawable getDrawable(Context context, TypedArray a, int resId) {
        final Drawable drawable;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = a.getDrawable(resId);
        } else {
            final int id = a.getResourceId(resId, -1);
            if (id != -1) {
                drawable = AppCompatResources.getDrawable(context, id);
            } else {
                drawable = null;
            }
        }
        if (drawable != null) {
            if (Build.VERSION.SDK_INT == 21 && VECTOR_DRAWABLE_CLAZZ_NAME.equals(drawable.getClass().getName())) {
                fixVectorDrawableTinting(drawable);
            }
            return drawable.mutate();
        } else {
            return null;
        }
    }

    /**
     * copy from ThemeUtils
     */
    private static void fixVectorDrawableTinting(final Drawable drawable) {
        final int[] originalState = drawable.getState();
        if (originalState.length == 0) {
            // The drawable doesn't have a state, so set it to be checked
            drawable.setState(new int[]{android.R.attr.state_checked});
        } else {
            // Else the drawable does have a state, so clear it
            drawable.setState(new int[0]);
        }
        // Now set the original state
        drawable.setState(originalState);
    }

    public static int getColor(TypedArray a, int resId) {
        return a.getColor(resId, DEFAULT_COLOR);
    }

    public static int getDimension(TypedArray a, int resId) {
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
