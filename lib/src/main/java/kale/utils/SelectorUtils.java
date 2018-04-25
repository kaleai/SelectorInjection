package kale.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import kale.injection.SelectorInjection;
import kale.ui.view.ISelectorView;

/**
 * @author Kale
 * @date 2018/4/25
 */
public class SelectorUtils {

    public static Drawable tintDrawable(Drawable drawable, int color) {
        drawable.mutate();
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color));
        return wrappedDrawable;
    }

    public static SelectorInjection injectionToTextView(View view, AttributeSet attrs, int defStyle) {
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
}
