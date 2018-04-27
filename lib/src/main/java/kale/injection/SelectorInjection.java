package kale.injection;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import kale.utils.SelectorUtils;

import static kale.utils.SelectorUtils.getColor;
import static kale.utils.SelectorUtils.getDimension;

/**
 * View的一个selector注入装置，通过构造函数即可注入。之后调用{@link #injection()}即可.
 *
 * @author Kale
 * @date 2015/5/25
 */
public class SelectorInjection {

    public static int DEFAULT_COLOR = -1;

    /**
     * 当前view的selector对象
     */
    private StateListDrawable selector = new StateListDrawable();

    public SelectorBean normal = new SelectorBean();

    public SelectorBean pressed = new SelectorBean();

    public SelectorBean checked = new SelectorBean();

    public SelectorBean disable = new SelectorBean();

    private View view;

    /**
     * 是否将drawable设置到src中，如果不是那么默认是background
     */
    public boolean inSrc;

    /**
     * 是否是智能模式，如果是的那么会自动计算按下后的颜色
     */
    public boolean isSmart;

    /**
     * 是否展示水波纹
     */
    public boolean showRipple;

    public SelectorInjection(View view) {
        this.view = view;
    }

    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        Context context = view.getContext();

        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.SelectorInjection, defStyleAttr, 0);

        isSmart = a.getBoolean(R.styleable.SelectorInjection_isSmart, true);
        inSrc = a.getBoolean(R.styleable.SelectorInjection_inSrc, false);
        showRipple = a.getBoolean(R.styleable.SelectorInjection_showRipple, false);

        normal.drawable = a.getDrawable(R.styleable.SelectorInjection_normalDrawable);
        pressed.drawable = a.getDrawable(R.styleable.SelectorInjection_pressedDrawable);
        checked.drawable = a.getDrawable(R.styleable.SelectorInjection_checkedDrawable);
        disable.drawable = a.getDrawable(R.styleable.SelectorInjection_disableDrawable);

        normal.color = getColor(a, R.styleable.SelectorInjection_normalColor);
        pressed.color = getColor(a, R.styleable.SelectorInjection_pressedColor);
        checked.color = getColor(a, R.styleable.SelectorInjection_checkedColor);
        disable.color = getColor(a, R.styleable.SelectorInjection_disableColor);

        normal.strokeColor = getColor(a, R.styleable.SelectorInjection_normalStrokeColor);
        normal.strokeWidth = getDimension(a, R.styleable.SelectorInjection_normalStrokeWidth);

        pressed.strokeColor = getColor(a, R.styleable.SelectorInjection_pressedStrokeColor);
        pressed.strokeWidth = getDimension(a, R.styleable.SelectorInjection_pressedStrokeWidth);

        checked.strokeColor = getColor(a, R.styleable.SelectorInjection_checkedStrokeColor);
        checked.strokeWidth = getDimension(a, R.styleable.SelectorInjection_checkedStrokeWidth);

        disable.strokeColor = getColor(a, R.styleable.SelectorInjection_disableStrokeColor);
        disable.strokeWidth = getDimension(a, R.styleable.SelectorInjection_disableStrokeWidth);

        a.recycle();
    }

    public void injection() {
        // 如果是智能模式，那么自动处理按压效果
        if (isSmart && normal.drawable != null && pressed.drawable == null) {
            pressed.drawable = normal.drawable.getConstantState().newDrawable();
        }
        if (pressed.drawable != null && pressed.color == DEFAULT_COLOR) {
            pressed.color = isSmart ? getPressedColor(normal.color) : pressed.color;
        }

        configDrawable(pressed, false, new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed});
        configDrawable(checked, false, new int[]{android.R.attr.state_enabled, android.R.attr.state_checked});
        configDrawable(normal, true, new int[]{-android.R.attr.state_pressed, android.R.attr.state_enabled});
        configDrawable(disable, false, new int[]{-android.R.attr.state_enabled});

        setSelectorDrawableToView();
    }

    private void configDrawable(SelectorBean bean, boolean normalDrawable, int[] stateSets) {
        if (bean.drawable == null) {
            return;
        }
        Drawable drawable = setColorAndStroke(bean, normalDrawable);
        selector.addState(stateSets, drawable);
        bean.drawable.mutate();
    }

    /**
     * 设置背景颜色和描边的颜色/宽度
     */
    private Drawable setColorAndStroke(SelectorBean bean, boolean isNormal) {
        Drawable drawable = bean.drawable;
        int color = bean.color;
        int strokeColor = bean.strokeColor;
        int strokeWidth = bean.strokeWidth;

        if (drawable instanceof GradientDrawable) {
            configShapeDrawable(drawable, color, strokeColor, strokeWidth, isNormal);
        } else if (drawable instanceof LayerDrawable) {
            // 如果是layer-list，先找到要设置的shape，然后再进行设置
            Drawable shape = ((LayerDrawable) drawable).findDrawableByLayerId(android.R.id.background);
            if (shape instanceof GradientDrawable) {
                configShapeDrawable(shape, color, strokeColor, strokeWidth, isNormal);
            }
        } else if (drawable instanceof BitmapDrawable) {
            // do nothing
        } else {
            // tint
            SelectorUtils.tintDrawable(drawable, color);
        }
        return drawable;
    }

    /**
     * 设置shape的颜色和描边
     *
     * @param color       shape对象的背景色
     * @param strokeColor shape的描边颜色
     * @param strokeWidth shape的描边宽度
     */
    private void configShapeDrawable(Drawable drawable, int color, int strokeColor, int strokeWidth, boolean isNormal) {
        GradientDrawable shape = (GradientDrawable) drawable;
        if (showRipple && !isNormal && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shape.setColor(normal.color);
        } else {
            shape.setColor(color);
        }
        if (strokeColor != DEFAULT_COLOR) {
            shape.setStroke(strokeWidth, strokeColor);
        }
    }

    /**
     * 重要方法，最终通过这个给view设置selector效果
     */
    private void setSelectorDrawableToView() {
        selector.setEnterFadeDuration(10);
        selector.setExitFadeDuration(10);

        if (view instanceof RadioButton) {
            ((RadioButton) view).setButtonDrawable(selector);
        } else if (view instanceof ImageButton && inSrc) {
            ((ImageButton) view).setImageDrawable(selector);
        } else {
            if (showRipple && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RippleDrawable ripple = (RippleDrawable) view.getContext().getDrawable(R.drawable.si_ripple);
                assert ripple != null;
                ripple.setDrawableByLayerId(android.R.id.background, selector);
                ripple.setColor(SelectorUtils.createColorStateList(pressed.color));
                view.setBackground(ripple);
            } else {
                view.setBackgroundDrawable(selector);
            }
        }
    }

    /**
     * 自动计算得到按下的颜色，如果不满足需求可重写
     */
    protected int getPressedColor(int normalColor) {
        return SelectorUtils.darken(normalColor);
    }

}
