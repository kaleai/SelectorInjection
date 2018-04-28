package kale.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RadioGroup;

import kale.injection.SelectorInjection;
import kale.utils.SelectorUtils;

/**
 * @author Kale
 * @date 2016/3/14
 * 
 * 如果有设置上下左右图片宽高的需求，可以采用https://github.com/woxingxiao/VectorCompatTextView
 *
 * 不在radioGroup中则是textView，所以它默认是没有选中能力的，必须手动设置check状态
 */
public class SelectorTextView extends android.support.v7.widget.AppCompatRadioButton implements ISelectorView {

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
//        setChecked(isChecked());
    }

    @Override
    public SelectorInjection createSelectorInjection(Context context, AttributeSet attrs) {
        return new SelectorInjection(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isInRadioGroup()) {
            performClick(); // for check state
        }
        return super.onTouchEvent(event);
    }

    @Override
    public SelectorInjection getSelectorInjection() {
        return injection;
    }

    /**
     * WARN: If the radio button is already checked, this method will not toggle the radio button.
     */
    @Override
    public void toggle() {
        if (isInRadioGroup()) {
            super.toggle();
        } else {
            // 如果一旦被checked，那么则不会更改状态，所以这里用toggleCompat()代替
        }
    }

    public void toggleCompat() {
        setChecked(!isChecked());
    }

    public boolean isInRadioGroup() {
        return getParent() instanceof RadioGroup;
    }
}
