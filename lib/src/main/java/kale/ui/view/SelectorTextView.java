package kale.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.AttributeSet;

import kale.injection.SelectorInjection;
import kale.injection.AppCompatTextViewHelper;

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
        
        new AppCompatTextViewHelper(this).loadFromAttributes(attrs, defStyle);
        
        injection = initSelectorInjection(context, attrs);
        injection.injection(this);


        setClickable(true);
//        setGravity(Gravity.CENTER);
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
        injection.setEnabled(this, enabled);
    }
}