package kale.injection;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Kale
 * @date 2016/9/6
 */
public class AppCompatTextViewHelper {

    private static final int DEFAULT_COLOR = -1;

    private TextView mView;

    public AppCompatTextViewHelper(TextView view) {
        mView = view;
    }

    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(mView.getContext(), attrs,
                R.styleable.SelectorInjection, defStyleAttr, 0);

        Drawable[] drawables = mView.getCompoundDrawables();
        try {
            int leftColor = a.getColor(R.styleable.SelectorInjection_drawableLeftTint, DEFAULT_COLOR);
            int topColor = a.getColor(R.styleable.SelectorInjection_drawableTopTint, DEFAULT_COLOR);
            int rightColor = a.getColor(R.styleable.SelectorInjection_drawableRightTint, DEFAULT_COLOR);
            int bottomColor = a.getColor(R.styleable.SelectorInjection_drawableBottomTint, DEFAULT_COLOR);

            int[] colors = {leftColor, topColor, rightColor, bottomColor};
            tintDrawable(drawables, colors);
        } finally {
            a.recycle();
        }
    }

    private static void tintDrawable(Drawable[] drawables, int[] colors) {
        Drawable drawable;
        
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] != DEFAULT_COLOR && (drawable = drawables[i]) != null) {
                tintDrawable(drawable, ColorStateList.valueOf(colors[i]));
            }
        }
    }

    private static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        drawable.mutate();
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

}
