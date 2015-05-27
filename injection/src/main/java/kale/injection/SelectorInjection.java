package kale.injection;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;

/**
 * view的一个selector注入装置，通过构造函数即可注入。之后调用injection()即可.
 * @author Jack Tony
 *        
 * @date 2015/5/25
 */
public class SelectorInjection {

    private static final int DEFAULT_COLOR = android.R.color.transparent;

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
     * 正常情况下的drawable
     */
    private Drawable mNormal;

    /**
     * 按下后的drawable
     */
    private Drawable mPressed;


    SelectorInjection(View view, Drawable normal, Drawable pressed, int normalColor, int pressedColor) {
        this(view, normal, pressed, normalColor, pressedColor, true);
    }

    SelectorInjection(View view, Drawable normal, Drawable pressed, int normalColor, int pressedColor, boolean isSmart) {
        this(view, normal, pressed, normalColor, pressedColor, isSmart, DEFAULT_COLOR, DEFAULT_STROKE_WIDTH);// 2是默认的描边宽度
    }

    SelectorInjection(View view, Drawable normal, Drawable pressed, int normalColor, int pressedColor, boolean isSmart,
            int strokeColor, int strokeWidth) {
        mView = view;
        mNormal = normal;
        mPressed = pressed;
        mNormalColor = normalColor;

        mPressedColor = pressedColor;
        mIsSmart = isSmart;
        mStrokeColor = strokeColor;
        mStrokeWidth = strokeWidth;

    }

    SelectorInjection(View view, TypedArray typedArray) {
        mView = view;
        mIsSmart = typedArray.getBoolean(R.styleable.SelectorImageButton_smart, true);
        mNormalColor = typedArray.getColor(R.styleable.SelectorImageButton_normal_color, DEFAULT_COLOR);
        mPressedColor = typedArray.getColor(R.styleable.SelectorImageButton_pressed_color, DEFAULT_COLOR);

        mStrokeColor = typedArray.getColor(R.styleable.SelectorImageButton_stroke_color, DEFAULT_COLOR);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.SelectorImageButton_stroke_width, DEFAULT_STROKE_WIDTH);
        mNormal = typedArray.getDrawable(R.styleable.SelectorImageButton_normal_drawable);
        mPressed = typedArray.getDrawable(R.styleable.SelectorImageButton_pressed_drawable);
        // typedArray.recycle();
    }

    protected void injection() {
        StateListDrawable selector = new StateListDrawable();// 背景选择器
        // 是否启动智能模式
        if (mIsSmart && mNormal != null && mNormalColor != DEFAULT_COLOR) {
            mPressed = mNormal.getConstantState().newDrawable();
        }

        /**
         * 开始设置按下后的样式（颜色，描边）
         */
        if (mPressed instanceof GradientDrawable) {
            // 如果是shape
            setPressedColor(mIsSmart, (GradientDrawable) mPressed, mNormalColor, mPressedColor);
            setStroke((GradientDrawable) mPressed, mStrokeColor, mStrokeWidth);
        }
        Drawable pressedDrawable;
        if (mPressed instanceof LayerDrawable
                && (pressedDrawable = ((LayerDrawable) mPressed).findDrawableByLayerId(R.id.background_shape)) instanceof GradientDrawable) {
            // 如果是layer-list
            setPressedColor(mIsSmart, (GradientDrawable) pressedDrawable, mNormalColor, mPressedColor);
            setStroke((GradientDrawable) pressedDrawable, mStrokeColor, mStrokeWidth);
        }
        // 设置pressed的selector
        if (mPressed != null) {
            selector.addState(new int[]{android.R.attr.state_pressed}, mPressed);
            selector.addState(new int[]{android.R.attr.state_focused}, mPressed);
            mPressed.mutate();
        }

        /**
         * 开始设置普通状态时的样式（颜色，描边）
         */
        if (mNormal instanceof GradientDrawable) {
            // 如果是shape
            ((GradientDrawable) mNormal).setColor(mNormalColor);
            setStroke((GradientDrawable) mNormal, mStrokeColor, mStrokeWidth);
        }
        Drawable normalDrawable;
        if (mNormal instanceof LayerDrawable
                && (normalDrawable = ((LayerDrawable) mNormal).findDrawableByLayerId(R.id.background_shape)) instanceof GradientDrawable) {
            // 如果是layer-list
            ((GradientDrawable) normalDrawable).setColor(mNormalColor);
            setStroke((GradientDrawable) normalDrawable, mStrokeColor, mStrokeWidth);
        }
        if (mNormal != null) {
            selector.addState(new int[]{}, mNormal);
        }

        int animationTime = 10;
        selector.setEnterFadeDuration(animationTime);
        selector.setExitFadeDuration(animationTime);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mView.setBackground(selector);
        } else {
            mView.setBackgroundDrawable(selector);
        }
    }

    /**
     * 设置按钮按下后显示的颜色
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
    protected static int getPressedColor(int normalColor) {
        int alpha = 255;
        int r = (normalColor >> 16) & 0xFF;
        int g = (normalColor >> 8) & 0xFF;
        int b = (normalColor >> 0) & 0xFF;
        r = (r - 50 < 0) ? 0 : r - 50;
        g = (g - 50 < 0) ? 0 : g - 50;
        b = (b - 50 < 0) ? 0 : b - 50;
        return Color.argb(alpha, r, g, b);
    }

}
