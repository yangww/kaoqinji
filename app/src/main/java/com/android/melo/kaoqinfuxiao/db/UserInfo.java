package com.android.melo.kaoqinfuxiao.db;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by melo on 2018/04/13.
 * 师生信息表
 */
@Table("user_info")
public class UserInfo {
    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("id")
    private int id;

    //姓名
    @Column("userName")
    private String userName;

    //卡号
    @Column("idCard")
    private String idCard;
    //卡号
    @Column("memberUid")
    private String memberUid;
    //班级
    @Column("className")
    private String className;

    //身份
    @Column("identity")
    private String identity;

    //照片名称
    @Column("imgName")
    private String imgName;

    public UserInfo(String userName, String memberUid, String idCard, String className, String identity,String imgName) {
        this.userName = userName;
        this.idCard = idCard;
        this.className = className;
        this.identity = identity;
        this.memberUid = memberUid;
        this.imgName=imgName;
    }


    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMemberUid() {
        return memberUid;
    }

    public void setMemberUid(String memberUid) {
        this.memberUid = memberUid;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
