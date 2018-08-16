package com.android.melo.kaoqinfuxiao.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志输出 管理
 *
 * @author melo
 */
public class LogUtils {
    public static final boolean LOG_ENABLE = false;

    public static void i(String tag, String message) {
        if (LOG_ENABLE)
            Log.i(tag, message);
    }

    public static void e(String tag, String message) {
        if (LOG_ENABLE)
            Log.e(tag, message);

    }

    public static void d(String tag, String message) {
        if (LOG_ENABLE)
            Log.d(tag, message);
    }

    public static void v(String tag, String message) {
        if (LOG_ENABLE)
            Log.v(tag, message);
    }

}
