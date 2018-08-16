package com.android.melo.kaoqinfuxiao.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.melo.kaoqinfuxiao.global.BaseEvent;
import com.android.melo.kaoqinfuxiao.global.Global;

import org.greenrobot.eventbus.EventBus;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by pku on 2018/4/9.
 * 网络工具类
 */

public class HttpUtils {


    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConn(Context context) {
        ConnectivityManager manager = (ConnectivityManager) Global.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return info.isConnected();
        } else {
            return false;
        }
    }

    /**
     * 判断以太网网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isIntenetConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mInternetNetWorkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
            boolean hasInternet = mInternetNetWorkInfo != null && mInternetNetWorkInfo.isConnected() && mInternetNetWorkInfo.isAvailable();
            return hasInternet;
        }
        return false;
    }

    /**
     * wifi是否可用
     */
    public static boolean getWifiState() {

        ConnectivityManager manager = (ConnectivityManager) Global.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info.isConnected()) {
            return true;
        } else {
            //wifi错误
            return false;
        }
    }
}
