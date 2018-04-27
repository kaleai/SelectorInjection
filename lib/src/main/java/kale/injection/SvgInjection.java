package kale.injection;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import kale.utils.SelectorUtils;

import static kale.injection.SelectorInjection.DEFAULT_COLOR;

/**
 * @author Kale
 * @date 2018/4/27
 *
 * 本身TintContextWrapper中已经通过VectorEnabledTintResources其实已经支持了svg了，但是里面加载drawable的方案没有实现
 * 
 * 这里读出svg然后进行图片的相关设置
 */
public class SvgInjection {

    private final View view;

    public SvgInjection(View v) {
        view = v;
    }

    public SvgInjection loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        Context context = view.getContext();
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.SelectorInjection, defStyleAttr, 0);

        if (view instanceof TextView) {
            tintTextViewDrawables((TextView) view, a);
        }

        if (view instanceof CompoundButton) {
            Drawable btnDrawable = a.getDrawable(R.styleable.SelectorInjection_button);
            ((CompoundButton) view).setButtonDrawable(btnDrawable);
        }
        a.recycle();
        return this;
    }

    public void injection(){
        
    }

    /**
     * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0801/3248.html
     */
    private void tintTextViewDrawables(TextView view, TintTypedArray a) {
        Drawable[] drawables = {
                a.getDrawable(R.styleable.SelectorInjection_drawableLeft),
                a.getDrawable(R.styleable.SelectorInjection_drawableTop),
                a.getDrawable(R.styleable.SelectorInjection_drawableRight),
                a.getDrawable(R.styleable.SelectorInjection_drawableBottom)
        };

        int[] colors = {
                a.getColor(R.styleable.SelectorInjection_drawableLeftTint, DEFAULT_COLOR),
                a.getColor(R.styleable.SelectorInjection_drawableTopTint, DEFAULT_COLOR),
                a.getColor(R.styleable.SelectorInjection_drawableRightTint, DEFAULT_COLOR),
                a.getColor(R.styleable.SelectorInjection_drawableBottomTint, DEFAULT_COLOR)};

        Drawable tempDrawable;

        for (int i = 0; i < colors.length; i++) {
            if (colors[i] != DEFAULT_COLOR && (tempDrawable = drawables[i]) != null) {
                SelectorUtils.tintDrawable(tempDrawable, colors[i]);
            }
        }
        view.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

}
