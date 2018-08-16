package com.android.melo.kaoqinfuxiao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.android.melo.kaoqinfuxiao.global.BaseEvent;
import com.android.melo.kaoqinfuxiao.global.Constants;
import com.android.melo.kaoqinfuxiao.global.Global;
import com.android.melo.kaoqinfuxiao.http.HttpUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * wifi状态监听
 */
public class WifiAndLineNetBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = WifiAndLineNetBroadcastReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Constants.NETWORK_STATE_CHANGE)) {
            //这里处理以太网是否可用
            handleLineNetChange();
            //判断wifi状态
            initWifiState();

//            boolean isNetOK = HttpUtils.isNetWorkConn(context);
//            Toast.makeText(context, "网络状态发生变化,是否可用：" + isNetOK, Toast.LENGTH_SHORT).show();

//            if (isNetOK) {
//                initWifiState();
//            } else {
//                //wifi 错误
//                EventBus.getDefault().post(new BaseEvent<>(BaseEvent.Type.WIFI_STRENGTH, 4 ));
//            }

        } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifistate = intent.getIntExtra(
                    WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_DISABLED);

            if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                //WiFi错误
                EventBus.getDefault().post(new BaseEvent<>(BaseEvent.Type.WIFI_STRENGTH, 4));
            } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                updateWifiStrength();
            }
        } else if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
            initWifiState();
        }
    }

    private void handleLineNetChange() {
        if (HttpUtils.isIntenetConnected(Global.getInstance())) {
            //以太网连接可用！
            EventBus.getDefault().post(new BaseEvent<>(BaseEvent.Type.LINENET_ISAVALIABLE, true));
        } else {
            //以太网连接不可用！
            EventBus.getDefault().post(new BaseEvent<>(BaseEvent.Type.LINENET_ISAVALIABLE, false));
        }
    }

    public void initWifiState() {

        ConnectivityManager manager = (ConnectivityManager) Global.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info.isConnected()) {
            updateWifiStrength();
        } else {
            //wifi错误
            EventBus.getDefault().post(new BaseEvent<>(BaseEvent.Type.WIFI_STRENGTH, 4));
        }
    }

    public void updateWifiStrength() {
        int strength = getStrength(Global.getInstance());
        if (strength >= 0 && strength <= 3)
            EventBus.getDefault().post(new BaseEvent<>(BaseEvent.Type.WIFI_STRENGTH, strength));
    }

    public int getStrength(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            int strength = WifiManager.calculateSignalLevel(info.getRssi(), 4);
            return strength;
        }
        return 0;
    }

}