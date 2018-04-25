package kale.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import kale.injection.SelectorInjection;

/**
 * @author Kale
 * @date 2016/3/14
 */
public class SelectorRadioButton extends android.support.v7.widget.AppCompatRadioButton implements ISelectorView {

    private SelectorInjection injection;

    public SelectorRadioButton(Context context) {
        this(context, null);
    }

    public SelectorRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injection = initSelectorInjection(context, attrs);
        injection.injection(this);
        
        setChecked(isChecked());
    }

    @Override
    public SelectorInjection initSelectorInjection(Context context, AttributeSet attrs) {
        return new SelectorInjection(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        return super.onTouchEvent(event);
    }

    @Override
    public SelectorInjection getSelectorInjection() {
        return injection;
    }
}
