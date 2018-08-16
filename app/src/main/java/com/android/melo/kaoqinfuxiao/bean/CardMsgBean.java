package com.android.melo.kaoqinfuxiao.bean;

import java.io.Serializable;

/**
 * Created by melo on 2018/4/17.
 * 刷卡信息bean
 */

public class CardMsgBean implements Serializable {

    public String picturePath;//图片路径
    public String icNumber;//卡号
    public String icDateTime;//刷卡时间
    public String memberUid;//
    public String type;//学生身份
}
