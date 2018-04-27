package kale.injection;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import kale.utils.SelectorUtils;

import static kale.utils.SelectorUtils.getColor;
import static kale.utils.SelectorUtils.getDimension;
import static kale.utils.SelectorUtils.getDrawable;

/**
 * View的一个selector注入装置，通过构造函数即可注入。之后调用{@link #injection(View)}即可.
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

    public SelectorBean normalBean = new SelectorBean();

    public SelectorBean pressedBean = new SelectorBean();

    public SelectorBean checkedBean = new SelectorBean();

    public SelectorBean disableBean = new SelectorBean();

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

    public SelectorInjection(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectorInjection);

        isSmart = a.getBoolean(R.styleable.SelectorInjection_isSmart, true);
        inSrc = a.getBoolean(R.styleable.SelectorInjection_inSrc, false);
        showRipple = a.getBoolean(R.styleable.SelectorInjection_showRipple, false);

        normalBean.drawable = getDrawable(context, a, R.styleable.SelectorInjection_normalDrawable);
        pressedBean.drawable = getDrawable(context, a, R.styleable.SelectorInjection_pressedDrawable);
        checkedBean.drawable = getDrawable(context, a, R.styleable.SelectorInjection_checkedDrawable);
        disableBean.drawable = getDrawable(context, a, R.styleable.SelectorInjection_disableDrawable);

        normalBean.color = getColor(a, R.styleable.SelectorInjection_normalColor);
        pressedBean.color = getColor(a, R.styleable.SelectorInjection_pressedColor);
        checkedBean.color = getColor(a, R.styleable.SelectorInjection_checkedColor);
        disableBean.color = getColor(a, R.styleable.SelectorInjection_disableColor);

        normalBean.strokeColor = getColor(a, R.styleable.SelectorInjection_normalStrokeColor);
        normalBean.strokeWidth = getDimension(a, R.styleable.SelectorInjection_normalStrokeWidth);

        pressedBean.strokeColor = getColor(a, R.styleable.SelectorInjection_pressedStrokeColor);
        pressedBean.strokeWidth = getDimension(a, R.styleable.SelectorInjection_pressedStrokeWidth);

        checkedBean.strokeColor = getColor(a, R.styleable.SelectorInjection_checkedStrokeColor);
        checkedBean.strokeWidth = getDimension(a, R.styleable.SelectorInjection_checkedStrokeWidth);

        disableBean.strokeColor = getColor(a, R.styleable.SelectorInjection_disableStrokeColor);
        disableBean.strokeWidth = getDimension(a, R.styleable.SelectorInjection_disableStrokeWidth);

        a.recycle();
    }

    public void injection(View view) {
        // 如果是智能模式，那么自动处理按压效果
        if (isSmart && normalBean.drawable != null && pressedBean.drawable == null) {
            pressedBean.drawable = normalBean.drawable.getConstantState().newDrawable();
        }
        if (pressedBean.drawable != null && pressedBean.color == DEFAULT_COLOR) {
            pressedBean.color = isSmart ? getPressedColor(normalBean.color) : pressedBean.color;
        }

        configPressedDrawable(selector);

        configCheckedDrawable(selector);

        configNormalDrawable(selector);

        configDisableDrawable(selector);

        setSelectorToView(view, selector);

    }

    public void setEnabled(View view, boolean enabled) {
        if (disableBean.drawable == null && isSmart) {
            // 如果是智能模式，那么自动处理不可用状态效果
            view.setAlpha(!enabled ? 0.3f : 1);
        }
    }

    /**
     * 开始设置普通状态时的样式（颜色，描边）
     */
    private void configNormalDrawable(StateListDrawable selector) {
        if (normalBean.drawable == null) {
            return;
        }
        setColorAndStroke(normalBean.drawable, normalBean.color, normalBean.strokeColor, normalBean.strokeWidth, true);
        selector.addState(new int[]{-android.R.attr.state_pressed, android.R.attr.state_enabled}, normalBean.drawable);
//        selector.addState(new int[]{}, normal);
        normalBean.drawable.mutate();
    }

    /**
     * 设置按下后的样式（颜色，描边）
     */
    private void configPressedDrawable(StateListDrawable selector) {
        if (pressedBean.drawable == null) {
            return;
        }
        setColorAndStroke(pressedBean.drawable, pressedBean.color, pressedBean.strokeColor, pressedBean.strokeWidth, false);
        // 给selector设置pressed的状态
        selector.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressedBean.drawable);
        selector.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, pressedBean.drawable);
        pressedBean.drawable.mutate();
    }

    /**
     * 设置选中状态下的样子
     */
    private void configCheckedDrawable(StateListDrawable selector) {
        if (checkedBean.drawable == null) {
            return;
        }
        setColorAndStroke(checkedBean.drawable, checkedBean.color, checkedBean.strokeColor, checkedBean.strokeWidth, false);
        selector.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_checked}, checkedBean.drawable);
        checkedBean.drawable.mutate();
    }

    /**
     * https://stackoverflow.com/questions/5092649/android-how-to-update-the-selectorstatelistdrawable-programmatically
     */
    private void configDisableDrawable(StateListDrawable selector) {
        if (disableBean.drawable == null) {
            return;
        }
        setColorAndStroke(disableBean.drawable, disableBean.color, disableBean.strokeColor, disableBean.strokeWidth, false);
        selector.addState(new int[]{-android.R.attr.state_enabled}, disableBean.drawable);
        disableBean.drawable.mutate();
    }

    /**
     * 重要方法，最终通过这个给view设置selector效果
     */
    private void setSelectorToView(View view, StateListDrawable selector) {
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
                ripple.setColor(createColorStateList(pressedBean.color, pressedBean.color, pressedBean.color, pressedBean.color));
                view.setBackground(ripple);
            } else {
                view.setBackgroundDrawable(selector);
            }
        }
    }

    /**
     * 设置背景颜色和描边的颜色/宽度
     */
    private void setColorAndStroke(Drawable drawable, int color, int strokeColor, int strokeWidth, boolean isNormal) {
        if (drawable instanceof GradientDrawable) {
            setShape((GradientDrawable) drawable, color, strokeColor, strokeWidth, isNormal);
        } else if (drawable instanceof LayerDrawable) {
            // 如果是layer-list，先找到要设置的shape
            Drawable shape = ((LayerDrawable) drawable).findDrawableByLayerId(android.R.id.background);
            if (shape instanceof GradientDrawable) {
                setShape((GradientDrawable) shape, color, strokeColor, strokeWidth, isNormal);
            }
        } else if (drawable instanceof BitmapDrawable) {
            // do nothing
        } else {
            // tint
            SelectorUtils.tintDrawable(drawable, color);
        }
    }

    /**
     * 设置shape的颜色和描边
     *
     * @param shape       shape对象
     * @param color       shape对象的背景色
     * @param strokeColor shape的描边颜色
     * @param strokeWidth shape的描边宽度
     */
    private void setShape(GradientDrawable shape, int color, int strokeColor, int strokeWidth, boolean isNormal) {
        if (showRipple && !isNormal && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shape.setColor(normalBean.color);
        } else {
            shape.setColor(color);
        }
        if (strokeColor != DEFAULT_COLOR) {
            shape.setStroke(strokeWidth, strokeColor);
        }
    }

    /**
     * 自动计算得到按下的颜色，如果不满足需求可重写
     */
    private int getPressedColor(int normalColor) {
        return darken(normalColor);
    }

    private int darken(final int color) {
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

    /**
     * 设置不同状态时其文字颜色
     *
     * @see "http://blog.csdn.net/sodino/article/details/6797821"
     */
    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};

        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};

        return new ColorStateList(states, colors);
    }

}
