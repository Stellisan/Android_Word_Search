package com.b105.tamilpuzzle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquareRelativeLayout extends RelativeLayout {

    public SquareRelativeLayout (Context context) {
        super(context);
    }

    public SquareRelativeLayout (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquareRelativeLayout (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}