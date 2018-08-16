package com.android.melo.kaoqinfuxiao.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.android.melo.kaoqinfuxiao.R;
import com.android.melo.kaoqinfuxiao.base.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * Created by melo on 2018/4/10.
 * 闪屏页面
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };


    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mHandler.sendEmptyMessageDelayed(1, 2000);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splsh;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

    }
}
