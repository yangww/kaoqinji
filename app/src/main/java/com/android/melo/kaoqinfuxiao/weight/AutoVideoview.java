package com.android.melo.kaoqinfuxiao.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by melo on 2018/4/27.
 */

public class AutoVideoview extends VideoView {
    public AutoVideoview(Context context) {
        super(context);
    }

    public AutoVideoview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = getDefaultSize(0, widthMeasureSpec);
        int heightSize = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);


    }
}
