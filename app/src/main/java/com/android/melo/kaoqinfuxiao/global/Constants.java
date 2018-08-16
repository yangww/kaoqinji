package com.android.melo.kaoqinfuxiao.global;

/**
 * Created by pku on 2018/4/8.
 * 全局常量
 */

public class Constants {
    //讯飞appid
    public static final String APPID_XUNFI = "5ad7f985";
    //bugly_appid
    public static final String APPID_BUGLY = "6cc44d346c";
    //数据同步成功
    public static final int UPDATA_SUCCESS = 0xc1;
    //读取刷卡卡号
    public static final int READ_ICCARD= 0xc2;
    //wifi监听
    public static final String NETWORK_STATE_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    /**
     * 网络请求类型
     */
    public static class NetRequestType {
        //登陆
        public static final int NETREQUEST_LOGIN = 0xE1;
        //更改时钟
        public static final int NETREQUEST_CHANGETIME = 0xE2;
        //刷卡
        public static final int NETREQUEST_DREDITCARD = 0xE3;

    }

    /**
     * 学校类型
     */
    public static class SchoolAddressType {
        //附校标识
        public static final String TAG_FX = "weixin.beidafuxiao.cn";
        //青鸟标识
        public static final String TAG_QN = "crm.beidafuxiao.cn";

    }


    /**
     * 网络请求结果类型
     */
    public static class NetResultType {
        //网络请求成功
        public static final int NETRESULT_SUCCESS = 0xf1;
        //网络请求失败
        public static final int NETRESULT_ERROR = 0xf2;

    }


    /**
     * SP数据类型
     */
    public static class SPType {
        //用户信息
        public static final String USERINFO = "UserInfo";
    }

    /**
     * 时间标签
     */
    public static class Time {
        //时间更新
        public static final int REFLASH = 0xd1;
        //是否开始合成
        public static final int IS_STARTSYNATH = 0xd2;
    }

    /**
     * 登录,sp存储 tag
     */
    public static class Share {
        public static final String SCHOOL_TYPE = "school_type";//学校类型
        public static final String SCHOOL_ADDRESS = "school_ADDRESS";//学校类型根地址
        public static final String LOGIN_ACCOUNT = "UserName";//账号
        public static final String lOGIN_PASSWORD = "Password";//密码
        public static final String LOGIN_JIHUOMA= "JiHuoMa";//激活码
        public static final String MEMBERUID = "Member_Uid";
        public static final String COMPANYUID = "Company_Uid";
        public static final String CAOMPANYDESC = "Company_Desc";
        public static final String COMPANYNAME = "Company_Name";
        public static final String ABOUTUS = "Aboutus";
    }

    /**
     *学校品牌类型
     */
    public static class SchoolType {
        public static final String SCHOOL_DIRECT = "school_direct";//直营
        public static final String SCHOOL_FUXIAO = "school_fuxiao";//附校
        public static final String SCHOOL_QINGNIAO = "school_qingniao";//青鸟
    }

    /**
     *关闭dialog
     */
    public static class Dialog {
        public static final int CLOSE = 0xa1;//关闭dialog
    }
    /**
     *Loading类型
     */
    public static class LoadingType {
        public static final int LOGIN_LOADING = 0xB1;//登录中
        public static final int UPDATA_LOADING = 0xB0;//同步中
        public static final int LOGIN_SUCCESS = 0xB2;//登录成功
        public static final int UPDATA_SUCCESS = 0xB3;//同步成功
        public static final int ICCARD_UNEXIST = 0xB4;//卡号信息不存在
        public static final int LOGIN_ERROR = 0xB5;//登录失败
        public static final int UPDATA_ERROR = 0xB6;//同步失败
        public static final int INTERNET_ERROR = 0xB7;//网络错误
    }


}
