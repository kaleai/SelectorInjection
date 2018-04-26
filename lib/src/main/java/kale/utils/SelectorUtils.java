package kale.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
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
            SelectorInjection injection = ((ISelectorView) view).initSelectorInjection(view.getContext(), attrs);
            injection.injection(view);
            
            if (view instanceof TextView) {
                new AppCompatTextViewHelper(((TextView) view)).loadFromAttributes(attrs, defStyle);
                view.setClickable(true);
            }
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
}
