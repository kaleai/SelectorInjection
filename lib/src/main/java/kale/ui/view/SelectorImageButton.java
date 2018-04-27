package kale.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import kale.injection.SelectorInjection;
import kale.utils.SelectorUtils;

public class SelectorImageButton extends AppCompatImageButton implements ISelectorView {

    private SelectorInjection injection;

    public SelectorImageButton(Context context) {
        this(context, null);
    }

    public SelectorImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        injection = SelectorUtils.injectionToSelectorView(this, attrs, defStyle);
//        setClickable(true);
    }

    @Override
    public SelectorInjection initSelectorInjection(Context context, AttributeSet attr) {
        return new SelectorInjection(this);
    }

    @Override
    public SelectorInjection getSelectorInjection() {
        return injection;
    }

    ///////////////////////////////////////////////////////////////////////////
    // For checkable
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    private boolean mIsChecked = false;

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