package com.android.melo.kaoqinfuxiao.global;

import android.app.Application;

import com.android.melo.kaoqinfuxiao.utils.LiteOrmDBUtil;
import com.android.melo.kaoqinfuxiao.utils.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;

import static com.android.melo.kaoqinfuxiao.global.Constants.APPID_BUGLY;


/**
 * Created by melo on 2018/4/8.
 * 程序入口
 */

public class Global extends Application {
    private static Global mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        //初始化bugly
        //配置bugly
        CrashReport.initCrashReport(getApplicationContext(), APPID_BUGLY, true);
        //初始化数据库
        LiteOrmDBUtil.createLiteOrm(this);
    }

    public static Global getInstance() {

        return mContext;
    }


}
