package com.android.melo.kaoqinfuxiao.weight;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.android.melo.kaoqinfuxiao.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by melo on 2018/4/11.
 * 自定义dialog
 */

public class CustomDialog extends Dialog {

    //定义模板
    public CustomDialog(Context context, int layout, int style) {
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WRAP_CONTENT, layout, style, Gravity.CENTER);
    }

    //定义属性
    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity, int anim) {
        super(context, style);
        //设置属性
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WRAP_CONTENT;
        layoutParams.height = WRAP_CONTENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
//        window.setWindowAnimations(anim);
        window.setBackgroundDrawableResource(R.drawable.bg_round_logindialog);

    }

    //实例
    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity) {
        this(context, width, height, layout, style, gravity, R.style.pop_anim_style);
    }


}