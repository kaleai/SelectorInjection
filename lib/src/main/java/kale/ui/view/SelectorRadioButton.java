package kale.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import kale.injection.SelectorInjection;
import kale.utils.SelectorUtils;

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

    public SelectorRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        injection = SelectorUtils.injectionToSelectorView(this, attrs, defStyle);
//        setChecked(isChecked());
    }

    @Override
    public SelectorInjection initSelectorInjection(Context context, AttributeSet attrs) {
        return new SelectorInjection(this);
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
