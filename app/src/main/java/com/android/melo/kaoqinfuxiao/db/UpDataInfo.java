package com.android.melo.kaoqinfuxiao.db;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by melo on 2018/4/17.
 * 待上传信息
 */
@Table("updata_info")
public class UpDataInfo {
    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("id")
    private int id;

    //卡号
    @Column("icNumber")
    private String icNumber;

    //刷卡时间
    @Column("icDateTime")
    private String icDateTime;

    //学生排号
    @Column("memberUid")
    private String memberUid;

    //照片数据
    @Column("picPath")
    private String picPath;
    //身份
    @Column("type")
    private String type;

    public UpDataInfo(String icNumber, String icDateTime, String memberUid, String picPath, String type) {
        this.icNumber = icNumber;
        this.icDateTime = icDateTime;
        this.memberUid = memberUid;
        this.picPath = picPath;
        this.type = type;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public String getIcDateTime() {
        return icDateTime;
    }

    public void setIcDateTime(String icDateTime) {
        this.icDateTime = icDateTime;
    }

    public String getMemberUid() {
        return memberUid;
    }

    public void setMemberUid(String memberUid) {
        this.memberUid = memberUid;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
