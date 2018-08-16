package com.android.melo.kaoqinfuxiao.weight;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by melo on 2018/4/11.
 * 透明背景surfaceview
 */

public class AlphaSurfaceView extends SurfaceView {
    public AlphaSurfaceView(Context context) {
        super(context);
        init();

    }

    public AlphaSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlphaSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

}
