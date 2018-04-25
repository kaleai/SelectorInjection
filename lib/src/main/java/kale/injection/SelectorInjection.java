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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import kale.utils.SelectorUtils;

/**
 * View的一个selector注入装置，通过构造函数即可注入。之后调用{@link #injection(View)}即可.
 *
 * @author Kale
 * @date 2015/5/25
 */
public class SelectorInjection {

    public static int DEFAULT_COLOR = 0x0106000d;

    private static final int DEFAULT_STROKE_WIDTH = 2, ANIMATION_TIME = 10;

    /**
     * 颜色
     */
    public int normalColor, pressedColor, checkedColor, disableColor;

    /**
     * 描边的宽度，如果不设置会根据默认的宽度进行描边
     */
    public int normalStrokeColor, normalStrokeWidth;

    public int pressedStrokeColor, pressedStrokeWidth;

    public int checkedStrokeColor, checkedStrokeWidth;

    public int disableStrokeColor, disableStrokeWidth;

    /**
     * drawable
     */
    public Drawable normal, pressed, checked, disable;

    private int normalId, pressedId, checkedId, disableId;

    /**
     * 是否将drawable设置到src中，如果不是那么默认是background
     */
    public boolean isSrc;

    /**
     * 是否是智能模式，如果是的那么会自动计算按下后的颜色
     */
    public boolean isSmart;

    /**
     * 是否展示水波纹
     */
    public boolean showRipple;

    private boolean isPressedForPreview;

    public SelectorInjection(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectorInjection);

        isSmart = a.getBoolean(R.styleable.SelectorInjection_isSmart, true);

        normal = a.getDrawable(R.styleable.SelectorInjection_normalDrawable);
        pressed = a.getDrawable(R.styleable.SelectorInjection_pressedDrawable);
        checked = a.getDrawable(R.styleable.SelectorInjection_checkedDrawable);
        disable = a.getDrawable(R.styleable.SelectorInjection_disableDrawable);

        normalColor = getColor(a, R.styleable.SelectorInjection_normalColor);
        pressedColor = getColor(a, R.styleable.SelectorInjection_pressedColor);
        checkedColor = getColor(a, R.styleable.SelectorInjection_checkedColor);
        disableColor = getColor(a, R.styleable.SelectorInjection_disableColor);

        normalStrokeColor = getColor(a, R.styleable.SelectorInjection_normalStrokeColor);
        normalStrokeWidth = a.getDimensionPixelSize(R.styleable.SelectorInjection_normalStrokeWidth, DEFAULT_STROKE_WIDTH);

        pressedStrokeColor = getColor(a, R.styleable.SelectorInjection_pressedStrokeColor);
        pressedStrokeWidth = a.getDimensionPixelOffset(R.styleable.SelectorInjection_pressedStrokeWidth, DEFAULT_STROKE_WIDTH);

        checkedStrokeColor = getColor(a, R.styleable.SelectorInjection_checkedStrokeColor);
        checkedStrokeWidth = a.getDimensionPixelSize(R.styleable.SelectorInjection_checkedStrokeWidth, DEFAULT_STROKE_WIDTH);

        disableStrokeColor = getColor(a, R.styleable.SelectorInjection_disableStrokeColor);
        disableStrokeWidth = a.getDimensionPixelSize(R.styleable.SelectorInjection_disableStrokeWidth, DEFAULT_STROKE_WIDTH);

        isSrc = a.getBoolean(R.styleable.SelectorInjection_isSrc, false);

        showRipple = a.getBoolean(R.styleable.SelectorInjection_showRipple, false);

        String string = a.getString(R.styleable.SelectorInjection_android_contentDescription);
        isPressedForPreview = TextUtils.equals(string, "isPressed");
        isPressedForPreview = a.getBoolean(R.styleable.SelectorInjection_isPressed, false);

        a.recycle();
    }

    public void injection(View view) {
        StateListDrawable selector = new StateListDrawable();// 背景选择器

        // 如果是智能模式，那么自动处理按压效果
        if (isSmart && normal != null && pressed == null) {
            pressed = normal.getConstantState().newDrawable();
        }

        configPressedDrawable(selector);

        configCheckedDrawable(selector);

        configNormalDrawable(selector);

        configDisableDrawable(selector);

        setSelector(view, selector);

        if (view.isInEditMode()) {
            if (isPressedForPreview) {
                // normal -> pressed
                view.setPressed(true);
            }
        }
    }

    public void setEnabled(View view, boolean enabled) {
        if (isSmart) {
            // 如果是智能模式，那么自动处理不可用状态效果
            view.setAlpha(!enabled ? 0.3f : 1);
        } else {
            view.setEnabled(enabled);
        }
    }

    /**
     * 开始设置普通状态时的样式（颜色，描边）
     */
    private void configNormalDrawable(StateListDrawable selector) {
        if (normal == null) {
            return;
        }
        setColorAndStroke(normal, normalColor, normalStrokeColor, normalStrokeWidth, true);
        selector.addState(new int[]{}, normal);
    }

    /**
     * 设置按下后的样式（颜色，描边）
     */
    private void configPressedDrawable(StateListDrawable selector) {
        if (pressed == null) {
            return;
        }
        if (pressedColor == DEFAULT_COLOR) {
            pressedColor = isSmart ? getPressedColor(normalColor) : pressedColor;
        }
        setColorAndStroke(pressed, pressedColor, pressedStrokeColor, pressedStrokeWidth, false);
        // 给selector设置pressed的状态
        selector.addState(new int[]{android.R.attr.state_pressed}, pressed);
        selector.addState(new int[]{android.R.attr.state_focused}, pressed);
        pressed.mutate();
    }

    /**
     * 设置选中状态下的样子
     */
    private void configCheckedDrawable(StateListDrawable selector) {
        if (checked == null) {
            return;
        }
        setColorAndStroke(checked, checkedColor, checkedStrokeColor, checkedStrokeWidth, false);
        selector.addState(new int[]{android.R.attr.state_checked}, checked);
        checked.mutate();
    }

    private void configDisableDrawable(StateListDrawable selector) {
        if (disable == null) {
            return;
        }
        setColorAndStroke(disable, disableColor, disableStrokeColor, disableStrokeWidth, false);
        selector.addState(new int[]{-android.R.attr.state_enabled}, disable);
        disable.mutate();
    }

    public void setSelector(View view, StateListDrawable selector) {
        selector.setEnterFadeDuration(ANIMATION_TIME);
        selector.setExitFadeDuration(ANIMATION_TIME);

        if (view instanceof RadioButton) {
            ((RadioButton) view).setButtonDrawable(selector);
        } else if (view instanceof ImageButton && isSrc) {
            ((ImageButton) view).setImageDrawable(selector);
        } else {
            if (showRipple && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RippleDrawable ripple = (RippleDrawable) view.getContext().getDrawable(R.drawable.si_ripple);
                assert ripple != null;
                ripple.setDrawableByLayerId(android.R.id.background, selector);
                ripple.setColor(createColorStateList(pressedColor, pressedColor, pressedColor, pressedColor));
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
            shape.setColor(normalColor);
        } else {
            shape.setColor(color);
        }
        if (strokeColor != DEFAULT_COLOR) {
            shape.setStroke(strokeWidth, strokeColor);
        }
    }

    /**
     * Make a dark color to press effect
     * 自动计算得到按下的颜色，如果不满足需求可重写
     */
    /*protected int getPressedColor(int normalColor) {
        int alpha = 255;
        int r = (normalColor >> 16) & 0xFF;
        int g = (normalColor >> 8) & 0xFF;
        int b = (normalColor >> 1) & 0xFF;
        r = (r - 50 < 0) ? 0 : r - 50;
        g = (g - 50 < 0) ? 0 : g - 50;
        b = (b - 50 < 0) ? 0 : b - 50;
        return Color.argb(alpha, r, g, b);
    }*/
    protected int getPressedColor(int normalColor) {
        return darken(normalColor, 0.3f);
    }

    public static int darken(final int color, float fraction) {
        return blendColors(Color.BLACK, color, fraction);
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

    private static int getColor(TypedArray a, int styleResId) {
        return a.getColor(styleResId, DEFAULT_COLOR);
    }

}
