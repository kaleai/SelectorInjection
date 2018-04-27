package kale.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.MotionEvent;

import kale.injection.SelectorInjection;
import kale.utils.SelectorUtils;

/**
 * 如果有设置上下左右图片宽高的需求，可以采用https://github.com/woxingxiao/VectorCompatTextView
 */
public class SelectorTextView extends AppCompatCheckBox implements ISelectorView {

    private SelectorInjection injection;

    public SelectorTextView(Context context) {
        this(context, null);
    }

    public SelectorTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        injection = SelectorUtils.injectionToSelectorView(this, attrs, defStyle);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        return super.onTouchEvent(event);
    }
    
    @Override
    public SelectorInjection initSelectorInjection(Context context, AttributeSet attr) {
        return new SelectorInjection(this);
    }
    
    @Override
    public SelectorInjection getSelectorInjection() {
        return injection;
    }
    
}