package kale.selectorinjection;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * https://github.com/axet/android-library/blob/master/src/main/java/com/github/axet/androidlibrary/widgets/EqualLinearLayout.java
 */
public class EqualLinearLayout extends LinearLayout {

    int icount;

    public EqualLinearLayout(Context context) {
        super(context);
    }

    public EqualLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EqualLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getOrientation() == HORIZONTAL) {
            measureHorizontal();
        } else {
            measureVertical();
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    void measureHorizontal() {
        icount = 0;

        int last = getChildCount() - 1;
        for (int i = 0; i <= last; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.gravity == -1) {
                lp.gravity = Gravity.CENTER_VERTICAL;
                if (i == 0) {
                    // first
                    lp.gravity |= Gravity.LEFT;
                } else if (i == last) {
                    // last
                    lp.gravity |= Gravity.RIGHT;
                } else {
                    // middle
                    lp.gravity |= Gravity.CENTER_HORIZONTAL;
                }
            }
            icount++;
        }
    }

    void measureVertical() {
        icount = 0;

        int last = getChildCount() - 1;
        for (int i = 0; i <= last; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.gravity == -1) {
                lp.gravity = Gravity.CENTER_HORIZONTAL;
                if (i == 0) {
                    // first
                    lp.gravity |= Gravity.TOP;
                } else if (i == last) {
                    // last
                    lp.gravity |= Gravity.BOTTOM;
                } else {
                    // middle
                    lp.gravity |= Gravity.CENTER_VERTICAL;
                }
            }
            icount++;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getOrientation() == HORIZONTAL)
            layoutHorizontal(changed, l, t, r, b);
        else
            layoutVertical(changed, l, t, r, b);
    }

    void layoutHorizontal(boolean changed, int l, int t, int r, int b) {
        // linearlayout width
        int llw = (r - l) - getPaddingLeft() - getPaddingRight();
        int llh = (b - t) - getPaddingTop() - getPaddingBottom();

        // child max width
        int cw = llw / icount;

        int ichild = 0;
        int ilast = getChildCount() - 1;

        for (int i = 0; i <= ilast; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;

            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int cl, cr, ct, cb;

            ct = 0;
            if ((lp.gravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
                ct = (llh - childHeight) / 2 + getPaddingTop();
            }
            cb = ct + childHeight;

            if (i == 0) {
                // first
                cl = getPaddingLeft() + lp.leftMargin;
                if ((lp.gravity & Gravity.LEFT) == Gravity.LEFT) {
                    ;
                } else if ((lp.gravity & Gravity.RIGHT) == Gravity.RIGHT) {
                    cl += cw - childWidth;
                } else if ((lp.gravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
                    cl += (cw - childWidth) / 2;
                }
            } else if (i == ilast) {
                // last
                cl = getPaddingLeft() + lp.leftMargin;
                // do left
                if ((lp.gravity & Gravity.LEFT) == Gravity.LEFT) {
                    cl += ichild * cw;
                } else if ((lp.gravity & Gravity.RIGHT) == Gravity.RIGHT) {
                    cl = (r - l) - childWidth - getPaddingRight() - lp.rightMargin;
                } else if ((lp.gravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
                    cl += ichild * cw + (cw - childWidth) / 2;
                }
            } else {
                // middle
                cl = getPaddingLeft() + lp.leftMargin;
                cl += ichild * cw + (cw - childWidth) / 2;
            }
            cr = cl + childWidth;

            child.layout(cl, ct, cr, cb);
            ichild++;
        }
    }

    void layoutVertical(boolean changed, int l, int t, int r, int b) {
        // linearlayout sizes
        int llw = (r - l) - getPaddingLeft() - getPaddingRight();
        int llh = (b - t) - getPaddingTop() - getPaddingBottom();

        // child max height
        int ch = llh / icount;

        int ichild = 0;
        int ilast = getChildCount() - 1;

        for (int i = 0; i <= ilast; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;

            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int cl, cr, ct, cb;

            cl = 0;
            if ((lp.gravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
                cl = (llw - childWidth) / 2 + getPaddingLeft();
            }
            cr = cl + childWidth;

            if (i == 0) {
                ct = getPaddingTop() + lp.topMargin;
                // first
                if ((lp.gravity & Gravity.TOP) == Gravity.TOP) {
                    ;
                } else if ((lp.gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
                    ct += ch - childHeight;
                } else if ((lp.gravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
                    ct += (ch - childHeight) / 2;
                }
            } else if (i == ilast) {
                // last
                ct = getPaddingTop() + lp.topMargin;

                if ((lp.gravity & Gravity.TOP) == Gravity.TOP) {
                    ct += ichild * ch;
                } else if ((lp.gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
                    ct = (b - t) - childHeight - getPaddingBottom() - lp.bottomMargin;
                } else if ((lp.gravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
                    ct += ichild * ch + (ch - childHeight) / 2;
                }
            } else {
                // middle
                ct = getPaddingTop() + lp.topMargin;
                ct += ichild * ch + (ch - childHeight) / 2;
            }
            cb = ct + childHeight;

            child.layout(cl, ct, cr, cb);
            ichild++;
        }
    }
}