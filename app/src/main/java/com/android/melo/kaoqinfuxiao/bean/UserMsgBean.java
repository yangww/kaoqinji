package com.android.melo.kaoqinfuxiao.bean;

import java.util.List;

/**
 * Created by melo on 2018/4/12.
 * 学生 老师信息bean
 */

public class UserMsgBean {

    private List<StudentBean> student;
    private List<TeacherBean> teacher;

    public List<StudentBean> getStudent() {
        return student;
    }

    public void setStudent(List<StudentBean> student) {
        this.student = student;
    }

    public List<TeacherBean> getTeacher() {
        return teacher;
    }

    public void setTeacher(List<TeacherBean> teacher) {
        this.teacher = teacher;
    }

    public static class StudentBean {
        /**
         * Member_Uid : 733
         * Member_Real_Name : 测试生
         * Member_Ic_Number_a : a:1:{s:19:"0.05557360210259632";s:7:"7468354";}
         * Member_Head_Img : http://w2.pkucollege.com//fuxiao/upload/weixin1/s/2017/1219/a048e7b186d6f87d65d580b09cde856e.jpg
         * Company_Name : 北大附属实验学校幼儿园
         * Class_Name : 大熊座
         * Member_Other_Extend_Img1 : http://blog.beidafuxiao.net/upload/logo/5a0aaaa9ca76b.jpg
         * Member_Other_Extend_Img2 : http://blog.beidafuxiao.net/upload/logo/5a0aaaa9ca76b.jpg
         * Member_Ic_Number : a:1:{s:19:"0.05557360210259632";s:7:"7468354";}
         */

        private String Member_Uid;
        private String Member_Real_Name;
        private String Member_Ic_Number_a;
        private String Member_Head_Img;
        private String Company_Name;
        private String Class_Name;
        private String Member_Other_Extend_Img1;
        private String Member_Other_Extend_Img2;
        private String Member_Ic_Number;

        public String getMember_Uid() {
            return Member_Uid;
        }

        public void setMember_Uid(String Member_Uid) {
            this.Member_Uid = Member_Uid;
        }

        public String getMember_Real_Name() {
            return Member_Real_Name;
        }

        public void setMember_Real_Name(String Member_Real_Name) {
            this.Member_Real_Name = Member_Real_Name;
        }

        public String getMember_Ic_Number_a() {
            return Member_Ic_Number_a;
        }

        public void setMember_Ic_Number_a(String Member_Ic_Number_a) {
            this.Member_Ic_Number_a = Member_Ic_Number_a;
        }

        public String getMember_Head_Img() {
            return Member_Head_Img;
        }

        public void setMember_Head_Img(String Member_Head_Img) {
            this.Member_Head_Img = Member_Head_Img;
        }

        public String getCompany_Name() {
            return Company_Name;
        }

        public void setCompany_Name(String Company_Name) {
            this.Company_Name = Company_Name;
        }

        public String getClass_Name() {
            return Class_Name;
        }

        public void setClass_Name(String Class_Name) {
            this.Class_Name = Class_Name;
        }

        public String getMember_Other_Extend_Img1() {
            return Member_Other_Extend_Img1;
        }

        public void setMember_Other_Extend_Img1(String Member_Other_Extend_Img1) {
            this.Member_Other_Extend_Img1 = Member_Other_Extend_Img1;
        }

        public String getMember_Other_Extend_Img2() {
            return Member_Other_Extend_Img2;
        }

        public void setMember_Other_Extend_Img2(String Member_Other_Extend_Img2) {
            this.Member_Other_Extend_Img2 = Member_Other_Extend_Img2;
        }

        public String getMember_Ic_Number() {
            return Member_Ic_Number;
        }

        public void setMember_Ic_Number(String Member_Ic_Number) {
            this.Member_Ic_Number = Member_Ic_Number;
        }
    }

    public static class TeacherBean {
        /**
         * Member_Uid : 176
         * Member_Real_Name : 唐绍峰
         * Member_Ic_Number_a : a:1:{s:5:"cards";s:10:"1536544182";}
         * Member_Head_Img : 1601/569ddaf2ecaaf.png
         * Company_Name : 北大附属实验学校幼儿园
         * Class_Name : 大熊座
         * Member_Other_Extend_Img1 : http://blog.beidafuxiao.net/upload/logo/5a0aaaa9ca76b.jpg
         * Member_Other_Extend_Img2 : http://blog.beidafuxiao.net/upload/logo/5a0aaaa9ca76b.jpg
         * Member_Ic_Number : a:1:{s:5:"cards";s:10:"1536544182";}
         */

        private String Member_Uid;
        private String Member_Real_Name;
        private String Member_Ic_Number_a;
        private String Member_Head_Img;
        private String Company_Name;
        private String Class_Name;
        private String Member_Other_Extend_Img1;
        private String Member_Other_Extend_Img2;
        private String Member_Ic_Number;

        public String getMember_Uid() {
            return Member_Uid;
        }

        public void setMember_Uid(String Member_Uid) {
            this.Member_Uid = Member_Uid;
        }

        public String getMember_Real_Name() {
            return Member_Real_Name;
        }

        public void setMember_Real_Name(String Member_Real_Name) {
            this.Member_Real_Name = Member_Real_Name;
        }

        public String getMember_Ic_Number_a() {
            return Member_Ic_Number_a;
        }

        public void setMember_Ic_Number_a(String Member_Ic_Number_a) {
            this.Member_Ic_Number_a = Member_Ic_Number_a;
        }

        public String getMember_Head_Img() {
            return Member_Head_Img;
        }

        public void setMember_Head_Img(String Member_Head_Img) {
            this.Member_Head_Img = Member_Head_Img;
        }

        public String getCompany_Name() {
            return Company_Name;
        }

        public void setCompany_Name(String Company_Name) {
            this.Company_Name = Company_Name;
        }

        public String getClass_Name() {
            return Class_Name;
        }

        public void setClass_Name(String Class_Name) {
            this.Class_Name = Class_Name;
        }

        public String getMember_Other_Extend_Img1() {
            return Member_Other_Extend_Img1;
        }

        public void setMember_Other_Extend_Img1(String Member_Other_Extend_Img1) {
            this.Member_Other_Extend_Img1 = Member_Other_Extend_Img1;
        }

        public String getMember_Other_Extend_Img2() {
            return Member_Other_Extend_Img2;
        }

        public void setMember_Other_Extend_Img2(String Member_Other_Extend_Img2) {
            this.Member_Other_Extend_Img2 = Member_Other_Extend_Img2;
        }

        public String getMember_Ic_Number() {
            return Member_Ic_Number;
        }

        public void setMember_Ic_Number(String Member_Ic_Number) {
            this.Member_Ic_Number = Member_Ic_Number;
        }
    }
}
