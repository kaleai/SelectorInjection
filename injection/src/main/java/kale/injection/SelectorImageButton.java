package kale.injection;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageButton;


public class SelectorImageButton extends ImageButton {

    private SelectorInjection injection;
    
    public SelectorImageButton(Context context) {
        super(context);
    }

    public SelectorImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SelectorImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SelectorImageButton);
        injection = new SelectorInjection(this, array);
        injection.injection();
        array.recycle();
    }

    public SelectorInjection getInjection() {
        return  injection;
    }
    

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            setAlpha(0.3f);
        }
    }
}