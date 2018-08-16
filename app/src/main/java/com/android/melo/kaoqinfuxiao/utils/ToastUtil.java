package com.android.melo.kaoqinfuxiao.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.melo.kaoqinfuxiao.R;
import com.android.melo.kaoqinfuxiao.global.Global;
import com.android.melo.kaoqinfuxiao.http.OkHttpHelper;


/**
 * 吐司
 *
 * @author melo
 */
public class ToastUtil {
//    private static Toast toast;
    private static ToastUtil sToastUtil;
    private static Toast sToast;
    private View toastView;
    private LinearLayout relativeLayout;
    private RelativeLayout.LayoutParams layoutParams;
    private TextView textView;

    private ToastUtil() {
        sToast = new Toast(Global.getInstance());
        //自定义Toast控件
        toastView = LayoutInflater.from(Global.getInstance()).inflate(R.layout.toast_clear_layout, null);
        relativeLayout = toastView.findViewById(R.id.toast_linear);
        //动态设置toast控件的宽高度，宽高分别是130dp //这里用了一个将dp转换为px的工具类PxUtil
        layoutParams = new RelativeLayout.LayoutParams(
                (int) PxUtil.dpToPx(Global.getInstance(), 300), (int) PxUtil.dpToPx(Global.getInstance(), 160));
        relativeLayout.setLayoutParams(layoutParams);
        textView = toastView.findViewById(R.id.tv_toast_clear);
    }

    /**
     * 单例模式构造对象
     *
     * @return OkHttpHelper
     */
    public static ToastUtil getInstance() {
        if (sToastUtil == null) {
            synchronized (ToastUtil.class) {
                if (sToastUtil == null) {
                    sToastUtil = new ToastUtil();
                }
            }
        }
        return sToastUtil;
    }

    /**
     * 显示
     * @param text
     */
    public void showToast(String text) {
        textView.setText(text);
        sToast.setView(toastView);
        sToast.setDuration(Toast.LENGTH_SHORT);
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.show();

    }


//    /**
//     * 强大的吐司，能够连续弹的吐司
//     *
//     * @param text
//     */
//    public static void showToast(String text) {
////        if (toast == null) {
////            toast = new Toast(Global.getInstance());
////        }
////        toast.setText(text);//如果不为空，则直接改变当前toast的文本
////        toast.setGravity(Gravity.CENTER, 0, 0);
////        toast.show();
//        showCoustomToast(text);
//    }
//
//    /**
//     * 自定义Toast
//     */
//    public static void showCoustomToast(String text) {
//        //自定义Toast控件
//        View toastView = LayoutInflater.from(Global.getInstance()).inflate(R.layout.toast_clear_layout, null);
//        LinearLayout relativeLayout = toastView.findViewById(R.id.toast_linear);
//        //动态设置toast控件的宽高度，宽高分别是130dp //这里用了一个将dp转换为px的工具类PxUtil
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                (int) PxUtil.dpToPx(Global.getInstance(), 300), (int) PxUtil.dpToPx(Global.getInstance(), 160));
//        relativeLayout.setLayoutParams(layoutParams);
//        TextView textView = toastView.findViewById(R.id.tv_toast_clear);
//        if (toast == null) {
//            toast = new Toast(Global.getInstance());
//        }
//        textView.setText(text);
//        toast.setView(toastView);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
//
//    }
}
