package com.android.melo.kaoqinfuxiao.global;

/**
 * Created by melo on 2018/04/17.
 * event实体
 */

public class BaseEvent<T> {


    public static class Type {
        public static final int UPDATA_COMPLETE = 0xbf;//更新上传任务通知
        public static final int NETLINE_CHANGE = 0xba;//网线连接是否可用
        public static final int WIFI_STRENGTH = 0xbB;//wifi强度
        public static final int LINENET_ISAVALIABLE = 0xbC;//以太网是否可用
        public static final int REQUEST_GETFCOUS = 0xbD;//通知屏幕获取焦点
    }

    private int type;
    private T t;

    public BaseEvent(int type) {
        this.type = type;


    }

    public BaseEvent(int type, T t) {
        this.type = type;

        this.t = t;

    }

    public int getType() {
        return type;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
