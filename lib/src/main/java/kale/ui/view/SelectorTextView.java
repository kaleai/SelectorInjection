package kale.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.AttributeSet;

import kale.injection.SelectorInjection;
import kale.utils.SelectorUtils;

/**
 * 如果有设置上下左右图片宽高的需求，可以采用https://github.com/woxingxiao/VectorCompatTextView
 */
public class SelectorTextView extends AppCompatCheckedTextView implements ISelectorView {

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

    @Override
    public SelectorInjection initSelectorInjection(Context context, AttributeSet attr) {
        return new SelectorInjection(context, attr);
    }

    @Override
    public SelectorInjection getSelectorInjection() {
        return injection;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (injection != null) {
            injection.setEnabled(this, enabled);
        }
    }
}