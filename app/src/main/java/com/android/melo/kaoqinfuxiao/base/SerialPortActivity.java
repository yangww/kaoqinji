package com.android.melo.kaoqinfuxiao.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.melo.kaoqinfuxiao.R;
import com.android.melo.kaoqinfuxiao.global.BaseEvent;
import com.android.melo.kaoqinfuxiao.global.Constants;
import com.android.melo.kaoqinfuxiao.weight.CustomDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;


/**
 * Created by melo on 2018/4/10.
 * 基类
 */
public abstract class SerialPortActivity extends Activity {


    private CustomDialog dialog;
    private ProgressBar mProgressBar;
    private ImageView mIvComplete;
    private TextView mTextView;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    EventBus.getDefault().post(new BaseEvent<>(BaseEvent.Type.REQUEST_GETFCOUS));
                    break;
            }
        }
    };


    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private void DisplayError(int resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SerialPortActivity.this.finish();
            }
        });
        b.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
//        ButterKnife.bind(this);
        try {
            mSerialPort = new SerialPort(new File("/dev/ttyS2"), 115200, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (InvalidParameterException e) {
            e.printStackTrace();

        } finally {
            init();
            initDiaLog();
        }

    }

    public void init() {
        initView();
        initData();
    }

    public abstract void initView();

    public abstract void initData();

    public abstract int getLayoutId();

    protected abstract void onDataReceived(final byte[] buffer, final int size);

    //初始化dialog
    protected void initDiaLog() {

    }

    //显示dialog
    protected void showDiaLog(int layoutId) {
//        dismissDiaLog(0);
        //初始化diaplg
        if (dialog != null) {
            if (dialog.isShowing()) {
            } else {
                dialog.show();
            }
        } else {
            dialog = new CustomDialog(this, 400, 500, layoutId, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
            mProgressBar = dialog.findViewById(R.id.pb_dialog_runing);
            mIvComplete = dialog.findViewById(R.id.iv_dialog_complete);
            mTextView = dialog.findViewById(R.id.tv_dialog_text);
            dialog.show();

        }

    }

    //dialog内容更改
    protected void changeDiaLogContent(int loadingType) {
        if (dialog != null && dialog.isShowing()) {
            switch (loadingType) {
                //登录中
                case Constants.LoadingType.LOGIN_LOADING:
                    mProgressBar.setVisibility(View.VISIBLE);
                    mIvComplete.setVisibility(View.INVISIBLE);
                    mTextView.setText("登录中,请稍后...");

                    break;
                case Constants.LoadingType.UPDATA_LOADING:
                    mProgressBar.setVisibility(View.VISIBLE);
                    mIvComplete.setVisibility(View.INVISIBLE);
                    mTextView.setText("同步数据中,请稍后...");

                    break;
                case Constants.LoadingType.LOGIN_SUCCESS:
                    //登录成功
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mIvComplete.setVisibility(View.VISIBLE);
                    mIvComplete.setImageResource(R.mipmap.iv_dialog_success);
                    mTextView.setText("登录成功");
                    break;
                case Constants.LoadingType.UPDATA_SUCCESS:
                    //同步成功
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mIvComplete.setVisibility(View.VISIBLE);
                    mIvComplete.setImageResource(R.mipmap.iv_dialog_success);
                    mTextView.setText("同步已完成，欢迎使用");
                    break;
                case Constants.LoadingType.ICCARD_UNEXIST:
                    //无此卡信息
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mIvComplete.setVisibility(View.VISIBLE);
                    mIvComplete.setImageResource(R.mipmap.iv_dialog_error);
                    mTextView.setText("无数据");
                    break;
                case Constants.LoadingType.LOGIN_ERROR:
                    //登录失败
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mIvComplete.setVisibility(View.VISIBLE);
                    mIvComplete.setImageResource(R.mipmap.iv_dialog_error);
                    mTextView.setText("登录失败,请重新登录");
                    break;
                case Constants.LoadingType.UPDATA_ERROR:
                    //同步失败
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mIvComplete.setVisibility(View.VISIBLE);
                    mIvComplete.setImageResource(R.mipmap.iv_dialog_error);
                    mTextView.setText("同步失败,请重新同步数据");
                    break;
                case Constants.LoadingType.INTERNET_ERROR:
                    //网络连接错误
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mIvComplete.setVisibility(View.VISIBLE);
                    mIvComplete.setImageResource(R.mipmap.iv_dialog_error);
                    mTextView.setText("网络连接错误");
                    break;
            }
        }
    }


    //关闭dialog
    protected void dismissDiaLog(int delayTime) {
        mHandler.sendEmptyMessageDelayed(1, delayTime);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ButterKnife.unbind(this);
        if (mReadThread != null)
            mReadThread.interrupt();
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }


}
