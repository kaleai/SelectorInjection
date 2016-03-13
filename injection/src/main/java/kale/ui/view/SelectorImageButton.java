package kale.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageButton;

import kale.injection.R;
import kale.injection.SelectorInjection;


public class SelectorImageButton extends ImageButton implements Checkable {

    private SelectorInjection injection;

    public SelectorImageButton(Context context) {
        super(context);
    }

    public SelectorImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SelectorImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SelectorInjection);
        injection = initSelectorInjection(array);
        injection.injection(this);
        array.recycle();
    }

    protected SelectorInjection initSelectorInjection(TypedArray array) {
        return new SelectorInjection(array);
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