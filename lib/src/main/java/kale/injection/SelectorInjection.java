package kale.injection;

import android.content.Context;
import android.content.res.ColorStateList;
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
import android.widget.ImageView;
import android.widget.RadioButton;

import kale.ui.view.SelectorRadioButton;
import kale.utils.SelectorUtils;

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

    private View view;

    public SelectorBean normal, pressed, checked, disable;

    /**
     * 是否将drawable设置到src中，如果不是那么默认是background
     */
    public boolean inSrc;

    /**
     * 是否是智能模式，如果是的那么会自动计算按下后的颜色
     */
    public boolean smartColor;

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

        normal = SelectorBean.create(a,
                R.styleable.SelectorInjection_normalDrawable,
                R.styleable.SelectorInjection_normalColor,
                R.styleable.SelectorInjection_normalStrokeColor,
                R.styleable.SelectorInjection_normalStrokeWidth
        );
        pressed = SelectorBean.create(a,
                R.styleable.SelectorInjection_pressedDrawable,
                R.styleable.SelectorInjection_pressedColor,
                R.styleable.SelectorInjection_pressedStrokeColor,
                R.styleable.SelectorInjection_pressedStrokeWidth
        );
        checked = SelectorBean.create(a,
                R.styleable.SelectorInjection_checkedDrawable,
                R.styleable.SelectorInjection_checkedColor,
                R.styleable.SelectorInjection_checkedStrokeColor,
                R.styleable.SelectorInjection_checkedStrokeWidth
        );
        disable = SelectorBean.create(a,
                R.styleable.SelectorInjection_disableDrawable,
                R.styleable.SelectorInjection_disableColor,
                R.styleable.SelectorInjection_disableStrokeColor,
                R.styleable.SelectorInjection_disableStrokeWidth
        );

        inSrc = a.getBoolean(R.styleable.SelectorInjection_inSrc, false);
        smartColor = a.getBoolean(R.styleable.SelectorInjection_smartColor, true);
        showRipple = a.getBoolean(R.styleable.SelectorInjection_ripple, false);

        Drawable src = a.getDrawable(R.styleable.SelectorInjection_src);
        if (src != null && view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(src);
        }

        Drawable background = a.getDrawable(R.styleable.SelectorInjection_background);
        int backgroundTint = a.getColor(R.styleable.SelectorInjection_backgroundTint, DEFAULT_COLOR);

        if (backgroundTint != DEFAULT_COLOR && background != null) {
            SelectorUtils.tintDrawable(background, backgroundTint);
            view.setBackground(background);
        }

        a.recycle();
    }

    public void injection() {
        if (pressed.drawable == null && smartColor && normal.drawable != null) {
            // 如果是智能模式，会自动根据normal生成按压时的drawable
            pressed.drawable = normal.drawable.getConstantState().newDrawable();
        }
        if (pressed.drawable != null && pressed.color == DEFAULT_COLOR) {
            // 如果没有设置按压效果，那么会自动计算出按压的色值
            pressed.color = smartColor ? getPressedColor(normal.color) : pressed.color;
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
//        bean.drawable.mutate();
    }

    /**
     * 设置背景颜色和描边的颜色/宽度
     */
    private Drawable setColorAndStroke(SelectorBean bean, boolean isNormal) {
        final Drawable drawable = bean.drawable;
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
        if (normal.drawable == null) {
            return;
        }

        selector.setEnterFadeDuration(10);
        selector.setExitFadeDuration(10);

        // 为了可读性用了分段的return

        if (view instanceof SelectorRadioButton) {
            ((RadioButton) view).setButtonDrawable(selector);
            return;
        }

        if (inSrc && view instanceof ImageButton) {
            ((ImageButton) view).setImageDrawable(selector);
            return;
        }

        if (showRipple && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // https://blog.csdn.net/AwayEagle/article/details/52583913
            RippleDrawable ripple = (RippleDrawable) view.getResources().getDrawable(R.drawable.si_ripple);
            assert ripple != null;

            ripple.setColor(ColorStateList.valueOf(pressed.color));
            ripple.setDrawableByLayerId(android.R.id.mask, normal.drawable); // 这里用的normal时的drawable做边界
            ripple.setDrawableByLayerId(android.R.id.content, selector);
            view.setBackground(ripple);
            return;
        }

        view.setBackground(selector);
    }

    /**
     * 自动计算得到按下的颜色，如果不满足需求可重写
     */
    protected int getPressedColor(int normalColor) {
        return SelectorUtils.darken(normalColor);
    }

}