package kale.injection;

import android.graphics.drawable.Drawable;

/**
 * @author Kale
 * @date 2018/4/27
 */
public class SelectorBean {

    public Drawable drawable;

    public int color;

    /**
     * 描边的宽度，如果不设置会根据默认的宽度（2px）进行描边
     */
    public int strokeColor, strokeWidth;
}
