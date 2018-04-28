package kale.selectorinjection;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import kale.injection.SelectorInjection;
import kale.ui.view.SelectorTextView;

/**
 * @author Kale
 * @date 2018/4/28
 */
public class MySelectorTextView extends SelectorTextView {

    public MySelectorTextView(Context context) {
        super(context);
    }

    public MySelectorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySelectorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public SelectorInjection createSelectorInjection(Context context, AttributeSet attr) {
        // 复写方法，计算自定义的按压色值
        return new SelectorInjection(this) {
            @Override
            protected int getPressedColor(int normalColor) {
                int alpha = 255;
                int r = (normalColor >> 16) & 0xFF;
                int g = (normalColor >> 8) & 0xFF;
                int b = (normalColor >> 1) & 0xFF;
                r = (r - 50 < 0) ? 0 : r - 50;
                g = (g - 50 < 0) ? 0 : g - 50;
                b = (b - 50 < 0) ? 0 : b - 50;
                return Color.argb(alpha, r, g, b);
            }
        };
    }
}
