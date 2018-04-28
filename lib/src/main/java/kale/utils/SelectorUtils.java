package kale.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;

import kale.injection.SelectorInjection;
import kale.injection.SvgInjection;
import kale.ui.view.ISelectorView;

/**
 * @author Kale
 * @date 2018/4/25
 */
public class SelectorUtils {

    public static SelectorInjection injectionToSelectorView(View view, AttributeSet attrs, int defStyle) {
        if (view instanceof ISelectorView) {

            SvgInjection svgInjection = new SvgInjection(view);
            svgInjection.loadFromAttributes(attrs, defStyle);
            svgInjection.injection();

            SelectorInjection injection = ((ISelectorView) view).createSelectorInjection(view.getContext(), attrs);
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
    public static void tintDrawable(Drawable drawable, int color) {
        drawable.mutate();
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color));
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
    public static int blendColors(int color1, int color2, float ratio) {
        final float inverseRatio = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRatio);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRatio);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRatio);
        return Color.rgb((int) r, (int) g, (int) b);
    }

}
