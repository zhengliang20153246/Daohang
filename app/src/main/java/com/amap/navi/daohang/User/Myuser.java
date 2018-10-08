package com.amap.navi.daohang.User;

import cn.bmob.v3.BmobUser;

/**
 * Created by LI on 2017/11/2.
 */

public class Myuser extends BmobUser {
    static String toolbartitle="信息";
    static String name="";
    static String nicheng="";
    static String sex="";
    static String qq="";
    static String weixin="";
    static String url="";
    static String phone="";
    static String qiandao="";
    static String objectid="";

    private String MobilePhoneNumber;
    private String Password;
    private String Username;
    private String qq1;
    private String weixin1;
    private String qiandaoshijian;
    private String Email;

    static Number jifen;

    @Override
    public String getEmail() {
        return Email;
    }

    @Override
    public void setEmail(String email) {
        Email = email;
    }

    public String getQq1() {
        return qq1;
    }

    public void setQq1(String qq1) {
        this.qq1 = qq1;
    }

    public String getWeixin1() {
        return weixin1;
    }

    public void setWeixin1(String weixin1) {
        this.weixin1 = weixin1;
    }

    public String getMobilePhoneNumber() {
        return MobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        MobilePhoneNumber = mobilePhoneNumber;
    }
    public Myuser() {
        this.setTableName("_User");
    }
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username=username;
    }

    public String getQiandaoshijian() {
        return qiandaoshijian;
    }

    public void setQiandaoshijian(String qiandaoshijian) {
        this.qiandaoshijian = qiandaoshijian;
    }


}
