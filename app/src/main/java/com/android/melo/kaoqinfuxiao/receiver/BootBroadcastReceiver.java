package com.android.melo.kaoqinfuxiao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.melo.kaoqinfuxiao.view.MainActivity;
import com.android.melo.kaoqinfuxiao.view.SplashActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";  
    @Override  
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {  
            Intent splashActivityIntent = new Intent(context, SplashActivity.class);  // 要启动的Activity
            splashActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(splashActivityIntent);
        }  
    }  
  
}  