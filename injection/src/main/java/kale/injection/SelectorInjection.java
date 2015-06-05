package kale.injection;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.ImageButton;

/**
 * view的一个selector注入装置，通过构造函数即可注入。之后调用injection()即可.
 *
 * @author Jack Tony
 * @date 2015/5/25
 */
public class SelectorInjection {

    private static int DEFAULT_COLOR = 0x0106000d;

    public static final int DEFAULT_STROKE_WIDTH = 2;

    private View mView;

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
    private int mStrokeColor;

    /**
     * 描边的宽度，如果不设置会根据默认的宽度进行描边
     */
    private int mStrokeWidth;

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


/*    SelectorInjection(View view, Drawable normal, Drawable pressed, int normalColor, int pressedColor) {
        this(view, normal, pressed, normalColor, pressedColor, true);
    }

    SelectorInjection(View view, Drawable normal, Drawable pressed, int normalColor, int pressedColor, boolean isSmart) {
        this(view, normal, pressed, normalColor, pressedColor, isSmart, 
                view.getResources().getColor(R.color.default_color), DEFAULT_STROKE_WIDTH);
    }

    SelectorInjection(View view, Drawable normal, Drawable pressed, int normalColor, int pressedColor, boolean isSmart,
            int strokeColor, int strokeWidth) {
        DEFAULT_COLOR = view.getResources().getColor(R.color.default_color);
        mView = view;
        mNormal = normal;
        mPressed = pressed;
        mNormalColor = normalColor;

        mPressedColor = pressedColor;
        mIsSmart = isSmart;
        mStrokeColor = strokeColor;
        mStrokeWidth = strokeWidth;

    }*/

    public SelectorInjection(View view, TypedArray typedArray) {
        mView = view;
        mIsSmart = typedArray.getBoolean(R.styleable.SelectorInjection_smart, true);

        mNormal = typedArray.getDrawable(R.styleable.SelectorInjection_normal_drawable);
        mPressed = typedArray.getDrawable(R.styleable.SelectorInjection_pressed_drawable);
        mChecked = typedArray.getDrawable(R.styleable.SelectorInjection_checked_drawable);

        mNormalColor = typedArray.getColor(R.styleable.SelectorInjection_normal_color, DEFAULT_COLOR);
        mPressedColor = typedArray.getColor(R.styleable.SelectorInjection_pressed_color, DEFAULT_COLOR);
        mCheckedColor = typedArray.getColor(R.styleable.SelectorInjection_checked_color, DEFAULT_COLOR);

        mStrokeColor = typedArray.getColor(R.styleable.SelectorInjection_stroke_color, DEFAULT_COLOR);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.SelectorInjection_stroke_width, DEFAULT_STROKE_WIDTH);
        // 目前还没支持pressed状态下的描边改变，默认是用正常状态下的描边样式
        mCheckedStrokeColor = typedArray.getColor(R.styleable.SelectorInjection_checked_stroke_color, DEFAULT_COLOR);
        mCheckedStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.SelectorInjection_checked_stroke_width, DEFAULT_STROKE_WIDTH);

        mIsSrc = typedArray.getBoolean(R.styleable.SelectorInjection_isSrc, false);
        // typedArray.recycle();
    }

    public void injection() {
        StateListDrawable selector = new StateListDrawable();// 背景选择器
        // 是否启动智能模式
        if (mIsSmart && mNormal != null) {
            mPressed = mNormal.getConstantState().newDrawable();
            mChecked = mNormal.getConstantState().newDrawable();
        }

        setPressedDrawable(selector);

        setCheckedDrawable(selector);
        
        setNormalDrawable(selector);
        
        int animationTime = 10;
        selector.setEnterFadeDuration(animationTime);
        selector.setExitFadeDuration(animationTime);

        if (mView instanceof ImageButton && mIsSrc) {
            ((ImageButton) mView).setImageDrawable(selector);
            mView.setBackgroundDrawable(null);
        } else {
            mView.setBackgroundDrawable(selector);
        }
    }

    private void setPressedDrawable(StateListDrawable selector) {
        /**
         * 开始设置按下后的样式（颜色，描边）
         */
        if (mPressed != null) {
            if (mPressed instanceof GradientDrawable) {
                // 如果是shape
                setPressedColor(mIsSmart, (GradientDrawable) mPressed, mNormalColor, mPressedColor);
                setStroke((GradientDrawable) mPressed, mStrokeColor, mStrokeWidth);
            }
            Drawable pressedDrawable;
            if (mPressed instanceof LayerDrawable
                    && (pressedDrawable = ((LayerDrawable) mPressed).findDrawableByLayerId(android.R.id.background)) instanceof GradientDrawable) {
                // 如果是layer-list
                setPressedColor(mIsSmart, (GradientDrawable) pressedDrawable, mNormalColor, mPressedColor);
                setStroke((GradientDrawable) pressedDrawable, mStrokeColor, mStrokeWidth);
            }
            // 设置pressed的selector
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
            if (mChecked instanceof GradientDrawable) {
                ((GradientDrawable) mChecked).setColor(mCheckedColor);
                setStroke((GradientDrawable) mChecked, mCheckedStrokeColor, mCheckedStrokeWidth);
            }
            Drawable checkedDrawable;
            if (mChecked instanceof LayerDrawable
                    && (checkedDrawable = ((LayerDrawable) mChecked).findDrawableByLayerId(android.R.id.background)) instanceof GradientDrawable) {
                // 如果是layer-list
                ((GradientDrawable) checkedDrawable).setColor(mCheckedColor);
                setStroke((GradientDrawable) checkedDrawable, mCheckedStrokeColor, mCheckedStrokeWidth);
            }
            selector.addState(new int[]{android.R.attr.state_checked}, mChecked);
            mChecked.mutate();
        }
    }
    
    /**
     * 开始设置普通状态时的样式（颜色，描边）
     */
    private void setNormalDrawable(StateListDrawable selector) {
        if (mNormal != null) {
            if (mNormal instanceof GradientDrawable) {
                // 如果是shape
                ((GradientDrawable) mNormal).setColor(mNormalColor);
                setStroke((GradientDrawable) mNormal, mStrokeColor, mStrokeWidth);
            }
            Drawable normalDrawable;
            if (mNormal instanceof LayerDrawable
                    && (normalDrawable = ((LayerDrawable) mNormal).findDrawableByLayerId(android.R.id.background)) instanceof GradientDrawable) {
                // 如果是layer-list
                ((GradientDrawable) normalDrawable).setColor(mNormalColor);
                setStroke((GradientDrawable) normalDrawable, mStrokeColor, mStrokeWidth);
            }
            selector.addState(new int[]{}, mNormal);
        }
    }

    /**
     * 设置按钮的颜色
     *
     * @param isSmart 是否是只能模式
     */
    private void setPressedColor(boolean isSmart, GradientDrawable pressed, int normalColor, int pressedColor) {
        if (pressedColor == DEFAULT_COLOR) {
            if (isSmart) {
                pressedColor = getPressedColor(normalColor);
            }
        }
        pressed.setColor(pressedColor);
    }

    /**
     * 设置shape的描边
     *
     * @param drawable    需要设置描边的drawable
     * @param strokeColor 描边的颜色
     * @param strokeWidth 描边的宽度
     */
    private void setStroke(GradientDrawable drawable, int strokeColor, int strokeWidth) {
        if (strokeColor != DEFAULT_COLOR) {
            drawable.setStroke(strokeWidth, strokeColor);
        }
    }

    /**
     * Make a dark color to press effect
     * 可重写
     */
    protected int getPressedColor(int normalColor) {
        int alpha = 255;
        int r = (normalColor >> 16) & 0xFF;
        int g = (normalColor >> 8) & 0xFF;
        int b = (normalColor >> 0) & 0xFF;
        r = (r - 50 < 0) ? 0 : r - 50;
        g = (g - 50 < 0) ? 0 : g - 50;
        b = (b - 50 < 0) ? 0 : b - 50;
        return Color.argb(alpha, r, g, b);
    }

    public void setNormalColor(int color) {
        mNormalColor = color;
    }

    public void setStrokeColor(int color) {
        mStrokeColor = color;
    }
    
}
