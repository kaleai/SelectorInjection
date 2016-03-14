package kale.injection;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

/**
 * View的一个selector注入装置，通过构造函数即可注入。之后调用{@link #injection(View)}即可.
 *
 * @author Kale
 * @date 2015/5/25
 */
public class SelectorInjection {

    public static int DEFAULT_COLOR = 0x0106000d;

    public static final int DEFAULT_STROKE_WIDTH = 2;

    /**
     * 是否是智能模式，如果是的那么会自动计算按下后的颜色
     */
    boolean mIsSmart = true;

    /**
     * 正常情况颜色
     */
    private int mNormalColor;

    /**
     * 按下后的颜色（smart关闭后才有效）
     */
    private int mPressedColor;

    /**
     * 描边的颜色
     */
    private int mNormalStrokeColor;

    /**
     * 描边的宽度，如果不设置会根据默认的宽度进行描边
     */
    private int mNormalStrokeWidth;

    /**
     * 按下后描边的颜色
     */
    private int mPressedStrokeColor;

    /**
     * 按下后描边的宽度
     */
    private int mPressedStrokeWidth;

    /**
     * 选中后的描边颜色
     */
    private int mCheckedStrokeColor;

    /**
     * 选中后的描边宽度
     */
    private int mCheckedStrokeWidth;

    /**
     * 正常情况下的drawable
     */
    private Drawable mNormal;

    /**
     * 按下后的drawable
     */
    private Drawable mPressed;

    /**
     * 是否将drawable设置到src中，如果不是那么默认是background
     */
    private boolean mIsSrc;

    /**
     * 被选中时的图片
     */
    private Drawable mChecked;

    /**
     * 被选中时的颜色
     */
    private int mCheckedColor;
    
    private boolean mShowRipple;
    
    public SelectorInjection(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectorInjection);

        mIsSmart = a.getBoolean(R.styleable.SelectorInjection_isSmart, true);

        mNormal = a.getDrawable(R.styleable.SelectorInjection_normalDrawable);
        mPressed = a.getDrawable(R.styleable.SelectorInjection_pressedDrawable);
        mChecked = a.getDrawable(R.styleable.SelectorInjection_checkedDrawable);

        mNormalColor = a.getColor(R.styleable.SelectorInjection_normalColor, DEFAULT_COLOR);
        mPressedColor = a.getColor(R.styleable.SelectorInjection_pressedColor, DEFAULT_COLOR);
        mCheckedColor = a.getColor(R.styleable.SelectorInjection_checkedColor, DEFAULT_COLOR);

        mNormalStrokeColor = a.getColor(R.styleable.SelectorInjection_normalStrokeColor, DEFAULT_COLOR);
        mNormalStrokeWidth = a.getDimensionPixelSize(R.styleable.SelectorInjection_normalStrokeWidth, DEFAULT_STROKE_WIDTH);

        mPressedStrokeColor = a.getColor(R.styleable.SelectorInjection_pressedStrokeColor, DEFAULT_COLOR);
        mPressedStrokeWidth = a.getDimensionPixelOffset(R.styleable.SelectorInjection_pressedStrokeWidth, DEFAULT_STROKE_WIDTH);

        mCheckedStrokeColor = a.getColor(R.styleable.SelectorInjection_checkedStrokeColor, DEFAULT_COLOR);
        mCheckedStrokeWidth = a.getDimensionPixelSize(R.styleable.SelectorInjection_checkedStrokeWidth, DEFAULT_STROKE_WIDTH);

        mIsSrc = a.getBoolean(R.styleable.SelectorInjection_isSrc, false);

        mShowRipple = a.getBoolean(R.styleable.SelectorInjection_showRipple, true);
        a.recycle();
    }

    public void injection(View view) {
        StateListDrawable selector = new StateListDrawable();// 背景选择器
        // 如果是智能模式，那么按下的图片和原图都是一致的，仅仅是背景的颜色会有差别
        if (mIsSmart && mNormal != null && mPressed == null) {
            mPressed = mNormal.getConstantState().newDrawable();
        }
        if (mIsSmart && mNormal != null && mChecked == null) {
            mChecked = mNormal.getConstantState().newDrawable();
        }

        setPressedDrawable(selector);

        setCheckedDrawable(selector);

        setNormalDrawable(selector);

        setSelector(view, selector);
    }

    public void setSelector(View view, StateListDrawable selector) {
        int animationTime = 10;
        selector.setEnterFadeDuration(animationTime);
        selector.setExitFadeDuration(animationTime);

        if (view instanceof ImageButton && mIsSrc) {
            // 如果是imageButton，那么就看这个selector是给背景的还是给src的
            ((ImageButton) view).setImageDrawable(selector);
            //mView.setBackgroundDrawable(null);
        } else {
            if (mShowRipple && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RippleDrawable ripple = (RippleDrawable)view.getContext().getDrawable(R.drawable.ripple);
                assert ripple != null;
                ripple.setDrawableByLayerId(android.R.id.background, selector);
                int rippleColor = mPressedColor;
                view.setBackground(ripple);
            } else {
                view.setBackgroundDrawable(selector);
            }
        }
    }

    /**
     * 设置按下后的样式（颜色，描边）
     */
    private void setPressedDrawable(StateListDrawable selector) {
        if (mPressed != null) {
            // 颜色
            if (mPressedColor == DEFAULT_COLOR) {
                mPressedColor = mIsSmart ? getPressedColor(mNormalColor) : mPressedColor;
            }
            setColorAndStroke(mPressed, mPressedColor, mPressedStrokeColor, mPressedStrokeWidth,false);
            // 给selector设置pressed的状态
            selector.addState(new int[]{android.R.attr.state_pressed}, mPressed);
            selector.addState(new int[]{android.R.attr.state_focused}, mPressed);
            mPressed.mutate();
        }
    }

    /**
     * 设置选中状态下的样子
     */
    private void setCheckedDrawable(StateListDrawable selector) {
        if (mChecked != null) {
            setColorAndStroke(mChecked, mCheckedColor, mCheckedStrokeColor, mCheckedStrokeWidth,false);
            selector.addState(new int[]{android.R.attr.state_checked}, mChecked);
            mChecked.mutate();
        }
    }

    /**
     * 开始设置普通状态时的样式（颜色，描边）
     */
    private void setNormalDrawable(StateListDrawable selector) {
        if (mNormal != null) {
            setColorAndStroke(mNormal, mNormalColor, mNormalStrokeColor, mNormalStrokeWidth, true);
            selector.addState(new int[]{}, mNormal);
        }
    }

    /**
     * 设置背景颜色和描边的颜色/宽度
     */
    private void setColorAndStroke(Drawable drawable, int color, int strokeColor, int strokeWidth,boolean isNormal) {
        if (drawable instanceof GradientDrawable) {
            setShape((GradientDrawable) drawable, color, strokeColor, strokeWidth, isNormal);
        } else if (drawable instanceof LayerDrawable) {
            // 如果是layer-list，先找到要设置的shape
            Drawable shape = ((LayerDrawable) drawable).findDrawableByLayerId(android.R.id.background);
            if (shape instanceof GradientDrawable) {
                setShape((GradientDrawable) shape, color, strokeColor, strokeWidth, isNormal);
            }
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
        if (mShowRipple && !isNormal && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shape.setColor(mNormalColor);
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
    protected int getPressedColor(int normalColor) {
        int alpha = 255;
        int r = (normalColor >> 16) & 0xFF;
        int g = (normalColor >> 8) & 0xFF;
        int b = (normalColor >> 1) & 0xFF;
        r = (r - 50 < 0) ? 0 : r - 50;
        g = (g - 50 < 0) ? 0 : g - 50;
        b = (b - 50 < 0) ? 0 : b - 50;
        return Color.argb(alpha, r, g, b);
    }

    public void setNormalColor(int color) {
        mNormalColor = color;
    }

    public void setNormalStrokeColor(int color) {
        mNormalStrokeColor = color;
    }

    public void setNormalStrokeWidth(int normalStrokeWidth) {
        mNormalStrokeWidth = normalStrokeWidth;
    }

    public void setPressedColor(int pressedColor) {
        mPressedColor = pressedColor;
    }

    public void setPressedStrokeColor(int pressedStrokeColor) {
        mPressedStrokeColor = pressedStrokeColor;
    }

    public void setPressedStrokeWidth(int pressedStrokeWidth) {
        mPressedStrokeWidth = pressedStrokeWidth;
    }

    public void setCheckedStrokeColor(int checkedStrokeColor) {
        mCheckedStrokeColor = checkedStrokeColor;
    }

    public void setCheckedStrokeWidth(int checkedStrokeWidth) {
        mCheckedStrokeWidth = checkedStrokeWidth;
    }

    public void setNormal(Drawable normal) {
        mNormal = normal;
    }

    public void setPressed(Drawable pressed) {
        mPressed = pressed;
    }

    public void setSrc(boolean src) {
        mIsSrc = src;
    }

    public void setChecked(Drawable checked) {
        mChecked = checked;
    }

    public void setCheckedColor(int checkedColor) {
        mCheckedColor = checkedColor;
    }

    public void setSmart(boolean smart) {
        mIsSmart = smart;
    }

    public int getPressedColor() {
        return mPressedColor;
    }
}
