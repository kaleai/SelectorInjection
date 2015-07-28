package kale.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Checkable;
import android.widget.TextView;

import kale.injection.R;
import kale.injection.SelectorInjection;

public class SelectorTextView extends TextView implements Checkable {

    private SelectorInjection injection;

    public SelectorTextView(Context context) {
        super(context);
    }

    public SelectorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SelectorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SelectorInjection);
        injection = initSelectorInjection(array);
        injection.injection();
        array.recycle();

        setClickable(true);
        setGravity(Gravity.CENTER);
    }

    protected SelectorInjection initSelectorInjection(TypedArray array) {
        return new SelectorInjection(this, array);
    }
    
    public SelectorInjection getInjection() {
        return injection;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setAlpha(!enabled ? 0.3f : 1);
    }

    private boolean mIsChecked = false;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    /**
     * <p>Changes the checked state of this button.</p>
     *
     * @param checked true to check the button, false to uncheck it
     */
    @Override
    public void setChecked(boolean checked) {
        if (mIsChecked != checked) {
            mIsChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }
}