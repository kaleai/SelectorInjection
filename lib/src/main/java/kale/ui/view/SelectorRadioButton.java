package kale.ui.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Kale
 * @date 2016/3/14
 */
public class SelectorRadioButton extends SelectorTextView {

    public SelectorRadioButton(Context context) {
        this(context, null);
    }

    public SelectorRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
