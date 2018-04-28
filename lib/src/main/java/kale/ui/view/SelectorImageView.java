package kale.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import kale.injection.SelectorInjection;
import kale.utils.SelectorUtils;

public class SelectorImageView extends AppCompatImageButton implements ISelectorView {

    private SelectorInjection injection;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public SelectorImageView(Context context) {
        this(context, null);
    }

    public SelectorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        injection = SelectorUtils.injectionToSelectorView(this, attrs, defStyle);
//        setClickable(true);
    }

    @Override
    public SelectorInjection createSelectorInjection(Context context, AttributeSet attr) {
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

    private boolean mChecked = false;

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;

            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
//            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    public interface OnCheckedChangeListener {

        void onCheckedChanged(SelectorImageView view, boolean isChecked);
    }
}