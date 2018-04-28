package kale.ui.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Kale
 * @date 2016/3/14
 *
 * 支持阴影
 */
public class SelectorButton extends SelectorTextView {

    // FIXME: 2018/4/25 layer-list做背景时会出现padding失效的问题

    public SelectorButton(Context context) {
        this(context, null);
    }

    public SelectorButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public SelectorButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
